package com.softwaremill.vrp.solver;

import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.util.Coordinate;
import com.softwaremill.vrp.input.Coordinates;
import com.softwaremill.vrp.input.RoutingProblem;
import io.vavr.Tuple2;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class Locations {

    private final Map<Coordinates, Location> locationMap;

    static Locations from(RoutingProblem problem) {
        var allCoordinates = problem.getAllCoordinates();
        return new Locations(
                IntStream.range(0, allCoordinates.size())
                        .mapToObj(idx -> new Tuple2<>(allCoordinates.get(idx), idx))
                        .collect(Collectors.toMap(t -> t._1, t -> toJspritLocation(t._1, t._2)))
        );
    }

    Location get(Coordinates coordinates) {
        return locationMap.get(coordinates);
    }

    private static Location toJspritLocation(Coordinates coordinates, int index) {
        return Location.Builder.newInstance()
                .setId(coordinates.key())
                .setName(coordinates.key())
                .setCoordinate(toJspritCoordinate(coordinates))
                .setIndex(index)
                .setUserData(coordinates)
                .build();
    }

    private static Coordinate toJspritCoordinate(Coordinates coordinates) {
        return new Coordinate(coordinates.lat().doubleValue(), coordinates.lon().doubleValue());
    }
}
