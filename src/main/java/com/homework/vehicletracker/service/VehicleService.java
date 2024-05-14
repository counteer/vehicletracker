package com.homework.vehicletracker.service;

import com.homework.vehicletracker.dto.VehicleDTO;
import com.homework.vehicletracker.entity.Vehicle;
import com.homework.vehicletracker.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id).orElseThrow();
    }

    public Vehicle addElement() {
        Vehicle vehicle = new Vehicle();
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getVehicles() {
        return vehicleRepository.findAll();
    }

    public List<Vehicle> getVehiclesNearVehicle(Vehicle vehicle) {
        List<Vehicle> vehiclesInRadius = getVehiclesInRadius(vehicle.getLatitude(), vehicle.getLongitude(), 200);
        return vehiclesInRadius.stream().filter(vehicle1 -> !vehicle.getId().equals(vehicle1.getId())).collect(Collectors.toList());
    }

    public List<Vehicle> getVehiclesInRadius(double latitude, double longitude, double radius) {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        return vehicles.stream()
                .filter(element -> haversineDistance(latitude, longitude, element.getLatitude(), element.getLongitude()) <= radius)
                .collect(Collectors.toList());
    }

    public void update(VehicleDTO vehicleDTO) {
        Vehicle vehicle = vehicleRepository.findById(vehicleDTO.getId()).orElseThrow();
        vehicle.setLongitude(vehicleDTO.getLongitude());
        vehicle.setLatitude(vehicleDTO.getLatitude());
        vehicleRepository.save(vehicle);
    }

    private double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        final int earthRadius = 6_371_000;
        return earthRadius * c;
    }
}