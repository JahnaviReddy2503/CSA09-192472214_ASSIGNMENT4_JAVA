public final class FeeService {
    private final PaymentRepository paymentRepository;

    public FeeService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentResult payFee(String studentName, String registerNumber, String amountText) {
        String cleanName = studentName == null ? "" : studentName.trim();
        String cleanRegisterNumber = registerNumber == null ? "" : registerNumber.trim();
        String cleanAmount = amountText == null ? "" : amountText.trim();

        if (cleanName.isEmpty()) {
            return failure("Student name is required.");
        }
        if (cleanRegisterNumber.isEmpty()) {
            return failure("Register number is required.");
        }

        final double amount;
        try {
            amount = Double.parseDouble(cleanAmount);
        } catch (NumberFormatException exception) {
            return failure("Amount must be a valid number.");
        }

        if (!Double.isFinite(amount) || amount <= 0) {
            return failure("Amount must be greater than zero.");
        }

        return paymentRepository.savePayment(cleanName, cleanRegisterNumber, amount);
    }

    private PaymentResult failure(String message) {
        return new PaymentResult(false, message, "");
    }
}
