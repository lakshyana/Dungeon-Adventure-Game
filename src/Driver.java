import java.io.InputStreamReader;

import controller.ConsoleController;
import controller.Controller;
import controller.GuiController;
import model.AdventureGame;
import model.Game;
import model.gameobjects.actors.Player;
import model.gameobjects.actors.Players;
import view.IView;
import view.MenuSwingView;

/**
 * The main class for the Dungeon adventure game. This class can be used to run the game as a
 * text-based adventure game, or as a graphical interface based game.
 */
public class Driver {
  /**
   * The main method to run the adventure game.
   * @param args provide inputs if running a text-based game.
   */
  public static void main(String[] args) {
    //If arguments provided for a text-based game
    if (args.length > 0) {
      int rows = Integer.parseInt(args[0]);
      int cols = Integer.parseInt(args[1]);
      boolean isWrapped = Boolean.parseBoolean(args[2].toLowerCase());
      int interconnectivity = Integer.parseInt(args[3]);
      int percentTreasure = Integer.parseInt(args[4]);
      int percentMonsters = Integer.parseInt(args[5]);
      Players player = new Player(1);
      AdventureGame game =
              new Game(rows, cols, interconnectivity, isWrapped, percentTreasure, percentMonsters,
                      player);
      new ConsoleController(new InputStreamReader(System.in), System.out, game).playGame();
    }
    
    //Run the game with graphical interface
    else {
      
      //1. Create an instance of the model (In real game, model will be created later
      AdventureGame model = new Game();
      
      //2. Create an instance of the view
      IView view = new MenuSwingView();
      
      //3. Create an instance of the controller.
      Controller controller = new GuiController(view, model);
      
      // 4. Call playGame() on the controller.
      controller.playGame();
    }
    
    
  }
}
