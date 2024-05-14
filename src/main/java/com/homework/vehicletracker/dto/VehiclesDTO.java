package com.homework.vehicletracker.dto;

import com.homework.vehicletracker.entity.Vehicle;

import java.util.List;

public class VehiclesDTO {

    private List<Vehicle> vehicles;

    private Vehicle central;

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public Vehicle getCentral() {
        return central;
    }

    public void setCentral(Vehicle central) {
        this.central = central;
    }
}
