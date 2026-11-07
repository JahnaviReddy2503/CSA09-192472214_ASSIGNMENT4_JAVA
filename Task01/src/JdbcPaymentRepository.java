import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class JdbcPaymentRepository implements PaymentRepository {
    private final Connection connection;

    public JdbcPaymentRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public PaymentResult save(String registrationNumber, double amount) {
        String sql = "INSERT INTO fee_payments(registration_number, amount) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, registrationNumber);
            statement.setDouble(2, amount);
            statement.executeUpdate();
            return new PaymentResult(true, "Payment saved");
        } catch (SQLException exception) {
            return new PaymentResult(false, "Database error: " + exception.getMessage());
        }
    }
}
