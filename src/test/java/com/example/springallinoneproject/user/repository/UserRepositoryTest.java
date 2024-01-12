package com.example.springallinoneproject.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.springallinoneproject.user.entity.SocialType;
import com.example.springallinoneproject.user.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @DisplayName("유저를 이메일로 조회하는 데 성공")
    @Test
    void findByEmailTest() {
        String email = "test@test.com";
        User user = User.builder()
                .email(email)
                .password("1234")
                .socialType(SocialType.GOOGLE)
                .username("username")
                .build();
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail(email);

        assertThat(foundUser.isPresent()).isTrue();
        assertThat(foundUser.get()).isEqualTo(user);
    }

    @DisplayName("올바르지 않은 이메일로 유저를 조회할 수 없음")
    @Test
    void findNoneByInvalidEmail() {
        String email = "test@test.com";
        User user = User.builder()
                .email(email)
                .password("1234")
                .socialType(SocialType.GOOGLE)
                .username("username")
                .build();
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail("other@other.com");

        assertThat(foundUser.isPresent()).isFalse();
    }
}