package model.gameobjects.actors;

import model.gameobjects.cell.Cell;

/**
 * This class represents an actor, including a player, and a monster that may be found in some
 * location in the Dungeon. This class includes the methods that are common to any creature in the
 * Dungeon that may need have a location, name, and status.
 */
public abstract class Actor implements Actors {
  
  protected final String name;
  protected final int id;
  protected Cell location;
  protected Status status;
  
  protected Actor(String name, int id) {
    this.name = name;
    this.id = id;
    this.status = Status.ALIVE;
    this.location = null;
  }
  
  /**
   * Get the ID of the actor.
   * @return the int ID.
   */
  @Override
  public int getID() {
    return id;
  }
  
  /**
   * Get the current cell where the actor is located.
   * @return the current cell where the actor is located.
   */
  @Override
  public Cell getLocation() {
    return this.location;
  }
  
  /**
   * Set the location of the actor in the Dungeon.
   */
  @Override
  public void setLocation(Cell location) {
    this.location = location;
  }
  
  /**
   * Get the name of the actor. Eg. Player 1.
   * @return the actor's name.
   */
  @Override
  public String getName() {
    return this.name;
  }
  
  /**
   * Get actor's status.
   * @return Status the status of the actor- alive, dead, or won for a player, and alive, wounded,
   *         or dead for a monster.
   */
  @Override
  public Status getStatus() {
    return status;
  }
  
  /**
   * Set the actor's status.
   */
  @Override
  public void setStatus(Status status) {
    this.status = status;
  }
  
}
