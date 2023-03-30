package com.zerobase.cms.user.service;

import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.domain.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SignUpCustomerServiceTest {

    @Autowired
    private SignUpCustomerService service;

    @Test
    public void SignUp() {
        //given
        SignUpForm form = SignUpForm.builder()
                .email("abcd@test.com")
                .name("test01")
                .password("1")
                .phone("01011111111")
                .birth(LocalDate.now())
                .build();

        //when
        Customer c = service.signUp(form);

        //then
        Assert.notNull(c);
        assertThat(c.getEmail()).isEqualTo("abcd@test.com");
        assertThat(c.getName()).isEqualTo("test01");
        assertThat(c.getPassword()).isEqualTo("1");
        assertThat(c.getPhone()).isEqualTo("01011111111");

    }
}