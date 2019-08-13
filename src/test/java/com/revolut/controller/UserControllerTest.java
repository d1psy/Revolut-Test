package com.revolut.controller;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collection;
import com.revolut.error.ErrorResponse;
import com.revolut.model.Model;
import com.revolut.model.User;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UserControllerTest extends TestCase {

    @Before
    public void setUp() {
        Model.deleteAll();
    }

    @Test
    public void testCreateUser() {
        User user = UserController.create(
                "name", "email@test.com"
        );

        assertThat(user.getName(), is("name"));
        assertThat(user.getEmail(), is("email@test.com"));
    }

    @Test
    public void testAccountsShouldHaveDifferentIDs() {
        User user1 = UserController.create(
                "name1", "email1@test.com"
        );
        User user2 = UserController.create(
                "name2", "email2@test.com"
        );

        assertNotSame(user1.getId(), user2.getId());
    }

    @Test
    public void testThrowError400WhenCreatingUserWithEmptyName() {
        try {
            UserController.create("", "email@test.com");
        } catch (ErrorResponse error) {
            assertThat(error.getErrorCode(), is(400));
        }
    }

    @Test
    public void testThrowError409WhenCreatingUsersWithSameEmail() {
        UserController.create("name1", "email1@test.com");
        try {
            UserController.create("name2", "email12@test.com");
        } catch (ErrorResponse error) {
            assertThat(error.getErrorCode(), is(409));
        }
    }

    @Test
    public void testListUsers() {
        User user1 = UserController.create("name1", "emai11@test.com");
        User user2 = UserController.create("name2", "emai2@test.com");
        Collection<User> users = new ArrayList<>(UserController.list());

        assertThat(users.size(), is(2));
        assertThat(users, containsInAnyOrder(user1, user2));
    }

    @Test
    public void testThrowError404WhenGetAccountWithNonExistingID() {
        try {
            UserController.getAccounts("no-id");
        } catch (ErrorResponse error) {
            assertThat(error.getErrorCode(), is(404));
        }
    }

    @Test
    public void testDeleteUser() {
        User user = UserController.create("name", "email@test.com");
        assertThat(UserController.list().size(), is(1));

        UserController.delete(user.getId());
        assertThat(UserController.list().size(), is(0));
    }
}