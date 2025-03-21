package org.example;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.Scanner;

public class App 
{
    public static void main( String[] args ) {
        String filename = "vehicles.csv";

        //repozytorium pojazdów i użytkowników
        VehicleRepository vehicleRepository = new VehicleRepository(new ArrayList<>());
        UserRepository userRepository = new UserRepository(vehicleRepository);

        //jako wypożyczalnia, dodajemy nowe samochody do repozytorium
//        if (vehicleRepository.getVehicles().isEmpty()) {
//            vehicleRepository.save("vehicles.csv", new Car("Toyota", "Corolla", 2020, 150.0f, false, "ABC123"));
//            vehicleRepository.save("vehicles.csv", new Motorcycle("Honda", "CBR600", 2018, 120.0f, false, "XYZ987", "A2"));
//            System.out.println("Dodano plik oraz pojazdy!\n");
//        }

        RentalService rentalService = new RentalService(userRepository,vehicleRepository);
        rentalService.start();

//      Klonowanie:
//   Zwracany jest obiekt (lista oryginalnych pojazdów - ma dokładnie ten sam adres w pamięci)  przez referencje (ten sam obiekt - nasza lista) - w tym momencie możemy zmieniać listę,
//   a chcemy zablokować dostęp z tego miejsca. Należy zwracać kopię obiektów - inne adresy w pamieci
        //List<Vehicle> vehTakenFromRepository = vehicleRepository.getVehicles();
//        for(Vehicle v : vehTakenFromRepository) {
//            System.out.println(v.toString());
//        }
//
//        System.out.println("____________________________________________");
//        Vehicle v1 = vehTakenFromRepository.get(0);
//        v1.setRented(true);
//        for(Vehicle v : vehicleRepository.getVehicles()) {
//            System.out.println(v.toString());
//        }

    }
}
