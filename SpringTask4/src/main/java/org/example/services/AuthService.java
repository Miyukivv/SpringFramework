package org.example.services;

import org.example.models.User;
import org.example.repositories.UserRepository;
import org.example.repositories.impl.UserJsonRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String login, String password, String role) {
        Optional<User> userWhoExist = userRepository.findByLogin(login);

        if (userWhoExist.isPresent()) {
            System.out.println("Użytkownik o podanym loginie już istnieje");
            return null;
        }
        if (role == null || role.isBlank()) {
            role = "user";
        }
        String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User newUser = User.builder().login(login).password(hashPassword).role(role).build();

        userRepository.save(newUser);
        System.out.println("Pomyślnie ");

        return newUser;
    }

    public User login(String login, String password) {
        Optional<User> user = userRepository.findByLogin(login);
        if (user.isEmpty()) {
            System.out.println("Nie istnieje użytkownik o takim loginie");
        }
        User checkUser = user.get();
        if (BCrypt.checkpw(password, checkUser.getPassword())) {
            System.out.println("Udało Ci się zalogować!");
            return checkUser;
        } else {
            System.out.println("Podałeś nieprawidłowe hasło.");
            return null;
        }
    }
}


