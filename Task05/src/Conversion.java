import java.util.Locale;

public final class Conversion {
    private Conversion() {}

    public static double rate(String currency) {
        return switch (currency) {
            case "USD" -> 0.012;
            case "EUR" -> 0.011;
            default -> throw new IllegalArgumentException("Unsupported currency");
        };
    }

    public static String convert(String text, String from, String to) {
        try {
            double amount = Double.parseDouble(text.trim());
            return String.format(Locale.ROOT, "%.2f %s",
                    amount * rate(from) / rate(to), to);
        } catch (NumberFormatException exception) {
            return "Invalid amount";
        }
    }
}
