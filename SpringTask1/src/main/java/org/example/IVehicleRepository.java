package org.example;

import java.util.List;

public interface IVehicleRepository {
     public List<Vehicle>  getVehicles();

    public boolean rentVehicle(String registrationPlate);
    public boolean returnVehicle(String registrationPlate); 

    public void save(String filename, Vehicle Vehicle_to_save);
    public void showVehicles();
}
