package com.zarinatta.zarinattaserver.user.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PhoneNumberRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void 유효한_전화번호면_Valid_성공() {
        // given
        PhoneNumberRequest phoneNumberRequest = PhoneNumberRequest.builder()
                .countryCode("+82")
                .phoneNumber("01012345678")
                .build();
        // when
        Set<ConstraintViolation<PhoneNumberRequest>> violations = validator.validate(phoneNumberRequest);
        // then
        assertThat(violations).isEmpty();
        assertThat(phoneNumberRequest.getFullPhoneNumber()).isEqualTo("+821012345678");
    }

    @Test
    public void 하이픈_전화번호_입력이면_Valid() {
        // given
        PhoneNumberRequest phoneNumberRequest = PhoneNumberRequest.builder()
                .countryCode("+82")
                .phoneNumber("010-1234-5678")
                .build();
        // when
        Set<ConstraintViolation<PhoneNumberRequest>> violations = validator.validate(phoneNumberRequest);
        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).contains("유효한 전화번호 형식이 아닙니다.");
    }

    @Test
    public void 잘못된_국가코드_입력시_Valid() {
        // given
        PhoneNumberRequest phoneNumberRequest = PhoneNumberRequest.builder()
                .countryCode("82")
                .phoneNumber("01012345678")
                .build();
        // when
        Set<ConstraintViolation<PhoneNumberRequest>> violations = validator.validate(phoneNumberRequest);
        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).contains("유효한 나라 코드 형식이 아닙니다.");
    }
}
