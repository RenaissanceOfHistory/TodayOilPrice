package today.oil.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import today.oil.model.User;
import today.oil.utils.JWTUtil;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void cleanUp() {
        Set<String> keySet = redisTemplate.keys("*");
        Optional.ofNullable(keySet)
                .ifPresent(set -> {
                    set.forEach(System.out::println);

                    log.info("Clean up all keys");
                    log.info("Deleted: {}", redisTemplate.delete(keySet));
                });


    }

    @Test
    void login() {
        User user = User.builder()
                .username("Nicholas")
                .password("one peace")
                .build();
        String token = userService.login(user);
        if (Objects.nonNull(token)) {
            System.out.println(token);
            System.out.println(JWTUtil.getUserFrom(token));
            return;
        }
        System.out.println("用户不存在");
    }

    @Test
    void enroll() {
        User user = new User();
        user.setUsername("Nicholas");
        user.setPassword("one peace");
        Integer enroll = userService.enroll(user);
        Optional.ofNullable(enroll)
                .ifPresent(integer -> System.out.println("注册成功"));
    }

    @Test
    void addUserDetail() {

    }

    @Test
    void updateUserDetail() {
    }

    @Test
    void deleteUser() {

    }

    @Test
    void selectDetailByUid() {
    }
}