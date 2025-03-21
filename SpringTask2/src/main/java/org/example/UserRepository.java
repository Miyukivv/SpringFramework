package org.example;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IUserRepository {
    private List<User> users;
    private static final String repositoryName = "userRepository.csv";
    private VehicleRepository vehicleRepository;

    private User loggedUser;

    public UserRepository(VehicleRepository vehicleRepository) {
        users = new ArrayList<>();
        this.vehicleRepository=vehicleRepository;
        initializeUsersFile();
        loadUsersFromFile();

        if (users.isEmpty()) {
            String passwordAdmin = "ad123";
            String hashPasswordAdmin= DigestUtils.sha256Hex(passwordAdmin);

            User admin = new User("admin",hashPasswordAdmin, "admin");
            users.add(admin);
            save(admin);
        }
    }

    @Override
    public User getUser() {
        return loggedUser;
    }
    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public void save(User userToSave) {
        if (userToSave != null) {
            boolean userExists = false;
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getLogin().equals(userToSave.getLogin())) {
                    users.set(i, userToSave);
                    userExists = true;
                    break;
                }
            }
            if (!userExists) {
                users.add(userToSave);
            }
        } else if (loggedUser != null){
            for (int i=0; i<users.size(); i++){
                if (users.get(i).getLogin().equals(loggedUser.getLogin())){
                    users.set(i,loggedUser);
                    break;
                }
            }
        }
        //nadpisywanie pliku, zapisywanie całej listy
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(repositoryName, false))) {
            for (User u : users) {
                if (u != null){
                    writer.write(u.to_csv());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Błąd zapisu klientów: " + e.getMessage());
        }
    }

    public void addUser(User user) {
        users.add(user);
    }

    private void initializeUsersFile() {
        File file = new File(repositoryName);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("Plik clients.csv został utworzony.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void loadUsersFromFile() {
        File file = new File(repositoryName);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader("userRepository.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 3) {
                    String login = parts[0];
                    String password = parts[1];
                    String role = parts[2];

                    User user = new User(login, password, role);
                    if (parts.length > 3 && !parts[3].equals("null")) {
                        String[] rentedPlatesOfVehicles = parts[3].split(";");
                        for (String plate : rentedPlatesOfVehicles) {
                            for (Vehicle vehicle : vehicleRepository.getVehicles()) {
                                if (vehicle.getRegistrationPlate().equals(plate)) {
                                    user.addRentedVehicle(vehicle.clone()); //kopia pojazdu
                                    break;
                                }
                            }
                        }
                    }
                    users.add(user);
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}

