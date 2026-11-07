import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Optional database adapter. The default application uses the in-memory adapter,
 * so this class does not require a MySQL driver to compile or run.
 */
public final class JdbcPaymentRepository implements PaymentRepository {
    private final Connection connection;

    public JdbcPaymentRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public PaymentResult savePayment(String studentName, String registerNumber, double amount) {
        String sql = """
                INSERT INTO fee_payments (student_name, register_number, amount)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, studentName);
            statement.setString(2, registerNumber);
            statement.setDouble(3, amount);
            statement.executeUpdate();
            return new PaymentResult(true, "Payment saved successfully.", "");
        } catch (SQLException exception) {
            return new PaymentResult(false, "Database error: " + exception.getMessage(), "");
        }
    }
}
