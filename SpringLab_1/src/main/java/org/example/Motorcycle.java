package org.example;

public class Motorcycle extends Vehicle{
    private String category; //kategoriaPrawaJazdy
    public Motorcycle(String brand, String model, Integer year, Float price, Boolean rented, String registrationPlate, String category) {
        super(brand, model, year, price, rented, registrationPlate);
        this.category=category;
    }

    @Override
    public String toCsv() {
        return  getBrand() + "," + getModel() + "," + getYear() + "," + getPrice() + "," + getRented() + "," + getRegistrationPlate();
    }

    @Override
    public String toString() {
        return "Motorcycle{" +
                "brand='" + getBrand() + '\'' +
                ", model='" + getModel() + '\'' +
                ", year=" + getYear() +
                ", price=" + getPrice() +
                ", rented=" + getRented() +
                ", registrationPlate='" + getRegistrationPlate() + '\'' +
                ", category='" + category +
                '}';
    }
}
