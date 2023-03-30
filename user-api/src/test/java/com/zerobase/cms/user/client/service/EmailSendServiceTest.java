package com.zerobase.cms.user.client.service;

import com.zerobase.cms.user.client.MailgunClient;
import com.zerobase.cms.user.client.mailgun.SendMailForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailSendServiceTest {

    @Autowired
    private MailgunClient mailgunClient;

    @Test
    public void EmailTest() {
        //given
        SendMailForm form = SendMailForm.builder()
                .from("zerbase-test@zerobase.com")
                .to("didtdjwns111@naver.com")
                .subject("Test email from zerobase")
                .text("my test")
                .build();

        //when
        mailgunClient.sendEmail(form);

        //then

    }
}