import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public final class ConverterA extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private final JTextField amount = new JTextField(8);
    private final JComboBox<String> from = new JComboBox<>(new String[]{"USD", "EUR"});
    private final JComboBox<String> to = new JComboBox<>(new String[]{"USD", "EUR"});
    private final JLabel result = new JLabel("Result");
    public ConverterA() {
        setTitle("Converter A");
        JButton button = new JButton("Convert");
        JPanel p = new JPanel(new FlowLayout());
        p.add(amount); p.add(from); p.add(to); p.add(button); p.add(result); add(p);
        button.addActionListener(this); amount.addActionListener(this); closeOnExit();
        setSize(500, 100);
    }
    @Override public void actionPerformed(ActionEvent event) { showResult(); }
    private void showResult() {
        result.setText(Conversion.convert(amount.getText(), (String) from.getSelectedItem(),
                (String) to.getSelectedItem()));
        JOptionPane.showMessageDialog(this, result.getText());
    }
    private void closeOnExit() { addWindowListener(new WindowAdapter() {
        @Override public void windowClosing(WindowEvent event) { dispose(); }
    }); }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new ConverterA().setVisible(true)); }
}
