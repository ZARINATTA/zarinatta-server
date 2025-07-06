package com.zarinatta.zarinattaserver.user.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.zarinatta.zarinattaserver.auth.service.JwtService;
import com.zarinatta.zarinattaserver.entity.User;
import com.zarinatta.zarinattaserver.user.dto.PhoneNumberRequest;
import com.zarinatta.zarinattaserver.user.dto.UserInputDto;
import com.zarinatta.zarinattaserver.user.dto.UserUpdateDto;
import com.zarinatta.zarinattaserver.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @Test
    @DisplayName("회원가입을 합니다.")
    public void saveUser() throws Exception {
        // given
        UserInputDto inputDto = UserInputDto.builder()
                .userEmail("email@naver.com")
                .userNick("eric")
                .build();
        Mockito.when(userService.save(any(UserInputDto.class))).thenReturn("userId123");

        // when // then
        mockMvc.perform(
                        post("/api/v1/users")
                                .content(objectMapper.writeValueAsString(inputDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("userId123"));
    }

    @Test
    @DisplayName("회원 탈퇴")
    public void deleteUser() throws Exception {
        // given // when // then
        mockMvc.perform(
                        delete("/api/v1/users")
                                .requestAttr("userId", "userId123")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 가입 한 유저 전화번호 등록")
    public void savePhoneNumber() throws Exception {
        // given
        UserUpdateDto updateDto = UserUpdateDto.builder()
                .userPhone("01012345678")
                .userDeviceToken("deviceToken123")
                .build();
        String jsonRequest = objectMapper.writeValueAsString(updateDto);
        // when // then
        mockMvc.perform(
                        post("/api/v1/users/update")
                                .requestAttr("accessToken", "accessToken123")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 가입 한 유저 전화번호 등록 (New)")
    public void saveUsersPhoneNumber() throws Exception {
        PhoneNumberRequest phoneRequest = PhoneNumberRequest.builder()
                .countryCode("+82")
                .phoneNumber("01012345678")
                .build();

        // jwtService에서 accessToken에 대해 Dummy User 반환
        User dummyUser = User.builder().id("user1").build();
        Mockito.when(jwtService.findUserByToken("accessToken123")).thenReturn(Optional.of(dummyUser));

        // when & then - POST 요청 후 상태 코드 201(CREATED) 검증
        mockMvc.perform(post("/api/v1/users/phone")
                        .requestAttr("accessToken", "accessToken123")
                        .content(objectMapper.writeValueAsString(phoneRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}