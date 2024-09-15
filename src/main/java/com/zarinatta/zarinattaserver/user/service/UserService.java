package com.zarinatta.zarinattaserver.user.service;

import com.zarinatta.zarinattaserver.auth.service.JwtService;
import com.zarinatta.zarinattaserver.entity.User;
import com.zarinatta.zarinattaserver.exception.exception.ZarinattaException;
import com.zarinatta.zarinattaserver.exception.ErrorCode;
import com.zarinatta.zarinattaserver.user.dto.UserInputDto;
import com.zarinatta.zarinattaserver.user.repository.UserRepository;
import com.zarinatta.zarinattaserver.user.dto.UserUpdateDto;
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
                .userEmail(userInputDto.getUserEmail())
                .userNick(userInputDto.getUserNick())
                .build();

        return userRepository.save(user).getId();
    }

    public void update(String accessToken, UserUpdateDto userUpdateDto) throws ZarinattaException {
        String userId = jwtService.decodeAccessToken(accessToken);

        if(userId == null) {
            throw new ZarinattaException(ErrorCode.INVALID_TOKEN_ERROR);
        }

        userRepository.update(userId, userUpdateDto.getUserDeviceToken(), userUpdateDto.getUserPhone());
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

}
