package com.softwaremill.vrp.distancematrix.provider;

import com.softwaremill.vrp.distancematrix.DistanceMatrix;
import com.softwaremill.vrp.input.Coordinates;

public interface DistanceProvider {
    DistanceMatrix.Entry fetch(Coordinates from, Coordinates to);
}
