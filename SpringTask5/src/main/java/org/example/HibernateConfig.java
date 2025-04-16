package org.example;

import lombok.Getter;
import org.example.models.Rental;
import org.example.models.User;
import org.example.models.Vehicle;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
public class HibernateConfig {
    @Getter
    private static final SessionFactory sessionFactory;
    static {
        try {
            String dbUrl = System.getenv("DB_URL");
            if (dbUrl == null || dbUrl.isEmpty()) {
                throw new RuntimeException("DB_URL not set in environment variables");
            }
            System.out.println("DB_URL: " + dbUrl); // Sprawdzenie wartości zmiennej środowiskowej
            Configuration configuration = new Configuration();
            configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
            configuration.setProperty("hibernate.connection.url", dbUrl);
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.format_sql", "true");
            configuration.setProperty("hibernate.hbm2ddl.auto", "validate");
            configuration.registerTypeOverride(new JsonBinaryType(), new String[]{"jsonb"});
            configuration.addAnnotatedClass(Vehicle.class);
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Rental.class);
            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError("Inicialize Hibernate ERROR: " + ex);
        }
    }
}
