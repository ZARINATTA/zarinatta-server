package com.zarinatta.zarinattaserver.user.service;

import com.zarinatta.zarinattaserver.entity.User;
import com.zarinatta.zarinattaserver.user.dto.PhoneNumberRequest;
import com.zarinatta.zarinattaserver.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void 유저의_휴대폰번호를_저장한다(){
        //given
        createDummyUser();
        User user = userRepository.findById("1").get();
        PhoneNumberRequest request = PhoneNumberRequest.builder()
                .countryCode("+82")
                .phoneNumber("01012345678")
                .build();
        //when
        userService.saveUserPhoneNumber(user, request);
        String phoneNumber = userRepository.findById("1").get().getUserPhoneNumber();
        //then
        assertThat(phoneNumber).isEqualTo("+821012345678");

    }

    @Transactional
    void createDummyUser() {
        User user1 = User.builder()
                .id("1")
                .userEmail("1234@kakao.com")
                .userNick("dummy1")
                .userPhoneNumber("dummy")
                .build();
        User user2 = User.builder()
                .id("2")
                .userEmail("12345@kakao.com")
                .userNick("dummy2")
                .userPhoneNumber("dummy")
                .build();
        User user3 = User.builder()
                .id("3")
                .userEmail("123456@kakao.com")
                .userNick("dummy3")
                .userPhoneNumber("dummy")
                .build();
        userRepository.saveAll(List.of(user1, user2, user3));
    }
}
