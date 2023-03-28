package com.zerobase.cms.user.service.test;

import com.zerobase.cms.user.client.MailgunClient;
import com.zerobase.cms.user.client.mailgun.SendMailForm;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSendService {

    private final MailgunClient mailgunClient;

    public String sendEmail() {

        SendMailForm form = SendMailForm.builder()
                .from("zerbase-test@zerobase.com")
                .to("didtdjwns111@naver.com")
                .subject("Test email from zerobase")
                .text("my test")
                .build();

        return mailgunClient.sendEmail(form).getBody();
    }
}
