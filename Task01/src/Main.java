public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        FeeService feeService = new FeeService(new InMemoryPaymentRepository());
        FeeWindow.show(feeService);
    }
}
