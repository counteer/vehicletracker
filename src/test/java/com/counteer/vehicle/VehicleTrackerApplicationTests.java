package com.counteer.vehicle;

import com.homework.vehicletracker.VehicleTrackerApplication;
import com.homework.vehicletracker.dto.VehicleDTO;
import com.homework.vehicletracker.dto.VehiclesDTO;
import com.homework.vehicletracker.entity.Vehicle;
import com.homework.vehicletracker.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(classes = {VehicleTrackerApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VehicleTrackerApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private VehicleRepository vehicleRepository;

    TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void testGetVehicleEndpoint() {
        vehicleRepository.deleteAll();
        ResponseEntity<Void> initResult = restTemplate
                .exchange(getUrlWithPort("/vehicles"), HttpMethod.POST, null, Void.class);
        assertThat(initResult.getStatusCode(), is(HttpStatus.OK));
        List<Vehicle> vehiclesInDb = vehicleRepository.findAll();

        assertThat(vehiclesInDb.size(), is(1));
        Long id = vehiclesInDb.get(0).getId();
        ResponseEntity<Void> updateResult = restTemplate
                .exchange(getUrlWithPort("/vehicle/{id}"), HttpMethod.POST,
                        new HttpEntity<>(getVehicle()), Void.class,
                        id);

        assertThat(updateResult.getStatusCode(), is(HttpStatus.OK));
        List<Vehicle> vehiclesInDbAfter = vehicleRepository.findAll();
        assertThat(vehiclesInDbAfter.get(0).getLatitude(), is(1.0));
        assertThat(vehiclesInDbAfter.get(0).getLongitude(), is(1.0));

        ResponseEntity<VehiclesDTO> getVehiclesInRadius = restTemplate
                .exchange(getUrlWithPort("/vehicles?latitude={latitude}&longitude={longitude}&radius={radius}"), HttpMethod.GET,
                        null, VehiclesDTO.class,
                        1.0,
                        1.0,
                        200.0);
        int vehiclesInRadius = getVehiclesInRadius.getBody().getVehicles().size();
        assertThat(vehiclesInRadius, is(1));

    }

    private String getUrlWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private VehicleDTO getVehicle() {
        VehicleDTO vehicle = new VehicleDTO();
        vehicle.setLatitude(1.0);
        vehicle.setLongitude(1.0);
        return vehicle;
    }
}
