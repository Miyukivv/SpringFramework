package org.example;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RentalService {
    private UserRepository userRepository;
    private Authentication authentication;
    private VehicleRepository vehicleRepository;
    private User loggedUser;
    private Scanner scanner;



    public RentalService(UserRepository userRepository, VehicleRepository vehicleRepository) {
        this.userRepository = userRepository;
        this.authentication=new Authentication(userRepository);
        this.vehicleRepository = vehicleRepository;
        this.scanner=new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("~~Wypożyczalnia pojazdów~~");
            System.out.println("Dostępne opcje: ");
            System.out.println("1 - Zarejestruj się");
            System.out.println("2 - Zaloguj się");
            System.out.println("3 - Wyjdź z wypożyczalni ");

            int choice = -1;

            while (true) {
                System.out.print("Wybierz swoją opcję: ");
                if (this.scanner.hasNextInt()) {
                    choice = this.scanner.nextInt();
                    this.scanner.nextLine();
                    if (choice >= 1 && choice <= 3) {
                        break;
                    } else {
                        System.out.println("Niepoprawna opcja");
                    }
                } else {
                    System.out.println("Błędny format");
                    this.scanner.next();
                }
            }
            switch (choice) {
                case 1:
                    System.out.println("Wpisz swój login:");
                    String login = this.scanner.next();
                    boolean userExistsInRepository =false;
                    for (User user : userRepository.getUsers()){
                        if(user.getLogin().equals(login)){
                            System.out.println("Taki użytkownik już istnieje!");
                            userExistsInRepository=true;
                            break;
                        }
                    }
                    if (userExistsInRepository){
                        break;
                    }

                    System.out.println("Wpisz swoje haslo:");
                    String password = this.scanner.next();


                    String hashPassword=DigestUtils.sha256Hex(password);
                    registration(login, hashPassword, null);
                    break;
                case 2:
                    System.out.println("Zaloguj się!\n");
                    System.out.println("Wpisz swój login:");
                    String yourLogin = this.scanner.next();
                    System.out.println("Wpisz swoje haslo:");
                    String yourPassword = this.scanner.next();

                    String hashPasswordToLogged = DigestUtils.sha256Hex(yourPassword);
                    loginUser(yourLogin,hashPasswordToLogged);
                    break;
                case 3:
                    System.out.println("Wyszedłeś z wypożyczalni");
                    return;
            }
        }
    }

    private void registration(String login, String password, String role) {
        //dodajemy admina samemu, mozemy zmienic role usera, podstawowo niech bedzie kazdy userem
        if (role == null) {
            role = "user";
        }
        User newUser = new User(login, password, role);

        //przekazujemy do metody ktora zapisuje klienta w repozytorium(?), musimy jeszcze sprawdzac czy jest juz uzytkownik o tym loginie czy nie
        userRepository.addUser(newUser);
        userRepository.save(null);
        System.out.println("Zostałeś zarejestrowany!");
    }

    private void loginUser(String login, String password) {
        loggedUser = authentication.login(login, password);
        if (loggedUser != null) {
            userRepository.setLoggedUser(loggedUser);
            if ("admin".equals(loggedUser.getRole())) {
                adminMenu();
            } else {
                userMenu();
            }
        }
    }

    private void adminMenu() {
        while (true) {
            System.out.println("\nWitaj w panelu administrowania!");

            System.out.println("1 - Wyświetl listę użytkowników");
            System.out.println("2 - Dodaj pojazd do repozytorium");
            System.out.println("3 - Usuń pojazd z repozytorium");
            System.out.println("4 - Wyloguj się");


            int choice = -1;

            while (true) {
                System.out.print("Wybierz swoją opcję: ");
                if (this.scanner.hasNextInt()) {
                    choice = this.scanner.nextInt();
                    this.scanner.nextLine();
                    if (choice >= 1 && choice <= 5) {
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
                case 1: //Wyświetl userów oraz ich dane szczegółowe
                    for (User user : userRepository.getUsers()) {
                        System.out.println(user.toShowAdmin());
                    }
                    break;
                case 2: //Dodaj pojazd
                    Vehicle newVehicle;
                    System.out.println("Jesteś przy dodawaniu pojazdu: ");
                    System.out.println("1 - jeśli chcesz dodać samochód");
                    System.out.println("2 - jeśli chcesz dodać motocykl");
                    int choice2=this.scanner.nextInt();

                    if (choice2==1){
                        System.out.println("Wpisz marke samochodu: ");
                        String brandCar = this.scanner.next();
                        System.out.println("Wpisz model samochodu: ");
                        String modelCar = this.scanner.next();
                        System.out.println("Wpisz rok produkcji: ");
                        int yearCar = this.scanner.nextInt();
                        System.out.println("Wpisz cene wypozyczenia samochodu: ");
                        float priceCar = this.scanner.nextFloat();
                        System.out.println("Wpisz tablice rejestracyjna samochodu: ");
                        String registrationPlate = scanner.next();

                        newVehicle = new Car(brandCar, modelCar, yearCar, priceCar, false, registrationPlate);
                        vehicleRepository.addVehicle(newVehicle);
                        break;
                    } else if (choice2 == 2) {
                        System.out.println("Wpisz marke motocyklu: ");
                        String brandMotorcycle = this.scanner.next();
                        System.out.println("Wpisz model motocyklu: ");
                        String modelMotorcycle = this.scanner.next();
                        System.out.println("Wpisz rok produkcji: ");
                        int yearMotorcycle = this.scanner.nextInt();
                        System.out.println("Wpisz cene wypozyczenia motocyklu: ");
                        float priceMotorcycle = this.scanner.nextFloat();
                        System.out.println("Wpisz tablice rejestracyjna motocyklu: ");
                        String registrationPlateMotorcycle = scanner.next();

                        System.out.println("Wpisz kategorie: ");
                        String category = scanner.next();

                        newVehicle = new Motorcycle(brandMotorcycle, modelMotorcycle, yearMotorcycle, priceMotorcycle, false, registrationPlateMotorcycle, category);
                        vehicleRepository.addVehicle(newVehicle);
                        break;
                    } else {
                        System.out.println("Podano niepoprawna opcje");
                        break;
                    }
                    case 3: //Usuń pojazd
                        vehicleRepository.showVehicles();
                        if (vehicleRepository.getVehicles().isEmpty()) {
                            System.out.println("Nie ma zadnych pojazdow do usuniecia");
                            break;
                        }

                        System.out.println("\n Wpisz numer tablicy rejestracyjnej pojazdu, ktorego chcesz usunac");
                        String registrationPlateToRemoveVehicle=scanner.next();

                        vehicleRepository.removeVehicle(registrationPlateToRemoveVehicle);

                        break;
                    case 4:
                        System.out.println("Wylogowano");
                        loggedUser = null;
                        userRepository.setLoggedUser((null));
                        return;
                    }
            }

    }
    private void userMenu() {
        while (true) {
            System.out.println("\n//Witaj w panelu użytkownika!//\n");
            System.out.println("1 - Wyświetl dostępne pojazdy");
            System.out.println("2 - Wypożycz pojazd");
            System.out.println("3 - Zwróć pojazd");
            System.out.println("4 - Wyświetl swoje dane");
            System.out.println("5 - Wyloguj");

            int choice = -1;

            while (true) {
                System.out.print("Wybierz swoją opcję: ");
                if (this.scanner.hasNextInt()) {
                    choice = this.scanner.nextInt();
                    this.scanner.nextLine();
                    if (choice >= 1 && choice <= 5) {
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
                case 1: //Wyświetl dostępne pojazdy
                    vehicleRepository.showVehicles();
                    break;
                case 2: //Wypożycz pojazd
                    String registrationPlate;
                    System.out.println("Podaj numer rejestracyjny pojazdu, który chcesz wypożyczyć: ");
                    registrationPlate = scanner.nextLine();
                    rentVehicleDependOnTheUser(registrationPlate);
                    break;
                case 3: //"Zwróć pojazd
                    returnVehicleDependOnTheUser();
                    break;
                case 4: //Wyświetl swoje dane
                    System.out.println(loggedUser.toString());
                    break;
                case 5:
                    System.out.println("Wylogowano");
                    loggedUser=null;
                    userRepository.setLoggedUser((null));
                    return;
            }
        }
    }
    public void rentVehicleDependOnTheUser(String registrationPlate) {
        Vehicle rentedVehicle = vehicleRepository.rentVehicle(registrationPlate);
        if (rentedVehicle != null) {
            loggedUser.addRentedVehicle(rentedVehicle);
            userRepository.save(loggedUser);
            System.out.println("Wypożyczyłeś/łaś pojazd!");
        } else {
            System.out.println("Nie udało się wypożyczyć pojazdu");
        }
    }
    public void returnVehicleDependOnTheUser(){
        List<Vehicle> rentedVehicles = loggedUser.getRentedVehicles();

        if (rentedVehicles.isEmpty()) {
            System.out.println("Nie ma żadnego pojazdu do zwrotu");
            return;
        }

        System.out.println("Twoje wypożyczone pojazdy: ");
        for (int i = 0; i < rentedVehicles.size(); i++) {
            System.out.println((i + 1) + ". " + rentedVehicles.get(i).getRegistrationPlate()); // Każdy pojazd osobno
        }
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

        //pobieramy wybrany pojazd
        Vehicle vehicleToReturn = rentedVehicles.remove(choice - 1);

        Vehicle returnedVehicle = vehicleRepository.returnVehicle(vehicleToReturn.getRegistrationPlate());

        if (returnedVehicle!=null) {
            System.out.println("Pojazd " + vehicleToReturn.getRegistrationPlate() + " został zwrócony.");
            loggedUser.removeRentedVehicle(vehicleToReturn);
            userRepository.save(loggedUser);
        } else {
            System.out.println("Nie udało się zwrócić pojazdu! Spróbuj ponownie.");
        }
    }
}
