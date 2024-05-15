package com.homework.vehicletracker.service;

import com.homework.vehicletracker.dto.VehicleDTO;
import com.homework.vehicletracker.entity.Vehicle;
import com.homework.vehicletracker.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    private final DistanceService distanceService;

    public VehicleService(VehicleRepository vehicleRepository, DistanceService distanceService) {
        this.vehicleRepository = vehicleRepository;
        this.distanceService = distanceService;
    }


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
        List<Vehicle> vehicles = vehicleRepository.findAllByLatitudeIsNotNullAndLongitudeIsNotNull();
        return vehicles.stream()
                .filter(element -> distanceService.haversineDistance(latitude, longitude, element.getLatitude(), element.getLongitude()) <= radius)
                .collect(Collectors.toList());
    }

    public void update(VehicleDTO vehicleDTO) {
        Vehicle vehicle = vehicleRepository.findById(vehicleDTO.getId()).orElseThrow();
        vehicle.setLongitude(vehicleDTO.getLongitude());
        vehicle.setLatitude(vehicleDTO.getLatitude());
        vehicleRepository.save(vehicle);
    }


}