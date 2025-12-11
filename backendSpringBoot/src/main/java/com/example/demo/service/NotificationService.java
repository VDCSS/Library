package com.example.demo.service;

import com.example.demo.model.User;

public interface NotificationService {
    /**
     * Envia (ou registra) uma notificação para o usuário.
     * Implementações podem enviar email, push, gravar log, etc.
     */
    void notifyUser(User user, String subject, String body);
}
