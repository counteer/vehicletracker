package com.homework.vehicletracker.controller;

import com.homework.vehicletracker.dto.VehiclesDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.homework.vehicletracker.service.VehicleService;
import com.homework.vehicletracker.dto.VehicleDTO;
import com.homework.vehicletracker.entity.Vehicle;

import java.util.List;

@Controller
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @CrossOrigin
    @GetMapping("/vehicles")
    public ResponseEntity<VehiclesDTO> getVehiclesInRadius(@RequestParam(required = false) Double latitude, @RequestParam(required = false) Double longitude, @RequestParam(required = false) Double radius) {
        List<Vehicle> vehiclesInRadius;
        if (latitude == null) {
            vehiclesInRadius = vehicleService.getVehicles();
        } else {
            vehiclesInRadius = vehicleService.getVehiclesInRadius(latitude, longitude, radius);
        }
        VehiclesDTO result = new VehiclesDTO();
        result.setVehicles(vehiclesInRadius);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/vehicles")
    public ResponseEntity<Vehicle> addVehicle() {
        Vehicle elementId = vehicleService.addElement();
        return new ResponseEntity<>(elementId, HttpStatus.OK);
    }

    @PostMapping("/vehicle/{id}")
    public ResponseEntity<Void> updateVehicle(@PathVariable Long id, @RequestBody VehicleDTO vehicleDTO) {
        if (vehicleDTO.getLatitude() == null || vehicleDTO.getLongitude() == null) {
            return ResponseEntity.badRequest().build();
        }
        vehicleDTO.setId(id);
        vehicleService.update(vehicleDTO);
        return ResponseEntity.ok().build();
    }
}
