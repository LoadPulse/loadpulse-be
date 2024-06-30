package com.pbl.loadpulse.email.service;

import java.util.UUID;

public interface EmailService {

  void sendMailConfirmRegister(String email, UUID confirmToken);
}
