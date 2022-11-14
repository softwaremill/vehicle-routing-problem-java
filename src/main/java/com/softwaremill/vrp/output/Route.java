package com.softwaremill.vrp.output;

import java.util.List;

public record Route(
        String vehicleId,
        List<Job> jobs,
        String mapUrl
) {
}
