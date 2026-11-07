import java.util.Locale;

public final class ConversionSupport {
    private ConversionSupport() {
    }

    public static double rate(String currency) {
        return switch (currency) {
            case "USD" -> 1.0;
            case "EUR" -> 0.92;
            case "INR" -> 83.0;
            default -> throw new IllegalArgumentException("Unsupported currency");
        };
    }

    public static String convert(String text, String from, String to) {
        try {
            double amount = Double.parseDouble(text.trim());
            double converted = amount * rate(from) / rate(to);
            return String.format(Locale.ROOT, "Result: %.2f %s", converted, to);
        } catch (NumberFormatException exception) {
            return "Invalid amount";
        }
    }
}
