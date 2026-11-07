public interface PaymentRepository {
    PaymentResult save(String registrationNumber, double amount);
}
