package org.example.geo.url;

import org.example.geo.input.Coordinates;

import java.util.List;

public interface RouteUrlCreator {
    String create(List<Coordinates> jobs);
}
