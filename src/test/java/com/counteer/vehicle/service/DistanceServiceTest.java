package com.counteer.vehicle.service;

import com.homework.vehicletracker.service.DistanceService;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class DistanceServiceTest {

    @Test
    public void testHaversineDistance() {
        double lat1 = 47.491945;
        double long1 = 19.017189;
        double lat2 = 47.47581;
        double long2 = 19.05749;
        double expectedDistance = 3520; //distance calculated at meridianoutpost.com
        DistanceService underTest = new DistanceService();
        double distance = underTest.haversineDistance(lat1, long1, lat2, long2);
        assertThat(Math.abs(distance-expectedDistance)<10, is(true));
    }
}
