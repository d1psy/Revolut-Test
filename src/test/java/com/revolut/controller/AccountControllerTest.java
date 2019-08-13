package com.revolut.controller;

import com.revolut.model.Account;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import java.util.Collection;
import com.revolut.error.ErrorResponse;
import com.revolut.model.Model;
import com.revolut.model.User;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;

public class AccountControllerTest extends TestCase {

    private User user1;
    private User user2;

    @Before
    public void setUp() {
        Model.deleteAll();
        user1 = UserController.create(
                "name1", "email1@test.com"
        );
        user2 = UserController.create(
                "name2", "email2@test.com"
        );
    }

    @Test
    public void testCreateAccount() {
        Account account = AccountController.create(user1.getId(), 10);
        assertThat(account.getUserId(), is(user1.getId()));
        assertThat(account.getBalance(), is(10.0));
    }

    @Test
    public void testThrowErrorCode404WhenUserDoesNotExist() {
        try {
            AccountController.create("no-id", 10);
        } catch (ErrorResponse error) {
            assertThat(error.getErrorCode(), is(404));
        }
    }

    @Test
    public void testThrowErrorCode400WhenCreateAnAccountWithNegativeBalance() {
        try {
            AccountController.create(user1.getId(), -1);
        } catch (ErrorResponse error) {
            assertThat(error.getErrorCode(), is(400));
        }
    }

    @Test
    public void testReturnAnAccount() {
        Account account = AccountController.create(user1.getId(), 1);
        assertThat(AccountController.get(account.getId()), is(account));
    }

    @Test
    public void testReturnErrorCode404WhenAccountDoesNotExist() {

        try {
            AccountController.get("no-id");
        } catch (ErrorResponse error) {
            assertThat(error.getErrorCode(), is(404));
        }
    }

    @Test
    public void testTransferMoneyFromOneAccountToAnother() {
        Account from = AccountController.create(user1.getId(), 10);
        Account to = AccountController.create(user2.getId(), 0);
        AccountController.transfer(from.getId(), to.getId(), 4);
        assertThat(from.getBalance(), is(6.0));
        assertThat(to.getBalance(), is(4.0));
    }

    @Test
    public void testThrowError400WhenTransferNegativeAmountOfMoney() {
        Account from = AccountController.create(user1.getId(), 5);
        Account to = AccountController.create(user2.getId(), 5);

        try {
            AccountController.transfer(from.getId(), to.getId(), -5);
        } catch (ErrorResponse error) {
            assertThat(error.getErrorCode(), is(400));
        }
    }

    @Test
    public void testThrowError400WhenTryingToTransferMoreMoneyThanPossible() {
        Account from = AccountController.create(user1.getId(), 10);
        Account to = AccountController.create(user2.getId(), 10);

        try {
            AccountController.transfer(from.getId(), to.getId(), 20);
        } catch (ErrorResponse error) {
            assertThat(error.getErrorCode(), is(400));
        }
    }

    @Test
    public void testThrowError404WhenTransferringMoneyToNonExistingAccount() {
        Account from = AccountController.create(user1.getId(), 10);

        try {
            AccountController.transfer(from.getId(), "no-id", 10);
        } catch (ErrorResponse error) {
            assertThat(error.getErrorCode(), is(404));
        }
    }

    @Test
    public void testThrowError400WhenTransferringMoneyToTheSameAccount() {
        Account from = AccountController.create(user1.getId(), 10);

        try {
            AccountController.transfer(from.getId(), from.getId(), 10);
        } catch (ErrorResponse error) {
            assertThat(error.getErrorCode(), is(400));
        }
    }
}