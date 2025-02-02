import java.util.Arrays;

public class Game {
    static Entity[] gameEntities = new Entity[10];
    static Location[] gameLocations = new Location[10];
    public static void main(String[] args) {

        //Location setup code
        gameLocations[0] = new Location(0, "Graveyard", "A beautiful graveyard with flowers in bloom.");
        gameLocations[1] = new Location(1, "Grove of Trees", "A lush grove of trees.");
        gameLocations[2] = new Location(2, "Cave Entrance", "A dark cave entrance.");
        gameLocations[3] = new Location(3, "Cave", "Despite the cave only being 30 feet or so, you can barely see what's in front of you.");
        gameLocations[3].isBlocked = true;
        gameLocations[3].blockDialog = "The wolf snarls at you and bears it's teeth. Best to not approach.";
        gameLocations[4] = new Location(4, "Cabin", "A cozy cabin in the woods.");
        gameLocations[5] = new Location(5, "Cabin Interior", "It's warmed up and cozy. Someone has been here but left in a hurry.");
        gameLocations[5].isBlocked = true;
        gameLocations[5].blockDialog = "The cabin door is locked. You need a key to enter.";

        //Connect Graveyard to Grove of Trees
        gameLocations[0].addConnectedLocation(gameLocations[1]);
        gameLocations[1].addConnectedLocation(gameLocations[0]);

        //Connect Grove of Trees to Cave Entrance
        gameLocations[1].addConnectedLocation(gameLocations[2]);
        gameLocations[2].addConnectedLocation(gameLocations[1]);

        //Connect Cave Entrance to Cave Depths
        gameLocations[2].addConnectedLocation(gameLocations[3]);
        gameLocations[3].addConnectedLocation(gameLocations[2]);

        //Connect Grove of Trees to Cabin
        gameLocations[1].addConnectedLocation(gameLocations[4]);
        gameLocations[4].addConnectedLocation(gameLocations[1]);

        //Connect Cabin to Cabin Interior
        gameLocations[4].addConnectedLocation(gameLocations[5]);
        gameLocations[5].addConnectedLocation(gameLocations[4]);

        System.out.println("RPG Game");
        menuIntro();
    }
    public static void menuIntro() {
        System.out.println("1: New Game, 2: Exit");
        String startGame = System.console().readLine("");

        switch (startGame) {
            case "1":
                newGame();
                break;
            case "2":
                System.exit(0);
                break;
            default:
                System.out.println("Invalid input, please try again.");
                System.out.println();
                menuIntro();
        }
    }
    public static void newGame() {
        System.out.println("Starting New Game");
        //Character creation
        String playerName = System.console().readLine("Enter your character name: ");

        //Character creation
        Entity player = new Entity(0, playerName, gameLocations[0]);
        player.inventory.addItem(new Item(0, "yellow gorse", "A small twig of gorse, despite the thorns, it's small yellow flowers are bright."));
        player.inventory.addItem(new Item(1, "rusted sword", "It fits comfortably in your hand."));
        gameEntities[0] = player;
        
        //rabbit
        Entity rabbit = new Entity(1, "rabbit", gameLocations[1]);
        rabbit.description = "A small rabbit rummaging through the grass, not paying attention to you.";
        rabbit.setMaxHealth(10);
        rabbit.setBaseDamage(0);
        rabbit.inventory.addItem(new Item(2, "rabbit pelt", "Very soft."));
        gameEntities[1] = rabbit;

        //wolf
        Entity wolf = new Entity(2, "wolf", gameLocations[2]);
        wolf.description = "A large wolf, it looks hungry and territorial. It's standing in the way of the cave entrance.";
        wolf.setMaxHealth(30);
        wolf.setBaseDamage(10);
        wolf.setHostile(true);
        wolf.deathUnblocksLocation(gameLocations[3]);
        gameEntities[2] = wolf;

        //old barrel
        Entity barrel = new Entity(4, "old barrel", gameLocations[3]);
        barrel.description = "An old barrel, you can faintly see something shine inside.";
        barrel.isInanimate = true;
        barrel.isBlocked = true;
        barrel.inventory.addItem(new Item(3, "A metal key", "It looks like it belongs to a door."));
        gameEntities[4] = barrel;

        //skeleton
        Entity skeleton = new Entity(3, "skeleton", gameLocations[3]);
        skeleton.description = "The ancient bones of a skeleton, you can feel the dark magic that reanimates it. It stands in the way of a barrel. It's looking at you.";
        skeleton.setMaxHealth(80);
        skeleton.setBaseDamage(10);
        skeleton.setHostile(true);
        skeleton.deathUnblocksEntity(barrel);
        gameEntities[3] = skeleton;

        //cabin bed
        gameEntities[5] = new Entity(5, "bed", gameLocations[5]);

        gameLoop();
    }
    public static void gameLoop() {
        boolean isRunning = true;
        Entity player = gameEntities[0];  // Get the player entity
        System.out.println();
        System.out.println("You find yourself in a graveyard. It's well kept and looked after.");
        System.out.println("The yellow gorse growing nearby, make beuty of the otherwise grim place.");
        System.out.println("You feel very tired and in need of rest. Find a safe place to stay before dark.");
        System.out.println("\nWhat would you like to do?");
        System.out.println("Commands: look, go, move, inventory, attack, use, take, help, quit");
        System.out.println(gameLocations[3].isBlocked);
    
        while (isRunning) {
            String input = System.console().readLine("> ").toLowerCase().trim();
            String[] command = input.split(" ");
    
            switch (command[0]) {
                case "quit":
                    System.out.println("Are you sure you want to quit? (y/n)");
                    String confirm = System.console().readLine("> ").toLowerCase();
                    if (confirm.equals("y")) {
                        isRunning = false;
                        System.out.println("Thanks for playing!");
                    }
                    break;
    
                case "inventory":
                    System.out.println("\nYour inventory:");
                    boolean hasItems = false;
                    for (int i = 0; i < player.inventory.items.length; i++) {
                        if (player.inventory.items[i] != null) {
                            hasItems = true;
                            System.out.println("- " + player.inventory.items[i].name);
                        }
                    }
                    if (!hasItems) {
                        System.out.println("Your inventory is empty.");
                    }
                    break;
    
                case "attack":
                    if (command.length < 2) {
                        System.out.println("What do you want to attack? Try 'attack [entity name]'");
                        break;
                    }
                    
                    String targetName = String.join(" ", Arrays.copyOfRange(command, 1, command.length)).toLowerCase();
                    boolean targetFound = false;
                    
                    for (Entity entity : gameEntities) {
                        if (entity != null && entity != player && 
                            entity.currentLocation == player.currentLocation &&
                            entity.name.toLowerCase().contains(targetName)) {
                            
                            if (entity.isBlocked) {
                                System.out.println("You cannot attack that right now.");
                                targetFound = true;
                                break;
                            }
                            
                            System.out.println("You attack the " + entity.name + "!");
                            
                            if (player.attack(entity)) {
                                System.out.println("You hit the " + entity.name + "!");
                                System.out.println(entity.getHealthStatus());
                                
                                if (!entity.isAlive) {
                                    System.out.println("The " + entity.name + " has been defeated!");
                                } else if (entity.isHostile()) {
                                    // Enemy counterattack
                                    entity.attack(player);
                                    System.out.println("The " + entity.name + " strikes back!");
                                    System.out.println(player.getHealthStatus());
                                    
                                    if (!player.isAlive) {
                                        System.out.println("You have been defeated!");
                                        isRunning = false;
                                    }
                                }
                            }
                            targetFound = true;
                            break;
                        }
                    }
                    
                    if (!targetFound) {
                        System.out.println("You don't see that here to attack.");
                    }
                    break;

                case "use":
                    if (command.length < 4) {
                        System.out.println("How do you want to use what? Try 'use [item name] on [target]'");
                        break;
                    }
                    
                    // Find the "on" keyword to separate item name from target
                    int onIndex = -1;
                    for (int i = 0; i < command.length; i++) {
                        if (command[i].equals("on")) {
                            onIndex = i;
                            break;
                        }
                    }
                    
                    if (onIndex == -1) {
                        System.out.println("Invalid use command. Try 'use [item name] on [target]'");
                        break;
                    }
                    
                    // Split the command into item name and target
                    String itemName = String.join(" ", Arrays.copyOfRange(command, 1, onIndex)).toLowerCase();
                    String useTarget = String.join(" ", Arrays.copyOfRange(command, onIndex + 1, command.length)).toLowerCase();
                    
                    // First find if player has the item
                    Item itemToUse = null;
                    for (Item item : player.inventory.items) {
                        if (item != null && item.name.toLowerCase().contains(itemName)) {
                            itemToUse = item;
                            break;
                        }
                    }
                    
                    if (itemToUse == null) {
                        System.out.println("You don't have that item.");
                        break;
                    }
                
                    // Key logic here before the entity check
                    if (itemToUse.name.toLowerCase().contains("key") && useTarget.toLowerCase().contains("cabin")) {
                        if (player.currentLocation == gameLocations[4]) { // If in cabin area
                            gameLocations[5].isBlocked = false;
                            System.out.println("You unlock the cabin door with the key.");
                            player.inventory.removeItem(itemToUse);
                        } else {
                            System.out.println("You need to be closer to the cabin to use the key.");
                        }
                        break;
                    }
                    
                    // Then find the target in the current location
                    boolean targetEntityFound = false;
                    for (Entity entity : gameEntities) {
                        if (entity != null && entity != player && 
                            entity.currentLocation == player.currentLocation &&
                            entity.name.toLowerCase().contains(useTarget)) {
                            System.out.println("You use the " + itemToUse.name + " on the " + entity.name + ".");
                            // Here you could add specific item-entity interaction logic
                            targetEntityFound = true;
                            break;
                        }
                    }
                    
                    if (!targetEntityFound) {
                        System.out.println("You don't see that here to use the item on.");
                    }
                    break;
    
                case "take":
                case "pick":
                    if ((command[0].equals("pick") && (command.length < 3 || !command[1].equals("up"))) ||
                        (command[0].equals("take") && command.length < 2)) {
                        System.out.println("What do you want to take? Try 'take [item name]' or 'pick up [item name]'");
                        break;
                    }
                    
                    // Get the item name from the command
                    int startIndex = command[0].equals("pick") ? 2 : 1;
                    String itemToTake = String.join(" ", Arrays.copyOfRange(command, startIndex, command.length)).toLowerCase();
                    
                    // Look for items in entities in the current location
                    boolean itemFound = false;
                    for (Entity entity : gameEntities) {
                        if (entity != null && entity != player && 
                            entity.currentLocation == player.currentLocation && !entity.isAlive) {
                            for (Item item : entity.inventory.items) {
                                if (item != null && item.name.toLowerCase().contains(itemToTake)) {
                                    // Remove item from entity and add to player inventory
                                    entity.inventory.removeItem(item);
                                    player.inventory.addItem(item);
                                    System.out.println("You take the " + item.name + " from the " + entity.name + ".");
                                    itemFound = true;
                                    break;
                                }
                            }
                            if (itemFound) break;
                        }
                    }
                    
                    if (!itemFound) {
                        System.out.println("You don't see that here to take.");
                    }
                    break;
    
                case "go":
                case "move":
                    if (command.length < 2) {
                        System.out.println("Where do you want to go? Try 'go [location name]'");
                        break;
                    }

                    if (command[1].equals("to")) {
                        command = Arrays.copyOfRange(command, 1, command.length);
                    }
                    
                    // Rebuild the location name from the command
                    String targetLocation = String.join(" ", Arrays.copyOfRange(command, 1, command.length)).toLowerCase();
                    
                    // Check if the target location is connected to current location
                    boolean locationFound = false;
                    for (Location connected : player.currentLocation.connectedLocations) {
                        if (connected != null && connected.name.toLowerCase().contains(targetLocation)) {
                            // We found the location they want to go to
                            locationFound = true;
                            
                            // Check if it's blocked
                            if (connected.isBlocked) {
                                System.out.println(connected.blockDialog);
                                break;
                            } else {
                                // Not blocked, so move there
                                player.currentLocation = connected;
                                System.out.println("\nYou move to " + connected.name);
                                System.out.println(connected.description);
                            }
                            break;
                        }
                    }
                    
                    if (!locationFound) {
                        System.out.println("You can't go there from here.");
                    }
                    break;
    
                case "look":
                    System.out.println("\n" + player.currentLocation.description);
                    
                    // Show available exits
                    System.out.println("\nExits:");
                    boolean hasExits = false;
                    for (Location connected : player.currentLocation.connectedLocations) {
                        if (connected != null) {
                            System.out.println("- " + connected.name);
                            hasExits = true;
                        }
                    }
                    if (!hasExits) {
                        System.out.println("There are no obvious exits.");
                    }
                    
                    // Show entities in current location
                    System.out.println("\nYou see:");
                    boolean foundEntities = false;
                    for (Entity entity : gameEntities) {
                        if (entity != null && entity != player && 
                            entity.currentLocation == player.currentLocation) {
                            if (!entity.isAlive) {
                                if (entity.isInanimate) {
                                    System.out.println("- A broken" + entity.name);
                                } else {
                                    System.out.println("- A dead " + entity.name);
                                }
                            } else {
                                System.out.println("- A " + entity.name);
                            }
                            // Show visible items in the entity's inventory
                            if (!entity.isAlive) {
                                for (Item item : entity.inventory.items) {
                                    if (item != null) {
                                        System.out.println("  " + item.name);
                                    }
                                }
                            }
                            foundEntities = true;
                        }
                    }
                    if (!foundEntities) {
                        System.out.println("There's nothing of interest here.");
                    }
                    break;
    
                case "examine":
                    if (command.length < 2) {
                        System.out.println("What do you want to examine? Try 'examine [item name]' or 'examine [entity name]'");
                        break;
                    }
                    
                    // Rebuild the target name from the command
                    targetName = String.join(" ", Arrays.copyOfRange(command, 1, command.length)).toLowerCase();
                    
                    // First check inventory
                    boolean examineItemFound = false;
                    boolean examineEntityFound = false;
                    
                    for (Item item : player.inventory.items) {
                        if (item != null && item.name.toLowerCase().contains(targetName)) {
                            System.out.println(item.name + ": " + item.description);
                            examineItemFound = true;
                            break;
                        }
                    }
                    
                    // If not in inventory, check entities in the room
                    if (!examineItemFound) {
                        for (Entity entity : gameEntities) {
                            if (entity != null && entity != player && 
                                entity.currentLocation == player.currentLocation &&
                                entity.name.toLowerCase().contains(targetName)) {
                                System.out.println("You examine the " + entity.name + ".");
                                if (entity.description != null && !entity.description.isEmpty() && entity.isAlive) {
                                    System.out.println(entity.description);
                                } else {
                                    System.out.println("There's nothing interesting about it.");
                                }
                                examineEntityFound = true;
                                break;
                            }
                        }
                    }
                    
                    if (!examineItemFound && !examineEntityFound) {
                        System.out.println("You don't see that here.");
                    }
                    break;
                case "sleep":
                case "rest":
                case "relax":
                case "nap":
                    if (player.currentLocation == gameLocations[5]) {
                        System.out.println("You lay down to take a well earned rest.");
                        //win
                        isRunning = false;
                        System.out.println("Congratulations! You have won the game!");
                    } else {
                        System.out.println("There's no place to rest here.");
                    }
                    break;
    
                case "help":
                    System.out.println("\nAvailable commands:");
                    System.out.println("- look: Look around your current location");
                    System.out.println("- examine [target]: Look closely at something");
                    System.out.println("- go/move [location]: Travel to a different location");
                    System.out.println("- inventory: Check your inventory");
                    System.out.println("- attack [target]: Attack an entity");
                    System.out.println("- use [item] on [target]: Use an item on something");
                    System.out.println("- take/pick up [item]: Take an item");
                    System.out.println("- help: Show this help message");
                    System.out.println("- quit: Exit the game");
                    break;
    
                default:
                    System.out.println("I don't understand that command. Type 'help' for available commands.");
                    break;
            }
        }
    }
}