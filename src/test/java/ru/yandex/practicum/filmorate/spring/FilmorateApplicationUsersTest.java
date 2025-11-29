package ru.yandex.practicum.filmorate.spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
public class FilmorateApplicationUsersTest {
    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = null;
    }

    @Test
    void getUsersList() {
        User user = User.builder()
                .email("test@test.net")
                .login("testUser")
                .birthday(LocalDate.of(1991, 1, 1))
                .build();
        userController.addNewUser(user);

        List<User> users = userController.getUsers().stream().toList();
        Assertions.assertNotNull(users);
        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals(user, users.getFirst());
    }

    @Test
    void addUserToFriend() {
        User user1 = User.builder()
                .email("test1@test.net")
                .login("testUser1")
                .birthday(LocalDate.of(1991, 1, 1))
                .build();
        User user2 = User.builder()
                .email("test2@test.net")
                .login("testUser2")
                .birthday(LocalDate.of(1991, 1, 1))
                .build();
        userController.addNewUser(user1);
        userController.addNewUser(user2);

        userController.addUserToFriend(user1.getId(), user2.getId());
        Assertions.assertEquals(1, user1.getFriends().size());
        Assertions.assertEquals(1, user2.getFriends().size());
        Assertions.assertEquals(2, user1.getFriends().stream().toList().getFirst());
        Assertions.assertEquals(1, user2.getFriends().stream().toList().getFirst());
    }

    @Test
    void getUsersFriendsList() {
        User user1 = User.builder()
                .email("test1@test.net")
                .login("testUser1")
                .birthday(LocalDate.of(1991, 1, 1))
                .build();
        User user2 = User.builder()
                .email("test2@test.net")
                .login("testUser2")
                .birthday(LocalDate.of(1991, 1, 1))
                .build();
        User user3 = User.builder()
                .email("test3@test.net")
                .login("testUser3")
                .birthday(LocalDate.of(1991, 1, 1))
                .build();
        User user4 = User.builder()
                .email("test4@test.net")
                .login("testUser4")
                .birthday(LocalDate.of(1991, 1, 1))
                .build();

        userController.addNewUser(user1);
        userController.addNewUser(user2);
        userController.addNewUser(user3);
        userController.addNewUser(user4);

        userController.addUserToFriend(user1.getId(), user2.getId());
        userController.addUserToFriend(user1.getId(), user4.getId());

//        List<User> friendsList = userController.getFriends(user1.getId()).stream().toList();
//        Assertions.assertEquals(2, friendsList.size());
//        Assertions.assertEquals(user2, friendsList.getFirst());
//        Assertions.assertEquals(user4, friendsList.getLast());
    }

    @Test
    void deleteUserFromFriends() {
        User user1 = User.builder()
                .email("test1@test.net")
                .login("testUser1")
                .birthday(LocalDate.of(1991, 1, 1))
                .build();
        User user2 = User.builder()
                .email("test2@test.net")
                .login("testUser2")
                .birthday(LocalDate.of(1991, 1, 1))
                .build();
        User user3 = User.builder()
                .email("test3@test.net")
                .login("testUser3")
                .birthday(LocalDate.of(1991, 1, 1))
                .build();
        User user4 = User.builder()
                .email("test4@test.net")
                .login("testUser4")
                .birthday(LocalDate.of(1991, 1, 1))
                .build();

        userController.addNewUser(user1);
        userController.addNewUser(user2);
        userController.addNewUser(user3);
        userController.addNewUser(user4);

        userController.addUserToFriend(user1.getId(), user2.getId());
        userController.addUserToFriend(user1.getId(), user4.getId());

        userController.removeUserFromFriends(user1.getId(), user2.getId());

//        List<User> friendsList = userController.getFriends(user1.getId()).stream().toList();
//        Assertions.assertEquals(1, friendsList.size());
//        Assertions.assertEquals(user4, friendsList.getFirst());
    }

    @Test
    void checkCommonFriends() {
        User user1 = User.builder()
                .email("test1@test.net")
                .login("testUser1")
                .birthday(LocalDate.of(1991, 1, 1))
                .build();
        User user2 = User.builder()
                .email("test2@test.net")
                .login("testUser2")
                .birthday(LocalDate.of(1991, 1, 1))
                .build();
        User user3 = User.builder()
                .email("test3@test.net")
                .login("testUser3")
                .birthday(LocalDate.of(1991, 1, 1))
                .build();
        User user4 = User.builder()
                .email("test4@test.net")
                .login("testUser4")
                .birthday(LocalDate.of(1991, 1, 1))
                .build();

        userController.addNewUser(user1);
        userController.addNewUser(user2);
        userController.addNewUser(user3);
        userController.addNewUser(user4);

        userController.addUserToFriend(user1.getId(), user2.getId());
        userController.addUserToFriend(user1.getId(), user3.getId());
        userController.addUserToFriend(user4.getId(), user3.getId());

//        List<User> user1friendsList = userController.getCommonFriends(user1.getId(), user4.getId()).stream().toList();
//        Assertions.assertEquals(1, user1friendsList.size());
//        Assertions.assertEquals(user3, user1friendsList.getFirst());
//
//        List<User> user4friendsList = userController.getCommonFriends(user4.getId(), user1.getId()).stream().toList();
//        Assertions.assertEquals(1, user4friendsList.size());
//        Assertions.assertEquals(user3, user4friendsList.getFirst());
    }

    @Test
    void checkErrorWhenAddSelfIdToFriend() {
        User user1 = User.builder()
                .email("test1@test.net")
                .login("testUser1")
                .birthday(LocalDate.of(1991, 1, 1))
                .build();
        userController.addNewUser(user1);
        Assertions.assertThrows(ValidationException.class,
                () -> userController.addUserToFriend(user1.getId(), user1.getId()));
    }
}
