package model.gameobjects.cell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import model.gameobjects.Directions;
import model.gameobjects.actors.Monsters;
import model.gameobjects.actors.Thieves;


/**
 * This class represents a cell in a dungeon, which can have a type "Cave" with 1, 3, or 4
 * entrances, and another type "Tunnel" with only 2 entrances. The class provides all the operations
 * needed for cells of these types, as listed below.
 */
public class Location implements Cell {
  private final int iD;
  private final Map<Directions, Cell> neighbors;
  private CellTypes type;
  private int x;
  private int y;
  private int n;
  private int s;
  private int e;
  private int w;
  private Map<Treasure, Integer> treasures;
  private int arrows;
  private Monsters monster;
  private Thieves thief;
  private boolean hasPlayer;
  private Smell smellLevel;
  private int smellUnits;
  private boolean isRevealed;
  private boolean hasThief;
  private boolean isNeighboringAPit;
  
  /**
   * Constructor for a cell in the Dungeon.
   * @param iD the int ID of the cell.
   * @param x The x coordinate of the cell.
   * @param y The y coordinate of the cell.
   */
  public Location(int iD, int x, int y) {
    if (x < 0 || y < 0) {
      throw new IllegalArgumentException("Location should have positive x and y coordinates");
    }
    this.x = x;
    this.y = y;
    this.iD = iD;
    this.type = null;
    this.n = 0;
    this.s = 0;
    this.e = 0;
    this.w = 0;
    this.monster = null;
    this.thief = null;
    this.smellLevel = Smell.NONE;
    this.smellUnits = 0;
    this.treasures = new HashMap<>();
    this.neighbors = new HashMap<>();
    this.arrows = 0;
    this.isRevealed = false;
    this.isNeighboringAPit = false;
    this.hasThief = false;
    this.hasPlayer = false;
  }
  
  /**
   * A copy constructor for a cell location.
   * @param location The cell location to copy.
   */
  public Location(Cell location) {
    if (location == null) {
      throw new IllegalArgumentException("Cell location cannot be null");
    }
    this.x = location.getX();
    this.y = location.getY();
    this.iD = location.getID();
    this.type = location.getType();
    this.n = location.getN();
    this.s = location.getS();
    this.e = location.getE();
    this.w = location.getW();
    this.monster = location.getMonster();
    this.thief = location.getThief();
    this.smellLevel = location.getSmellLevel();
    this.smellUnits = location.getSmellUnits();
    this.treasures = location.getTreasures();
    this.neighbors = location.getNeighbors();
    this.arrows = location.getArrows();
    this.isRevealed = location.isVisited();
    this.isNeighboringAPit = location.isNextToPit();
    this.hasThief = location.hasThief();
    this.hasPlayer = location.hasPlayer();
    
  }
  
  /**
   * Get the cell ID.
   * @return the cell ID.
   */
  @Override
  public int getID() {
    return this.iD;
  }
  
  /**
   * Check if the cell has an entrance to the north.
   * @return 1 if there is an entrance and 0 if not.
   */
  @Override
  public int getN() {
    return this.n;
  }
  
  /**
   * Set the entrance to the north. 1 if there is an entrance and 0 if not.
   */
  @Override
  public void setN(int n) {
    this.n = n;
    updateType();
  }
  
  /**
   * Check if the cell has an entrance to the south.
   * @return 1 if there is an entrance and 0 if not.
   */
  @Override
  public int getS() {
    return this.s;
  }
  
  /**
   * Set the entrance to the south. 1 if there is an entrance and 0 if not.
   */
  @Override
  public void setS(int s) {
    this.s = s;
    updateType();
  }
  
  /**
   * Check if the cell has an entrance to the East.
   * @return 1 if there is an entrance and 0 if not.
   */
  @Override
  public int getE() {
    return this.e;
  }
  
  /**
   * Set the entrance to the east. if there is an entrance and 0 if not.
   */
  @Override
  public void setE(int e) {
    this.e = e;
    updateType();
  }
  
  /**
   * Check if the cell has an entrance to the west.
   * @return 1 if there is an entrance and 0 if not.
   */
  @Override
  public int getW() {
    return this.w;
  }
  
  /**
   * Set the entrance to the east. 1 if there is an entrance and 0 if not.
   */
  @Override
  public void setW(int w) {
    this.w = w;
    updateType();
  }
  
  /**
   * Check if the location has been visited by the player.
   */
  @Override
  public boolean isVisited() {
    return this.isRevealed;
  }
  
  /**
   * Set if the location's has been visited by the player.
   */
  @Override
  public void setVisited(boolean b) {
    this.isRevealed = b;
  }
  
  /**
   * Get the location of the cell.
   * @return list of x and y coordinates.
   */
  @Override
  public List<Integer> getLocation() {
    List<Integer> location = List.of(this.x, this.y);
    return location;
  }
  
  /**
   * Set the location of the cell.
   */
  @Override
  public void setLocation(int[] location) {
    this.x = location[0];
    this.y = location[1];
  }
  
  /**
   * Get the x coordinate of the cell.
   * @return x coordinate.
   */
  @Override
  public int getX() {
    return this.x;
  }
  
  /**
   * Get the y coordinate of the cell.
   * @return y coordinate.
   */
  @Override
  public int getY() {
    return this.y;
  }
  
  /**
   * Get the treasures in this cell if any.
   * @return a map of treasure and its quantity.
   */
  @Override
  public Map<Treasure, Integer> getTreasures() {
    return this.treasures;
  }
  
  /**
   * Set the treasures in this cell.
   */
  @Override
  public void setTreasure(Map<Treasure, Integer> treasures) {
    this.treasures = treasures;
  }
  
  /**
   * Get the number of arrows contained in the cell.
   * @return count of arrows.
   */
  @Override
  public int getArrows() {
    return arrows;
  }
  
  /**
   * Set the arrows contained in the cell.
   * @param arrows count of arrows in the cell
   */
  @Override
  public void setArrows(int arrows) {
    this.arrows = arrows;
  }
  
  /**
   * Get the monster contained in the cell if any.
   * @return the Monster that may be present in the cell.
   */
  @Override
  public Monsters getMonster() {
    return monster;
  }
  
  /**
   * Set a monster in the cell.
   */
  @Override
  public void setMonster(Monsters monster) {
    this.monster = monster;
  }
  
  /**
   * Get the thief contained in the cell if any.
   * @return the thief that may be present in the cell.
   */
  @Override
  public Thieves getThief() {
    return this.thief;
  }
  
  /**
   * Set a thief in the cell.
   */
  @Override
  public void setThief(Thieves thief) {
    if (thief == null) {
      throw new IllegalArgumentException("Thief can't be null.");
    }
    this.thief = thief;
    this.hasThief = true;
  }
  
  /**
   * Check if the location has any thief.
   * @return thief at this location.
   */
  @Override
  public boolean hasThief() {
    return this.hasThief;
  }
  
  /**
   * Check if the location has any player.
   * @return true or false.
   */
  @Override
  public boolean hasPlayer() {
    return this.hasPlayer;
  }
  
  /**
   * Set if there's a player at this location.
   */
  @Override
  public void setPlayer(Boolean b) {
    this.hasPlayer = b;
  }
  
  /**
   * Get the type of this cell.
   * @return "Cave" or "Tunnel".
   */
  @Override
  public CellTypes getType() {
    return this.type;
  }
  
  /**
   * Set the type of this cell.
   */
  @Override
  public void setType(CellTypes type) {
    this.type = type;
  }
  
  /**
   * Get the smell level in this cell.
   * @return the smell in the cell, based on the presence or absence of a monster nearby.
   */
  @Override
  public Smell getSmellLevel() {
    return smellLevel;
  }
  
  /**
   * Get the smell units in this cell based on the presence or absence, and quantity and distance of
   * a monster nearby.
   * @return the smell units in the cell.
   */
  @Override
  public int getSmellUnits() {
    return smellUnits;
  }
  
  /**
   * Set the smell in this cell.
   */
  @Override
  public void setSmell(Smell smell, int units) {
    this.smellUnits += units;
    if (smellUnits < 1) {
      this.smellLevel = Smell.NONE;
    } else {
      this.smellLevel = smell;
    }
  }
  
  /**
   * Check if the location is near a pit.
   */
  @Override
  public boolean isNextToPit() {
    return this.isNeighboringAPit;
  }
  
  /**
   * Set if the location is near a pit.
   */
  @Override
  public void setAsPitNeighbor(boolean b) {
    this.isNeighboringAPit = b;
  }
  
  
  /**
   * Get the neighbors of the cell.
   * @return Map of direction and neighbor in that direction.
   */
  @Override
  public Map<Directions, Cell> getNeighbors() {
    return this.neighbors;
  }
  
  /**
   * Set the neighbors of the cell. With map of direction and neighbor in that direction.
   */
  @Override
  public void setNeighbors(Directions dir, Cell cell, String action) {
    if (action.equalsIgnoreCase("remove")) {
      this.neighbors.remove(dir);
      //
    } else if (action.equalsIgnoreCase("add")) {
      this.neighbors.put(dir, cell);
    }
  }
  
  /**
   * Get directions of entrances in this cell that a player can take from here.
   * @return List of directions that can be taken by a player.
   */
  @Override
  public List<Directions> getEntrances() {
    List<Directions> directions = List.of(Directions.values());
    List<Integer> entrances = List.of(this.n, this.s, this.e, this.w);
    //Get directions where entrances are present.
    List<Directions> actualDirections =
            IntStream.range(0, directions.size()).filter(i -> (entrances.get(i) == 1))
                    .mapToObj(i -> directions.get(i)).collect(Collectors.toList());
    return actualDirections;
  }
  
  @Override
  public String toString() {
    return String.format("%s %d%d", "Cell ", x, y);
  }
  
  /**
   * Get the formatted grid for display.
   * @return formatted grid for display.
   */
  @Override
  public String toFormattedString() {
    return String.format("%s Cell %d%d", type.name().charAt(0), x, y);
  }
  
  
  //Update the type of this cell.
  private void updateType() {
    long count = IntStream.of(this.n, this.s, this.e, this.w).filter(i -> i == 1).count();
    if (count == 2) {
      this.type = CellTypes.TUNNEL;
    } else if (count != 2) {
      this.type = CellTypes.CAVE;
    }
    
    
  }
  
}
