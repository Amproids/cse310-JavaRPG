public class Entity {
    // Entity attributes
    int id;
    String name;
    Location currentLocation;
    boolean isAlive = true;
    boolean isInanimate = false;
    boolean isBlocked = false;
    Location locationToUnblock = null;
    Entity entityToUnblock = null;
    Inventory inventory = new Inventory();
    String description = "";
    
    // Combat attributes
    private int maxHealth = 100;
    private int currentHealth = 100;
    private int baseDamage = 10;
    private int defense = 5;
    private boolean isHostile = false;  // For NPCs that can fight back
    
    // Constructor
    public Entity(int id, String name, Location currentLocation) {
        this.id = id;
        this.name = name;
        this.currentLocation = currentLocation;
        this.isAlive = true;
    }
    
    // Combat methods
    public void takeDamage(int damage) {
        int actualDamage = Math.max(1, damage - defense); // Minimum 1 damage
        currentHealth -= actualDamage;
        
        if (currentHealth <= 0) {
            currentHealth = 0;
            isAlive = false;
            // Unblock the location when the entity dies
            if (locationToUnblock != null) {
                locationToUnblock.isBlocked = false;
            }
            // Unblock the other entity when this one dies
            if (entityToUnblock != null) {
                entityToUnblock.isBlocked = false;
            }
        }
    }
    
    public boolean attack(Entity target) {
        if (!isAlive || !target.isAlive) {
            return false;
        }

        // Calculate damage based on equipped weapon or base damage
        int damageDealt = calculateDamage();
        target.takeDamage(damageDealt);
        
        return true;
    }
    
    private int calculateDamage() {
        // Check for weapon in inventory
        for (Item item : inventory.items) {
            if (item != null && item.name.toLowerCase().contains("sword")) {
                return baseDamage + 5; // Sword adds 5 damage
            }
        }
        return baseDamage;
    }
    
    // Getters and setters
    public int getCurrentHealth() {
        return currentHealth;
    }
    
    public int getMaxHealth() {
        return maxHealth;
    }
    
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }
    
    public void heal(int amount) {
        currentHealth = Math.min(currentHealth + amount, maxHealth);
    }
    
    public void setBaseDamage(int damage) {
        this.baseDamage = damage;
    }
    
    public void setDefense(int defense) {
        this.defense = defense;
    }
    
    public void setHostile(boolean hostile) {
        this.isHostile = hostile;
    }
    
    public boolean isHostile() {
        return isHostile;
    }
    
    public String getHealthStatus() {
        return name + ": " + currentHealth + "/" + maxHealth + " HP";
    }
    public void deathUnblocksLocation(Location location) {
        this.locationToUnblock = location;
    }
    public void deathUnblocksEntity(Entity entity) {
        this.entityToUnblock = entity;
    }
    // Existing Inventory inner class
    public class Inventory {
        public Item[] items = new Item[10];
        
        public Inventory() {
        }
        
        // Inventory methods
        public boolean inventoryHas(Item item) {
            for (int i = 0; i < items.length; i++) {
                if (items[i] == item) {
                    return true;
                }
            }
            return false;
        }
        
        public void addItem(Item item) {
            for (int i = 0; i < items.length; i++) {
                if (items[i] == null) {
                    items[i] = item;
                    break;
                }
            }
        }
        
        public void removeItem(Item item) {
            for (int i = 0; i < items.length; i++) {
                if (items[i] == item) {
                    items[i] = null;
                    break;
                }
            }
        }
    }
}