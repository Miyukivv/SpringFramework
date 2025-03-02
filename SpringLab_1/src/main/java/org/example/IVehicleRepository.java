package org.example;

import java.util.List;

public interface IVehicleRepository {
    List<Vehicle>  getVehicles();

    boolean rentVehicle(String registrationPlate);
    boolean returnVehicle(String registrationPlate);

    void save(String filename, Vehicle Vehicle_to_save);
    void showVehicles();
}
