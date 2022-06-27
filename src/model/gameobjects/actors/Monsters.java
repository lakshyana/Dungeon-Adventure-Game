package model.gameobjects.actors;

/**
 * This interface represents a monster in the caves. At least one monster is located in the
 * destination cave cell, and more may be found throughout the Dungeon in randomly selected caves
 * Note: The start cell will never have a monster.
 */
public interface Monsters extends Actors {
  
  /**
   * Get the number of hits taken by a monster.
   * @return the hits taken by the monster.
   */
  int getHits();
  
  
  /**
   * Set the number of hits taken by the monster after getting hit by an arrow by the player.
   */
  void takeHit();
  
  
}
