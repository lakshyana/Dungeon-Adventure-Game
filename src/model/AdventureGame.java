package model;

import model.gameobjects.Directions;
import model.gameobjects.actors.Players;
import model.gameobjects.cell.Cell;

/**
 * This interface provides operations to play a Dungeon Game with one player. A Dungeon is arranged
 * like a 2D grid with a network of caves and tunnels, that the player can explore. In this game,
 * the player will enter the Dungeon at a random cave, and will explore the Dungeon until it arrives
 * at it's randomly selected Destination cave. The required minimum distance for a destination cave
 * is at least 5. Note: The ReadonlyAdventureGame interface is used in the implementation to prevent
 * the access to mutable objects to the view and the controller.
 */
public interface AdventureGame extends ReadonlyAdventureGame {
  
  /**
   * Move the player to a chosen direction.
   * @param direction direction to move the player to.
   */
  void movePlayer(Directions direction);
  
  /**
   * Move the player to a chosen cell location by x and y coordinate.
   * @param x and y coordinate of the cell to move the player to.
   */
  void movePlayerTo(int x, int y);
  
  /**
   * Move the player to a chosen cell location.
   * @param location cell to move the player to.
   */
  void movePlayerTo(Cell location);
  
  /**
   * Shoot an arrow at the Monster, if present towards the given direction and at the exact
   * distance.
   * @param direction direction to shoot.
   * @param distance distance measured by the number of cave that an arrow travels.
   * @return output of the shooting.
   */
  String shootArrow(Directions direction, int distance);
  
  /**
   * Get the start Cave cell in this game.
   * @return the cell of cave type chosen as the start.
   */
  Cell getStart();
  
  /**
   * Get the destination Cave cell in this game.
   * @return the cell of cave type chosen as the destination.
   */
  Cell getDestination();
  
  /**
   * Get the player in this game.
   * @return the name of the player.
   */
  Players getPlayer();
  
  
}
