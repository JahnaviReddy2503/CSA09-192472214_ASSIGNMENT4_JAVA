import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConverterA2 extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private final JTextField amount = new JTextField(10);
    private final JComboBox<String> from = new JComboBox<>(new String[]{"USD", "EUR", "INR"});
    private final JComboBox<String> to = new JComboBox<>(new String[]{"USD", "EUR", "INR"});
    private final JLabel result = new JLabel("Result: ");
    public ConverterA2() {
        super("Converter A2");
        JButton convert = new JButton("Convert"); JPanel p = new JPanel(new FlowLayout());
        p.add(amount); p.add(from); p.add(to); p.add(convert); p.add(result); add(p);
        convert.addActionListener(this); amount.addActionListener(this);
        addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { dispose(); } });
        setSize(500, 100); setLocationRelativeTo(null);
    }
    @Override public void actionPerformed(ActionEvent e) {
        String value = ConversionSupport.convert(amount.getText(), (String) from.getSelectedItem(), (String) to.getSelectedItem());
        result.setText(value); JOptionPane.showMessageDialog(this, value);
    }
    public static void main(String[] a) { SwingUtilities.invokeLater(() -> new ConverterA2().setVisible(true)); }
}
