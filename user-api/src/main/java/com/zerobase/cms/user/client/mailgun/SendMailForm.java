package com.zerobase.cms.user.client.mailgun;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SendMailForm {

    private String from;
    private String to;
    private String subject;
    private String text;

    @Builder
    public SendMailForm(String from, String to, String subject, String text) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.text = text;
    }
}
