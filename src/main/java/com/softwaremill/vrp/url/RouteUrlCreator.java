package com.softwaremill.vrp.url;

import com.softwaremill.vrp.input.Coordinates;

import java.util.List;

public interface RouteUrlCreator {
    String create(List<Coordinates> jobs);
}
