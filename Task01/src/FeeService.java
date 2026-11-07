public final class FeeService {
    private final PaymentRepository repository;

    public FeeService(PaymentRepository repository) {
        this.repository = repository;
    }

    public PaymentResult validate(String registrationNumber, String amountText) {
        if (registrationNumber == null || registrationNumber.trim().isEmpty()) {
            return new PaymentResult(false, "Registration number is required");
        }
        try {
            double amount = Double.parseDouble(amountText == null ? "" : amountText.trim());
            if (!Double.isFinite(amount) || amount <= 0) {
                return new PaymentResult(false, "Amount must be positive");
            }
            return new PaymentResult(true, "Input valid");
        } catch (NumberFormatException exception) {
            return new PaymentResult(false, "Invalid amount");
        }
    }

    public PaymentResult pay(String registrationNumber, String amountText) {
        PaymentResult validation = validate(registrationNumber, amountText);
        if (!validation.success()) {
            return validation;
        }
        return repository.save(registrationNumber.trim(), Double.parseDouble(amountText.trim()));
    }
}
