package org.example;

import java.util.List;

public interface IVehicleRepository {
     public List<Vehicle>  getVehicles();

    public Vehicle rentVehicle(String registrationPlate);
    public Vehicle returnVehicle(String registrationPlate);

    public void save(String filename, Vehicle Vehicle_to_save);
    public void showVehicles();
}
