package org.example.app;

import org.example.models.Rental;
import org.example.models.User;
import org.example.models.Vehicle;
import org.example.services.impl.AuthHibernateService;
import org.example.services.impl.RentalHibernateService;
import org.example.services.impl.VehicleHibernateService;

import java.util.*;

public class App {
    private final AuthHibernateService authHibernateService;
    private final VehicleHibernateService vehicleHibernateService;
    private final RentalHibernateService rentalHibernateService;
    private final Scanner scanner = new Scanner(System.in);

    public App(AuthHibernateService authHibernateService,
               VehicleHibernateService vehicleHibernateService,
               RentalHibernateService rentalHibernateService) {
        this.authHibernateService = authHibernateService;
        this.vehicleHibernateService = vehicleHibernateService;
        this.rentalHibernateService = rentalHibernateService;
    }

    private static void adminMenu(Scanner scanner,
                                  VehicleHibernateService vehicleHibernateService,
                                  RentalHibernateService rentalHibernateService,
                                  AuthHibernateService authHibernateService) {
        while (true) {
            System.out.println("\n[ADMIN MENU]");
            System.out.println("1. Dodaj pojazd");
            System.out.println("2. Usuń pojazd");
            System.out.println("3. Wyświetl wszystkie pojazdy");
            System.out.println("4. Wyświetl wszystkich użytkowników");
            System.out.println("5. Wyloguj");
            System.out.print("Wybierz: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addVehicle(scanner, vehicleHibernateService);
                case 2 -> removeVehicle(scanner, vehicleHibernateService);
                case 3 -> showAllVehicles(vehicleHibernateService);
                case 4 -> showAllUsers(authHibernateService);
                case 5 -> {
                    System.out.println("Wylogowanie.");
                    return;
                }
                default -> System.out.println("Niepoprawna opcja.");
            }
        }
    }

    private static void userMenu(Scanner scanner,
                                 User loggedUser,
                                 RentalHibernateService rentalHibernateService,
                                 VehicleHibernateService vehicleHibernateService) {
        while (true) {
            System.out.println("\n[USER MENU]");
            System.out.println("1. Wyświetl dostępne pojazdy");
            System.out.println("2. Wypożycz pojazd");
            System.out.println("3. Zwróć pojazd");
            System.out.println("4. Wyloguj");
            System.out.print("Wybierz: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> showAvailableVehicles(vehicleHibernateService);
                case 2 -> rentVehicle(scanner, loggedUser, rentalHibernateService);
                case 3 -> returnVehicle(scanner, loggedUser, rentalHibernateService);
                case 4 -> {
                    System.out.println("Wylogowano użytkownika: " + loggedUser.getLogin());
                    return;
                }
                default -> System.out.println("Niepoprawna opcja.");
            }
        }
    }

    public void run() {
        while (true) {
            System.out.println("\n<<Wypożyczalnia pojazdów>>");
            System.out.println("1. Rejestracja");
            System.out.println("2. Logowanie");
            System.out.println("3. Wyjście");
            System.out.print("Wybierz opcję: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> registerUser(scanner);
                case 2 -> loginUser(scanner);
                case 3 -> {
                    System.out.println("Koniec programu.");
                    return;
                }
                default -> System.out.println("Niepoprawna opcja.");
            }
        }
    }

    private void registerUser(Scanner scanner) {
        System.out.print("Podaj login: ");
        String login = scanner.nextLine();
        System.out.print("Podaj hasło: ");
        String password = scanner.nextLine();
        boolean success = authHibernateService.register(login, password, "user");
        if (success) {
            System.out.println("Zarejestrowano użytkownika!");
        } else {
            System.out.println("Użytkownik o tym loginie już istnieje.");
        }
    }

    private void loginUser(Scanner scanner) {
        System.out.print("Podaj login: ");
        String login = scanner.nextLine();
        System.out.print("Podaj hasło: ");
        String password = scanner.nextLine();
        Optional<User> loggedUser = authHibernateService.login(login, password);
        if (loggedUser.isPresent()) {
            User user = loggedUser.get();
            if ("admin".equalsIgnoreCase(user.getRole())) {
                adminMenu(scanner, vehicleHibernateService, rentalHibernateService, authHibernateService);
            } else {
                userMenu(scanner, user, rentalHibernateService, vehicleHibernateService);
            }
        } else {
            System.out.println("Błędne dane logowania.");
        }
    }
    private static void addVehicle(Scanner scanner,
                                   VehicleHibernateService vehicleHibernateService) {

        System.out.print("Podaj kategorię pojazdu: ");
        String category = scanner.nextLine();
        System.out.print("Podaj markę pojazdu: ");
        String brand = scanner.nextLine();
        System.out.print("Podaj model pojazdu: ");
        String model = scanner.nextLine();

        int year;
        while (true) {
            System.out.print("Podaj rok produkcji: ");
            try {
                year = scanner.nextInt();
                scanner.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Proszę wprowadzić poprawny rok (liczba całkowita).");
                scanner.nextLine();
            }
        }

        System.out.print("Podaj numer rejestracyjny: ");
        String plate = scanner.nextLine();

        double price;
        while (true) {
            System.out.print("Podaj cenę pojazdu: ");
            try {
                price = scanner.nextDouble();
                scanner.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Proszę wprowadzić poprawną cenę (liczba zmiennoprzecinkowa).");
                scanner.nextLine();
            }
        }


        Map<String, Object> attrs = new HashMap<>();
        while (true) {
            System.out.print("Dodać atrybut? (t/n): ");
            String more = scanner.nextLine().trim().toLowerCase();
            if (!more.equals("t")) break;

            System.out.print("  Nazwa atrybutu: ");
            String key = scanner.nextLine().trim();

            System.out.print("  Wartość atrybutu: ");
            String value = scanner.nextLine().trim();

            attrs.put(key, value);
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setId(UUID.randomUUID().toString());
        vehicle.setCategory(category);
        vehicle.setBrand(brand);
        vehicle.setModel(model);
        vehicle.setYear(year);
        vehicle.setPlate(plate);
        vehicle.setPrice(price);
        vehicle.setAttributes(attrs);

        vehicleHibernateService.save(vehicle);
        System.out.println("Pojazd został dodany.");
    }

    private static void removeVehicle(Scanner scanner, VehicleHibernateService vehicleHibernateService) {
        System.out.print("Podaj ID pojazdu do usunięcia: ");
        String vehicleId = scanner.nextLine();

        boolean deleted = vehicleHibernateService.deleteById(vehicleId);
        if (deleted) {
            System.out.println("Pojazd o ID " + vehicleId + " został usunięty.");
        } else {
            System.out.println("Nie znaleziono pojazdu o podanym ID – nic nie usunięto.");
        }
    }
    private static void showAllVehicles(VehicleHibernateService vehicleHibernateService) {
        List<Vehicle> vehicles = vehicleHibernateService.findAll();
        vehicles.forEach(System.out::println);
    }

    private static void showAvailableVehicles(VehicleHibernateService vehicleHibernateService) {
        List<Vehicle> availableVehicles = vehicleHibernateService.findAvailableVehicles();
        if (availableVehicles.isEmpty()) {
            System.out.println("Brak dostępnych pojazdów.");
        } else {
            availableVehicles.forEach(System.out::println);
        }
    }

    private static void rentVehicle(Scanner scanner,
                                    User loggedUser,
                                    RentalHibernateService rentalHibernateService) {
        System.out.print("Podaj ID pojazdu do wypożyczenia: ");
        String vehicleId = scanner.nextLine();
        Rental rented = rentalHibernateService.rent(vehicleId, loggedUser.getId());
        if (rented != null) {
            System.out.println("Pojazd został wypożyczony.");
        } else {
            System.out.println("Pojazd jest już wypożyczony lub nie istnieje.");
        }
    }

    private static void returnVehicle(Scanner scanner,
                                      User loggedUser,
                                      RentalHibernateService rentalService) {
        List<Vehicle> myVehicles = rentalService.findActiveVehiclesByUserId(loggedUser.getId());
        if (myVehicles.isEmpty()) {
            System.out.println("Nie masz żadnych aktualnie wypożyczonych pojazdów.");
            return;
        }

        System.out.println("Twoje aktualnie wypożyczone pojazdy:");
        myVehicles.forEach(System.out::println);

        System.out.print("Podaj ID pojazdu do zwrócenia: ");
        String vehicleId = scanner.nextLine();

        boolean ownsVehicle = myVehicles.stream().anyMatch(v -> v.getId().equals(vehicleId));
        if (!ownsVehicle) {
            System.out.println("Nie posiadasz pojazdu o podanym ID.");
            return;
        }

        boolean returned = rentalService.returnRental(vehicleId, loggedUser.getId());
        if (returned) {
            System.out.println("Pojazd został zwrócony.");
        } else {
            System.out.println("Nie udało się zwrócić pojazdu");
        }
    }

    private static void showAllUsers(AuthHibernateService authHibernateService) {
        List<User> users = authHibernateService.findAllUsers();
        if (users.isEmpty()) {
            System.out.println("Brak zarejestrowanych użytkowników.");
        } else {
            users.forEach(System.out::println);
        }
    }
}
