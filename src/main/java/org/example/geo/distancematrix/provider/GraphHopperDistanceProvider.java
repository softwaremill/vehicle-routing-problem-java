package org.example.geo.distancematrix.provider;

import com.graphhopper.GHRequest;
import com.graphhopper.GraphHopper;
import com.graphhopper.config.CHProfile;
import com.graphhopper.config.Profile;
import jakarta.inject.Singleton;
import org.example.geo.distancematrix.DistanceMatrix;
import org.example.geo.input.Coordinates;
import org.example.geo.util.Distance;

import java.time.Duration;
import java.util.Locale;

@Singleton
public class GraphHopperDistanceProvider implements DistanceProvider {

    public static String DATA_FILE = ".data/pomorskie-latest.osm.pbf";

    private final GraphHopper graphHopper;

    public GraphHopperDistanceProvider() {
        this.graphHopper = createGraphHopperInstance();
    }

    private static GraphHopper createGraphHopperInstance() {
        var hopper = new GraphHopper();
        hopper.setOSMFile(DATA_FILE);
        hopper.setGraphHopperLocation(".cache/routing-graph");
        hopper.setProfiles(new Profile("car").setVehicle("car").setWeighting("fastest").setTurnCosts(false));
        hopper.getCHPreparationHandler().setCHProfiles(new CHProfile("car"));
        hopper.importOrLoad();
        return hopper;
    }

    @Override
    public DistanceMatrix.Entry fetch(Coordinates from, Coordinates to) {
        var ghRequest = new GHRequest(
                from.lat().doubleValue(),
                from.lon().doubleValue(),
                to.lat().doubleValue(),
                to.lon().doubleValue())
                .setProfile("car")
                .setLocale(Locale.ENGLISH);
        var ghResponse = graphHopper.route(ghRequest);

        if (ghResponse.hasErrors()) {
            throw new IllegalStateException(ghResponse.getErrors().toString());
        }

        var path = ghResponse.getBest();
        var distanceInMeters = path.getDistance();
        var timeInMs = path.getTime();
        return new DistanceMatrix.Entry(Duration.ofMillis(timeInMs), Distance.ofMeters(distanceInMeters));
    }
}
