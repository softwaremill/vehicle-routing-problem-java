package com.softwaremill.vrp.input;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public record RoutingProblem(
        List<Vehicle> vehicles,
        List<Shipment> shipments
) {

    public List<Coordinates> getAllCoordinates() {
        return Stream.concat(
                vehicles.stream().flatMap(v -> Stream.of(v.startLocation(), v.endLocation())),
                shipments.stream().flatMap(s -> Stream.of(s.pickupLocation(), s.deliveryLocation()))
        ).distinct().sorted(Comparator.comparing(Coordinates::key)).toList();
    }
}
