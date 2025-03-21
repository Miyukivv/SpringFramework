package org.example;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CarTest {

    @Test
    public void testEquals() {
        Vehicle car1 = new Car("Toyota", "Corolla", 2020, 50000f, false, "ABC123");
        Vehicle car2 = new Car("Fiat", "Multipla", 1999, 435345f, true, "ABC123");
        Vehicle car3 = new Car("Honda", "Civic", 2021, 55000f, false, "XYZ789");
        Vehicle motorcycle = new Motorcycle("Honda", "Civic", 2021, 55000f, false, "XYZ789", "sasdfdsa");

        // Sprawdzenie equals()
        assertEquals(car1, car2);
        assertNotEquals(car1, car3);
        assertNotEquals(car3, motorcycle);
    }
    @Test
    public void testHashEquals() {
        Vehicle car1 = new Car("Toyota", "Corolla", 2020, 150.0f, false, "ABC123");
        Vehicle car2 = new Car("Toyota", "Corolla", 2020, 150.0f, false, "ABC123");
        Vehicle car3 = new Car("Toyota", "Corolla", 2020, 150.0f, false, "EHI123");

        assertEquals(car1.hashCode(),car2.hashCode());
        assertNotEquals(car1.hashCode(),car3.hashCode());
    }
}

