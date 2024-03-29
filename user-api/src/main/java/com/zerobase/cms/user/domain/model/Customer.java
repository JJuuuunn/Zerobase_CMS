package com.zerobase.cms.user.domain.model;

import com.zerobase.cms.user.domain.SignUpForm;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AuditOverride(forClass = BaseEntity.class)
public class Customer extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true)
    private String email;
    private String name;
    private String password;
    private String phone;
    private LocalDate birth;

    @Column(columnDefinition = "int default 0")
    private Integer balance;

    private LocalDateTime verifyExpiredAt;
    private String verificationCode;
    private boolean verify;

    @Builder
    public Customer(String email, String name, String password, String phone, LocalDate birth, LocalDateTime verifyExpiredAt, String verificationCode, boolean verify, Integer balance) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.birth = birth;
        this.verifyExpiredAt = verifyExpiredAt;
        this.verificationCode = verificationCode;
        this.verify = verify;
        this.balance = balance;
    }

    public static Customer from(SignUpForm form) {
        return Customer.builder()
                .email(form.getEmail().toLowerCase(Locale.ROOT))
                .name(form.getName())
                .password(form.getPassword())
                .phone(form.getPhone())
                .birth(form.getBirth())
                .verify(false)
                .build();
    }

    public void setVerificationCode(String verificationCode, LocalDateTime verifyExpiredAt) {
        this.verificationCode = verificationCode;
        this.verifyExpiredAt = verifyExpiredAt;
    }

    public void verificationSuccess(boolean verify) {
        this.verify = verify;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }
}
