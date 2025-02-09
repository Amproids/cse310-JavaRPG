public class Entity {
    // Basic entity properties
    protected int id;
    protected String name;
    protected Location currentLocation;
    protected boolean isAlive;
    protected boolean isInanimate;
    protected boolean isBlocked;
    protected Location locationToUnblock;
    protected Entity entityToUnblock;
    protected Inventory inventory;
    protected String description;
    
    // Combat stats
    protected int maxHealth;
    protected int currentHealth;
    protected int baseDamage;
    protected int defense;
    protected boolean isHostile;
    
    // Creates a new entity with basic properties
    public Entity(int id, String name, Location currentLocation, String description, boolean isInanimate) {
        this.id = id;
        this.name = name;
        this.currentLocation = currentLocation;
        this.description = description;
        this.isInanimate = isInanimate;
        
        // Initialize other properties
        this.isAlive = true;
        this.isBlocked = false;
        this.locationToUnblock = null;
        this.entityToUnblock = null;
        this.inventory = new Inventory();
        
        // Set default combat stats
        this.maxHealth = 10;
        this.currentHealth = 10;
        this.baseDamage = 0;
        this.defense = 0;
        this.isHostile = false;
    }
    
    // Handles damage calculation and death effects
    public void takeDamage(int damage) {
        int actualDamage = Math.max(1, damage - defense);
        currentHealth -= actualDamage;
        
        if (currentHealth <= 0) {
            currentHealth = 0;
            isAlive = false;
            if (locationToUnblock != null) {
                locationToUnblock.isBlocked = false;
            }
            if (entityToUnblock != null) {
                entityToUnblock.isBlocked = false;
            }
        }
    }
    
    // Performs attack on target entity
    public boolean attack(Entity target) {
        if (!isAlive || !target.isAlive) {
            return false;
        }

        int damageDealt = calculateDamage();
        target.takeDamage(damageDealt);
        return true;
    }
    
    // Calculates damage based on equipped weapons
    private int calculateDamage() {
        for (Item item : inventory.items) {
            if (item != null && item.name.toLowerCase().contains("sword")) {
                return baseDamage + 5;
            }
        }
        return baseDamage;
    }
    
    // Health getters and setters
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
    
    // Heals entity up to max health
    public void heal(int amount) {
        currentHealth = Math.min(currentHealth + amount, maxHealth);
    }
    
    // Combat stat setters
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
    
    // Returns formatted health status string
    public String getHealthStatus() {
        return name + ": " + currentHealth + "/" + maxHealth + " HP";
    }

    // Sets location to unblock on death
    public void deathUnblocksLocation(Location location) {
        this.locationToUnblock = location;
    }

    // Sets entity to unblock on death
    public void deathUnblocksEntity(Entity entity) {
        this.entityToUnblock = entity;
    }

    // Inventory management inner class
    public class Inventory {
        public Item[] items = new Item[10];
        
        public Inventory() {
        }
        
        // Checks if item exists in inventory
        public boolean inventoryHas(Item item) {
            for (int i = 0; i < items.length; i++) {
                if (items[i] == item) {
                    return true;
                }
            }
            return false;
        }
        
        // Adds item to first empty slot
        public void addItem(Item item) {
            for (int i = 0; i < items.length; i++) {
                if (items[i] == null) {
                    items[i] = item;
                    break;
                }
            }
        }
        
        // Removes specified item from inventory
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