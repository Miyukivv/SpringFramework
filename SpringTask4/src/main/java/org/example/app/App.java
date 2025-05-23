package org.example.app;
import org.example.db.JdbcConnectionManager;
import org.example.models.User;
import org.example.models.Vehicle;
import org.example.services.AuthService;
import org.example.services.RentalService;
import org.example.services.VehicleService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class App 
{

    private final AuthService authService;
    private final VehicleService vehicleService;
    private final RentalService rentalService;
    private final Scanner scanner = new Scanner(System.in);
    public App(AuthService authService, VehicleService vehicleService, RentalService rentalService) {
        this.authService = authService;
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
    }

    private static void adminMenu(Scanner scanner, VehicleService vehicleService, RentalService rentalService) {
        while(true) {
            System.out.println("\n[ADMIN MENU]");
            System.out.println("1. Dodaj pojazd");
            System.out.println("2. Usuń pojazd");
            System.out.println("3. Wyświetl wszystkie pojazdy");
            System.out.println("4. Wyloguj");
            System.out.print("Wybierz: ");
            int choice = -1;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Niepoprawna wartość!");
                scanner.nextLine();
                continue;
            }

            switch(choice) {
                case 1:

                    System.out.print("Podaj kategorię pojazdu ");
                    String category = scanner.nextLine();

                    System.out.print("Podaj markę: ");
                    String brand = scanner.nextLine();

                    System.out.print("Podaj model: ");
                    String model = scanner.nextLine();

                    System.out.print("Podaj rok produkcji: ");
                    int year = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Podaj numer tablicy: ");
                    String plate = scanner.nextLine();

                    System.out.print("Podaj cenę: ");
                    Double price = Double.valueOf(scanner.nextLine());


                    Map<String, Object> attributes = new HashMap<>();
                    System.out.println("Dodaj dodatkowe atrybuty pojazdu (wpisz 'koniec' aby zakończyć):");
                    while (true) {
                        System.out.print("Podaj nazwę atrybutu: ");
                        String key = scanner.nextLine();
                        if ("koniec".equalsIgnoreCase(key)) break;

                        System.out.print("Podaj wartość atrybutu: ");
                        String value = scanner.nextLine();
                        attributes.put(key, value);
                    }
                    Vehicle newVehicle = new Vehicle() {
                    };
                    newVehicle.setCategory(category);
                    newVehicle.setBrand(brand);
                    newVehicle.setModel(model);
                    newVehicle.setYear(year);
                    newVehicle.setPlate(plate);
                    newVehicle.setPrice(price);
                    newVehicle.setAttributes(attributes);

                    vehicleService.addVehicle(newVehicle);
                    System.out.println("Dodano pojazd!");
                    break;

                case 2:
                    System.out.print("Podaj ID pojazdu do usunięcia: ");
                    String vehicleId = scanner.nextLine();
                    vehicleService.removeVehicle(vehicleId);
                    System.out.println("Usunięto pojazd o ID: " + vehicleId);
                    break;

                case 3:
                    List<Vehicle> allVeh = vehicleService.getAllVehicles();
                    if (allVeh.isEmpty()) {
                        System.out.println("Brak pojazdów!");
                    } else {
                        System.out.println("Lista pojazdów:");
                        allVeh.forEach(System.out::println);
                    }
                    break;
                case 4:
                    System.out.println("Wylogowanie z panelu admina.");
                    return;
                default:
                    System.out.println("Nieznana operacja.");

            }
        }
    }
    private static void userMenu(Scanner scanner, User loggedUser, RentalService rentalService, VehicleService vehicleService) {

        while(true) {
            System.out.println("\n[USER MENU]");
            System.out.println("1. Wyświetl dostępne pojazdy");
            System.out.println("2. Wypożycz pojazd");
            System.out.println("3. Zwróć pojazd");
            System.out.println("4. Wyloguj");
            System.out.print("Wybierz: ");
            int choice = -1;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Niepoprawna wartość!");
                scanner.nextLine();
                continue;
            }

            switch(choice) {
                case 1:
                    vehicleService.getAllVehicles().forEach(System.out::println);
                    break;

                case 2:
                    System.out.print("Podaj ID pojazdu do wypożyczenia: ");
                    String vIdToRent = scanner.nextLine();
                    rentalService.rentVehicle(loggedUser, vIdToRent);
                    break;

                case 3:
                    System.out.print("Podaj ID pojazdu do zwrotu: ");
                    String vIdToReturn = scanner.nextLine();
                    rentalService.returnVehicle(loggedUser, vIdToReturn);
                    break;

                case 4:
                    System.out.println("Wylogowano użytkownika: " + loggedUser.getLogin());
                    return;
                default:
                    System.out.println("Nieznana opcja!");
            }
        }
    }

    public void run() {
        String filename = "vehicles.csv";

        Scanner scanner = new Scanner(System.in);

        User loggedUser = null;
        JdbcConnectionManager connectionManager =JdbcConnectionManager.getInstance();
        connectionManager.getConnection();
        while(true) {
            System.out.println("\n~~~ Wypożyczalnia pojazdów ~~~");
            System.out.println("1. Rejestracja");
            System.out.println("2. Logowanie");
            System.out.println("3. Wyjście");
            System.out.print("Wybierz opcję: ");
            int option = -1;
            if (scanner.hasNextInt()) {
                option = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Niepoprawny format. Spróbuj jeszcze raz.");
                scanner.nextLine();
                continue;
            }

            switch(option) {
                case 1:
                    System.out.print("Podaj login: ");
                    String regLogin = scanner.nextLine();
                    System.out.print("Podaj hasło: ");
                    String regPassword = scanner.nextLine();
                    User registered = authService.register(regLogin, regPassword, null);
                    if (registered != null) {
                        System.out.println("Zarejestrowano użytkownika: " + registered.getLogin());
                    }
                    break;
                case 2:
                    System.out.print("Podaj login: ");
                    String logLogin = scanner.nextLine();
                    System.out.print("Podaj hasło: ");
                    String logPassword = scanner.nextLine();
                    loggedUser = authService.login(logLogin, logPassword);
                    if (loggedUser != null) {
                        if ("admin".equalsIgnoreCase(loggedUser.getRole())) {
                            adminMenu(scanner, vehicleService, rentalService);
                        } else {
                            userMenu(scanner, loggedUser, rentalService, vehicleService);
                        }
                    }
                    break;
                case 3:
                    System.out.println("Koniec programu.");
                    return;
                default:
                    System.out.println("Nieznana opcja!");
            }
        }
    }
}