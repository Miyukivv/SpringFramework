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
    }


    void loadVehiclesFromFile(String filename) {
        Path path = Paths.get(filename);
        if (!Files.exists(path)) {
            System.out.println("Plik nie istnieje");

            try {
                Files.createFile(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Plik vehicles.csv zostal utworzony");

            Vehicle newCar = new Car("Toyota", "Corolla", 2020, 150.0f, false, "ABC123");
            Vehicle newMotorcycle=new Motorcycle("Honda", "CBR600", 2018, 120.0f, false, "XYZ987", "A2");
            vehicles.add(newCar);
            vehicles.add(newMotorcycle);
            save(filename, null);
            System.out.println("Dodano przykładowe pojazdy do pliku vehicles.csv");
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
    public Vehicle rentVehicle(String registrationPlate) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getRegistrationPlate().equals(registrationPlate)) {
                if (!vehicle.getRented()) {
                    vehicle.setRented(true);
                   // System.out.println("Twój pojazd został wypożyczony");
                    save("vehicles.csv",null);
                    return vehicle;
                } else {
                  //  System.out.println("Nie możesz wypożyczyć tego pojazdu, ponieważ już jest wypożyczony");
                    return null;
                }
            }
        }
      //  System.out.println("Nie znaleziono pojazdu o takim numerze rejestracyjnym");
        return null;
    }

    @Override
    public Vehicle returnVehicle(String registrationPlate) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getRegistrationPlate().equals(registrationPlate)) {
                if (vehicle.getRented()){
                    vehicle.setRented(false);
            //        System.out.println("Pojazd zwrócono");
                    save("vehicles.csv",null);
                    return vehicle;
                } else {
            //        System.out.println("Chcesz zwrócić pojazd, który nie jest wypożyczony!");
                    return null;
                }
            }
        }
    //    System.out.println("Nie znaleziono pojazdu o takim numerze rejestracyjnym!");
        return null;
    }

    @Override
    public List<Vehicle> getVehicles() {
        List<Vehicle> clonedVehicles=new ArrayList<>();
        for (Vehicle vehicle : vehicles){
            clonedVehicles.add(vehicle.clone());
        }
        return clonedVehicles;
    }


   // Do IVehicleReposiotry oraz jego implementacji dopisz getVehicle. (po id/tablicy rejestracyjnej)

    public Vehicle getVehicle(String registrationPlateToFindVehicle){
        for (Vehicle vehicle : vehicles){
            if (vehicle.getRegistrationPlate().equals(registrationPlateToFindVehicle)){

                return vehicle;
            }
        }
        return null;
    }
    public boolean addVehicle(Vehicle newVehicle) {
        if (vehicles.contains(newVehicle)) {
            System.out.println("Pojazd z numerem rejestracyjnym " + newVehicle.getRegistrationPlate() + " już istnieje!");
            return false;
        }
        vehicles.add(newVehicle);
        save("vehicles.csv", newVehicle);
        System.out.println("Pomyslnie dodano pojazd");
        return true;
    }

    public boolean removeVehicle(String registrationPlateToRemoveVehicle){
        Vehicle vehicleToRemove = getVehicle(registrationPlateToRemoveVehicle);
        if (vehicleToRemove != null){
            vehicles.remove(vehicleToRemove);
            save("vehicles.csv",null);
            System.out.println("Pomyslnie usunieto pojazd");
            return true;
        } else {
            System.out.println("Nie możesz usunąć pojazdu, nie ma go w repozytorium");
        }
        return false;
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
    public boolean showVehicles() {
        if (vehicles.isEmpty()){
            System.out.println("Brak dostępnych pojazdów!\n");
            return false;
        }
        System.out.println("Dostępne pojazdy: ");
        boolean isAnyVehicle=false;
        for (Vehicle vehicle : vehicles){
            if (!vehicle.getRented()){
                System.out.println(vehicle);
                isAnyVehicle=true;
            }
        }
        if (isAnyVehicle==false){
            System.out.println("Wszystkie pojazdy zostały wypożyczone!");
        }
        return isAnyVehicle;
    }
}
