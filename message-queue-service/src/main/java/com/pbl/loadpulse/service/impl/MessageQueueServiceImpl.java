package com.pbl.loadpulse.service.impl;

import com.pbl.loadpulse.domain.UserPrincipal;
import com.pbl.loadpulse.service.MessageQueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageQueueServiceImpl implements MessageQueueService {

  private final ConnectionFactory connectionFactory;

  private final RabbitTemplate rabbitTemplate;

  @Override
  public String createQueue(UserPrincipal userPrincipal) {
    RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);

    String queueName = userPrincipal.getId().toString();
    rabbitAdmin.declareQueue(new Queue(queueName, false, false, false));

    String exchangeName = userPrincipal.getId().toString();
    rabbitAdmin.declareExchange(new DirectExchange(exchangeName, false, false));
    rabbitAdmin.declareBinding(
        new Binding(
            queueName,
            Binding.DestinationType.QUEUE,
            exchangeName,
            userPrincipal.getId().toString(),
            null));

    return queueName;
  }

  @Override
  @Async("threadPoolTaskExecutor")
  public void sendMessage(String exchange, String routingKey, Object data) {
    try {
      rabbitTemplate.convertAndSend(exchange, routingKey, data);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }
}
