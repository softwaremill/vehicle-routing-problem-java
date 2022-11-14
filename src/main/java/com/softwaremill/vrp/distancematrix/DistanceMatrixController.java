package com.softwaremill.vrp.distancematrix;

import com.softwaremill.vrp.input.Coordinates;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import jakarta.inject.Named;
import org.reactivestreams.Publisher;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Controller("/distance-matrix")
public class DistanceMatrixController {

    private final Scheduler scheduler;
    private final DistanceMatrixService distanceMatrixService;

    public DistanceMatrixController(
            @Named(TaskExecutors.IO) ExecutorService executorService,
            DistanceMatrixService distanceMatrixService
    ) {
        this.scheduler = Schedulers.fromExecutorService(executorService);
        this.distanceMatrixService = distanceMatrixService;
    }

    @Post(consumes = MediaType.APPLICATION_JSON)
    @SingleResult
    Publisher<DistanceMatrix> byName(@Body List<Coordinates> coordinates) {
        return distanceMatrixService.createDistanceMatrix(coordinates)
                .subscribeOn(scheduler);
    }
}
