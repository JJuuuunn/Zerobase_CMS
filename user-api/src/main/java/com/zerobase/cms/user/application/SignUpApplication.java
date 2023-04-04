package com.zerobase.cms.user.application;

import com.zerobase.cms.user.client.MailgunClient;
import com.zerobase.cms.user.client.mailgun.SendMailForm;
import com.zerobase.cms.user.domain.SignUpForm;
import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.domain.model.Seller;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import com.zerobase.cms.user.service.customer.SignUpCustomerService;
import com.zerobase.cms.user.service.seller.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignUpApplication {

    private final MailgunClient mailgunClient;
    private final SignUpCustomerService signUpCustomerService;
    private final SellerService sellerService;

    public String customerSignUp(SignUpForm form) {
        if (signUpCustomerService.isEmailExist(form.getEmail())) {
            //exception
            throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
        } else {
            Customer c = signUpCustomerService.signUp(form);
            LocalDateTime now = LocalDateTime.now();
            String code = getRandomCode();

            SendMailForm mailForm = SendMailForm.builder()
                    .from("zerbase-cms-test@test.com")
                    .to("didtdjwns111@naver.com")
                    .subject("Verification Email")
                    .text(getVerificationEmailBody(c.getEmail(), c.getName(), "customer", code))
                    .build();

            log.info("Send Email Result : " + mailgunClient.sendEmail(mailForm).getBody());

            signUpCustomerService.changeCustomerValidateEmail(c.getId(), code);
            return "회원가입에 성공하였습니다.";
        }
    }

    public String sellerSignUp(SignUpForm form) {
        if (sellerService.isEmailExist(form.getEmail())) {
            //exception
            throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
        } else {
            Seller seller = sellerService.signUp(form);
            LocalDateTime now = LocalDateTime.now();
            String code = getRandomCode();

            SendMailForm mailForm = SendMailForm.builder()
                    .from("zerbase-cms-test@test.com")
                    .to("didtdjwns111@naver.com")
                    .subject("Verification Email")
                    .text(getVerificationEmailBody(seller.getEmail(), seller.getName(), "seller", code))
                    .build();

            log.info("Send Email Result : " + mailgunClient.sendEmail(mailForm).getBody());

            sellerService.changeCustomerValidateEmail(seller.getId(), code);
            return "회원가입에 성공하였습니다.";
        }
    }

    private String getRandomCode() {
        return RandomStringUtils.random(10, true, true);
    }

    private String getVerificationEmailBody(String email, String name, String type, String code) {
        StringBuilder sb = new StringBuilder();
        return sb.append("Hello ").append(name).append("! Please Click Link for verification.\n\n")
                .append("http://localhost:8081/signup/" + type + "/verify?email=")
                .append(email)
                .append("&code=")
                .append(code)
                .toString();
    }

    public void customerVerify(String email, String code) {
        signUpCustomerService.verifyEmail(email, code);
    }
    public void sellerVerify(String email, String code) {
        sellerService.verifyEmail(email, code);
    }
}
