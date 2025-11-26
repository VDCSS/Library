package com.example.demo.controller;

import com.example.demo.model.Notification;
import com.example.demo.repository.NotificationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/notifications")
public class NotificationController {

    private final NotificationRepository repo;

    public NotificationController(NotificationRepository repo) { this.repo = repo; }

    @GetMapping
    public ResponseEntity<List<Notification>> unread() { return ResponseEntity.ok(repo.findByIsReadFalseOrderByCreatedAtDesc()); }

    @PostMapping("/{id}/read")
    public ResponseEntity<?> markRead(@PathVariable Long id) {
        Notification n = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        n.setRead(true);
        repo.save(n);
        return ResponseEntity.ok().build();
    }
}
