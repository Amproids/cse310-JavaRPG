import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    private Player player;
    private ArrayList<Enemy> enemies;
    private Scanner scanner;
    private boolean isRunning;
    private Random random;

    public Game() {
        scanner = new Scanner(System.in);
        random = new Random();
        isRunning = false;
    }

    /**
     * Starts the game
     */
    public void start() {
        isRunning = true;
        initializeGame();
        gameLoop();
    }

    /**
     * Initializes the game state
     */
    private void initializeGame() {
        // Create player
        System.out.println("Welcome to the Fantasy RPG!");
        System.out.print("Enter your hero's name: ");
        String playerName = scanner.nextLine();
        player = new Player(playerName);

        // Give player some starting items
        player.getInventory().addItem(new Item.HealthPotion("Small Health Potion", 
            "Restores 30 HP", 10, 30));

        // Initialize enemy list
        enemies = new ArrayList<>();
    }

    /**
     * Main game loop
     */
    private void gameLoop() {
        while (isRunning) {
            displayMainMenu();
            String choice = scanner.nextLine().toLowerCase();
            
            switch (choice) {
                case "1":
                    explore();
                    break;
                case "2":
                    player.getInventory().displayInventory();
                    break;
                case "3":
                    player.getStatus();
                    break;
                case "4":
                    useItem();
                    break;
                case "q":
                    isRunning = false;
                    System.out.println("Thanks for playing!");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }

            // Check if player is dead
            if (!player.isAlive()) {
                System.out.println("Game Over! Your hero has fallen!");
                isRunning = false;
            }
        }
    }

    /**
     * Displays the main menu
     */
    private void displayMainMenu() {
        System.out.println("\n=== " + player.getName() + "'s Adventure ===");
        System.out.println("1. Explore");
        System.out.println("2. Check Inventory");
        System.out.println("3. Check Status");
        System.out.println("4. Use Item");
        System.out.println("Q. Quit");
        System.out.print("Choose an action: ");
    }

    /**
     * Handles the explore action where player can find enemies
     */
    private void explore() {
        System.out.println("\nExploring...");
        if (random.nextDouble() < 0.7) { // 70% chance to find an enemy
            Enemy enemy = generateEnemy();
            System.out.println("You encountered a " + enemy.getName() + "!");
            handleCombat(enemy);
        } else {
            System.out.println("You found nothing of interest...");
        }
    }

    /**
     * Generates a random enemy based on player's level
     */
    private Enemy generateEnemy() {
        String[] enemyTypes = {"goblin", "skeleton", "dragon"};
        String type = enemyTypes[random.nextInt(enemyTypes.length)];
        
        // Enemy level will be player level ±2
        int levelDiff = random.nextInt(5) - 2;
        int enemyLevel = Math.max(1, player.getLevel() + levelDiff);
        
        return Enemy.createEnemy(type, enemyLevel);
    }

    /**
     * Handles combat between player and enemy
     */
    private void handleCombat(Enemy enemy) {
        while (enemy.isAlive() && player.isAlive()) {
            // Display combat status
            System.out.println("\n=== Combat Status ===");
            System.out.println("Player: " + player.getStatus());
            System.out.println("Enemy: " + enemy.getStatus());
            
            // Player's turn
            displayCombatMenu();
            String choice = scanner.nextLine().toLowerCase();
            
            switch (choice) {
                case "1":
                    player.attack(enemy);
                    break;
                case "2":
                    useItem();
                    break;
                case "3":
                    if (random.nextDouble() < 0.5) { // 50% chance to flee
                        System.out.println("You successfully fled!");
                        return;
                    } else {
                        System.out.println("Couldn't escape!");
                    }
                    break;
                default:
                    System.out.println("Invalid choice!");
                    continue;
            }
            
            // Enemy's turn if still alive
            if (enemy.isAlive()) {
                enemy.attack(player);
            }
        }
        
        // Combat ended - check result
        if (!enemy.isAlive()) {
            handleVictory(enemy);
        }
    }

    /**
     * Displays combat menu
     */
    private void displayCombatMenu() {
        System.out.println("\n=== Combat Actions ===");
        System.out.println("1. Attack");
        System.out.println("2. Use Item");
        System.out.println("3. Try to Flee");
        System.out.print("Choose an action: ");
    }

    /**
     * Handles victory after defeating an enemy
     */
    private void handleVictory(Enemy enemy) {
        System.out.println("\nVictory! You defeated " + enemy.getName() + "!");
        
        // Grant experience
        player.gainExperience(enemy.getExperienceValue());
        
        // Handle loot
        ArrayList<Item> loot = enemy.dropLoot();
        if (!loot.isEmpty()) {
            System.out.println("\nLoot dropped:");
            for (Item item : loot) {
                if (player.getInventory().addItem(item)) {
                    System.out.println("- " + item.getName());
                } else {
                    System.out.println("Inventory full! Couldn't pick up " + item.getName());
                }
            }
        }
    }

    /**
     * Handles using items from inventory
     */
    private void useItem() {
        if (player.getInventory().getCurrentSize() == 0) {
            System.out.println("No items in inventory!");
            return;
        }
        
        player.getInventory().displayInventory();
        System.out.print("Enter item slot number to use (or 'c' to cancel): ");
        String input = scanner.nextLine();
        
        if (input.equalsIgnoreCase("c")) {
            return;
        }
        
        try {
            int slot = Integer.parseInt(input);
            Item item = player.getInventory().getItem(slot);
            if (item != null) {
                player.useItem(item);
            } else {
                System.out.println("No item in that slot!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
        }
    }

    /**
     * Main method to run the game
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}