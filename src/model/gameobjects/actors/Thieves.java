package model.gameobjects.actors;

import java.util.Map;

import model.gameobjects.cell.Treasure;

/**
 * This interface represents a thief in an adventure game or dungeon game. The class provides
 * operations to get the location of the thief, and information on the treasure and arrows stolen by
 * the thief.
 */
public interface Thieves extends Actors {
  
  /**
   * Get the x coordinate of thief's current position.
   * @return the x coordinate of a thief's current position.
   */
  int getPositionX();
  
  /**
   * Get the y coordinate of thief's current position.
   * @return the y coordinate of a thief's current position.
   */
  int getPositionY();
  
  /**
   * Pick up treasure from a location.
   */
  void pickTreasure(Treasure t);
  
  /**
   * Steal treasure from a player.
   */
  void stealTreasure(Map<Treasure, Integer> treasures);
  
  /**
   * Get the total treasure stolen so far by the thief.
   * @return the treasure stolen so far.
   */
  Map<Treasure, Integer> getTreasureCollected();
  
}
