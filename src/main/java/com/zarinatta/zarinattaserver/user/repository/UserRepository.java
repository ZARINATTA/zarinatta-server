package com.zarinatta.zarinattaserver.user.repository;

import com.zarinatta.zarinattaserver.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE SET USER_DEVICE_TOKEN = COALESCE(:token, USER_DEVICE_TOKEN),\n" +
            "USER_PHONE = COALESCE(:phone, USER_PHONE) WHERE USER_ID = :id", nativeQuery = true)
    void update(@Param("id") String id, @Param("token") String token, @Param("phone") String phone);

    @Query(value = "select USER_ID from users where USER_EMAIL = :email", nativeQuery = true)
    String findUserIdByEmail(@Param("email") String email);

    @Query(value = "select USER_EMAIL from users where USER_ID = :id", nativeQuery = true)
    String findUserEmailById(@Param("id") String userId);

}


