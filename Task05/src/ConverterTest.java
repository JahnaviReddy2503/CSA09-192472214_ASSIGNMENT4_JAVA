public final class ConverterTest {
    public static void main(String[] args) {
        System.out.println("Task: 5");
        System.out.println("Requirement: valid and invalid conversion output");
        System.out.println("Command: javac -d Task05\\out Task05\\src\\*.java && java -cp Task05\\out ConverterTest");
        System.out.println("Date: 2026-11-07");
        String valid = Conversion.convert("100", "USD", "EUR");
        String invalid = Conversion.convert("abc", "USD", "EUR");
        System.out.println("valid_conversion=" + valid + " " + (valid.equals("109.09 EUR") ? "PASS" : "FAIL"));
        System.out.println("invalid_amount=" + invalid + " " + (invalid.equals("Invalid amount") ? "PASS" : "FAIL"));
    }
}
