package org.example.app;

import org.example.services.impl.AuthHibernateService;
import org.example.HibernateConfig;
import org.example.services.impl.RentalHibernateService;
import org.example.services.impl.VehicleHibernateService;
import org.example.repositories.impl.RentalHibernateRepository;
import org.example.repositories.impl.UserHibernateRepository;
import org.example.repositories.impl.VehicleHibernateRepository;
import org.hibernate.Session;

public class Main {
    public static void main(String[] args) {
        Session session = HibernateConfig.getSessionFactory().openSession();


        VehicleHibernateRepository vehicleRepo = new VehicleHibernateRepository(session);
        RentalHibernateRepository rentalRepo = new RentalHibernateRepository(session);
        UserHibernateRepository userRepo = new UserHibernateRepository(session);

        VehicleHibernateService vehicleHibernateService = new VehicleHibernateService(vehicleRepo, rentalRepo);  // Przekazujemy oba repozytoria
        RentalHibernateService rentalHibernateService = new RentalHibernateService(rentalRepo, vehicleRepo, userRepo);
        AuthHibernateService authService = new AuthHibernateService(userRepo);

        org.example.app.App app = new org.example.app.App(authService, vehicleHibernateService, rentalHibernateService);
        app.run();
    }
}