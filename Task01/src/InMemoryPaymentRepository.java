import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class InMemoryPaymentRepository implements PaymentRepository {
    private static final DateTimeFormatter RECEIPT_TIME =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public PaymentResult savePayment(String studentName, String registerNumber, double amount) {
        String receipt = """
                FEE PAYMENT RECEIPT
                -------------------
                Student: %s
                Register No: %s
                Amount: %.2f
                Paid at: %s
                """.formatted(
                studentName,
                registerNumber,
                amount,
                LocalDateTime.now().format(RECEIPT_TIME));
        return new PaymentResult(true, "Payment saved successfully.", receipt);
    }
}
