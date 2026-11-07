import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public final class ConverterD extends JFrame {
    private static final long serialVersionUID = 1L;
    private final JTextField amount = new JTextField(8);
    private final JComboBox<String> from = new JComboBox<>(new String[]{"USD", "EUR"});
    private final JComboBox<String> to = new JComboBox<>(new String[]{"USD", "EUR"});
    private final JLabel result = new JLabel("Result");
    public ConverterD() {
        setTitle("Converter D");
        JButton button = new JButton("Convert"); JPanel p = new JPanel(new FlowLayout());
        p.add(amount); p.add(from); p.add(to); p.add(button); p.add(result); add(p);
        ActionListener converter = event -> convert();
        button.addActionListener(converter);
        amount.addActionListener(converter);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent event) { dispose(); }
        });
        setSize(500, 100);
    }
    private void convert() {
        String value = Conversion.convert(amount.getText(), (String) from.getSelectedItem(),
                (String) to.getSelectedItem());
        result.setText(value);
        JOptionPane.showMessageDialog(this, value);
    }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new ConverterD().setVisible(true)); }
}
