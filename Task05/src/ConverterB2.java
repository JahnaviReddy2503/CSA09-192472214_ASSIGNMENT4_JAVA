import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConverterB2 extends JFrame {
    private static final long serialVersionUID = 1L;
    private final JTextField amount = new JTextField(10);
    private final JComboBox<String> from = new JComboBox<>(new String[]{"USD", "EUR", "INR"});
    private final JComboBox<String> to = new JComboBox<>(new String[]{"USD", "EUR", "INR"});
    public ConverterB2() {
        super("Converter B2"); JButton convert = new JButton("Convert"); JPanel p = new JPanel(new FlowLayout());
        p.add(amount); p.add(from); p.add(to); p.add(convert); add(p);
        ActionListener handler = new ConvertHandler(); convert.addActionListener(handler); amount.addActionListener(handler);
        addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { dispose(); } });
        setSize(500, 100); setLocationRelativeTo(null);
    }
    private class ConvertHandler implements ActionListener {
        @Override public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(thisFrame(), ConversionSupport.convert(amount.getText(), (String) from.getSelectedItem(), (String) to.getSelectedItem()));
        }
    }
    private JFrame thisFrame() { return this; }
    public static void main(String[] a) { SwingUtilities.invokeLater(() -> new ConverterB2().setVisible(true)); }
}
