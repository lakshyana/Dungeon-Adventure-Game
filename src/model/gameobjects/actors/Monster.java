package model.gameobjects.actors;

/**
 * This class represents a monster in the caves and extends the actor class that captures the
 * similar functionalities. At least one monster is located in the destination cave cell, and more
 * may be found throughout the Dungeon in randomly selected caves. The start cell will never have a
 * monster present.
 */
public class Monster extends Actor implements Monsters {
  private int hits;
  
  public Monster(String name, int id) {
    super(name, id);
    this.hits = 0;
  }
  
  /**
   * Get the number of hits taken by a monster.
   * @return the hits taken by the monster.
   */
  @Override
  public int getHits() {
    return this.hits;
  }
  
  /**
   * Set the number of hits taken by the monster after getting hit by an arrow by the player.
   */
  @Override
  public void takeHit() {
    this.hits += 1;
    setStatus(Status.ALIVE); //Note: this parameter is not used in case of a monster
  }
  
  /**
   * Set monster's status- alive, wounded, or dead.
   */
  @Override
  public void setStatus(Status status) {
    if (hits == 1) {
      this.status = Status.WOUNDED;
    } else if (hits >= 2) {
      this.status = Status.DEAD;
    }
  }
  
  @Override
  public String toString() {
    return String.format("Monster %s. No. of hits taken: %d hits", name, id, hits);
  }
}
