package com.pbl.loadpulse.service;

import com.pbl.loadpulse.domain.UserPrincipal;

public interface MessageQueueService {

  String createQueue(UserPrincipal userPrincipal);

  void sendMessage(String exchange, String routingKey, Object data);
}
