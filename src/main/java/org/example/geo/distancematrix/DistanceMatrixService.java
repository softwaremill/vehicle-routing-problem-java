package org.example.geo.distancematrix;

import io.micronaut.scheduling.TaskExecutors;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.example.geo.distancematrix.provider.DistanceProvider;
import org.example.geo.input.Coordinates;
import org.example.geo.util.Distance;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Slf4j
@Singleton
public class DistanceMatrixService {

    private final Scheduler scheduler;
    private final DistanceProvider distanceProvider;

    public DistanceMatrixService(
            @Named(TaskExecutors.IO) ExecutorService executorService,
            DistanceProvider distanceProvider
    ) {
        this.scheduler = Schedulers.fromExecutorService(executorService);
        this.distanceProvider = distanceProvider;
    }

    public Mono<DistanceMatrix> createDistanceMatrix(List<Coordinates> coordinates) {
        return Flux.fromIterable(coordinates)
                .flatMap(from -> Flux.fromIterable(coordinates)
                        .flatMap(to -> getEntry(from, to)
                                .map(e -> new Tuple3<>(from, to, e))
                                .subscribeOn(scheduler)
                        ))
                .collectList()
                .map(entries -> createDistanceMatrix(coordinates, entries));
    }

    private Mono<DistanceMatrix.Entry> getEntry(Coordinates from, Coordinates to) {
        return Mono.fromSupplier(() -> {
            if (from.equals(to)) {
                return new DistanceMatrix.Entry(Duration.ZERO, Distance.ZERO);
            }
            return distanceProvider.fetch(from, to);
        });
    }

    private DistanceMatrix createDistanceMatrix(List<Coordinates> coordinates, List<Tuple3<Coordinates, Coordinates, DistanceMatrix.Entry>> entries) {
        var map = entries.stream()
                .collect(Collectors.toMap(t -> new Tuple2<>(t._1, t._2), t -> t._3));
        var entriesOrdered = coordinates.stream()
                .map(from -> coordinates.stream()
                        .map(to -> map.get(new Tuple2<>(from, to)))
                        .toList())
                .toList();
        return new DistanceMatrix(entriesOrdered);
    }
}
