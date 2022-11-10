package org.example.geo.input;

import java.math.BigDecimal;
import java.util.Locale;

public record Coordinates(BigDecimal lat, BigDecimal lon) {

    public String key() {
        return String.format(Locale.ENGLISH, "%s,%s", lat, lon);
    }
}
