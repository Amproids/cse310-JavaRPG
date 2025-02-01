public class Inventory {
    int capacity;
    Item[] items;
    int[] quantity;

    public Inventory(int capacity) {
        this.capacity = capacity;
        this.items = new Item[capacity];
        this.quantity = new int[capacity];
    }

    public boolean addItem(Item item, int amount) {
        // First check if item already exists
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].id == item.id) {
                quantity[i] += amount;
                return true;
            }
        }
        // If not found, add to first empty slot
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                items[i] = item;
                quantity[i] = amount;
                return true;
            }
        }
        return false;  // Inventory is full
    }
    public boolean removeItem(Item item, int amount) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].id == item.id) {
                if (quantity[i] >= amount) {
                    quantity[i] -= amount;
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        return false;
    }
}