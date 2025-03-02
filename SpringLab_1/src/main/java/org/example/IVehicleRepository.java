package org.example;

import java.util.List;

public interface IVehicleRepository {

    void rentVehicle(String registrationPlate);
    void returnVehicle(String registrationPlate);

    List<Vehicle>  getVehicles();

    void save(String filename, Vehicle Vehicle_to_save);
}
