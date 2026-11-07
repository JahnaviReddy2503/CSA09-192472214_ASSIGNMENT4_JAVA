import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

public final class LibrarySearch extends JFrame {
    private static final long serialVersionUID = 1L;
    private final JTextField search = new JTextField(18);
    private final JComboBox<String> category = new JComboBox<>(
            new String[]{"All", "Computer Science", "Literature"});
    private final JTextArea results = new JTextArea();
    private final JLabel status = new JLabel("Ready");
    private final List<String> books = List.of(
            "Java: The Complete Reference | Computer Science",
            "Clean Code | Computer Science",
            "The Guide | Literature");

    public LibrarySearch() {
        setTitle("Campus Library Search");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel top = new JPanel(new BorderLayout());
        JPanel title = new JPanel(new FlowLayout(FlowLayout.CENTER));
        title.add(new JLabel("Campus Library Search"));
        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton searchButton = new JButton("Search");
        searchRow.add(new JLabel("Text:")); searchRow.add(search);
        searchRow.add(category); searchRow.add(searchButton);
        top.add(title, BorderLayout.NORTH); top.add(searchRow, BorderLayout.SOUTH);
        results.setEditable(false);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(results), BorderLayout.CENTER);
        JPanel bottom = new JPanel(new GridLayout(2, 1));
        JPanel buttons = new JPanel(new FlowLayout());
        JButton previous = new JButton("Prev");
        JButton next = new JButton("Next");
        JButton reserve = new JButton("Reserve");
        buttons.add(previous); buttons.add(next); buttons.add(reserve);
        bottom.add(buttons); bottom.add(status);
        add(bottom, BorderLayout.SOUTH);
        searchButton.addActionListener(event -> find());
        search.addActionListener(event -> find());
        previous.addActionListener(event -> status.setText("Previous page selected"));
        next.addActionListener(event -> status.setText("Next page selected"));
        reserve.addActionListener(event -> status.setText("Book reserved"));
        find();
    }

    private void find() {
        String text = search.getText().trim().toLowerCase();
        String selected = (String) category.getSelectedItem();
        results.setText("");
        for (String book : books) {
            if ((selected.equals("All") || book.endsWith(selected))
                    && (text.isEmpty() || book.toLowerCase().contains(text))) {
                results.append(book + "\n");
            }
        }
        status.setText("Search complete");
    }

    private static void testResize() {
        JPanel root = new JPanel(new BorderLayout());
        JPanel north = new JPanel(new FlowLayout());
        north.add(new JLabel("Title")); north.add(new JTextField(10));
        JPanel center = new JPanel(new BorderLayout());
        center.add(new JScrollPane(new JTextArea()), BorderLayout.CENTER);
        JPanel south = new JPanel(new FlowLayout());
        south.add(new JButton("Prev")); south.add(new JButton("Next"));
        root.add(north, BorderLayout.NORTH); root.add(center, BorderLayout.CENTER);
        root.add(south, BorderLayout.SOUTH);
        for (Dimension size : List.of(new Dimension(500, 400),
                new Dimension(800, 600), new Dimension(1100, 800))) {
            root.setSize(size); root.doLayout();
            System.out.println(size.width + "x" + size.height
                    + " north=" + north.getWidth() + "x" + north.getHeight()
                    + " center=" + center.getWidth() + "x" + center.getHeight()
                    + " south=" + south.getWidth() + "x" + south.getHeight());
        }
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--test")) {
            System.out.println("Task: 3");
            System.out.println("Requirement: search and resize behavior");
            System.out.println("Command: javac -d Task03\\out Task03\\src\\*.java && java -cp Task03\\out LibrarySearch --test");
            System.out.println("Date: 2026-11-07");
            testResize();
            return;
        }
        SwingUtilities.invokeLater(() -> new LibrarySearch().setVisible(true));
    }
}
