import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.ExecutionException;

public final class FeeWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    private final FeeService feeService;
    private final JTextField studentNameField = new JTextField();
    private final JTextField registerNumberField = new JTextField();
    private final JTextField amountField = new JTextField();
    private final JLabel statusLabel = new JLabel("Enter payment details.");
    private final JButton payButton = new JButton("Pay Fee");
    private final JButton clearButton = new JButton("Clear");
    private final JButton receiptButton = new JButton("Receipt");
    private PaymentResult lastPayment;

    public FeeWindow(FeeService feeService) {
        this.feeService = feeService;
        setTitle("College Fee Payment");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(440, 260);
        setLocationRelativeTo(null);
        buildUi();
        attachListeners();
    }

    private void buildUi() {
        var fields = new JPanel(new GridLayout(3, 2, 8, 8));
        fields.setBorder(BorderFactory.createEmptyBorder(12, 12, 8, 12));
        fields.add(new JLabel("Student name:"));
        fields.add(studentNameField);
        fields.add(new JLabel("Register number:"));
        fields.add(registerNumberField);
        fields.add(new JLabel("Amount:"));
        fields.add(amountField);

        var buttons = new JPanel();
        buttons.add(payButton);
        buttons.add(clearButton);
        buttons.add(receiptButton);

        add(fields, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        add(statusLabel, BorderLayout.NORTH);
    }

    private void attachListeners() {
        payButton.addActionListener(event -> startPayment());
        clearButton.addActionListener(event -> clearForm());
        receiptButton.addActionListener(event -> showReceipt());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                dispose();
            }
        });
    }

    private void startPayment() {
        setPaymentControlsEnabled(false);
        statusLabel.setText("Processing payment...");

        String name = studentNameField.getText();
        String registerNumber = registerNumberField.getText();
        String amount = amountField.getText();

        SwingWorker<PaymentResult, Void> worker = new SwingWorker<>() {
            @Override
            protected PaymentResult doInBackground() {
                return feeService.payFee(name, registerNumber, amount);
            }

            @Override
            protected void done() {
                try {
                    lastPayment = get();
                    statusLabel.setText(lastPayment.message());
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    lastPayment = new PaymentResult(false, "Payment was interrupted.", "");
                    statusLabel.setText(lastPayment.message());
                } catch (ExecutionException exception) {
                    lastPayment = new PaymentResult(false, "Payment could not be completed.", "");
                    statusLabel.setText(lastPayment.message());
                } finally {
                    setPaymentControlsEnabled(true);
                }
            }
        };
        worker.execute();
    }

    private void showReceipt() {
        if (lastPayment == null || !lastPayment.successful()) {
            JOptionPane.showMessageDialog(this, "Complete a successful payment first.",
                    "Receipt", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, lastPayment.receiptText(),
                "Receipt", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearForm() {
        studentNameField.setText("");
        registerNumberField.setText("");
        amountField.setText("");
        lastPayment = null;
        statusLabel.setText("Enter payment details.");
    }

    private void setPaymentControlsEnabled(boolean enabled) {
        payButton.setEnabled(enabled);
        receiptButton.setEnabled(enabled);
    }

    public static void show(FeeService feeService) {
        SwingUtilities.invokeLater(() -> new FeeWindow(feeService).setVisible(true));
    }
}
