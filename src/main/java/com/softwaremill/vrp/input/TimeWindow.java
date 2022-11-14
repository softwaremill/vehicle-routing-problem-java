package com.softwaremill.vrp.input;

import java.time.OffsetDateTime;

public record TimeWindow(
        OffsetDateTime start,
        OffsetDateTime end
) {
}
