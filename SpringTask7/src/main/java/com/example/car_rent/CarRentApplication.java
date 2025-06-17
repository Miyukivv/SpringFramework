package com.example.car_rent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
/*TODO Dodanie logowania i zabezpieczenia endpointów do aplikacji wypożyczalni w Spring.
SpringSecurity + JWT
Zmiana wypożyczenia i zwracania pojazdów wykorzystując id zalogowanego użytkownika.
 */
@SpringBootApplication
@RestController
public class CarRentApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarRentApplication.class, args);
	}
	@GetMapping("/hello")
	public String helloWorld(){
		return String.format("Hello world");
	}
}
