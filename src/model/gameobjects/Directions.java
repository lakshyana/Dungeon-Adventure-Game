package model.gameobjects;

/**
 * This class represents an enum for the directions that a player can take, or entrances of a
 * location in a dungeon.
 */
public enum Directions {
  NORTH(1), SOUTH(0), EAST(3), WEST(2);
  
  private final int reversePosition;
  private Directions reverseDir;  //Reverse direction of each direction.
  
  /**
   * Constructor for a direction.
   * @param reversePosition the index of the direction opposite to this direction.
   */
  Directions(int reversePosition) {
    this.reversePosition = reversePosition;
    reverseDir = null;
  }
  
  //Set the index of the reverse direction of this direction.
  private void setReverseDir() {
    this.reverseDir = Directions.values()[reversePosition];
  }
  
  /**
   * Get the reverse direction of this direction. This is useful to track the movements of objects
   * that enter from one direction, and pass through an opposite direction, in case of an arrow
   * passing through a cave, for example.
   * @return the reverse direction of the given direction.
   */
  public Directions getReverse() {
    if (this.reverseDir == null) {
      setReverseDir();
    }
    return this.reverseDir;
  }
}




