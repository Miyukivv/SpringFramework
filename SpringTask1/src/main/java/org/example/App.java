package org.example;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App 
{
    public static void main( String[] args ) {
        String filename = "vehicles.csv";
        VehicleRepository vehicleRepository = new VehicleRepository(new ArrayList<>());

        if (vehicleRepository.getVehicles().isEmpty()) {
            vehicleRepository.save("vehicles.csv", new Car("Toyota", "Corolla", 2020, 150.0f, false, "ABC123"));
            vehicleRepository.save("vehicles.csv", new Motorcycle("Honda", "CBR600", 2018, 120.0f, false, "XYZ987", "A2"));
            System.out.println("Dodano plik oraz pojazdy!\n");
//            vehicleRepository.loadVehiclesFromFile(filename);
        }

        //Nowe logowanie użytkownika:
        Scanner scanner = new Scanner(System.in);
        System.out.println("Witaj w wypożyczalni pojazdów!");
          System.out.println("Podaj swoje imię: ");
        String firstName = scanner.nextLine();
        System.out.println("Podaj swoje nazwisko: ");
        String lastName = scanner.nextLine();

        Client client = new Client(firstName, lastName);
        System.out.println("\nWitaj " + client.getLogin() + "!");
        client.vehicleRentalService(vehicleRepository);
    }
}
