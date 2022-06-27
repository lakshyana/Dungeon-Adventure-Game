package model;

import java.util.List;
import java.util.Set;

import model.gameobjects.cell.Location;

/**
 * This interface represents a Dungeon builder that uses a modified Kruskal algorithm to build
 * Dungeons with varying parameters, such as rows, columns, degree of interconnectivity, wrapping
 * type, and containing a network of tunnels and caves. The Dungeon need to have a minimum number of
 * rows and columns that are needed to fulfill the criteria of 5 minimum possible distance between
 * start and end nodes for the adventure game.
 */
public interface ModifiedKruskal {
  
  /**
   * The method to build a Dungeon for a game.
   * @return a Dungeon object with cells of cave and tunnel types, and some of the caves loaded with
   *         treasures.
   */
  Dungeon buildDungeon();
  
  /**
   * Get the potential paths for the cell nodes.
   * @return List of sets of cells, representing paths or edges.
   */
  List<Set<Location>> getPotentialPaths();
  
  /**
   * Get the final list of paths between cell nodes that were built in this game.
   * @return List of sets of cells, representing paths or edges that were built.
   */
  List<Set<Location>> getFinalPaths();
  
  /**
   * Get the selected list of paths between cell nodes that were used while building the Dungeon.
   * This can be useful for testing.
   * @return List of sets of cells, representing paths or edges that will be built.
   */
  List<Set<Location>> getSelectedPaths();
  
  /**
   * Get the list of discarded paths between cell nodes that were not built in this game.
   * @return List of sets of cells, representing paths or edges that have been discarded.
   */
  List<Set<Location>> getDiscardedPaths();
  
  /**
   * Get the grid will cells that will be used to create a Dungeon object. Useful for testing.
   * @return a 2d array of cells representing the Dungeon grid.
   */
  Location[][] getGrid();
}
