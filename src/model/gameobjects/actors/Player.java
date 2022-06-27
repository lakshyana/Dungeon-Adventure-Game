package model.gameobjects.actors;

import java.util.HashMap;
import java.util.Map;

import model.gameobjects.cell.Treasure;

/**
 * This class represents a player in an adventure game or dungeon game. The class provides
 * operations to get the location of a player, and information on the treasure and arrows collected
 * by a player.
 */
public class Player extends Actor implements Players {
  private Map<Treasure, Integer> treasureCollected;
  private int arrowCount;
  
  /**
   * Constructor for a player taking part in the Dungeon game.
   * @param id the ID number of a player.
   */
  public Player(int id) {
    super("Player ", id);
    initializeTreasureCollected();
    this.arrowCount = 3;
    
  }
  
  /**
   * Get the x coordinate of player's current position.
   * @return the x coordinate of a player's current position.
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
   * Get the y coordinate of player's current position.
   * @return the y coordinate of a player's current position.
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
   * Pick the treasure found in the player's current position.
   */
  @Override
  public void pickTreasure(Treasure t) {
    //add the treasure found in a cave;
    if (! location.getTreasures().isEmpty() && getStatus() == Status.ALIVE) {
      int newAmount = location.getTreasures().get(t);
      int currentAmount = treasureCollected.get(t);
      treasureCollected.put(t, currentAmount + newAmount);
      //Remove the collected treasure from the cell
      Map<Treasure, Integer> updatedTreasure = location.getTreasures();
      updatedTreasure.put(t, 0);
      location.setTreasure(updatedTreasure);
    } else {
      throw new IllegalArgumentException("Cannot pick treasure");
    }
    
  }
  
  /**
   * Get the total treasure collected so far by the player.
   * @return the treasure collected so far.
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
  
  /**
   * Reset treasure collected after a thief steals the player's treasure.
   */
  @Override
  public void resetTreasure() {
    initializeTreasureCollected();
  }
  
  /**
   * Gets the number of arrows at player's disposal.
   * @return the count of arrows held by the player.
   */
  @Override
  public int getArrowCount() {
    return arrowCount;
  }
  
  /**
   * Decrease the number of arrows with the player.
   */
  @Override
  public void decreaseNumArrows() {
    if (arrowCount == 0) {
      throw new IllegalStateException("There are no arrows with the player.");
    }
    this.arrowCount -= 1;
  }
  
  /**
   * Pick up the arrows found in the current cave or tunnel cell in the Dungeon, if available.
   */
  @Override
  public void pickArrows() {
    if (getStatus() == Status.ALIVE) {
      //add the arrows found in a cave or tunnel;
      int arrowsFound = location.getArrows();
      this.arrowCount += arrowsFound;
      location.setArrows(0);
    } else {
      throw new IllegalArgumentException("Cannot pick arrows");
    }
    
  }
  
  
}
  
  
  
