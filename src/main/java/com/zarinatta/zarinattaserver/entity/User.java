package com.zarinatta.zarinattaserver.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "users")
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "USER_ID")
    private String id;

    @Column(name = "USER_EMAIL", nullable = false, unique = true)
    private String userEmail;

    @Column(name = "USER_NICK", nullable = false)
    private String userNick;

    @Column(name = "USER_PHONE", nullable = false)
    private String userPhoneNumber;

    @Column(name = "USER_DEVICE_TOKEN")
    private String userDeviceToken;

    public void saveUserPhoneNumber(String phoneNumber) {
        this.userPhoneNumber = phoneNumber;
    }
}
