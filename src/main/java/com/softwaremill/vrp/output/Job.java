package com.softwaremill.vrp.output;

import com.softwaremill.vrp.input.Coordinates;

import java.time.OffsetDateTime;

public record Job(
        String shipmentId,
        JobType type,
        Coordinates location,
        OffsetDateTime arrivalTime,
        OffsetDateTime departureTime
) {
}
