package com.example.demo.service.impl;

import com.example.demo.model.User;
import com.example.demo.service.NotificationService;
import org.springframework.stereotype.Service;
import java.util.logging.Logger;

/**
 * Implementação simples que apenas loga a notificação.
 * Pode ser substituída por um sender SMTP, push, etc.
 */
@Service
public class EmailNotificationServiceImpl implements NotificationService {

    private static final Logger LOGGER = Logger.getLogger(EmailNotificationServiceImpl.class.getName());

    @Override
    public void notifyUser(User user, String subject, String body) {
        // Placeholder: em produção integre SMTP, SES, Twilio, etc.
        String to = user != null ? user.getEmail() : "unknown";
        LOGGER.info(() -> String.format("Notify %s | to=%s | subject=%s | body=%s", user==null?"[null]":user.getName(), to, subject, body));
        // se desejar enviar email real, injete JavaMailSender e envie aqui
    }
}
