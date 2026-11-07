import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConverterA extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private final JTextField amountField = new JTextField(10);
    private final JComboBox<String> fromBox = new JComboBox<>(new String[]{"USD", "EUR", "INR"});
    private final JComboBox<String> toBox = new JComboBox<>(new String[]{"USD", "EUR", "INR"});
    private final JLabel resultLabel = new JLabel("Result: ");
    private final JButton convertButton = new JButton("Convert");

    public ConverterA() {
        super("Converter A - Implements ActionListener");
        buildUi();
        convertButton.addActionListener(this);
        amountField.addActionListener(this);
        addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { dispose(); } });
    }

    private void buildUi() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel("Amount:")); panel.add(amountField);
        panel.add(fromBox); panel.add(new JLabel("to")); panel.add(toBox);
        panel.add(convertButton); panel.add(resultLabel);
        add(panel); setSize(600, 100); setLocationRelativeTo(null);
    }

    @Override public void actionPerformed(ActionEvent event) {
        String result = ConversionSupport.convert(amountField.getText(),
                (String) fromBox.getSelectedItem(), (String) toBox.getSelectedItem());
        resultLabel.setText(result);
        JOptionPane.showMessageDialog(this, result, "Conversion result",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConverterA().setVisible(true));
    }
}
