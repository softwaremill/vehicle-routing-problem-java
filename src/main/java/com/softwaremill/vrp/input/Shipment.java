package com.softwaremill.vrp.input;

public record Shipment(
        String id,
        Coordinates pickupLocation,
        Coordinates deliveryLocation,
        TimeWindow pickupTimeWindow,
        TimeWindow deliveryTimeWindow
) {
}
