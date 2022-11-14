package org.example.geo.solver;

import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.Jsprit;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.geo.distancematrix.DistanceMatrix;
import org.example.geo.input.RoutingProblem;
import org.example.geo.output.Routes;
import org.example.geo.url.RouteUrlCreator;

@Slf4j
@RequiredArgsConstructor
public class RoutingProblemSolver {

    private static final int MAX_ITERATIONS = 1000;

    private final VehicleRoutingAlgorithm vehicleRoutingAlgorithm;
    private final RouteUrlCreator routeUrlCreator;

    public static RoutingProblemSolver from(RoutingProblem routingProblem, DistanceMatrix distanceMatrix, RouteUrlCreator routeUrlCreator) {
        var locations = Locations.from(routingProblem);
        return new RoutingProblemSolver(createAlgorithm(routingProblem, distanceMatrix, locations), routeUrlCreator);
    }

    public Routes solve() {
        var solutions = vehicleRoutingAlgorithm.searchSolutions();
        return solutions.stream().findFirst()
                .map(solution -> FromJspritMappers.toRoutes(solution, routeUrlCreator))
                .orElseThrow();
    }

    private static VehicleRoutingAlgorithm createAlgorithm(RoutingProblem problem, DistanceMatrix distanceMatrix, Locations locations) {
        var vehicleRoutingProblem = VehicleRoutingProblem.Builder.newInstance()
                .setFleetSize(VehicleRoutingProblem.FleetSize.FINITE)
                .addAllJobs(problem.shipments().stream().map(shipment -> ToJspritMappers.toShipment(shipment, locations)).toList())
                .addAllVehicles(problem.vehicles().stream().map(vehicle -> ToJspritMappers.toVehicle(vehicle, locations)).toList())
                .setRoutingCost(new DistanceMatrixAdapter(distanceMatrix))
                .build();

        return Jsprit.Builder.newInstance(vehicleRoutingProblem)
                .setProperty(Jsprit.Parameter.ITERATIONS, Integer.toString(MAX_ITERATIONS))
                .buildAlgorithm();
    }
}
