package com.revolut.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.revolut.model.Model;
import com.revolut.model.User;

public class UserDAOImpl implements UserDAO {

    private Map<String, User> emailToUser = new HashMap<>();
    private SortedMap<String, User> idToUser = new TreeMap<>();

    public User create(String name, String email) {
        User user = new User(name, email);
        if (emailToUser.get(email) != null) {
            throw new RuntimeException(email);
        }

        emailToUser.put(user.getEmail(), user);
        idToUser.put(user.getId(), user);
        return user;
    }

    public User get(String id) {
        return idToUser.get(id);
    }

    public User delete(String id) {
        List accounts = Model.accountDAO.listByUser(id);
        if (accounts != null && accounts.size() > 0) {
            throw new RuntimeException(id);
        }
        User user = idToUser.remove(id);
        if (user != null) {
            emailToUser.remove(user.getEmail());
        }

        return user;
    }

    public Collection<User> list() {
        return idToUser.values();
    }

    public void deleteAll() {
        emailToUser.clear();
        idToUser.clear();
    }

}
