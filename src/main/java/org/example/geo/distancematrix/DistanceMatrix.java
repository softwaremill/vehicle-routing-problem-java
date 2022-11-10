package org.example.geo.distancematrix;

import org.example.geo.util.Distance;

import java.time.Duration;
import java.util.List;

public record DistanceMatrix(List<List<Entry>> entries) {

    public record Entry(Duration time, Distance distance) {}
}
