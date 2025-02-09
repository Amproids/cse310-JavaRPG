public class Enemy extends Entity {
    public Enemy(int id, String name, Location currentLocation, String description, 
                int maxHealth, int baseDamage, int defense) {
        super(id, name, currentLocation, description, false);  // Enemies are never inanimate
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.baseDamage = baseDamage;
        this.defense = defense;
        this.isHostile = true;  // Enemies are always hostile
    }
}