import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConverterD extends JFrame {
    private static final long serialVersionUID = 1L;
    private final JTextField amountField = new JTextField(10);
    private final JComboBox<String> fromBox = new JComboBox<>(new String[]{"USD", "EUR", "INR"});
    private final JComboBox<String> toBox = new JComboBox<>(new String[]{"USD", "EUR", "INR"});
    private final JLabel resultLabel = new JLabel("Result: ");

    public ConverterD() {
        super("Converter D - Lambda");
        JPanel panel = new JPanel(new FlowLayout());
        JButton button = new JButton("Convert");
        panel.add(new JLabel("Amount:")); panel.add(amountField);
        panel.add(fromBox); panel.add(new JLabel("to")); panel.add(toBox);
        panel.add(button); panel.add(resultLabel);
        add(panel); setSize(600, 100); setLocationRelativeTo(null);
        ActionListener converter = event -> convert();
        button.addActionListener(converter);
        amountField.addActionListener(converter);
        addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { dispose(); } });
    }

    private void convert() {
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            double converted = amount * ConversionSupport.rate((String) fromBox.getSelectedItem())
                    / ConversionSupport.rate((String) toBox.getSelectedItem());
            String result = String.format("Result: %.2f %s", converted, toBox.getSelectedItem());
            resultLabel.setText(result);
            JOptionPane.showMessageDialog(this, result, "Conversion result",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException exception) {
            resultLabel.setText("Invalid amount");
            JOptionPane.showMessageDialog(this, "Invalid amount", "Conversion error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConverterD().setVisible(true));
    }
}
