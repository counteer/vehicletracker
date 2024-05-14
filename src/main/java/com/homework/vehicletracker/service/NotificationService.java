package com.homework.vehicletracker.service;

import com.homework.vehicletracker.dto.NotificationDTO;
import com.homework.vehicletracker.entity.Notification;
import com.homework.vehicletracker.entity.Vehicle;
import com.homework.vehicletracker.repository.NotificationRepository;
import com.homework.vehicletracker.repository.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public NotificationService(NotificationRepository notificationRepository, VehicleRepository vehicleRepository) {
        this.notificationRepository = notificationRepository;
        this.vehicleRepository = vehicleRepository;
    }

    private final NotificationRepository notificationRepository;

    private final VehicleRepository vehicleRepository;

    public void addNotification(NotificationDTO notificationDTO) {
        Vehicle vehicle = vehicleRepository.findById(notificationDTO.getVehicleId()).orElseThrow(() -> new EntityNotFoundException("NOT FOUND"));
        Notification notification = notificationRepository.findByVehicle(vehicle).orElse(new Notification(vehicle));
        notification.setMessage(notificationDTO.getMessage());

        notification.setVehicle(vehicle);
        notificationRepository.save(notification);
    }
}
