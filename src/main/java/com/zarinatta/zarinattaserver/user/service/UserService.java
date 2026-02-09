package com.zarinatta.zarinattaserver.user.service;

import com.zarinatta.zarinattaserver.auth.service.JwtService;
import com.zarinatta.zarinattaserver.entity.User;
import com.zarinatta.zarinattaserver.exception.ErrorCode;
import com.zarinatta.zarinattaserver.exception.exception.NotFound.UserNotFoundException;
import com.zarinatta.zarinattaserver.exception.exception.ZarinattaException;
import com.zarinatta.zarinattaserver.user.dto.PhoneNumberRequest;
import com.zarinatta.zarinattaserver.user.dto.UserInfoResponse;
import com.zarinatta.zarinattaserver.user.dto.UserInputDto;
import com.zarinatta.zarinattaserver.user.dto.UserUpdateDto;
import com.zarinatta.zarinattaserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtService jwtService;

    private final UserRepository userRepository;

    public String save(UserInputDto userInputDto) {
        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .userEmail(userInputDto.userEmail())
                .userNick(userInputDto.userNick())
                .build();

        return userRepository.save(user).getId();
    }

    public void update(String accessToken, UserUpdateDto userUpdateDto) throws ZarinattaException {
        String userId = jwtService.decodeAccessToken(accessToken);

        if(userId == null) {
            throw new ZarinattaException(ErrorCode.INVALID_TOKEN_ERROR);
        }

        userRepository.update(userId, userUpdateDto.userDeviceToken(), userUpdateDto.userPhone());
    }

    public void delete(String userId) {
        userRepository.deleteById(userId);
    }

    public String findUserIdByEmail(String email) {
        return userRepository.findUserIdByEmail(email);
    }

    public String findUserEmailById(String userId) {
        return userRepository.findUserEmailById(userId);
    }

    public void saveUserPhoneNumber(User user, PhoneNumberRequest phoneNumber) {
        user.saveUserPhoneNumber(phoneNumber.getFullPhoneNumber());
        userRepository.save(user);
    }

    public UserInfoResponse getUserInfoByToken(String accessToken){
        // 1. 토큰으로 사용자 정보를 조회
        User user = jwtService.findUserByToken(accessToken)
                .orElseThrow(() -> new UserNotFoundException("유효하지 않은 토큰으로 사용자를 찾을 수 없습니다."));

        // 2. 조회된 사용자 정보를 DTO로 변환
        UserInfoResponse response = UserInfoResponse.builder()
                .id(user.getId())
                .email(user.getUserEmail())
                .nickname(user.getUserNick())
                .phoneNumber(user.getUserPhoneNumber())
                .build();
        return response;
    }
}
