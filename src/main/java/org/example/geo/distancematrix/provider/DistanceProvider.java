package org.example.geo.distancematrix.provider;

import org.example.geo.distancematrix.DistanceMatrix;
import org.example.geo.input.Coordinates;

public interface DistanceProvider {
    DistanceMatrix.Entry fetch(Coordinates from, Coordinates to);
}
