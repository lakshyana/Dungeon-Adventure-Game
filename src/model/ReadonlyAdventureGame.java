package model;

import java.util.List;
import java.util.Map;

import model.gameobjects.actors.Status;
import model.gameobjects.cell.Cell;
import model.gameobjects.cell.Treasure;

/**
 * Represents an interface for the Dungeon adventure game model, including read-only operations.
 */
public interface ReadonlyAdventureGame {
  /**
   * Get the randomly selected start position in this game.
   * @return Map of Integers for x and y position.
   */
  Map<Integer, Integer> getStartPosition();
  
  /**
   * Get the random selected end position in this game, that meets the min distance criteria.
   * @return Map of Integers for x and y position.
   */
  Map<Integer, Integer> getEndPosition();
  
  /**
   * Check if the game is over.
   * @return True or False indicating if the game is over.
   */
  boolean isGameOver();
  
  /**
   * Get the interconnectivity of the Dungeon.
   * @return the degree of interconnectivity.
   */
  int getInterconnectivity();
  
  /**
   * Get the rows in the dungeon.
   * @return number of rows.
   */
  int getRows();
  
  /**
   * Get the columns in the dungeon.
   * @return number of columns.
   */
  int getColumns();
  
  
  /**
   * Get player's location.
   * @return list of x and y coordinates.
   */
  List<Integer> getPlayerLocation();
  
  /**
   * Get the status of the player.
   * @return status of the player, alive, dead, winner.
   */
  Status getPlayerStatus();
  
  /**
   * Get the treasure stolen by the thief in this game.
   * @return the treasure collected by the thief.
   */
  Map<Treasure, Integer> getTreasureStolen();
  
  /**
   * Get the treasure collected by the player in this game.
   * @return the treasure collected by the player.
   */
  Map<Treasure, Integer> getTreasureCollected();
  
  /**
   * Get the arrows collected by the player in this game.
   * @return the arrows collected by the player.
   */
  Integer getArrowCount();
  
  /**
   * Get if the dungeon is wrapped.
   * @return if the dungeon is wrapped or not.
   */
  boolean isWrapped();
  
  /**
   * Get the percent of caves in the dungeon with treasure.
   * @return percentage.
   */
  int getPercentOfTreasure();
  
  /**
   * Get the Dungeon grid copy.
   * @return the copied Dungeon Grid.
   */
  Cell[][] getGridCopy();
  
  /**
   * Get the current location of the player.
   * @return the current location of the player.
   */
  Cell getCurrentLocation();
  
  /**
   * Get a description of the current location of player.
   * @return Map with descriptions of Type of cell, location, treasures, & available moves.
   */
  Map<String, List> describeCurrentLocation();
  
}
