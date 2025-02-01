public class Item {
    int id;
    String name;
    int damage;
    String description;
    String attackString;

    public Item(int id, String name, int damage, String description, String attackString) {
        this.id = id;
        this.name = name;
        this.damage = damage;
        this.description = description;
        this.attackString = attackString;
    }
}