public record PaymentResult(
        boolean successful,
        String message,
        String receiptText) {
}
