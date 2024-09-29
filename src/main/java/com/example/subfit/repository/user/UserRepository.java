package com.example.subfit.repository.user;

import com.example.subfit.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 사용자 ID로 사용자 존재 여부를 확인하는 메서드 정의
    boolean existsByUserId(String userId);

    Optional<User> findByUserId(String userId);
}