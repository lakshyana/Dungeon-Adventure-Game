package model.gameobjects.actors;

import java.util.HashMap;
import java.util.Map;

import model.gameobjects.cell.Treasure;

/**
 * This class represents a thief and extends the actor class that captures
 * similar functionalities. As thief can steal treasures from a player when it enters the cave where
 * a thief might be present.
 */
public class Thief extends Actor implements Thieves {
  private Map<Treasure, Integer> treasureCollected;
  
  
  /**
   * Constructor for the thief found in the Dungeon game.
   * @param id the ID number of the thief.
   */
  public Thief(int id) {
    super("Thief ", id);
    initializeTreasureCollected();
  }
  
  /**
   * Get the x coordinate of thief's current position.
   * @return the x coordinate of a thief's current position.
   */
  @Override
  public int getPositionX() {
    if (this.location == null) {
      throw new IllegalArgumentException("The player has not entered the Dungeon yet.");
    } else {
      return getLocation().getX();
    }
  }
  
  /**
   * Get the y coordinate of thief's current position.
   * @return the y coordinate of a thief's current position.
   */
  @Override
  public int getPositionY() {
    if (this.location == null) {
      throw new IllegalArgumentException("The player has not entered the Dungeon yet.");
    } else {
      return getLocation().getY();
    }
  }
  
  /**
   * Steal the treasure from the player.
   */
  @Override
  public void pickTreasure(Treasure t) {
    //steal the treasure from the player;
  }
  
  /**
   * Steal the treasure from a player.
   */
  @Override
  public void stealTreasure(Map<Treasure, Integer> treasures) {
    //steal the treasure from a player
    for (Treasure t : treasures.keySet()) {
      int newAmount = treasures.get(t);
      int currentAmount = treasureCollected.get(t);
      treasureCollected.put(t, currentAmount + newAmount);
    }
  }
  
  /**
   * Get the total treasure stolen so far by the thief.
   * @return the treasure stolen so far.
   */
  @Override
  public Map<Treasure, Integer> getTreasureCollected() {
    return this.treasureCollected;
  }
  
  //Private method to initialize the treasure collected.
  private void initializeTreasureCollected() {
    treasureCollected = new HashMap<>();
    for (Treasure t : Treasure.values()) {
      //Assume the quantity of each treasure in a cave to be between 1, and 5
      int quantity = 0;
      treasureCollected.put(t, quantity); //Add the treasure and a random quantity;
    }
  }
  
}
