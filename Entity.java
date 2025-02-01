public class Entity {
    // Basic Identity
    public int id;
    public String name;
    public String description;
    public String type;        // PLAYER, ENEMY, or NPC
    
    // Core Stats
    public int hp;
    public int maxHp;
    public int baseDamage;
    public int defense;
    public int speed;
    public boolean isAlive;
    
    // Progression
    public int level;
    public int experience;
    public int experienceToNextLevel;
    public int gold;
    
    // Equipment & Inventory
    public Inventory inventory;
    public Item equippedWeapon;
    public Item equippedArmor;
    
    // World Interaction
    public Location entityLocation;

    /**
     * Creates a new Entity with specified attributes.
     * @param id Unique identifier for the entity
     * @param name Entity name
     * @param hp Current health points
     * @param maxHp Maximum health points
     * @param entityLocation Starting location
     * @param baseDamage Base damage value
     */
    public Entity(int id, String name, int hp, int maxHp, Location entityLocation) {
        if (entityLocation == null) {
            throw new IllegalArgumentException("Entity location cannot be null");
        }
        this.id = id;
        this.name = name;
        this.hp = hp;
        this.maxHp = maxHp;
        this.inventory = new Inventory(10);
        this.entityLocation = entityLocation;
        this.level = 1;
        this.experience = 100;
        this.defense = 1;
        this.speed = 10;
        this.gold = 0;
        this.type = "NPC";
        this.isAlive = true;
        this.experienceToNextLevel = 100;
        entityLocation.addEntity(this);
    }

    // Status Methods
    public boolean isAlive() { 
        return hp > 0; 
    }
    
    public int getHp() { 
        return hp; 
    }
    
    public int getMaxHp() { 
        return maxHp; 
    }
    
    public String getName() { 
        return name; 
    }

    // Combat Methods
    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);
    }

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }

    public int getAttackDamage() {
        return baseDamage + (equippedWeapon != null ? equippedWeapon.damage : 0);
    }

    public void attack(Entity target) {
        if (target != null && target.isAlive()) {
            target.takeDamage(getAttackDamage());
        }
    }

    // XP methods
    public void gainExperience(int amount) {
        if (amount <= 0) return;
        
        experience += amount;
        while (experience >= experienceToNextLevel) {
            levelUp();
        }
    }

    public void levelUp() {
        level++;
        experience -= experienceToNextLevel;
        experienceToNextLevel = calculateNextLevelExp();
        
        // Increase stats
        maxHp += 10;
        hp = maxHp;
        baseDamage += 2;
    }
    
    private int calculateNextLevelExp() {
        // Common RPG formula: each level needs 50% more XP than the last
        return (int)(experienceToNextLevel * 1.5);
    }

    // Movement/Location Methods
    public boolean moveTo(Location newLocation) {
        return entityLocation.moveEntity(this, newLocation);
    }

    public Location getLocation() {
        return entityLocation;
    }

    // Equipment Methods
    public boolean equipWeapon(Item item) {
        if (inventory.removeItem(item, 1)) {  // Check if we have the item
            if (equippedWeapon != null) {
                inventory.addItem(equippedWeapon, 1);  // Put old weapon back in inventory
            }
            equippedWeapon = item;
            return true;
        }
        return false;
    }

    public boolean unequipWeapon() {
        if (equippedWeapon != null && inventory.addItem(equippedWeapon, 1)) {
            equippedWeapon = null;
            return true;
        }
        return false;
    }

    // Inventory Methods
    public boolean hasItem(Item item) {
        for (int i = 0; i < inventory.items.length; i++) {
            if (inventory.items[i] != null && inventory.items[i].id == item.id) {
                return inventory.quantity[i] > 0;
            }
        }
        return false;
    }

    public Item[] getInventoryContents() {
        return inventory.items;
    }

    // Status/Info Methods
    public String getStatus() {
        StringBuilder status = new StringBuilder();
        status.append(String.format("%s HP: %d/%d", name, hp, maxHp));
        if (equippedWeapon != null) {
            status.append(String.format(" | Weapon: %s", equippedWeapon.name));
        }
        return status.toString();
    }

    @Override
    public String toString() {
        return String.format("Entity[%s, HP=%d/%d, Loc=%s]", 
            name, hp, maxHp, entityLocation.getName());
    }
}