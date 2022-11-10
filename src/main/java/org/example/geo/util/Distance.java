package org.example.geo.util;

import java.math.BigDecimal;

public record Distance(BigDecimal meters) {
    public static final Distance ZERO = new Distance(BigDecimal.ZERO);

    public static Distance ofMeters(double value) {
        return new Distance(BigDecimal.valueOf(value));
    }
}
