package org.example;

import java.io.*;
import java.util.*;

public class Client {
    private String login;
    private String password;
    private static Map<String, List<String>> clients = new HashMap<>();


    public Client(String login, String password) {
        this.login = login;
        this.password = password;

        initializeClientsFile();
        loadClientsFromFile();
    }

    //Rejestracja

    public String getLogin(){
        return login ;
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
            if (!clients.containsKey(getLogin())) {
                clients.put(getLogin(), new ArrayList<>());
            }
            //Dodajemy pojazd do listy klienta
            clients.get(getLogin()).add(registrationPlate);

            saveClientsToFile();
            System.out.println("Wypożyczono pojazd: " + registrationPlate);
        }
    }
    public void returnVehicleDependOnTheClient(VehicleRepository vehicleRepository) {
        List<String> rentedVehicles = clients.get(getLogin());

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

            boolean isReturned = vehicleRepository.returnVehicle(registrationPlate);

        if (isReturned) {
            System.out.println("Pojazd " + registrationPlate + " został zwrócony.");


            //Zwrócono wszystkie pojazdy, usuwamy klienta
            if (rentedVehicles.isEmpty()) {
                clients.remove(getLogin());
                System.out.println("Usunięto klienta, bo zwrócił wszystkie pojazdy.");
            }

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
//            System.out.println("1 - Zarejestruj się");
//            System.out.println("2 - Zaloguj się");
            System.out.println("3 - Wyświetl dostępne pojazdy");
            System.out.println("4 - Wypożycz pojazd ");
            System.out.println("5 - Zwróć pojazd");
            System.out.println("6 - Wyjdź z wypożyczalni ");

            int choice = -1;

            while (true) {
                System.out.print("Wybierz swoją opcję: ");
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    if (choice >= 1 && choice <= 6) {
                        break;
                    } else {
                        System.out.println("Niepoprawna opcja");
                    }
                } else {
                    System.out.println("Błędny format");
                    scanner.next();
                }
            }
            switch (choice) {
                case 1:
                    System.out.println("Wpisz swój login:\n");
                    String login=scanner.next();
                    System.out.println("Wpisz swoje haslo:\n");
                    String password=scanner.next();


                   // registration(login,password);
                    break;
                case 2:

                    break;
                case 3:
                    vehicleRepository.showVehicles();
                    break;
                case 4:
                    String registrationPlate;

                    System.out.println("Podaj numer rejestracyjny pojazdu, który chcesz wypożyczyć: ");
                    registrationPlate = scanner.nextLine();
                    rentVehicleDependOnTheClient(vehicleRepository, registrationPlate);
                    break;
                case 5:
                    returnVehicleDependOnTheClient(vehicleRepository);
                    break;
                case 6:
                    System.out.println("Wyszedłeś z wypożyczalni");
                    return;
            }

        }
    }
}
