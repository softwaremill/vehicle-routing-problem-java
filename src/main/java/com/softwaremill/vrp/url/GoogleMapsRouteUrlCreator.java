package com.softwaremill.vrp.url;

import com.softwaremill.vrp.input.Coordinates;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class GoogleMapsRouteUrlCreator implements RouteUrlCreator {

    @Override
    public String create(List<Coordinates> locations) {
        var uniqueStops = new ArrayList<String>();
        for (var location : locations) {
            var key = location.key();
            if (!uniqueStops.isEmpty() && uniqueStops.get(uniqueStops.size() - 1).equals(key)) {
                continue;
            }
            uniqueStops.add(key);
        }
        var query = String.join("/", uniqueStops);
        return "https://www.google.com/maps/dir/" + query;
    }
}
