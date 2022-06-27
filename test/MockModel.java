import java.util.List;
import java.util.Map;

import model.AdventureGame;
import model.gameobjects.Directions;
import model.gameobjects.actors.Players;
import model.gameobjects.actors.Status;
import model.gameobjects.cell.Cell;
import model.gameobjects.cell.Treasure;

/**
 * This class is a mock up of the Dungeon Adventure Game Interface.
 */
public class MockModel implements AdventureGame {
  private final Appendable out;
  
  /**
   * Construct a new Mock Model instance.
   * @param output saves the outputs.
   */
  public MockModel(Appendable output) {
    this.out = output;
  }
  
  /**
   * Move the player to a chosen direction.
   * @param direction direction to move the player to.
   */
  @Override
  public void movePlayer(Directions direction) {
    try {
      out.append("movePlayerTo() by direction ");
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
  /**
   * Move the player to a chosen cell location by x and y coordinate.
   * @param x and y coordinate of the cell to move the player to.
   */
  @Override
  public void movePlayerTo(int x, int y) {
    try {
      out.append("movePlayerTo() x and y ");
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
  /**
   * Move the player to a chosen cell location.
   * @param location cell to move the player to.
   */
  @Override
  public void movePlayerTo(Cell location) {
    try {
      out.append("movePlayerTo() cell ");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Shoot an arrow at the Monster, if present towards the given direction and at the exact
   * distance.
   * @param direction direction to shoot.
   * @param distance distance measured by the number of cave that an arrow travels.
   * @return output of the shooting.
   */
  @Override
  public String shootArrow(Directions direction, int distance) {
    try {
      out.append("shootArrow() ");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  /**
   * Get the start Cave cell in this game.
   * @return the cell of cave type chosen as the start.
   */
  @Override
  public Cell getStart() {
    try {
      out.append("getStart() ");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  /**
   * Get the destination Cave cell in this game.
   * @return the cell of cave type chosen as the destination.
   */
  @Override
  public Cell getDestination() {
    try {
      out.append("getDestination() ");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  /**
   * Get the player in this game.
   * @return the name of the player.
   */
  @Override
  public Players getPlayer() {
    try {
      out.append("getPlayer() ");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  /**
   * Get the randomly selected start position in this game.
   * @return Map of Integers for x and y position.
   */
  @Override
  public Map<Integer, Integer> getStartPosition() {
    try {
      out.append("getStartPosition() ");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  /**
   * Get the random selected end position in this game, that meets the min distance criteria.
   * @return Map of Integers for x and y position.
   */
  @Override
  public Map<Integer, Integer> getEndPosition() {
    try {
      out.append("getEndPosition() ");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  /**
   * Check if the game is over.
   * @return True or False indicating if the game is over.
   */
  @Override
  public boolean isGameOver() {
    try {
      out.append("isGameOver() ");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
  
  /**
   * Get the interconnectivity of the Dungeon.
   * @return the degree of interconnectivity.
   */
  @Override
  public int getInterconnectivity() {
    try {
      out.append("getInterconnectivity() ");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }
  
  /**
   * Get the rows in the dungeon.
   * @return number of rows.
   */
  @Override
  public int getRows() {
    try {
      out.append("getRows() ");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }
  
  /**
   * Get the columns in the dungeon.
   * @return number of columns.
   */
  @Override
  public int getColumns() {
    try {
      out.append("getColumns() ");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }
  
  /**
   * Get player's location.
   * @return list of x and y coordinates.
   */
  @Override
  public List<Integer> getPlayerLocation() {
    try {
      out.append("getPlayerLocation() ");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  /**
   * Get the status of the player.
   * @return status of the player, alive, dead, winner.
   */
  @Override
  public Status getPlayerStatus() {
    try {
      out.append("getPlayerStatus() ");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  /**
   * Get the treasure stolen by the thief in this game.
   * @return the treasure collected by the thief.
   */
  @Override
  public Map<Treasure, Integer> getTreasureStolen() {
    try {
      out.append("getTreasureStolen() ");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  /**
   * Get the treasure collected by the player in this game.
   * @return the treasure collected by the player.
   */
  @Override
  public Map<Treasure, Integer> getTreasureCollected() {
    try {
      out.append("getTreasureCollected() ");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  /**
   * Get the arrows collected by the player in this game.
   * @return the arrows collected by the player.
   */
  @Override
  public Integer getArrowCount() {
    try {
      out.append("getArrowCount() ");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  /**
   * Get if the dungeon is wrapped.
   * @return if the dungeon is wrapped or not.
   */
  @Override
  public boolean isWrapped() {
    try {
      out.append("isWrapped() ");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
  
  /**
   * Get the percent of caves in the dungeon with treasure.
   * @return percentage.
   */
  @Override
  public int getPercentOfTreasure() {
    try {
      out.append("getPercentOfTreasure() ");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }
  
  /**
   * Get the Dungeon grid copy.
   * @return the copied Dungeon Grid.
   */
  @Override
  public Cell[][] getGridCopy() {
    try {
      out.append("getGridCopy() ");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  /**
   * Get the current location of the player.
   * @return the current location of the player.
   */
  @Override
  public Cell getCurrentLocation() {
    try {
      out.append("getCurrentLocation() ");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  /**
   * Get a description of the current location of player.
   * @return Map with descriptions of Type of cell, location, treasures, & available moves.
   */
  @Override
  public Map<String, List> describeCurrentLocation() {
    try {
      out.append("describeCurrentLocation() ");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
