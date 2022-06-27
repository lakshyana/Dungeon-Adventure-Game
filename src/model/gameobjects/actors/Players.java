package model.gameobjects.actors;

import java.util.Map;

import model.gameobjects.cell.Treasure;

/**
 * This interface represents a player in an adventure game or dungeon game. The class provides
 * operations to get the location of a player, and information on the treasure and arrows collected
 * by a player.
 */
public interface Players extends Actors {
  
  /**
   * Get the x coordinate of player's current position.
   * @return the x coordinate of a player's current position.
   */
  int getPositionX();
  
  /**
   * Get the y coordinate of player's current position.
   * @return the y coordinate of a player's current position.
   */
  int getPositionY();
  
  /**
   * Pick the treasure found in the player's current position.
   */
  void pickTreasure(Treasure t);
  
  /**
   * Get the total treasure collected so far by the player.
   * @return the treasure collected so far.
   */
  Map<Treasure, Integer> getTreasureCollected();
  
  /**
   * Reset treasure collected after the treasure is stolen.
   */
  void resetTreasure();
  
  /**
   * Gets the number of arrows at player's disposal.
   * @return the count of arrows held by the player.
   */
  int getArrowCount();
  
  /**
   * Decrease the number of arrows with the player.
   */
  void decreaseNumArrows();
  
  /**
   * Pick up the arrows found in the current cave or tunnel cell in the Dungeon, if available.
   */
  void pickArrows();
}
