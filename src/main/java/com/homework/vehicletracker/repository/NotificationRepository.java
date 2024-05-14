package com.homework.vehicletracker.repository;

import com.homework.vehicletracker.entity.Notification;
import com.homework.vehicletracker.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findByVehicle(Vehicle vehicle);
}