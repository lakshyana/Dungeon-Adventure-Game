package controller;

import java.util.List;

/**
 * This interface represents a set of features that the program offers. Each feature is exposed as a
 * function in this interface. This function is used suitably as a callback by the view, to pass
 * control to the controller. How the view uses them as callbacks is completely up to how the view
 * is designed (e.g. it could use them as a callback for a button, or a callback for a dialog box,
 * or a set of text inputs, etc. Each function is designed to take in the necessary data to complete
 * that functionality.
 */

public interface Features extends Controller {
  
  /**
   * Process the keyboard inputs entered by the user for dungeon game.
   * @param inputs the inputs entered by the user for dungeon configuration
   */
  boolean processGameInputs(List<String> inputs);
  
  /**
   * start the program.
   */
  void startProgram();
  
  /**
   * Exit the program.
   */
  void exitProgram();
  
  /**
   * Restart the game with the same Dungeon.
   */
  void restartSameGame();
  
  /**
   * Restart the game with a new Dungeon.
   */
  void restartNewGame();
  
  /**
   * Handle an action in a single cell of the board, such as to make a move.
   * @param row the row of the clicked cell
   * @param col the column of the clicked cell
   */
  void handleCellClick(int row, int col);
  
  /**
   * Process the relevant keyboard inputs for game related actions, such as moving a player,
   * shooting an arrow, picking up treasure, etc.
   * @param input action command for the model.
   * @return string of output message returned by the model
   */
  String handleKeyboardInputs(String input);
  
  
}
