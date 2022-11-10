package org.example.geo.solver;

import io.micronaut.core.io.ResourceLoader;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.example.geo.output.Route;
import org.example.geo.output.Routes;
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

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    ResourceLoader resourceLoader;

    @BeforeAll
    static void setUp() throws IOException {
        var file = ".data/pomorskie-latest.osm.pbf";
        if (!new File(file).exists()) {
            var url = "http://download.openstreetmap.fr/extracts/europe/poland/pomorskie-latest.osm.pbf";
            var stream = new URL(url).openStream();
            new File(".data").mkdir();
            Files.copy(stream, Paths.get(file), StandardCopyOption.REPLACE_EXISTING);
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