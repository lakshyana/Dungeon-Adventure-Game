package model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import model.gameobjects.cell.Cell;
import model.gameobjects.cell.CellTypes;
import model.gameobjects.cell.Location;
import model.gameobjects.cell.Treasure;
import random.RandomGenerator;

/**
 * This is a package private class that represents a Dungeon, which is a grid of interconnected
 * cells of "Cave" and "Tunnel" type. Caves and tunnels are distinguished by the number of
 * entrances. A cave can have 1, 3, or 4 entrances, while the tunnels may have only 2 entrances. A
 * Dungeon can be of wrapping or non-wrapping type, and can have 0 or more degree of
 * interconnectivity.
 */
class Dungeon {
  RandomGenerator random;
  Location[][] grid;
  boolean isWrapped;
  int rows;
  int columns;
  int interconnectivity;
  int percentOfTreasure;
  int percentOfMonsters;
  int percentOfArrows;
  Map<Integer, Integer> startPosition;
  Map<Integer, Integer> endPosition;
  
  
  /**
   * Constructor for a Dungeon in the adventure game.
   * @param random The random instance that will be used throughout the program for
   *         assigning random values.
   * @param grid the Dungeon grid with cells in the dungeon.
   * @param rows the number of rows in the dungeon.
   * @param cols the number of columns in the dungeon.
   * @param interconnectivity the interconnectivity of the dungeon.
   * @param isWrapped the wrapping type of the dungeon (wrapping or non-wrapping).
   * @param percentOfTreasure Percentage of caves in the Dungeon with treasures.
   */
  protected Dungeon(RandomGenerator random, Location[][] grid, int rows, int cols,
                    int interconnectivity, boolean isWrapped, int percentOfTreasure,
                    int percentOfMonsters) {
    this.random = random;
    this.grid = grid;
    this.rows = rows;
    this.columns = cols;
    this.isWrapped = isWrapped;
    this.interconnectivity = interconnectivity;
    this.percentOfTreasure = percentOfTreasure;
    this.percentOfMonsters = percentOfMonsters;
    this.percentOfArrows = percentOfTreasure;
    
    //Assign treasure to caves;
    setTreasure();
    //Assign arrows to caves and tunnels;
    setArrows();
    
  }
  
  
  /**
   * Get the interconnectivity of this Dungeon.
   * @return the degree of interconnectivity.
   */
  //@Override
  protected int getInterconnectivity() {
    return this.interconnectivity;
  }
  
  /**
   * Get the rows in the dungeon.
   * @return number of rows.
   */
  //@Override
  protected int getRows() {
    return this.rows;
  }
  
  /**
   * Get the columns in the dungeon.
   * @return number of columns.
   */
  //@Override
  protected int getColumns() {
    return this.columns;
  }
  
  
  /**
   * Get the percent of caves with a Monster in the dungeon.
   * @return percentage.
   */
  //@Override
  protected int getPercentOfMonsters() {
    return percentOfMonsters;
  }
  
  /**
   * Get if the dungeon is wrapped.
   * @return if the dungeon is wrapped or not.
   */
  //@Override
  protected boolean isWrapped() {
    return this.isWrapped;
  }
  
  /**
   * Get the percent of caves in the dungeon with treasure.
   * @return percentage.
   */
  //@Override
  protected int getPercentOfTreasure() {
    return this.percentOfTreasure;
  }
  
  /**
   * Get the Dungeon grid.
   * @return the Dungeon grid.
   */
  //@Override
  protected Cell[][] getGrid() {
    return this.grid;
  }
  
  /**
   * Get the Dungeon grid copy.
   * @return the Dungeon grid copy.
   */
  //@Override
  protected Cell[][] getGridCopy() {
    Cell[][] gridCopy = new Cell[this.grid.length][this.grid[0].length];
    for (int i = 0; i < this.grid.length; i++) {
      for (int j = 0; j < this.grid[0].length; j++) {
        gridCopy[i][j] = new Location(this.grid[i][j]);
      }
    }
    return gridCopy;
  }
  
  /**
   * Get the cell at a location in Dungeon grid.
   * @param x the x coordinate.
   * @param y the y coordinate.
   */
  //@Override
  protected Cell getCellAt(int x, int y) {
    return this.grid[x][y];
  }
  
  
  //Set treasure to caves in the dungeon.
  private void setTreasure() {
    int treasureInt = 0;
    
    //Get all the caves
    List<Cell> caveCells = Arrays.stream(grid).flatMap(Arrays::stream)
            .filter(s -> s.getType().equals(CellTypes.CAVE)).collect(Collectors.toList());
    
    //get the number of caves to assign treasure
    if (this.percentOfTreasure != 0) {
      int numCavesWithTreasure = (int) Math.ceil((caveCells.size() * this.percentOfTreasure) / 100);
      
      //Assign treasure to random caves
      for (int i = 0; i < numCavesWithTreasure; i++) {
        int randomIndex = random.getRandomInt(0, caveCells.size() - 1);
        Cell cave = caveCells.get(randomIndex);
        Map<Treasure, Integer> treasures = new HashMap<>();
        
        //assign random amount of treasure.
        for (Treasure t : Treasure.values()) {
          //Assume the quantity of each treasure in a cave to be between 1, and 5
          int quantity = random.getRandomInt(1, 5);
          treasures.put(t, quantity); //Add the treasure and a random quantity;
        }
        cave.setTreasure(treasures);
        caveCells.remove(cave);
      }
    }
  }
  
  //Set arrows in random cave and tunnel cells in the dungeon.
  private void setArrows() {
    //Get all the cells including tunnels
    List<Cell> cells = Arrays.stream(grid).flatMap(Arrays::stream).collect(Collectors.toList());
    //get the number of cells to assign the arrows.
    if (this.percentOfArrows != 0) {
      int numCells = (int) Math.ceil((cells.size() * this.percentOfArrows) / 100);
      
      //Assign arrows to random caves and tunnels
      for (int i = 0; i < numCells; i++) {
        int randomIndex = random.getRandomInt(0, cells.size() - 1);
        Cell cell = cells.get(randomIndex);
        
        //assign random amount number of arrows.
        int quantity = random.getRandomInt(1, 5);
        cell.setArrows(quantity);
        cells.remove(cell);
      }
    }
  }
  
  
}
