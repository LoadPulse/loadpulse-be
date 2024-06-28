package com.pbl.loadpulse.email.service.impl;

import com.pbl.loadpulse.email.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender javaMailSender;

  @Override
  @Async("threadPoolTaskExecutor")
  public void sendMailConfirmRegister(String email, UUID confirmToken) {
    String subject = "Confirm your account";
    String confirmationUrl = "http://yourwebsite.com/confirm?token=" + confirmToken.toString();
    String message =
        "Please confirm your account by clicking the following link: <a href=\""
            + confirmationUrl
            + "\">Confirm Account</a>";

    this.sendMail(email, subject, message);
  }

  private void sendMail(String email, String subject, String detail) {
    try {
      MimeMessage message = javaMailSender.createMimeMessage();

      MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "utf-8");
      messageHelper.setFrom("kinphan189@gmail.com");
      messageHelper.setTo(email);
      messageHelper.setSubject(subject);
      messageHelper.setText(detail, true);
      javaMailSender.send(message);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
