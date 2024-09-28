package com.example.subfit.dto.user;

import com.example.subfit.entity.user.Card;
import com.example.subfit.entity.user.Mobile;
import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class UserRegistrationDto {

    @NotBlank(message = "아이디는 필수 입력 사항입니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입력 사항입니다.")
    private String name;

    @NotNull(message = "성별은 필수 입력 사항입니다.")
    private Boolean gender;

    @NotNull(message = "나이는 필수 입력 사항입니다.")
    @Max(100)
    private Integer age;

    private String job;

    @NotNull(message = "통신사는 필수 입력 사항입니다.")
    private Mobile mobile;

    @NotNull(message = "카드는 필수 입력 사항입니다.")
    private Card card;
}
