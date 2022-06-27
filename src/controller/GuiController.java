package controller;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;

import model.AdventureGame;
import model.Game;
import model.gameobjects.Directions;
import model.gameobjects.actors.Player;
import model.gameobjects.cell.Treasure;
import view.IView;

/**
 * This is a controller for the graphical interface and implements the Features interface. This
 * means each of the functions in the features interface will give control to the controller.
 */
public class GuiController implements Features {
  private final IView view;
  //Better than basic mvc as it has no specific references to swing components.
  // Features represents what actions can be performed in this program.
  private AdventureGame model;
  private int seed;
  private int rows;
  private int cols;
  private int interconnectivity;
  private boolean wrap;
  private int percentTreasure;
  private int percentMonster;
  
  /**
   * Constructor for the GUI controller.
   * @param model the game model to use
   */
  public GuiController(IView view, AdventureGame model) {
    this.model = model;
    this.view = view;
  }
  
  /**
   * Set the read-only model for the game after user provides the Dungeon settings.
   * @param r number of rows in the Dungeon grid.
   * @param c number of columns in the Dungeon grid.
   * @param ic interconnectivity.
   * @param wrap wrapping or non-wrapping Dungeon.
   * @param t percent of treasure and arrows.
   * @param m percent of monster and thieves.
   * @return true if a valid game instance was created and false otherwise.
   */
  @Override
  public boolean setModel(int r, int c, int ic, boolean wrap, int t, int m) {
    this.rows = r;
    this.cols = c;
    this.wrap = wrap;
    this.interconnectivity = ic;
    this.percentTreasure = t;
    this.percentMonster = m;
    boolean valid = true;
    
    //Start a new game with the provided inputs
    try {
      this.model = new Game(r, c, ic, wrap, t, m, new Player(1), seed);
      
      //if successful then set the view's read-only model
      view.setModel(model);
      //set panel features after setting model.
      view.setPanelFeatures(this);
    } catch (IllegalArgumentException e) {
      valid = false;
    }
    return valid;
  }
  
  
  /**
   * Process the keyboard inputs entered by the user to create a Dungeon for the game.
   * @param inputs the inputs entered by the user for dungeon configuration
   * @return true or false to indicate if a Dungeon was created successfully.
   */
  @Override
  public boolean processGameInputs(List<String> inputs) {
    boolean validInputs = true;
    int row;
    int col;
    int intercon;
    int percentTreasure;
    int percentMonster;
    boolean wrap;
    try {
      row = Integer.parseInt(inputs.get(0));
      col = Integer.parseInt(inputs.get(1));
      wrap = Boolean.parseBoolean(inputs.get(2));
      intercon = Integer.parseInt(inputs.get(3));
      percentTreasure = Integer.parseInt(inputs.get(4));
      percentMonster = Integer.parseInt(inputs.get(5));
    } catch (Exception e) {
      validInputs = false;
      view.showErrorMessage(e.getMessage()); //Display error message in a dialog box
      return validInputs;
    }
    validInputs = setModel(row, col, intercon, wrap, percentTreasure, percentMonster);
    return validInputs;
  }
  
  /**
   * start the program by making the view visible.
   */
  @Override
  public void startProgram() {
    view.makeVisible();
  }
  
  
  /**
   * Exit the program.
   */
  @Override
  public void exitProgram() {
    System.exit(0);
  }
  
  /**
   * Restart the game with the same Dungeon.
   */
  @Override
  public void restartSameGame() {
    //Set model again with the same settings
    setModel(rows, cols, interconnectivity, wrap, percentTreasure, percentMonster);
  }
  
  /**
   * Restart the game with a new Dungeon.
   */
  @Override
  public void restartNewGame() {
    seed = new Random().nextInt(100);
    // Clear the current view
    view.clearView();
    // The new game will be created by the user provides the new Dungeon settings
  }
  
  /**
   * Handle the keyboard commands such as move player, pickup treasure, and shoot an arrow.
   * @param input String input with the command sent by the view.
   * @return Output of the actions in string form.
   */
  @Override
  public String handleKeyboardInputs(String input) {
    StringBuilder out = new StringBuilder();
    String[] inputs = input.split(" ");
    String option = inputs[0];
    
    try {
      switch (option.toLowerCase()) {
        case "move":
        case "m":
          String dir = inputs[1];
          switch (dir) {
            case "north":
            case "n":
              model.movePlayer(Directions.NORTH);
              view.refresh();
              break;
            case "south":
            case "s":
              model.movePlayer(Directions.SOUTH);
              view.refresh();
              break;
            case "east":
            case "e":
              model.movePlayer(Directions.EAST);
              view.refresh();
              break;
            case "west":
            case "w":
              model.movePlayer(Directions.WEST);
              view.refresh();
              break;
            default:
              out.append("\nNot a valid move. Please try again.");
          }
          break;
        
        case "pickup":
        case "p":
          String item = inputs[1];
          
          switch (item) {
            case "ruby":
            case "r":
            case "rubies":
              try {
                model.getPlayer().pickTreasure(Treasure.RUBIES);
                out.append("You picked up Rubies. \n");
                view.refresh();
              } catch (IllegalArgumentException e) {
                //don't do anything if player is dead
              }
              break;
            case "diamond":
            case "diamonds":
            case "d":
              try {
                model.getPlayer().pickTreasure(Treasure.DIAMONDS);
                out.append("You picked up Diamonds. \n");
                view.refresh();
              } catch (IllegalArgumentException e) {
                //don't do anything if player is dead
              }
              break;
            case "sapphire":
            case "sapphires":
            case "s":
              try {
                model.getPlayer().pickTreasure(Treasure.SAPPHIRES);
                out.append("You picked up Sapphires. \n");
                view.refresh();
              } catch (IllegalArgumentException e) {
                //don't do anything if player is dead
              }
              break;
            case "arrow":
            case "a":
              try {
                model.getPlayer().pickArrows();
                out.append("You picked up Arrows. \n");
                view.refresh();
              } catch (IllegalArgumentException e) {
                //don't do anything if player is dead
              }
              break;
            default:
              out.append("\nInvalid pickup. Please try again.");
          }
          break;
        
        case "Shoot":
        case "SHOOT":
        case "shoot":
        case "s":
          int dis = 0;
          String direc = null;
          try {
            dis = Integer.parseInt(inputs[1]);
            direc = inputs[2];
          } catch (InputMismatchException e) {
            //no need to catch this
          }
          switch (direc) {
            case "n":
            case "north":
              out.append(model.shootArrow(Directions.NORTH, dis));
              view.refresh();
              break;
            case "s":
            case "south":
              out.append(model.shootArrow(Directions.SOUTH, dis));
              view.refresh();
              break;
            case "e":
            case "east":
              out.append(model.shootArrow(Directions.EAST, dis));
              view.refresh();
              break;
            case "w":
            case "west":
              out.append(model.shootArrow(Directions.WEST, dis));
              view.refresh();
              break;
            default:
              out.append("\nInvalid direction. Please try again.");
          }
          break;
        default:
          out.append("\nInvalid input, please try again");
      }
    } catch (IllegalArgumentException e) {
      // Invalid direction
      view.showErrorMessage(e.getMessage());
      view.refresh();
    }
    
    //Reset focus after the game state is updated
    view.resetFocus();
    return out.toString();
  }
  
  
  /**
   * Handle an action in a single cell of the board, such as to make a move.
   * @param row the row of the clicked cell
   * @param col the column of the clicked cell
   */
  @Override
  public void handleCellClick(int row, int col) {
    try {
      //make the move based at the cell at given row and column.
      //get the corresponding cell from current location's neighbors
      Directions dir = null;
      
      // Move the player
      //model.movePlayerTo(model.getCellAt(col, row));
      model.movePlayerTo(row, col);
      
      //refresh the view to update the view after the move.
      view.refresh();
      
    } catch (IllegalArgumentException e) {
      view.showErrorMessage(e.getMessage());
    } catch (IllegalStateException ie) {
      
      view.showErrorMessage(ie.getMessage());
    }
  }
  
  
  /**
   * Provide the feature callbacks to the view when the program starts. When the game is over, the
   * playGame method ends.
   */
  @Override
  public void playGame() {
    // give the feature callbacks to the view
    view.setFeatures(this);
  }
}