public class Location {
    int id;
    String name;
    String description;
    boolean isBlocked;
    String blockDialog;
    Location[] connectedLocations = new Location[10];

    public Location(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isBlocked = false;
        this.blockDialog = "The way is blocked.";
    }
    public void addConnectedLocation(Location location) {
        for (int i = 0; i < connectedLocations.length; i++) {
            if (connectedLocations[i] == null) {
                connectedLocations[i] = location;
                break;
            }
        }
    }
}