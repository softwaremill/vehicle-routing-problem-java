package org.example.geo.solver;

import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.example.geo.distancematrix.DistanceMatrix;
import org.example.geo.input.RoutingProblem;
import org.example.geo.output.Routes;
import reactor.core.publisher.Mono;

@Slf4j
@Singleton
public class RoutingProblemSolverService {

    public Mono<Routes> solve(RoutingProblem routingProblem, DistanceMatrix distanceMatrix) {
        return Mono.just(routingProblem)
                .map(ignored -> RoutingProblemSolver.from(routingProblem, distanceMatrix).solve());
    }
}
