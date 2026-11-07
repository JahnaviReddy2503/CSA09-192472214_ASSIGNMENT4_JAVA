public final class InMemoryPaymentRepository implements PaymentRepository {
    @Override
    public PaymentResult save(String registrationNumber, double amount) {
        return new PaymentResult(true, "Payment accepted for " + registrationNumber
                + ": " + String.format("%.2f", amount));
    }
}
