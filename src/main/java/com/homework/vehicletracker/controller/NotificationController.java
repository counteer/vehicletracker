package com.homework.vehicletracker.controller;

import com.homework.vehicletracker.dto.NotificationDTO;
import com.homework.vehicletracker.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class NotificationController {

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    private final NotificationService notificationService;

    @PostMapping("/notifications")
    public ResponseEntity<Void> addNotification(@RequestBody NotificationDTO notification) {
        notificationService.addNotification(notification);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
