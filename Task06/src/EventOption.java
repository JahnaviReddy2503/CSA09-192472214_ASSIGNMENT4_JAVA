public record EventOption(int id, String name, String date, String venue) {
    @Override
    public String toString() {
        return id + " - " + name + " (" + date + ", " + venue + ")";
    }
}
