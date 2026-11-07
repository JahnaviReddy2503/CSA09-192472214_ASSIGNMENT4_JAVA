public final class FeeServiceTest {
    public static void main(String[] args) {
        System.out.println("Task: 1");
        System.out.println("Requirement: FeeService valid and invalid payment cases");
        System.out.println("Command: javac -d Task01\\out Task01\\src\\*.java && java -cp Task01\\out FeeServiceTest");
        System.out.println("Date: 2026-11-07");
        FeeService service = new FeeService(new InMemoryPaymentRepository());
        check("valid", service.pay("ST101", "1250"), true);
        check("empty registration", service.pay("", "1250"), false);
        check("non-numeric", service.pay("ST101", "abc"), false);
        check("negative", service.pay("ST101", "-1"), false);
    }

    private static void check(String name, PaymentResult result, boolean expected) {
        System.out.println(name + ": " + (result.success() == expected ? "PASS" : "FAIL")
                + " - " + result.message());
    }
}
