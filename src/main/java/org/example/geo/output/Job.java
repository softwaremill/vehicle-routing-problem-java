package org.example.geo.output;

import org.example.geo.input.Coordinates;

import java.time.OffsetDateTime;

public record Job(
        String shipmentId,
        JobType type,
        Coordinates location,
        OffsetDateTime arrivalTime,
        OffsetDateTime departureTime
) {
}
