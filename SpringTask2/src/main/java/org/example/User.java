package org.example;

import java.util.*;

public class User {
    private String login;
    private String password;
    private String role;
    public List<Vehicle> rentedVehicles;
    public User(String login, String password,String role) {
        this.login = login;
        this.password = password;
        this.role=role;
        this.rentedVehicles=new ArrayList<>();
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public List<Vehicle> getRentedVehicles() {
        return rentedVehicles;
    }
    public String toShowAdmin(){
        String string;
        StringBuilder rentedVehiclesCsv = new StringBuilder();
        if (rentedVehicles !=null){
            if (rentedVehicles.isEmpty()) {
                rentedVehiclesCsv.append("null");
            } else {
                for (int i = 0; i < rentedVehicles.size(); i++) {
                    rentedVehiclesCsv.append(rentedVehicles.get(i).getRegistrationPlate());
                    if (i < rentedVehicles.size() - 1) {
                        rentedVehiclesCsv.append(";");
                    }
                }
            }
            if (rentedVehiclesCsv.equals(null)){
                string="login:" + login +  " password: " + password +
                        "rola:" + role +  " " +
                        "wypożyczony pojazd:" + "brak" +"\n";
            } else{
                string="login:" + login  + " password: " + password +
                        " rola:" + role  +
                        " wypożyczone pojazdy:" + rentedVehiclesCsv + "\n";
            }
        }
        else {
            string ="login: " + login + " password: " + password +
                    " rola=" + role + "\n";
        }
        return string;
    }

    @Override
    public String toString() {
        String string;
        StringBuilder rentedVehiclesCsv = new StringBuilder();
        if (rentedVehicles != null && !rentedVehicles.isEmpty()) {
            for (int i = 0; i < rentedVehicles.size(); i++) {
                rentedVehiclesCsv.append(rentedVehicles.get(i).getRegistrationPlate());
                if (i < rentedVehicles.size() - 1) {
                    rentedVehiclesCsv.append(";");
                }
            }
            string = "\nTwoje dane: \n" +
                    "Login: " + login  +
                    "\nRola: " + role  +
                    "\nWypożyczone pojazdy: " + rentedVehiclesCsv + "\n";
        } else {
            string = "\nTwoje dane: " +
                    "\nLogin: " + login +
                    "\nRola: " + role +
                    "\nWypożyczone pojazdy: brak\n";
        }
        return string;
    }
    public String to_csv(){
        StringBuilder rentedVehiclesCsv = new StringBuilder();
        if (rentedVehicles.isEmpty()){
            return login + ";" + password + ";" + role;
        }
        if (rentedVehicles.isEmpty()) {
            rentedVehiclesCsv.append("null");
        } else {
            for (int i = 0; i < rentedVehicles.size(); i++) {
                rentedVehiclesCsv.append(rentedVehicles.get(i).getRegistrationPlate());
            }
        }
        return login + ";" + password + ";" + role + ";" + rentedVehiclesCsv;
    }

    public void addRentedVehicle(Vehicle vehicle) {
        rentedVehicles.add(vehicle);
    }

    public void removeRentedVehicle(Vehicle vehicle) {
        rentedVehicles.remove(vehicle);
    }
}
