package com.zerobase.cms.user.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpForm {

    private String email;
    private String name;
    private String password;
    private String phone;
    private LocalDate birth;

    @Builder
    public SignUpForm(String email, String name, String password, String phone, LocalDate birth) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.birth = birth;
    }
}
