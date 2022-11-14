package com.softwaremill.vrp.solver;

import com.softwaremill.vrp.distancematrix.provider.GraphHopperDistanceProvider;
import com.softwaremill.vrp.output.Route;
import com.softwaremill.vrp.output.Routes;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class RoutingProblemSolverControllerTest {

    static final String OSM_URL = "http://download.openstreetmap.fr/extracts/europe/poland/pomorskie-latest.osm.pbf";

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    ResourceLoader resourceLoader;

    @BeforeAll
    static void setUp() throws IOException {
        var destination = Paths.get(GraphHopperDistanceProvider.DATA_DIR, GraphHopperDistanceProvider.PBF_FILE_NAME);
        if (!destination.toFile().exists()) {
            var stream = new URL(OSM_URL).openStream();
            new File(GraphHopperDistanceProvider.DATA_DIR).mkdir();
            Files.copy(stream, destination, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @Test
    void testHello() throws IOException {
        var body = new String(resourceLoader.getResourceAsStream("request.json").orElseThrow().readAllBytes());
        var request = HttpRequest.POST("/routing-problem", body);
        var response = client.toBlocking().retrieve(request, Routes.class);

        assertThat(response.routes()).singleElement()
                .extracting(Route::jobs)
                .asList()
                .hasSize(16);
    }
}