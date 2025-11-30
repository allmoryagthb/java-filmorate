package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.dal.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.dal.dto.UserDto;
import ru.yandex.practicum.filmorate.dal.mappers.UserMapper;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
public class FilmorateApplicationUsersTest {
    @Autowired
    private UserController userController;
    @Autowired
    private UserDbStorage userDbStorage;

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
        Assertions.assertEquals(user, users.get(Math.toIntExact(user.getId()) - 1));
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

        List<UserDto> user1Friends = userController.getFriends(user1.getId()).stream().toList();
        List<UserDto> user2Friends = userController.getFriends(user2.getId()).stream().toList();

        Assertions.assertEquals(1, user1Friends.size());
        Assertions.assertEquals(0, user2Friends.size());
        Assertions.assertEquals(UserMapper.jpaToDto(user2), user1Friends.getFirst());
    }

    @Test
    void checkUsersFriendshipStatusAfter() {
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
        Assertions.assertFalse(userDbStorage.getFriendshipStatus(user1.getId(), user2.getId()));

        userController.addUserToFriend(user2.getId(), user1.getId());
        Assertions.assertTrue(userDbStorage.getFriendshipStatus(user1.getId(), user2.getId()));
        Assertions.assertTrue(userDbStorage.getFriendshipStatus(user2.getId(), user1.getId()));

        userController.removeUserFromFriends(user1.getId(), user2.getId());
        Assertions.assertFalse(userDbStorage.getFriendshipStatus(user2.getId(), user1.getId()));
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
        userController.addUserToFriend(user2.getId(), user3.getId());
        userController.addUserToFriend(user2.getId(), user4.getId());

        Assertions.assertEquals(List.of(UserMapper.jpaToDto(user3)), userController.getCommonFriends(user1.getId(),user2.getId()));
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
