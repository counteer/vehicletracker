package com.homework.vehicletracker.dto;

import com.homework.vehicletracker.entity.Vehicle;

import java.util.List;

public class VehiclesDTO {

    private List<VehicleDTO> vehicles;

    private VehicleDTO central;

    public List<VehicleDTO> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles.stream().map(this::convert).toList();
    }

    public VehicleDTO getCentral() {
        return central;
    }

    public void setCentral(Vehicle central) {
        if (central != null) {
            this.central = convert(central);
        }
    }

    private VehicleDTO convert(Vehicle vehicle) {
        VehicleDTO converted = new VehicleDTO();
        converted.setLongitude(vehicle.getLongitude());
        converted.setLatitude(vehicle.getLatitude());
        converted.setId(vehicle.getId());
        if (vehicle.getNotification() != null) {
            converted.setMessage(vehicle.getNotification().getMessage());
        }
        return converted;
    }
}
