# Java Text Adventure Game

A simple text-based adventure game where you must find a safe place to rest while dealing with hostile creatures and solving puzzles.

## Features

- Combat system with health, damage, and defense stats
- Inventory system for collecting and using items 
- Multiple connected locations to explore
- Interactive NPCs and objects
- Command parser for natural language input like "go to cave" or "pick up key"

## How to Play

Basic commands:
- `look` - View current location and surroundings
- `go [location]` - Move to a different area
- `attack [target]` - Fight enemies
- `use [item] on [target]` - Use items to solve puzzles
- `take [item]` - Pick up items
- `inventory` - Check your items

Win condition: Find and reach a safe place to rest.

## Demo Video



## Files

- `Game.java` - Main game loop and command handling
- `Entity.java` - Base class for player, NPCs and items
- `Location.java` - Map locations and connections
- `Item.java` - Items that can be collected and used
