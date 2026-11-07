package model;
public record Event(int id, String name, String date, String venue) {
    @Override public String toString() { return name + " (" + date + ", " + venue + ")"; }
}
