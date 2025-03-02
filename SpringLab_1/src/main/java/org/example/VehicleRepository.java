package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class VehicleRepository implements IVehicleRepository{
    List<Vehicle> vehicles;
    public VehicleRepository(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    private void isExists(String filename) {
        Path path = Paths.get(filename);
        if (!Files.exists(path)) {
            System.out.println("Plik nie istnieje");
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                Vehicle vehicle = parseVehicle(line);
                if (vehicle != null) {
                    vehicles.add(vehicle);
                }
            }
        } catch (IOException e) {
            System.out.println("Błąd odczytu " + e.getMessage());
        }
    }

    private Vehicle parseVehicle(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length < 5) {
            return null;
        }

        String brand = parts[0];
        String model = parts[1];
        Integer year =Integer.parseInt(parts[2]);
        Float price = Float.parseFloat(parts[3]);
        Boolean rented = Boolean.parseBoolean(parts[4]);
        String registrationPlate = parts[5];

        if (parts[6] !=null){
            String category = parts[6];
            Vehicle motorcycle = new Motorcycle(brand,model,year,price,rented,registrationPlate,category);
            vehicles.add(motorcycle);
        } else {
            Vehicle car = new Car(brand,model,year,price,rented,registrationPlate);
            vehicles.add(car);
        }

        return null;
    }

    @Override
    public void rentVehicle(String registrationPlate) {
        for (Vehicle vehicle : vehicles){
            if (registrationPlate == vehicle.getRegistrationPlate()){
                if (!vehicle.getRented()){
                    vehicle.setRented(true);
                    System.out.println("Udało się zwrócić pojazd");
                } else {
                    System.out.println("Nie możesz wypożyczyć tego pojazdu, ponieważ już jest wypożyczony");
                }
            }
        }
        System.out.println("Nie ma pojazdu z taką rejestracją");
    }

    @Override
    public void returnVehicle(String registrationPlate) {
        for (Vehicle vehicle : vehicles) {

            if (registrationPlate == vehicle.getRegistrationPlate()) {
                if (!vehicle.getRented()) {
                    vehicle.setRented(true);
                    System.out.println("Pojazd zwrócony");
                    ;
                } else {
                    System.out.println("Chcesz zwrócić pojazd, który nie jest wypożyczony!");
                }
            } else {
                System.out.println("Nie ma twojego pojazdu w bazie!");
            }
        }
    }

    @Override
    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    @Override
    public void save(String filename, Vehicle vehicleToSave) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))){
            writer.append(vehicleToSave.toCsv());
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
