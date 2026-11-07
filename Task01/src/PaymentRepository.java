public interface PaymentRepository {
    PaymentResult savePayment(String studentName, String registerNumber, double amount);
}
