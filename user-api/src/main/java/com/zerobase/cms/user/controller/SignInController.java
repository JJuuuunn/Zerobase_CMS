package com.zerobase.cms.user.controller;

import com.zerobase.cms.user.application.SignInApplication;
import com.zerobase.cms.user.application.SignUpApplication;
import com.zerobase.cms.user.domain.SignInForm;
import com.zerobase.cms.user.domain.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signIn")
@RequiredArgsConstructor
public class SignInController {

    private final SignInApplication signInApplication;

    @PostMapping("/customer")
    public ResponseEntity<String> signInCustomer(@RequestBody SignInForm form) {
        return ResponseEntity.ok(signInApplication.customerLoginToken(form));
    }

}
