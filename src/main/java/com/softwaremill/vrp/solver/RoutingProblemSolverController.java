
package com.softwaremill.vrp.solver;

import com.softwaremill.vrp.distancematrix.DistanceMatrixService;
import com.softwaremill.vrp.input.RoutingProblem;
import com.softwaremill.vrp.output.Routes;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import jakarta.inject.Named;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ExecutorService;

@Controller("/routing-problem")
public class RoutingProblemSolverController {

    private final Scheduler scheduler;
    private final DistanceMatrixService distanceMatrixService;
    private final RoutingProblemSolverService routingProblemSolverService;

    public RoutingProblemSolverController(
            @Named(TaskExecutors.IO) ExecutorService executorService,
            DistanceMatrixService distanceMatrixService,
            RoutingProblemSolverService routingProblemSolverService
    ) {
        this.scheduler = Schedulers.fromExecutorService(executorService);
        this.distanceMatrixService = distanceMatrixService;
        this.routingProblemSolverService = routingProblemSolverService;
    }

    @Post(consumes = MediaType.APPLICATION_JSON)
    @SingleResult
    Publisher<Routes> solve(@Body RoutingProblem routingProblem) {
        return Mono.just(routingProblem)
                .flatMap(ignored -> distanceMatrixService.createDistanceMatrix(routingProblem.getAllCoordinates()))
                .flatMap(dm -> routingProblemSolverService.solve(routingProblem, dm))
                .subscribeOn(scheduler);
    }
}
