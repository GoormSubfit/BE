package com.example.subfit.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 아이디 (Primary Key)

    @Column(name = "user_id", length = 20, nullable = false, unique = true)
    private String userId;  // 아이디

    @Column(name = "password", length = 255, nullable = false)
    private String password;  // 비밀번호

    @Column(name = "name", length = 20, nullable = false)
    private String name;  // 이름

    @Column(name = "gender", nullable = false)
    private Boolean gender;  // 성별 (남자/여자, Boolean으로 설정)

    @Column(name = "age", nullable = false)
    private Integer age;  // 나이

    @Column(name = "job", length = 20)
    private String job;  // 직업

    @Enumerated(EnumType.STRING)
    @Column(name = "mobile", nullable = false)
    private Mobile mobile;  // 통신사 (Enum)

    @Enumerated(EnumType.STRING)
    @Column(name = "card", nullable = false)
    private Card card;  // 카드 (Enum)

    @Column(name = "profile_image", length = 100)
    private String profileImage;  // 프로필 사진
}

