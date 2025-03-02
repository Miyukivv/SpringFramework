package org.example;

public class Car extends Vehicle {
    public Car(String brand, String model, Integer year, Float price, Boolean rented, String registrationPlate) {
        super(brand, model, year, price, rented, registrationPlate);
    }

    @Override
    public String toCsv() {
        return  getBrand() + "," + getModel() + "," + getYear() + "," + getPrice() + "," + getRented() + "," + getRegistrationPlate();
    }

    @Override
    public String toString() {
        return "Car{" +
                "brand='" + getBrand() + '\'' +
                ", model='" + getModel() + '\'' +
                ", year=" + getYear() +
                ", price=" + getPrice() +
                ", rented=" + getRented() +
                ", registrationPlate='" + getRegistrationPlate() + '\'' +
                '}';
    }
}
