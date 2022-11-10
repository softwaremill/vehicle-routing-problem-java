package org.example.geo.solver;

import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.cost.AbstractForwardVehicleRoutingTransportCosts;
import com.graphhopper.jsprit.core.problem.driver.Driver;
import com.graphhopper.jsprit.core.problem.vehicle.Vehicle;
import lombok.RequiredArgsConstructor;
import org.example.geo.distancematrix.DistanceMatrix;

@RequiredArgsConstructor
class DistanceMatrixAdapter extends AbstractForwardVehicleRoutingTransportCosts {

    private final DistanceMatrix distanceMatrix;

    @Override
    public double getDistance(Location from, Location to, double departureTime, Vehicle vehicle) {
        return distanceMatrix.entries().get(from.getIndex()).get(to.getIndex()).distance().meters().doubleValue();
    }

    @Override
    public double getTransportTime(Location from, Location to, double departureTime, Driver driver, Vehicle vehicle) {
        return distanceMatrix.entries().get(from.getIndex()).get(to.getIndex()).time().toMillis();
    }

    @Override
    public double getTransportCost(Location from, Location to, double departureTime, Driver driver, Vehicle vehicle) {
        if (vehicle == null) {
            return getDistance(from, to, departureTime, null);
        }
        var costParams = vehicle.getType().getVehicleCostParams();
        return costParams.perDistanceUnit * getDistance(from, to, departureTime, vehicle) +
                costParams.perTransportTimeUnit * getTransportTime(from, to, departureTime, driver, vehicle);

    }
}
