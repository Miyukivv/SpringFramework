package org.example;

import java.util.List;

public interface IVehicleRepository {
     public List<Vehicle>  getVehicles();

    public boolean rentVehicle(String registrationPlate); //nie wiem czy tu dobrze
    public boolean returnVehicle(String registrationPlate); //nie wiem czy tu dobrze

    public void save(String filename, Vehicle Vehicle_to_save);
    public void showVehicles();
}
