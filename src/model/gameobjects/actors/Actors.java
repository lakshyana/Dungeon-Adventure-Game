package model.gameobjects.actors;

import model.gameobjects.cell.Cell;

/**
 * This interface represents an actor within a dungeon game such as a person and a monster, that has
 * a name, Id, and some location within a dungeon.
 */
public interface Actors {
  
  /**
   * Get the ID of the actor.
   * @return The actor's ID.
   */
  int getID();
  
  /**
   * Get the name of the actor.
   * @return the name of the actor.
   */
  String getName();
  
  /**
   * Get the location of the actor.
   * @return the cell where the actor is located.
   */
  Cell getLocation();
  
  /**
   * Set the location of the actor.
   * @param location The cell where the actor is to be located.
   */
  void setLocation(Cell location);
  
  /**
   * Get the status of the actor.
   * @return the status of the actor.
   */
  Status getStatus();
  
  /**
   * Set the status of the actor.
   * @param status the status of the actor.
   */
  void setStatus(Status status);
  
}
