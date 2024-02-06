package com.pbl.loadtestweb.service;

import com.pbl.loadtestweb.domain.UserPrincipal;

public interface MessageQueueService {

  String createQueue(UserPrincipal userPrincipal);

  void sendMessage(String exchange, String routingKey, Object data);
}
