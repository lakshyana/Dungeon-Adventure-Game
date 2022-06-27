package controller;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import model.AdventureGame;
import model.gameobjects.Directions;
import model.gameobjects.actors.Status;
import model.gameobjects.cell.CellTypes;
import model.gameobjects.cell.Treasure;

/**
 * Controller implementation for the Dungeon adventure game to handle user moves by executing them
 * using the model.
 */
public class ConsoleController implements Controller {
  private final Appendable out;
  private final Scanner scanner;
  private final AdventureGame game;
  private boolean quit;
  
  /**
   * Constructor for controller.
   * @param in the source to read from
   * @param out the target to print to
   * @param game the game model
   */
  public ConsoleController(Readable in, Appendable out, AdventureGame game) {
    if (in == null || out == null || game == null) {
      throw new IllegalArgumentException("Readable, Appendable, and the model can't be null");
    }
    this.out = out;
    scanner = new Scanner(in);
    this.game = game;
    this.quit = false;
  }
  
  /**
   * Set the game model.
   * @param r number of rows in the Dungeon grid.
   * @param c number of columns in the Dungeon grid.
   * @param ic interconnectivity.
   * @param wrap wrapping or non-wrapping Dungeon.
   * @param t percent of treasure and arrows.
   * @param m percent of monster and thieves.
   * @return true or false to indicate if a model was successfully created.
   */
  @Override
  public boolean setModel(int r, int c, int ic, boolean wrap, int t, int m) {
    return false;
  }
  
  /**
   * Execute a single Dungeon adventure game for a Dungeon game Model. When the game is over, that
   * is when the player dies, the playGame method ends.
   */
  @Override
  public void playGame() {
    try {
      while (game.getPlayer().getStatus().equals(Status.ALIVE) && ! quit) {
        //Sequence of messages
        displayMessages();
        displayActionMenu();
        if (game.getPlayer().getStatus().equals(Status.WON)) {
          out.append("\nYou are at the destination and survived the monster. You won!");
        }
        if (game.getPlayer().getStatus().equals(Status.DEAD)) {
          if (game.getPlayer().getLocation().getType().equals(CellTypes.PIT)) {
            out.append("\nYou fell into a pit!\n Better luck next time.");
          } else {
            out.append("\nChomp, chomp, chomp, you are eaten by an Otyugh!"
                    + "\n Better luck next time.");
          }
        }
      }
    } catch (IOException e) {
      throw new IllegalStateException("Append failed", e);
    }
  }
  
  //Helper method to display messages.
  private void displayMessages() {
    try {
      String smell = "";
      if (game.getPlayer().getLocation().getSmellUnits() == 1) {
        smell = "\nYou smell something slightly pungent nearby";
      }
      if (game.getPlayer().getLocation().getSmellUnits() > 1) {
        smell = "\nYou smell something terribly pungent nearby";
      }
      out.append(smell);
      out.append(String.format("\nYou are in %s %d%d",
              game.getPlayer().getLocation().getType().toString().toLowerCase(),
              game.getPlayer().getPositionX(), game.getPlayer().getPositionY()));
      if (game.getPlayer().getLocation().getArrows() > 0) {
        out.append(String.format("\nYou find %d arrow(s) here",
                game.getPlayer().getLocation().getArrows()));
      }
      if (! game.getPlayer().getLocation().getTreasures().isEmpty()) {
        out.append(String.format("\nYou find %d rubies, %d sapphires, & %d diamonds here",
                game.getPlayer().getLocation().getTreasures().get(Treasure.RUBIES),
                game.getPlayer().getLocation().getTreasures().get(Treasure.SAPPHIRES),
                game.getPlayer().getLocation().getTreasures().get(Treasure.DIAMONDS)));
      }
      out.append("\nEntrances can be found at ");
      for (Directions dir : game.getPlayer().getLocation().getNeighbors().keySet()) {
        out.append(dir.toString().substring(0, 1)).append(", ");
      }
    } catch (IOException ioe) {
      throw new IllegalStateException("Append failed", ioe);
    }
  }
  
  //Helper method to display action menu for choice of moves.
  private void displayActionMenu() {
    try {
      out.append("\n\nMove, Pickup, or Shoot (M-P-S)? ");
      
      String option;
      option = scanner.next();
      out.append(option);
      
      switch (option.toLowerCase()) {
        case "move":
        case "m":
          out.append("\nWhere to? ");
          String dir = scanner.next();
          //out.append(dir);
          switch (dir.toLowerCase()) {
            case "north":
            case "n":
              game.movePlayer(Directions.NORTH);
              break;
            case "south":
            case "s":
              game.movePlayer(Directions.SOUTH);
              break;
            case "east":
            case "e":
              game.movePlayer(Directions.EAST);
              break;
            case "west":
            case "w":
              game.movePlayer(Directions.WEST);
              break;
            default:
              out.append("\nNot a valid move. Please try again.");
          }
          break;
        
        case "pickup":
        case "p":
          out.append("\nWhat? ");
          String item = scanner.next();
          //out.append(dir);
          switch (item.toLowerCase()) {
            case "ruby":
            case "r":
            case "rubies":
              game.getPlayer().pickTreasure(Treasure.RUBIES);
              out.append("You pick up Rubies. \n");
              break;
            case "diamond":
            case "diamonds":
            case "d":
              game.getPlayer().pickTreasure(Treasure.DIAMONDS);
              out.append("You pick up Diamonds. \n");
              break;
            case "sapphire":
            case "sapphires":
            case "s":
              game.getPlayer().pickTreasure(Treasure.SAPPHIRES);
              out.append("You pick up Sapphires. \n");
              break;
            case "arrow":
            case "a":
              game.getPlayer().pickArrows();
              out.append("You pick up arrows. \n");
              break;
            default:
              out.append("\nInvalid pickup. Please try again.");
          }
          break;
        
        case "Shoot":
        case "shoot":
        case "s":
          out.append("\nNo. of caves? ");
          int distance = 0;
          try {
            distance = scanner.nextInt();
            out.append(String.format("%s", distance));
          } catch (InputMismatchException e) {
            try {
              out.append("Invalid option. Please try again.");
            } catch (IOException ioe) {
              throw new IllegalStateException("Append failed", ioe);
            }
          }
          
          out.append("\nWhere to? ");
          String direc = scanner.next();
          out.append(direc);
          out.append("\nYou shoot an arrow into the darkness\n");
          switch (direc.toLowerCase()) {
            case "n":
            case "north":
              out.append(game.shootArrow(Directions.NORTH, distance));
              break;
            case "s":
            case "south":
              out.append(game.shootArrow(Directions.SOUTH, distance));
              break;
            case "e":
            case "east":
              out.append(game.shootArrow(Directions.EAST, distance));
              break;
            case "w":
            case "west":
              out.append(game.shootArrow(Directions.WEST, distance));
              break;
            default:
              out.append("\nInvalid direction. Please try again.");
          }
          break;
        
        case "Quit":
        case "quit":
        case "Q":
        case "q":
          out.append("\nQuitting the game.");
          quit = true;
          break;
        
        default:
          out.append("\nInvalid input, please try again");
      }
    } catch (IllegalArgumentException e) {
      try {
        out.append("Invalid option. Please try again.");
      } catch (IOException ioe) {
        throw new IllegalStateException("Append failed", ioe);
      }
    } catch (IOException ioe) {
      throw new IllegalStateException("Append failed", ioe);
    }
  }
  
  
}
