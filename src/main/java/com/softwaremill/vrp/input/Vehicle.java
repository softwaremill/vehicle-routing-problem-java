package com.softwaremill.vrp.input;

public record Vehicle(
        String id,
        Coordinates startLocation,
        Coordinates endLocation,
        TimeWindow timeWindow
) {
}
