package org.example;

import java.util.Objects;

public class Car extends Vehicle {
    @Override
    public Vehicle clone() {
        Vehicle car = new Car(getBrand(), getModel(), getYear(),getPrice(),getRented(),getRegistrationPlate());
        return car;
    }

    public Car(String brand, String model, Integer year, Float price, Boolean rented, String registrationPlate) {
        super(brand, model, year, price, rented, registrationPlate);
    }

    @Override
    public String toCsv() {
        return  getBrand() + ";" + getModel() + ";" + getYear() + ";" + getPrice() + ";" + getRented() + ";" + getRegistrationPlate();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(getRegistrationPlate(), car.getRegistrationPlate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRegistrationPlate());
    }



}
