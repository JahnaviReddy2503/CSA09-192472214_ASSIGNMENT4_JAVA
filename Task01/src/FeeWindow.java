import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class FeeWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    private final FeeService service;
    private final JTextField registrationField = new JTextField();
    private final JTextField amountField = new JTextField();
    private final JLabel status = new JLabel("Ready");
    private final JButton pay = new JButton("Pay");
    private final JButton clear = new JButton("Clear");
    private final JButton receipt = new JButton("Receipt");
    private PaymentResult lastResult;

    public FeeWindow(FeeService service) {
        this.service = service;
        setTitle("Fee Payment");
        setSize(430, 220);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        JPanel fields = new JPanel(new GridLayout(3, 2, 6, 6));
        fields.add(new JLabel("Registration no:")); fields.add(registrationField);
        fields.add(new JLabel("Amount:")); fields.add(amountField);
        fields.add(new JLabel("Status:")); fields.add(status);
        JPanel buttons = new JPanel();
        buttons.add(pay); buttons.add(clear); buttons.add(receipt);
        add(fields, "Center"); add(buttons, "South");
        pay.addActionListener(event -> startPayment());
        clear.addActionListener(event -> clearForm());
        receipt.addActionListener(event -> showReceipt());
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent event) { dispose(); }
        });
    }

    private void startPayment() {
        pay.setEnabled(false);
        String registration = registrationField.getText();
        String amount = amountField.getText();
        new SwingWorker<PaymentResult, Void>() {
            @Override protected PaymentResult doInBackground() {
                return service.pay(registration, amount);
            }
            @Override protected void done() {
                try {
                    lastResult = get();
                    status.setText(lastResult.message());
                } catch (Exception exception) {
                    lastResult = new PaymentResult(false, "Payment failed");
                    status.setText(lastResult.message());
                } finally {
                    pay.setEnabled(true);
                }
            }
        }.execute();
    }

    private void clearForm() {
        registrationField.setText(""); amountField.setText("");
        lastResult = null; status.setText("Ready");
    }

    private void showReceipt() {
        JOptionPane.showMessageDialog(this,
                lastResult == null ? "No successful payment" : lastResult.message());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FeeWindow(new FeeService(
                new InMemoryPaymentRepository())).setVisible(true));
    }
}
