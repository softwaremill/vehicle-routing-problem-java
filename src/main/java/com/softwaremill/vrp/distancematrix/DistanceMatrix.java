package com.softwaremill.vrp.distancematrix;

import com.softwaremill.vrp.util.Distance;

import java.time.Duration;
import java.util.List;

public record DistanceMatrix(List<List<Entry>> entries) {

    public record Entry(Duration time, Distance distance) {}
}
