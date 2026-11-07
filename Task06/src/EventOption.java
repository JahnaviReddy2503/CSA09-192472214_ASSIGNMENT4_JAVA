public record EventOption(int id, String name) {
    @Override
    public String toString() {
        return id + " - " + name;
    }
}
