import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public final class ConverterB extends JFrame {
    private static final long serialVersionUID = 1L;
    private final JTextField amount = new JTextField(8);
    private final JComboBox<String> from = new JComboBox<>(new String[]{"USD", "EUR"});
    private final JComboBox<String> to = new JComboBox<>(new String[]{"USD", "EUR"});
    public ConverterB() {
        setTitle("Converter B");
        JButton button = new JButton("Convert"); JPanel p = new JPanel(new FlowLayout());
        p.add(amount); p.add(from); p.add(to); p.add(button); add(p);
        ActionListener handler = new Handler(); button.addActionListener(handler);
        amount.addActionListener(handler); closeOnExit(); setSize(500, 100);
    }
    private final class Handler implements ActionListener {
        @Override public void actionPerformed(ActionEvent event) {
            JOptionPane.showMessageDialog(ConverterB.this,
                    Conversion.convert(amount.getText(), (String) from.getSelectedItem(),
                            (String) to.getSelectedItem()));
        }
    }
    private void closeOnExit() { addWindowListener(new WindowAdapter() {
        @Override public void windowClosing(WindowEvent event) { dispose(); }
    }); }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new ConverterB().setVisible(true)); }
}
