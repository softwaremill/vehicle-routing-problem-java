package org.example.geo.output;

import java.util.List;

public record Route(
        String vehicleId,
        List<Job> jobs,
        String mapUrl
) {
}
