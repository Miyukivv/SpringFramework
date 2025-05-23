package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class VehicleRepository implements IVehicleRepository {
    private List<Vehicle> vehicles;
    public VehicleRepository(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
        loadVehiclesFromFile("vehicles.csv");
//        System.out.println("Adresy pojazdów w konstruktorze w repozytorium: ");
//        for(Vehicle v :vehicles) {
//            System.out.println(v.hashCode());
//        }
    }


    void loadVehiclesFromFile(String filename) {
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
        String[] parts = csvLine.split(";");
        if (parts.length < 6) {
            return null;
        }

        String brand = parts[0];
        String model = parts[1];
        Integer year = Integer.parseInt(parts[2]);
        Float price = Float.parseFloat(parts[3]);
        Boolean rented = Boolean.parseBoolean(parts[4]);
        String registrationPlate = parts[5];

        if (parts.length > 6) {
            String category = parts[6];
            Vehicle motorcycle = new Motorcycle(brand, model, year, price, rented, registrationPlate, category);
            vehicles.add(motorcycle);
        } else {
            Vehicle car = new Car(brand, model, year, price, rented, registrationPlate);
            vehicles.add(car);
        }
        return null;
    }

    @Override
    public boolean rentVehicle(String registrationPlate) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getRegistrationPlate().equals(registrationPlate)) {
                if (!vehicle.getRented()) {
                    vehicle.setRented(true);
                    System.out.println("Twój pojazd został wypożyczony");
                    save("vehicles.csv",null);
                    return true;
                } else {
                    System.out.println("Nie możesz wypożyczyć tego pojazdu, ponieważ już jest wypożyczony");
                    return false;
                }
            }
        }
        System.out.println("Nie znaleziono pojazdu o takim numerze rejestracyjnym");
        return false;
    }

    @Override
    public boolean returnVehicle(String registrationPlate) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getRegistrationPlate().equals(registrationPlate)) {
                if (vehicle.getRented()){
                    vehicle.setRented(false);
                    System.out.println("Pojazd zwrócono");
                    save("vehicles.csv",null);
                    return true;
                } else {
                    System.out.println("Chcesz zwrócić pojazd, który nie jest wypożyczony!");
                    return false;
                }
            }
        }
        System.out.println("Nie znaleziono pojazdu o takim numerze rejestracyjnym!");
        return false;
    }

    @Override
    public List<Vehicle> getVehicles() {
        List<Vehicle> clonedVehicles=new ArrayList<>();
        for (Vehicle vehicle : vehicles){
            clonedVehicles.add(vehicle.clone());
        }
        return clonedVehicles;
    }
    public boolean addVehicle(Vehicle newVehicle) {
        if (vehicles.contains(newVehicle)) {
            System.out.println("Pojazd z numerem rejestracyjnym " + newVehicle.getRegistrationPlate() + " już istnieje!");
            return false;
        }
        vehicles.add(newVehicle);
        save("vehicles.csv", newVehicle);
        return true;
    }
    @Override
    public void save(String filename, Vehicle vehicleToSave) {
            if (vehicleToSave != null) {
                //jak przekazano obiekt, to dopisujemy go do pliku
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
                    writer.append(vehicleToSave.toCsv());
                    writer.newLine();
                } catch (IOException e) {
                    throw new RuntimeException();
                }
            } else {
                //Nadpisywanie pliku
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) {
                    for (Vehicle vehicle : vehicles) {
                        writer.write(vehicle.toCsv());
                        writer.newLine();
                    }
                    System.out.println();
                } catch (IOException e) {
                    throw new RuntimeException();
                }
            }
    }
    @Override
    public void showVehicles() {
        if (vehicles.isEmpty()){
            System.out.println("Brak dostępnych pojazdów!\n");
            return;
        }
        System.out.println("Dostępne pojazdy: \n");
        for (Vehicle vehicle : vehicles){
            if (!vehicle.getRented()){
                System.out.println(vehicle);
            }
        }
    }
}
