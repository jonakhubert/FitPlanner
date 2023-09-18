package com.fitplanner.authentication.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService underTest;

    @Test
    public void send_TestEmail_MessageSent() {
        // given
        var to = "recipient@example.com";
        var emailContent = "This is a test email.";
        var subject = "subject";
        MimeMessage mimeMessage = mock(MimeMessage.class);

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // when
        underTest.send(to, subject, emailContent);

        // then
        verify(javaMailSender, times(1)).createMimeMessage();
        verify(javaMailSender, times(1)).send(mimeMessage);
    }

    @Test
    public void send_TestEmail_MailSendException() {
        // given
        var to = "recipient@example.com";
        var emailContent = "This is a test email.";
        var subject = "subject";

        when(javaMailSender.createMimeMessage()).thenThrow(new MailSendException("Failed to send an email."));

        // then
        assertThrows(MailSendException.class, () -> underTest.send(to, subject, emailContent));
    }
}
