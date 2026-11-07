import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConverterB extends JFrame {
    private static final long serialVersionUID = 1L;
    private final JTextField amountField = new JTextField(10);
    private final JComboBox<String> fromBox = new JComboBox<>(new String[]{"USD", "EUR", "INR"});
    private final JComboBox<String> toBox = new JComboBox<>(new String[]{"USD", "EUR", "INR"});
    private final JLabel resultLabel = new JLabel("Result: ");

    public ConverterB() {
        super("Converter B - Named Inner Class");
        JPanel panel = new JPanel(new FlowLayout());
        JButton button = new JButton("Convert");
        panel.add(new JLabel("Amount:")); panel.add(amountField);
        panel.add(fromBox); panel.add(new JLabel("to")); panel.add(toBox);
        panel.add(button); panel.add(resultLabel);
        add(panel); setSize(600, 100); setLocationRelativeTo(null);
        ActionListener handler = new ConvertHandler();
        button.addActionListener(handler);
        amountField.addActionListener(handler);
        addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { dispose(); } });
    }

    private class ConvertHandler implements ActionListener {
        @Override public void actionPerformed(ActionEvent event) {
            String result = ConversionSupport.convert(amountField.getText(),
                    (String) fromBox.getSelectedItem(), (String) toBox.getSelectedItem());
            resultLabel.setText(result);
            JOptionPane.showMessageDialog(ConverterB.this, result, "Conversion result",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConverterB().setVisible(true));
    }
}
