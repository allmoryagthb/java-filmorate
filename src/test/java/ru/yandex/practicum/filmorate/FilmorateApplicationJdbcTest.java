package ru.yandex.practicum.filmorate;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
@AutoConfigureTestDatabase
@AllArgsConstructor(onConstructor_ = @Autowired)
public class FilmorateApplicationJdbcTest {
//    private  UserDbStorage userDbStorage;
//    private  RatingDbStorage ratingDbStorage;
//    private  GenreDbStorage genreDbStorage;
//
//    @Test
//    public void testFindUserById() {
//        User userDb = User.builder()
//                .email("test123@test.net")
//                .login("testUser123")
//                .birthday(LocalDate.of(1991, 1, 1))
//                .build();
//        userDbStorage.addNewUser(userDb);
//        Optional<User> userOptional = userDbStorage.getUserById(userDb.getId());
//        assertThat(userOptional)
//                .isPresent()
//                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("id", userDb.getId()));
//    }
//
//    @Test
//    public void testFindGenreById() {
//        Optional<Genre> genreOptional = Optional.ofNullable(genreDbStorage.getGenreById(1L));
//        assertThat(genreOptional)
//                .isPresent()
//                .hasValueSatisfying(genre -> assertThat(genre).hasFieldOrPropertyWithValue("id", 1L));
//    }
//
//    @Test
//    public void testFindRatingById() {
//        Optional<Rating> ratingOptional = Optional.ofNullable(ratingDbStorage.getRatingById(1L));
//        assertThat(ratingOptional)
//                .isPresent()
//                .hasValueSatisfying(rating -> assertThat(rating).hasFieldOrPropertyWithValue("id", 1L));
//    }
}
