public class Location {
    private final int id;
    private final String name;
    private final String description;
    private Entity[] entities = new Entity[10];

    public Location(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    //Adds enity object to location's entity array. Returns true if successful.
    public boolean addEntity(Entity entity) {
        for (int i = 0; i < entities.length; i++) {
            if (entities[i] == null) {
                entities[i] = entity;
                return true;
            }
        }
        return false;
    }
    public boolean removeEntity(Entity entity) {
        for (int i = 0; i < entities.length; i++) {
            if (entities[i] == entity) {
                entities[i] = null;
                return true;
            }
        }
        return false;
    }
    public boolean moveEntity(Entity entity, Location newLocation) {
        if (newLocation.addEntity(entity)) {
            if (removeEntity(entity)) {
                return true;
            }
        }
        return false;
    }
    public void locationDescription() {
        System.out.println(description);
    }
    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
}