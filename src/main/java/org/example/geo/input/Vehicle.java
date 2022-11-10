package org.example.geo.input;

public record Vehicle(
        String id,
        Coordinates startLocation,
        Coordinates endLocation,
        TimeWindow timeWindow
) {
}
