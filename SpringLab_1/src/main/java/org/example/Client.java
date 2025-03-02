package org.example;

import java.io.*;
import java.util.*;

public class Client {
    private String firstName;
    private String lastName;
    private static Map<String, List<String>> clients = new HashMap<>();



    public Client(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;

        initializeClientsFile();
        loadClientsFromFile();
    }


    public String getFullName(){
        return firstName + " " + lastName;
    }

    private void initializeClientsFile() {
        File file = new File("clients.csv");
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("Plik clients.csv jest utworzony.");
                }
            } catch (IOException e) {
                System.out.println("Błąd podczas tworzenia pliku: " + e.getMessage());
            }
        }
    }


    private void saveClientsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("clients.csv", false))) {
            for (Map.Entry<String, List<String>> entry : clients.entrySet()) {
                for (String plate : entry.getValue()) {
                    writer.write(entry.getKey() + ";" + plate);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Błąd zapisu klientów: " + e.getMessage());
        }
    }

    private void loadClientsFromFile(){
        File file = new File("clients.csv");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader("clients.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    clients.computeIfAbsent(parts[0], k -> new ArrayList<>()).add(parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Błąd odczytu klientów: " + e.getMessage());
        }
    }

    public void rentVehicleDependOnTheClient(VehicleRepository vehicleRepository, String registrationPlate) {
        boolean isWorked = vehicleRepository.rentVehicle(registrationPlate);
        if (isWorked) {
            //Jak klient nie ma żadnej listy, to tworzymy ją
            if (!clients.containsKey(getFullName())) {
                clients.put(getFullName(), new ArrayList<>());
            }
            //Dodajemy pojazd do listy klienta
            clients.get(getFullName()).add(registrationPlate);

            saveClientsToFile();
            System.out.println("Wypożyczono pojazd: " + registrationPlate);
        }
    }
    public void returnVehicleDependOnTheClient(VehicleRepository vehicleRepository) {
        List<String> rentedVehicles = clients.get(getFullName());

        if (rentedVehicles == null || rentedVehicles.isEmpty()) {
            System.out.println("Nie ma żadnego pojazdu do zwrotu");
            return;
        }

        System.out.println("Twoje wypożyczone pojazdy: ");
        for (int i = 0; i < rentedVehicles.size(); i++) {
            System.out.println((i + 1) + ". " + rentedVehicles.get(i)); // Każdy pojazd osobno
        }

        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (true) {
            System.out.print("Podaj numer pojazdu do zwrotu: ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();

                if (choice >= 1 && choice <= rentedVehicles.size()) {
                    break;
                } else {
                    System.out.println("Niepoprawny wybór");
                }
            } else {
                System.out.println("To nie jest liczba,  wprowadź numer z listy.");
                scanner.next();
            }
        }

            String registrationPlate = rentedVehicles.remove(choice - 1);
            System.out.println("DEBUG: Wybrano pojazd do zwrotu: " + registrationPlate);

            boolean isReturned = vehicleRepository.returnVehicle(registrationPlate);

        if (isReturned) {
            System.out.println("Pojazd " + registrationPlate + " został zwrócony.");

            //Usuwamy pojazd z listy klienta
            System.out.println("DEBUG: Lista po zwrocie: " + rentedVehicles);

            //Zwrócono wszystkie pojazdy, usuwamy klienta
            if (rentedVehicles.isEmpty()) {
                clients.remove(getFullName());
                System.out.println("Usunięto klienta, bo zwrócił wszystkie pojazdy.");
            }

            //Zapisujemy aktualizację klientów
            saveClientsToFile();
        } else {
            System.out.println("Nie udało się zwrócić pojazdu! Spróbuj ponownie.");
        }
        }

    void vehicleRentalService(VehicleRepository vehicleRepository){
        Scanner scanner = new Scanner(System.in);

        while (true){
            System.out.println("~~Wypożyczalnia pojazdów~~");
            System.out.println("Dostępne opcje: ");
            System.out.println("1 - Wyświetl dostępne pojazdy");
            System.out.println("2 - Wypożycz pojazd ");
            System.out.println("3 - Zwróć pojazd");
            System.out.println("4 - Wyjdź z wypożyczalni");

            int choice = -1; // Inicjalizacja zmiennej do przechowywania wyboru

            while (true) {
                System.out.print("Wybierz swoją opcję: ");
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Oczyszczenie bufora wejścia
                    if (choice >= 1 && choice <= 4) {
                        break; // Jeśli wybór jest poprawny (1-4), wychodzimy z pętli
                    } else {
                        System.out.println("Niepoprawna opcja");
                    }
                } else {
                    System.out.println("Błędny format");
                    scanner.next(); // Oczyszczenie błędnego wejścia
                }
            }

            switch (choice){
                case 1:
                    vehicleRepository.showVehicles();
                    break;
                case 2:
                    String registrationPlate;

                    System.out.println("Podaj numer rejestracyjny pojazdu, który chcesz wypożyczyć: ");
                    registrationPlate=scanner.nextLine();
                    rentVehicleDependOnTheClient(vehicleRepository,registrationPlate);

                    break;
                case 3:
                    returnVehicleDependOnTheClient(vehicleRepository);
                    break;
                case 4:
                    System.out.println("Wyszedłeś z wypożyczalni");
                    return;
            }
        }
    }
}
