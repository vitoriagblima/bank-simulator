package model.util;

import java.math.RoundingMode;

public final class MonetarioConfig {

    public static final int SCALE = 2;
    public static final int SCALE_TAXA = 4;
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;

    private MonetarioConfig() {
    }
}