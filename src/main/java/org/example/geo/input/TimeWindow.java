package org.example.geo.input;

import java.time.OffsetDateTime;

public record TimeWindow(
        OffsetDateTime start,
        OffsetDateTime end
) {
}
