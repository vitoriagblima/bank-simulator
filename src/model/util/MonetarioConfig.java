package model.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

public final class MonetarioConfig {

    public static final int SCALE = 2;
    public static final int SCALE_TAXA = 4;
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
    public static final Locale LOCALE = Locale.forLanguageTag("pt-BR");

    private MonetarioConfig() {
    }

    /** Formata um valor monetário no padrão brasileiro. Ex.: R$ 1.300,00 */
    public static String formatar(BigDecimal valor) {
        return String.format(LOCALE, "R$ %,.2f", valor);
    }

    /** Formata uma taxa decimal como percentual. Ex.: 0.0050 -> 0,50% */
    public static String formatarPercentual(BigDecimal taxa) {
        return String.format(LOCALE, "%.2f%%", taxa.movePointRight(2));
    }
}