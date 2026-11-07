public record LibraryBook(String title, String author, String category, boolean available) {
    @Override
    public String toString() {
        String availability = available ? "Available" : "Checked out";
        return title + " — " + author + " [" + category + "] - " + availability;
    }
}
