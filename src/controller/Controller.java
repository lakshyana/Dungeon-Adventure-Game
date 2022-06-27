package controller;

/**
 * Represents a controller for the Dungeon adventure game, which handles user inputs and executes
 * the commands entered to the model, and represents the updated game state to the user in graphical
 * or text form.
 */
public interface Controller {
  /**
   * Set the model to use by the controller after user provides the inputs for creating a Dungeon.
   * @param r number of rows in the Dungeon grid.
   * @param c number of columns in the Dungeon grid.
   * @param ic interconnectivity.
   * @param wrap wrapping or non-wrapping Dungeon.
   * @param t percent of treasure and arrows.
   * @param m percent of monster and thieves.
   * @return true or false to indicate if a model was successfully created
   */
  boolean setModel(int r, int c, int ic, boolean wrap, int t, int m);
  
  /**
   * Execute one game of Dungeon Adventure game. This method will end once the game is over.
   */
  void playGame();
}
