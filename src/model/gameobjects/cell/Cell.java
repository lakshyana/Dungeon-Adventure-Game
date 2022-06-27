package model.gameobjects.cell;

import java.util.List;
import java.util.Map;

import model.gameobjects.Directions;
import model.gameobjects.actors.Monsters;
import model.gameobjects.actors.Thieves;

/**
 * This interface represents a cell or a location in a dungeon, which can have a type "Cave" with 1,
 * 3, or 4 entrances, and another type "Tunnel" with only 2 entrances. The class provides all the
 * operations needed for cells of these types, as listed below.
 */
public interface Cell {
  
  /**
   * Get the cell ID.
   * @return the cell ID.
   */
  int getID();
  
  /**
   * Check if the cell has an entrance to the north.
   * @return 1 if there is an entrance and 0 if not.
   */
  int getN();
  
  /**
   * Set the entrance to the north. 1 if there is an entrance and 0 if not.
   */
  void setN(int n);
  
  /**
   * Check if the cell has an entrance to the south.
   * @return 1 if there is an entrance and 0 if not.
   */
  int getS();
  
  /**
   * Set the entrance to the south. 1 if there is an entrance and 0 if not.
   */
  void setS(int s);
  
  /**
   * Check if the cell has an entrance to the East.
   * @return 1 if there is an entrance and 0 if not.
   */
  int getE();
  
  
  /**
   * Set the entrance to the east. if there is an entrance and 0 if not.
   */
  void setE(int e);
  
  /**
   * Check if the cell has an entrance to the west.
   * @return 1 if there is an entrance and 0 if not.
   */
  int getW();
  
  /**
   * Set the entrance to the east. 1 if there is an entrance and 0 if not.
   */
  void setW(int w);
  
  /**
   * Check if the location has been visited by the player.
   */
  boolean isVisited();
  
  /**
   * Set if the location's has been visited by the player.
   */
  void setVisited(boolean b);
  
  /**
   * Get the location of the cell.
   * @return list of x and y coordinates.
   */
  List<Integer> getLocation();
  
  /**
   * Set the location of the cell.
   */
  void setLocation(int[] location);
  
  /**
   * Get the x coordinate of the cell.
   * @return x coordinate.
   */
  int getX();
  
  /**
   * Get the y coordinate of the cell.
   * @return y coordinate.
   */
  int getY();
  
  /**
   * Get the treasures in this cell if any.
   * @return a map of treasure and its quantity.
   */
  Map<Treasure, Integer> getTreasures();
  
  /**
   * Set the treasures in this cell.
   */
  void setTreasure(Map<Treasure, Integer> treasures);
  
  /**
   * Get the number of arrows contained in the cell.
   * @return count of arrows.
   */
  int getArrows();
  
  /**
   * Set the arrows contained in the cell.
   * @param arrows count of arrows in the cell
   */
  void setArrows(int arrows);
  
  /**
   * Check if the location has any thief.
   * @return true or false.
   */
  boolean hasThief();
  
  /**
   * Check if the location has any player.
   * @return true or false.
   */
  boolean hasPlayer();
  
  /**
   * Set if there's a player at this location.
   */
  void setPlayer(Boolean b);
  
  /**
   * Get the type of this cell.
   * @return "Cave" or "Tunnel".
   */
  CellTypes getType();
  
  /**
   * Set the type of this cell.
   */
  void setType(CellTypes type);
  
  /**
   * Get the smell level in this cell.
   * @return the smell in the cell, based on the presence or absence of a monster nearby.
   */
  Smell getSmellLevel();
  
  /**
   * Get the monster contained in the cell if any.
   * @return the Monster that may be present in the cell.
   */
  Monsters getMonster();
  
  /**
   * Set a monster in the cell.
   */
  void setMonster(Monsters monster);
  
  /**
   * Get the thief contained in the cell if any.
   * @return the thief that may be present in the cell.
   */
  Thieves getThief();
  
  /**
   * Add a thief to the cell.
   * @param thief thief person.
   */
  void setThief(Thieves thief);
  
  /**
   * Get the smell units in this cell based on the presence or absence, and quantity and distance of
   * a monster nearby.
   * @return the smell units in the cell.
   */
  int getSmellUnits();
  
  /**
   * Set the smell in this cell.
   */
  void setSmell(Smell smell, int units);
  
  /**
   * Check if the location is near a pit.
   */
  boolean isNextToPit();
  
  /**
   * Set if the location is near a pit.
   */
  void setAsPitNeighbor(boolean b);
  
  /**
   * Get the neighbors of the cell.
   * @return Map of direction and neighbor in that direction.
   */
  Map<Directions, Cell> getNeighbors();
  
  /**
   * Set the neighbors of the cell. With map of direction and neighbor in that direction.
   */
  void setNeighbors(Directions dir, Cell cell, String action);
  
  /**
   * Get directions of entrances in this cell that a player can take from here.
   * @return List of directions that can be taken by a player.
   */
  List<Directions> getEntrances();
  
  /**
   * Get the formatted grid for display.
   * @return formatted grid for display.
   */
  String toFormattedString();
  
  
}
