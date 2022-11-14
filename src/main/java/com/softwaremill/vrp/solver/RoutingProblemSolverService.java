package com.softwaremill.vrp.solver;

import com.softwaremill.vrp.distancematrix.DistanceMatrix;
import com.softwaremill.vrp.input.RoutingProblem;
import com.softwaremill.vrp.output.Routes;
import com.softwaremill.vrp.url.RouteUrlCreator;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Singleton
public class RoutingProblemSolverService {

    private final RouteUrlCreator routeUrlCreator;

    public RoutingProblemSolverService(RouteUrlCreator routeUrlCreator) {
        this.routeUrlCreator = routeUrlCreator;
    }

    public Mono<Routes> solve(RoutingProblem routingProblem, DistanceMatrix distanceMatrix) {
        return Mono.just(routingProblem)
                .map(ignored -> RoutingProblemSolver.from(routingProblem, distanceMatrix, routeUrlCreator).solve());
    }
}
