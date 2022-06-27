package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import model.gameobjects.Directions;
import model.gameobjects.cell.Location;
import random.RandomGenerator;


/**
 * This class represents a builder for a dungeon in an adventure game. The dungeon is built using a
 * modified kruskal algorithm.
 */
public class DungeonBuilderKruskal implements ModifiedKruskal {
  private final RandomGenerator random;
  private final int interconnectivity;
  private final List<Set<Location>> potentialPaths;
  private final List<Set<Location>> discardedPaths;
  private final List<Set<Location>> finalPaths;
  private final int percentOfTreasure;
  private final int percentOfMonster;
  int rows;
  int cols;
  boolean isWrapping;
  private List<Set<Location>> selectedPaths;
  private Location[][] grid;
  
  /**
   * The constructor for a dungeon builder.
   * @param rows the number of rows.
   * @param cols the number of columns.
   * @param interconnectivity the degree of interconnectivity.
   * @param isWrapping If the dungeon is of wrapping or non-wrapping type.
   * @param random the random instance to be used to make random assignments throughout the
   *         program.
   * @param percentOfTreasure the percent of caves to have the treasure.
   */
  public DungeonBuilderKruskal(int rows, int cols, int interconnectivity, boolean isWrapping,
                               RandomGenerator random, int percentOfTreasure,
                               int percentOfMonster) {
    validateGameInputs(rows, cols, interconnectivity, isWrapping, percentOfTreasure,
            percentOfMonster);
    
    this.random = random;
    this.rows = rows;
    this.cols = cols;
    this.isWrapping = isWrapping;
    this.interconnectivity = interconnectivity;
    this.potentialPaths = new ArrayList<>();
    this.selectedPaths = new ArrayList<>();
    this.discardedPaths = new ArrayList<>();
    this.finalPaths = new ArrayList<>();
    this.percentOfTreasure = percentOfTreasure;
    this.percentOfMonster = percentOfMonster;
    setGrid();
    setPotentialPaths();
    setSelectedPath();
  }
  
  /**
   * Builds a dungeon using modified kruskal algorithm. The dungeon network of caves and tunnels is
   * built randomly.
   * @return a dungeon with cells of cave and tunnel types after populating the treasure.
   */
  @Override
  public Dungeon buildDungeon() {
    //selectedPaths = potentialPaths.stream()
    
    //First select paths assuming interconnectivity = 0.
    while (potentialPaths.size() > 0) {
      Set<Location> randomEdge = pickRandomPath(potentialPaths); //Pick a random edge of two cells
      boolean cyclicPath = isPathCyclic(randomEdge); //Check if the path will become cyclic
      
      //Path is cyclic
      if (cyclicPath) {
        discardedPaths.add(randomEdge);
        
        potentialPaths.remove(randomEdge);
      }
      
      //Path not cyclic
      else {
        finalPaths.add(randomEdge);
        potentialPaths.remove(randomEdge);
      }
    }
    // For higher interconnectivity
    if (interconnectivity > 0) {
      //Pick a random path until desired interconnectivity is reached.
      for (int i = 0; i < interconnectivity; i++) {
        Set<Location> randomEdge = pickRandomPath(discardedPaths); //Pick a random edge of two cells
        finalPaths.add(randomEdge);
        discardedPaths.remove(randomEdge);
        
      }
    }
    
    updateNeighbors(); //build paths for final list of paths.
    return new Dungeon(random, grid, rows, cols, interconnectivity, isWrapping, percentOfTreasure,
            percentOfMonster);
  }
  
  /**
   * Get the list of potential paths used to build the Dungeon.
   * @return The list of sets of cells, where each set represents an edge.
   */
  @Override
  public List<Set<Location>> getPotentialPaths() {
    return potentialPaths;
  }
  
  /**
   * Get the paths that were built in the Dungeon.
   * @return the list of set of cell nodes representing an edge.
   */
  @Override
  public List<Set<Location>> getFinalPaths() {
    return finalPaths;
  }
  
  /**
   * Get the list of sets of cells used to build the dungeon.
   * @return the list of sets of cell nodes.
   */
  @Override
  public List<Set<Location>> getSelectedPaths() {
    return selectedPaths;
  }
  
  /**
   * Get the paths that were discarded in the Dungeon.
   * @return the list of set of cell nodes representing the discarded paths.
   */
  @Override
  public List<Set<Location>> getDiscardedPaths() {
    return discardedPaths;
  }
  
  /**
   * Get the Dungeon grid.
   * @return The grid with cells used to build a new Dungeon object for the game.
   */
  @Override
  public Location[][] getGrid() {
    return grid;
  }
  
  
  private void setSelectedPath() {
    List<Set<Location>> finalList = new ArrayList<>();
    for (Set<Location> edge : this.potentialPaths) {
      Iterator<Location> iter = edge.iterator();
      Location thisCell = iter.next();
      Location otherCell = iter.next();
      finalList.add(Set.of(thisCell));
      finalList.add(Set.of(otherCell));
    }
    selectedPaths = finalList.stream().distinct().collect(Collectors.toList());
  }
  
  private void updateNeighbors() {
    for (Set<Location> path : this.discardedPaths) {
      Iterator<Location> iter = path.iterator();
      Location thisCell = iter.next();
      Location otherCell = iter.next();
      //Destroy the existing path for discarded edge
      updatePath(thisCell, otherCell, 0, "remove");
    }
  }
  
  //Add a 2D grid of cells.
  private void setGrid() {
    Location[][] grid = new Location[rows][cols];
    int iD = 0;
    //starting with a grid of cells
    for (int x = 0; x < rows; x++) {
      for (int y = 0; y < cols; y++) {
        Location cell = new Location(iD, x, y);
        grid[x][y] = cell;
        iD += 1;
      }
    }
    this.grid = grid;
  }
  
  //Add a set of cells to potential paths list based on their indices in the grid.
  private void addPotentialPath(int x1, int y1, int x2, int y2, String edgeType) {
    Set<Location> edge = Set.of(grid[x1][y1], grid[x2][y2]);
    potentialPaths.add(edge);
    if (edgeType.equals("vertical")) {
      grid[x1][y1].setS(1);
      grid[x1][y1].setNeighbors(Directions.SOUTH, grid[x2][y2], "add");
      
      grid[x2][y2].setN(1);
      grid[x2][y2].setNeighbors(Directions.NORTH, grid[x1][y1], "add");
    } else if (edgeType.equals("horizontal")) {
      grid[x1][y1].setE(1);
      grid[x1][y1].setNeighbors(Directions.EAST, grid[x2][y2], "add");
      grid[x2][y2].setW(1);
      grid[x2][y2].setNeighbors(Directions.WEST, grid[x1][y1], "add");
    }
  }
  
  private Set<Location> pickRandomPath(List<Set<Location>> paths) {
    int randomIndex = random.getRandomInt(0, paths.size() - 1);
    //Check what happens for lower bound and upper bound = 0;
    return paths.get(randomIndex);
  }
  
  private boolean isPathCyclic(Set<Location> path) {
    Iterator<Location> iter = path.iterator();
    Location cell1 = iter.next();
    Location cell2 = iter.next();
    
    //Check if both the nodes in this path have another edge on the selected path list.
    //Logic used to check for cyclic path: If both the nodes are present in the list, they will
    // create a cyclic and should be included in a discarded set.
    boolean matchNode1 = selectedPaths.stream().anyMatch(s -> s.containsAll(List.of(cell1, cell2)));
    //boolean matchNode2 = selectedPaths.stream().anyMatch(s -> s.contains(cell2));
    
    if (! matchNode1) {
      //System.out.println("path not cyclic");
      Set<Location> set1 =
              selectedPaths.stream().filter(s -> s.contains(cell1)).findFirst().orElse(null);
      Set<Location> set2 =
              selectedPaths.stream().filter(s -> s.contains(cell2)).findFirst().orElse(null);
      
      selectedPaths.remove(set1);
      selectedPaths.remove(set2);
      
      Set<Location> mergedSet = new HashSet<>();
      mergedSet.addAll(set1);
      mergedSet.addAll(set2);
      selectedPaths.add(mergedSet);
    }
    
    return matchNode1;
    //return matchNode1 && matchNode2;
  }
  
  //Update the path when an edge is discarded
  private void updatePath(Location thisCell, Location otherCell, int value, String action) {
    // if the path is to be added, value is 1;
    // if the path is to be removed, value is 0;
    Location left = null;
    Location right = null;
    //if this cell is above or to the left of the other cell.
    if (thisCell.getX() < otherCell.getX() || thisCell.getY() < otherCell.getY()) {
      //if this cell is wrapped neighbor to the other cell
      left = thisCell;
      right = otherCell;
    } else if ((otherCell.getX() < thisCell.getX() || otherCell.getY() < thisCell.getY())) {
      left = otherCell;
      right = thisCell;
    }
    if ((Math.abs(left.getX() - right.getX()) > 1) || (Math.abs(left.getY() - left.getY()) > 1)) {
      //If wrapping neighbors.
      Location temp = right;
      right = left;
      left = temp;
    }
    
    //If horizontal edge
    if (left.getX() == right.getX()) {
      left.setE(value);
      right.setW(value);
      left.setNeighbors(Directions.EAST, right, action);
      right.setNeighbors(Directions.WEST, left, action);
    }
    //If vertical edge
    else if (left.getY() == right.getY()) {
      left.setS(value);
      right.setN(value);
      left.setNeighbors(Directions.SOUTH, right, action);
      right.setNeighbors(Directions.NORTH, right, action);
    }
  }
  
  /**
   * Get all potential edges.
   */
  private void setPotentialPaths() {
    
    //If it's a Wrapping Dungeon.
    if (isWrapping) {
      for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
          //Add horizontal and vertical edges for vertices that are not on the corners.
          if (i < rows - 1 && j < cols - 1) {
            //if not a corner add two edges
            // add vertical edge
            addPotentialPath(i, j, i + 1, j, "vertical");
            //add horizontal edge
            addPotentialPath(i, j, i, j + 1, "horizontal");
          }
          
          //Wrap to the right for vertices that are in the last column but not in the bottom row.
          else if (i < rows - 1 && j == cols - 1) {
            //if the row is less than last row and col is equal to the last column
            // add vertical edge
            addPotentialPath(i, j, i + 1, j, "vertical");
            //add horizontal edge
            addPotentialPath(i, j, i, 0, "horizontal");  //wrap right
          }
          
          //Wrap to the right and also bottom for vertices that are in the last row of the grid.
          else if (i == rows - 1 && j == cols - 1) {
            //If last row and last col.
            // add vertical edge
            addPotentialPath(i, j, 0, j, "vertical"); //wrap down
            //add horizontal edge
            addPotentialPath(i, j, i, 0, "horizontal");  //wrap right
          }
          
          // Wrap down for vertices that are in the first row but not last column.
          else if (i == rows - 1 && j < cols - 1) {
            //If last row but not last col.
            // add vertical edge
            addPotentialPath(i, j, 0, j, "vertical"); //wrap down
            //add horizontal edge
            addPotentialPath(i, j, i, j + 1, "horizontal");
          }
        }
      }
    }
    
    //If it's a non-wrapping Dungeon.
    else if (! isWrapping) {
      for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
          
          //Add horizontal and vertical edges for vertices that are not on the corners.
          if (i < rows - 1 && j < cols - 1) {
            //if not in corner
            // add vertical edge
            addPotentialPath(i, j, i + 1, j, "vertical");
            //add horizontal edge
            addPotentialPath(i, j, i, j + 1, "horizontal");
          }
          // Add vertical edge only for rightmost column.
          else if (i < rows - 1 && j == cols - 1) {
            // add vertical edge
            addPotentialPath(i, j, i + 1, j, "vertical");
          }
          // Add horizontal edge only for bottommost row
          else if (i == rows - 1 && j < cols - 1) {
            //add horizontal edge
            addPotentialPath(i, j, i, j + 1, "horizontal");
          }
          // for the right-most and bottom-most vertex
          else if (i == rows - 1 && j == cols - 1) {
            // no edges for this one.
          }
        }
      }
    }
    
  }
  
  //Validate all the inputs provided.
  private void validateGameInputs(int rows, int cols, int interconnectivity, boolean isWrapped,
                                  int percentOfTreasure, int percentOfMonster)
          throws IllegalArgumentException {
    if (rows < 1 || cols < 1) {
      throw new IllegalArgumentException("Rows and columns cannot be 0 or negative.");
    } else if ((rows == 1 && cols < 6) || (rows < 6 && cols == 1) || (rows == 2 && cols < 3)) {
      throw new IllegalArgumentException("Invalid rows or columns. Not enough nodes in the graph."
              + " To meet the requirement of minimum distance to be at least 5 nodes.");
    }
    if (interconnectivity < 0) {
      throw new IllegalArgumentException("Interconnectivity cannot be negative.");
    }
    
    if (interconnectivity > 0 && isWrapped) {
      int maximumEdges = 2 * rows * cols;
      if (interconnectivity > maximumEdges) {
        throw new IllegalArgumentException(
                "Invalid degree of interconnectivity for the given row\" \n"
                        + "                + \" and column values for wrapping dungeon.");
      }
    }
    if (interconnectivity > 0 && ! isWrapped) {
      int maximumNumEdges = 2 * rows * cols - rows - cols;
      if (interconnectivity > maximumNumEdges - (rows * cols - 1)) {
        throw new IllegalArgumentException("Invalid degree of interconnectivity for the given row"
                + " and column values for non-wrapping dungeon.");
      }
    }
    if (percentOfTreasure < 0 || percentOfTreasure > 100) {
      throw new IllegalArgumentException(
              "The percent of treasure cannot be negative or more than" + " 100");
    }
    if (percentOfMonster < 0 || percentOfMonster > 100) {
      throw new IllegalArgumentException(
              "The percent of treasure cannot be negative or more than" + " 100");
    }
    
  }
  
  
}


