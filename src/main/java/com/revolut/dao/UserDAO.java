package com.revolut.dao;

import java.util.Collection;

import com.revolut.model.User;

public interface UserDAO {

    User create(String name, String email);
    User get(String id);
    User delete(String id);
    Collection<User> list();
    void deleteAll();
}
