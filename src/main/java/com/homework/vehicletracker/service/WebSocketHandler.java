package com.homework.vehicletracker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.vehicletracker.dto.VehiclesDTO;
import com.homework.vehicletracker.entity.Vehicle;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WebSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;

    private final VehicleService vehicleService;

    private final Set<WebSocketSession> sessions = new HashSet<>();

    public WebSocketHandler(ObjectMapper objectMapper, VehicleService vehicleService) {
        this.objectMapper = objectMapper;
        this.vehicleService = vehicleService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String path = Objects.requireNonNull(session.getUri()).getPath();
        Long id = extractIdFromPath(path);
        session.getAttributes().put("vehicleId", id);
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }

    private Long extractIdFromPath(String path) {
        Pattern pattern = Pattern.compile("/websocket/(\\d+)");
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            String id = matcher.group(1);
            return Long.parseLong(id);
        } else {
            return null; // ID not found in the path
        }
    }

    @Scheduled(fixedRate = 100) // Send update every second
    public void sendPeriodicDto() {
        if (sessions.isEmpty()) {
            return;
        }
        for (WebSocketSession session : sessions) {
            Long id = (Long) session.getAttributes().get("vehicleId");
            Vehicle vehicle = vehicleService.getVehicleById(id);
            List<Vehicle> vehicles = vehicleService.getVehiclesNearVehicle(vehicle);
            vehicles.remove(vehicle);
            VehiclesDTO vehiclesDTO = new VehiclesDTO();
            vehiclesDTO.setVehicles(vehicles);
            vehiclesDTO.setCentral(vehicle);
            try {
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(vehiclesDTO)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}