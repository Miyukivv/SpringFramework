package org.example.services;

import org.example.models.Rental;
import org.example.repositories.RentalRepository;
import org.example.repositories.VehicleRepository;
import org.example.repositories.impl.RentalJsonRepository;
import org.example.repositories.impl.VehicleJsonRepository;
import org.example.models.User;
import org.example.models.Vehicle;

import java.time.LocalDateTime;
import java.util.Optional;

/*
RentalService (logika wypożyczania i zwracania pojazdów; ewentualnie łączy się z RentalRepository, VehicleRepository i sprawdza dostępność pojazdów, tworzy rekordy wypożyczenia w bazie itp.).
 */
public class RentalService {
    private VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;
    public RentalService(VehicleRepository vehicleRepository,RentalRepository rentalRepository) {
        this.vehicleRepository = vehicleRepository;
        this.rentalRepository = rentalRepository;
    }

    public Rental rentVehicle(User user, String vehicleId){
        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);

        if (vehicle.isEmpty()) {
            System.out.println("Brak pojazdu o podanym ID");
            return null;
        }
        Vehicle vehicleToCheck = vehicle.get();
        Optional<Rental> isRented = rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId);
        if (isRented == null){
            System.out.println("Pojazd już jest wypożyczony!");
            return null;
        }

        Rental newRental = Rental.builder().vehicleId(vehicleToCheck.getId()).userId(user.getId()).rentDateTime(LocalDateTime.now().toString()).returnDateTime(null).build();
        rentalRepository.save(newRental);
        System.out.println("Udało się wypożyczyć pojazd");
        return newRental;
    }

    public void returnVehicle(User user, String vehicleId){
        Optional<Rental> rental= rentalRepository.findByVehicleId(vehicleId).filter(r->r.getReturnDateTime() == null);

        if (rental.isEmpty()) {
            System.out.println("Brak aktywnego wypożyczenia pojazdu");
            return;
        }
        Rental checkRental=rental.get();

        if (!checkRental.getUserId().equals(user.getId())){
            System.out.println("Ten pojazd nie jest przez Ciebie wypożyczony");
            return;
        }

        checkRental.setReturnDateTime(LocalDateTime.now().toString());
        rentalRepository.save(checkRental);

        System.out.println("Udało zwrócić się pojazd!");
    }

}
