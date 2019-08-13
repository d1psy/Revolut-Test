package com.revolut.controller;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.revolut.dao.AccountDAO;
import com.revolut.dao.UserDAO;
import com.revolut.error.ErrorResponse;
import com.revolut.model.Model;
import com.revolut.model.User;

public class UserController {

    private static UserDAO userDAO = Model.userDAO;
    private static AccountDAO accountDAO = Model.accountDAO;
    private static String emailRegex = "^(.+)@(.+)$";
    private static Pattern emailPattern = Pattern.compile(emailRegex);

    public static Collection<User> list() {
        return userDAO.list();
    }

    public static User create(String name, String email) {

        if (name.equals("") || email.equals("")) {
            throw new ErrorResponse("Name or email can not be empty",400);
        }

        Matcher matcher = emailPattern.matcher(email);

        if (!matcher.matches()) {
            throw new ErrorResponse("Invalid email", 400);
        }

        try {
            return userDAO.create(name, email);
        } catch (RuntimeException error) {
            throw new ErrorResponse(error.getMessage(), 409);
        }
    }

    public static User get(String id) {
        User user = userDAO.get(id);
        if (user == null) {
            throw new ErrorResponse("User already exists",404);
        }

        return user;
    }

    public static List getAccounts(String id) {
        User user = get(id);
        List accounts = accountDAO.listByUser(user.getId());
        if (accounts.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        return accounts;
    }

    public static void delete(String id) {
        User deletedUser;
        try {
            deletedUser = userDAO.delete(id);
        } catch (RuntimeException error) {
            throw new ErrorResponse("User cannot be deleted as he has accounts",400);
        }
        if (deletedUser == null) {
            throw new ErrorResponse("User not found", 404);
        }
    }
}