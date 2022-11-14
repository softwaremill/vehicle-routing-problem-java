package org.example.geo.solver;

import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.solution.route.VehicleRoute;
import com.graphhopper.jsprit.core.problem.solution.route.activity.*;
import org.example.geo.input.Coordinates;
import org.example.geo.output.Job;
import org.example.geo.output.JobType;
import org.example.geo.output.Route;
import org.example.geo.output.Routes;
import org.example.geo.url.RouteUrlCreator;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Stream;

class FromJspritMappers {

    static Routes toRoutes(VehicleRoutingProblemSolution solution, RouteUrlCreator routeUrlCreator) {
        return new Routes(solution.getRoutes().stream().map(route -> toRoute(route, routeUrlCreator)).toList());
    }

    private static Route toRoute(VehicleRoute route, RouteUrlCreator routeUrlCreator) {
        return new Route(
                route.getVehicle().getId(),
                getJobs(route),
                getMapUrl(route, routeUrlCreator)
        );
    }

    public static List<Job> getJobs(VehicleRoute route) {
        return getTourActivityStream(route)
                .map(FromJspritMappers::toJob).toList();
    }

    private static Stream<TourActivity> getTourActivityStream(VehicleRoute route) {
        return Stream.concat(Stream.concat(Stream.of(route.getStart()), route.getActivities().stream()), Stream.of(route.getEnd()));
    }

    private static String getMapUrl(VehicleRoute route, RouteUrlCreator routeUrlCreator) {
        var coordinates = getTourActivityStream(route).map(act -> ((Coordinates) act.getLocation().getUserData())).toList();
        return routeUrlCreator.create(coordinates);
    }

    private static Job toJob(TourActivity activity) {
        if (activity instanceof Start start) {
            return new Job(
                    null,
                    JobType.START,
                    (Coordinates) start.getLocation().getUserData(),
                    toDateTime(start.getArrTime()),
                    toDateTime(start.getEndTime()));
        } else if (activity instanceof End end) {
            return new Job(
                    null,
                    JobType.END,
                    (Coordinates) end.getLocation().getUserData(),
                    toDateTime(end.getArrTime()),
                    toDateTime(end.getEndTime()));
        }
        else if (activity instanceof PickupShipment pickupShipment) {
            return new Job(
                    pickupShipment.getJob().getId(),
                    JobType.PICKUP,
                    (Coordinates) pickupShipment.getLocation().getUserData(),
                    toDateTime(pickupShipment.getArrTime()),
                    toDateTime(pickupShipment.getEndTime()));
        } else if (activity instanceof DeliverShipment deliverShipment) {
            return new Job(
                    deliverShipment.getJob().getId(),
                    JobType.DELIVER,
                    (Coordinates) deliverShipment.getLocation().getUserData(),
                    toDateTime(deliverShipment.getArrTime()),
                    toDateTime(deliverShipment.getEndTime()));
        } else {
            throw new IllegalArgumentException("Unexpected activity: " + activity.getClass().getName());
        }
    }

    private static OffsetDateTime toDateTime(double time) {
        if (time == 0) {
            return null;
        }
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli((long) time), ZoneId.of("UTC"));
    }
}
