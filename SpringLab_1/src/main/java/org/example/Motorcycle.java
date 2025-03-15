package org.example;

import java.util.Objects;

public class Motorcycle extends Vehicle{
    private String category; //kategoriaPrawaJazdy
    public Motorcycle(String brand, String model, Integer year, Float price, Boolean rented, String registrationPlate, String category) {
        super(brand, model, year, price, rented, registrationPlate);
        this.category=category;
    }
    //jak jest lista pojazdów w repozytorium, to żeby nie było tak że w mainie zwrocona zostanie lista, i zmienimy sobie jakis element, to oryginalna lista ma nie ulec zmianie
    //wstawiajac pojazd, zmieniamy w mainie, ten ktory jest w repozytorium w liscie to zeby nie ulegl zmianie

    @Override
    public Vehicle clone() {
        Vehicle motorcycle = new Motorcycle(getBrand(),getModel(),getYear(),getPrice(),getRented(),getRegistrationPlate(),category);
        return motorcycle;
    }

    @Override
    public String toCsv() {
        return  getBrand() + ";" + getModel() + ";" + getYear() + ";" + getPrice() + ";" + getRented() + ";" + getRegistrationPlate() + ";" + category;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Motorcycle motorcycle = (Motorcycle) o;
        return Objects.equals(getRegistrationPlate(), motorcycle.getRegistrationPlate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRegistrationPlate());
    }


}
