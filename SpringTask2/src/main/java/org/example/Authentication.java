package org.example;

//Metoda sprawdzajaca czy dane logowania sa poprawne
public class Authentication {
    UserRepository userRepository;

    public Authentication(UserRepository userRepository) {
        this.userRepository=userRepository;
    }

    public User login(String login, String password){
        for (User user : userRepository.getUsers()){
            if (user.getLogin().equals(login)){
                if (user.getPassword().equals(password)){
                    System.out.println("Zostałeś zalogowany!");

                    return user;
                } else {
                    System.out.println("Podales nieprawidlowe haslo");
                    return null;
                }
            }
        }
        System.out.println("Nie istnieje takie konto");
        return null;
    }
}
