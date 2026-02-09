package com.zarinatta.zarinattaserver.user.controller;

import com.zarinatta.zarinattaserver.auth.service.JwtService;
import com.zarinatta.zarinattaserver.entity.User;
import com.zarinatta.zarinattaserver.exception.exception.NotFound.UserNotFoundException;
import com.zarinatta.zarinattaserver.exception.exception.ZarinattaException;
import com.zarinatta.zarinattaserver.user.dto.PhoneNumberRequest;
import com.zarinatta.zarinattaserver.user.dto.UserInfoResponse;
import com.zarinatta.zarinattaserver.user.dto.UserInputDto;
import com.zarinatta.zarinattaserver.user.dto.UserUpdateDto;
import com.zarinatta.zarinattaserver.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("")
    public ResponseEntity<Map<String, String>> saveUser(UserInputDto userInputDto) {
        String userId = userService.save(userInputDto);

        return ResponseEntity.ok(Map.of("userId", userId));
    }

    @DeleteMapping("")
    public ResponseEntity<Void> deleteUser(HttpServletRequest request) throws ZarinattaException {
        String userId = (String) request.getAttribute("userId");

        userService.delete(userId);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Void> savePhoneNumber(HttpServletRequest request, @RequestBody UserUpdateDto userUpdateDto) throws ZarinattaException {
        String accessToken = (String) request.getAttribute("accessToken");

        userService.update(accessToken, userUpdateDto);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping("/phone")
    public ResponseEntity<Void> saveUsersPhoneNumber(HttpServletRequest request, @RequestBody @Valid PhoneNumberRequest phoneNumber) throws ZarinattaException {
        String accessToken = (String) request.getAttribute("accessToken");
        User user = jwtService.findUserByToken(accessToken)
                .orElseThrow(() -> new UserNotFoundException("saveUsersPhoneNumber"));
        userService.saveUserPhoneNumber(user, phoneNumber);
        log.info(user.getUserNick() + "님의 전화번호가 저장 되었습니다.");
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getUserInfo(@CookieValue(name = "skt", required = false) String accessToken) {
        // 1. 쿠키에 accessToken이 없는 경우 401 응답
        if (accessToken == null || accessToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // 2. 토큰으로 사용자 정보를 조회
        UserInfoResponse response = userService.getUserInfoByToken(accessToken);
        // 3. DTO를 포함한 200 OK 응답을 반환합니다.
        return ResponseEntity.ok(response);
    }
}
