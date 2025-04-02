import org.example.repositories.impl.RentalRepository;
import org.example.repositories.impl.UserRepository;
import org.example.repositories.impl.VehicleJsonRepository;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        String storageType = "json";

        //TODO: Zmiana typu storage w zaleznosci od parametru przekazanego do programu
        //TODO: Utworzenie RentalJdbcRepository implementujacej RentalRepository
        //TODO: Utworzenie UserJdbcRepository implementujacej UserRepository

        //TODO: Dorzucenie do projektu swoich jsonrepo.

        UserRepository userRepo;
        VehicleJsonRepository vehicleRepo;
        RentalRepository rentalRepo;

        switch (storageType) {
            case "jdbc" -> {
                userRepo = new UserJdbcRepository();
                vehicleRepo = new VehicleJdbcRepository();
                rentalRepo = new RentalJdbcRepository();
            }
            case "json" -> {
                userRepo = new UserJsonRepository();
                vehicleRepo = new VehicleJsonRepository();
                rentalRepo = new RentalJsonRepository();
            }
            default -> throw new IllegalArgumentException("Unknown storage type: " + storageType);
        }
        //TODO:Przerzucenie logiki wykorzystującej repozytoria do serwisów
        AuthService authService = new AuthService(userRepo);
        //TODO:W VehicleService mozna wykorzystac rentalRepo dla wyszukania dostepnych pojazdow
        VehicleService vehicleService = new VehicleService(vehicleRepo, rentalRepo);
        RentalService rentalService = new RentalService(rentalRepo);

        //TODO:Przerzucenie logiki interakcji z userem do App
        App app = new App(authService, vehicleService, rentalService);
        app.run();

    }
}