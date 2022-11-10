package org.example.geo.solver;

import com.graphhopper.jsprit.core.problem.AbstractVehicle;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleTypeImpl;
import org.example.geo.input.Shipment;
import org.example.geo.input.TimeWindow;
import org.example.geo.input.Vehicle;

import java.time.Duration;

class ToJspritMappers {

    private static final double COST_PER_KILOMETER = 50.0;
    private static final double COST_PER_MINUTE = 5.0;
    private static final double BASE_COST = 1000.0;

    static AbstractVehicle toVehicle(Vehicle vehicle, Locations locations) {
        var costPerMeter = COST_PER_KILOMETER / 1000.0;
        var costPerMillisecond = COST_PER_MINUTE / Duration.ofMinutes(1).toMillis();
        var builder = VehicleImpl.Builder.newInstance(vehicle.id());
        builder.setStartLocation(locations.get(vehicle.startLocation()));
        builder.setEndLocation(locations.get(vehicle.endLocation()));
        builder.setReturnToDepot(true);
        builder.setType(VehicleTypeImpl.Builder.newInstance(vehicle.id() + "-type")
                .setCostPerDistance(costPerMeter)
                .setCostPerTransportTime(costPerMillisecond)
                .setCostPerServiceTime(costPerMillisecond)
                .setCostPerWaitingTime(costPerMillisecond)
                .setFixedCost(BASE_COST)
                .build());
        builder.setEarliestStart(vehicle.timeWindow().start().toInstant().toEpochMilli());
        builder.setLatestArrival(vehicle.timeWindow().end().toInstant().toEpochMilli());
        return builder.build();
    }

    static com.graphhopper.jsprit.core.problem.job.Job toShipment(Shipment shipment, Locations locations) {
        var builder = com.graphhopper.jsprit.core.problem.job.Shipment.Builder.newInstance(shipment.id())
                .setPickupLocation(locations.get(shipment.pickupLocation()))
                .setDeliveryLocation(locations.get(shipment.deliveryLocation()));
        builder.setPickupTimeWindow(toTimeWindow(shipment.pickupTimeWindow()));
        builder.setDeliveryTimeWindow(toTimeWindow(shipment.deliveryTimeWindow()));
        return builder.build();
    }

    static com.graphhopper.jsprit.core.problem.solution.route.activity.TimeWindow toTimeWindow(TimeWindow timeWindow) {
        return new com.graphhopper.jsprit.core.problem.solution.route.activity.TimeWindow(
                timeWindow.start().toInstant().toEpochMilli(),
                timeWindow.end().toInstant().toEpochMilli()
        );
    }
}
