package com.revolut.model;

import com.revolut.dao.UserDAO;
import com.revolut.dao.UserDAOImpl;
import com.revolut.dao.AccountDAO;
import com.revolut.dao.AccountDAOImpl;

public class Model {

    public static UserDAO userDAO = new UserDAOImpl();
    public static AccountDAO accountDAO = new AccountDAOImpl();

    public static void deleteAll() {
        userDAO.deleteAll();
        accountDAO.deleteAll();
    }

}
