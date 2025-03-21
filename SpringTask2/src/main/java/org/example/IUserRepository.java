package org.example;

import java.util.List;

public interface IUserRepository {
    public  User getUser();
    public List<User> getUsers();
    public void save(User user);
}
