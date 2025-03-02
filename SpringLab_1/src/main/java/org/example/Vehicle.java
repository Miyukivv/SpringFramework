package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public abstract class Vehicle {
    private String brand;
    private String model;
    private Integer year;
    private Float price;
    private Boolean rented;

    //pole które będzie jednoznacznie odróżniało od siebie pojazdy
    private String registrationPlate;

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public Integer getYear() {
        return year;
    }

    public Float getPrice() {
        return price;
    }

    public Boolean getRented() {
        return rented;
    }

    public String getRegistrationPlate() {
        return registrationPlate;
    }

    public void setRented(Boolean rented) {
        this.rented = rented;
    }

    public Vehicle(String brand, String model, Integer year, Float price, Boolean rented, String registrationPlate) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.rented = rented;
        this.registrationPlate = registrationPlate;
    }


    public abstract String toCsv();

    public abstract String toString();
}
