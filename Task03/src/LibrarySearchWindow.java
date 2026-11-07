import javax.swing.BorderFactory;
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
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public final class LibrarySearchWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    private final JTextField searchField = new JTextField(22);
    private final JComboBox<String> categoryBox =
            new JComboBox<>(new String[]{"All", "Computer Science", "Literature", "Science"});
    private final JButton searchButton = new JButton("Search");
    private final JTextArea resultsArea = new JTextArea();
    private final JButton previousButton = new JButton("Prev");
    private final JButton nextButton = new JButton("Next");
    private final JButton reserveButton = new JButton("Reserve");
    private final JLabel statusLabel = new JLabel("Ready to search.");
    private final List<LibraryBook> books = List.of(
            new LibraryBook("Effective Java", "Joshua Bloch", "Computer Science", true),
            new LibraryBook("Clean Code", "Robert C. Martin", "Computer Science", false),
            new LibraryBook("The Guide", "R. K. Narayan", "Literature", true),
            new LibraryBook("A Brief History of Time", "Stephen Hawking", "Science", true));
    private List<LibraryBook> currentResults = books;
    private int page = 0;

    public LibrarySearchWindow() {
        setTitle("Campus Library Search");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);
        buildUi();
        attachListeners();
        displayResults();
    }

    private void buildUi() {
        setLayout(new BorderLayout(8, 8));
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(createTitlePanel(), BorderLayout.NORTH);
        topPanel.add(createSearchPanel(), BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultsArea), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
        resultsArea.setEditable(false);
        resultsArea.setLineWrap(true);
        resultsArea.setWrapStyleWord(true);
        resultsArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(new JLabel("Campus Library Search"));
        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Search:"));
        panel.add(searchField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryBox);
        panel.add(searchButton);
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(previousButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(reserveButton);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(statusLabel, BorderLayout.SOUTH);
        return panel;
    }

    private void attachListeners() {
        searchButton.addActionListener(event -> search());
        previousButton.addActionListener(event -> changePage(-1));
        nextButton.addActionListener(event -> changePage(1));
        reserveButton.addActionListener(event -> reserveSelectedBook());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                dispose();
            }
        });
    }

    private void search() {
        String query = searchField.getText().trim().toLowerCase();
        String category = (String) categoryBox.getSelectedItem();
        currentResults = books.stream()
                .filter(book -> category.equals("All") || book.category().equals(category))
                .filter(book -> query.isEmpty()
                        || book.title().toLowerCase().contains(query)
                        || book.author().toLowerCase().contains(query))
                .toList();
        page = 0;
        displayResults();
    }

    private void changePage(int direction) {
        if (currentResults.isEmpty()) {
            statusLabel.setText("There are no results to browse.");
            return;
        }
        page = Math.max(0, Math.min(currentResults.size() - 1, page + direction));
        displayResults();
    }

    private void reserveSelectedBook() {
        if (currentResults.isEmpty()) {
            statusLabel.setText("Search for a book before reserving.");
            return;
        }
        LibraryBook book = currentResults.get(page);
        if (!book.available()) {
            statusLabel.setText("This book is currently checked out.");
            return;
        }
        statusLabel.setText("Reserved: " + book.title());
    }

    private void displayResults() {
        resultsArea.setText("");
        for (int index = 0; index < currentResults.size(); index++) {
            String marker = index == page ? " > " : "   ";
            resultsArea.append(marker + (index + 1) + ". " + currentResults.get(index) + "\n");
        }
        if (currentResults.isEmpty()) {
            resultsArea.setText("No books matched your search.");
        }
        statusLabel.setText(currentResults.size() + " result(s).");
        previousButton.setEnabled(page > 0);
        nextButton.setEnabled(page < currentResults.size() - 1);
        reserveButton.setEnabled(!currentResults.isEmpty());
    }

    public static void showWindow() {
        SwingUtilities.invokeLater(() -> new LibrarySearchWindow().setVisible(true));
    }
}
