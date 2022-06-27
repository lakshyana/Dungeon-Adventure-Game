import org.junit.Before;
import org.junit.Test;

import java.util.List;

import controller.Features;
import controller.GuiController;
import model.AdventureGame;
import model.Game;
import view.IView;

import static org.junit.Assert.assertTrue;

/**
 * This class contains the tests for the Graphical adventure game view and controller actions
 * with the help of the mock view and model.
 */
public class GuiControllerTest {
  private Appendable out;
  private AdventureGame model;
  private Features controller;
  
  @Before
  public void setUp() {
    out = new StringBuilder();
    model = new Game();
    IView view = new MockView(model, out);
    controller = new GuiController(view, model);
  }
  
  @Test
  public void playGame() {
    controller.playGame();
    assertTrue(out.toString().contains("set Features() was called"));
  }
  
  @Test
  public void setModel() {
    controller.setModel(10, 10, 3, true, 100, 10);
    assertTrue(out.toString().contains("setModel method invoked"));
    //check that game instance was successfully created and the location of player message is
    // displayed
    assertTrue(out.toString().contains("Player is location atCell"));
    //asset that set panel features was called
    assertTrue(out.toString().contains("et panel features was called"));
  }
  
  @Test
  public void processGameInputs() {
    controller.processGameInputs(List.of("10", "10", "3", "true", "100", "10"));
    //Assert that show error message method was called and true was returned for successful game
    // creation
    assertTrue(out.toString().contains("show error message was calledFor input string: \"true\""));
  }
  
  @Test
  public void startProgram() {
    controller.startProgram();
    //Assert that the map view is made visible and makeVisible() was called
    assertTrue(out.toString().contains("makeVisible() was called"));
  }
  
  @Test
  public void restartSameGame() {
    controller.setModel(10, 10, 3, true, 100, 10);
    controller.startProgram();
    controller.restartSameGame();
    //Assert that the map view is made visible and makeVisible() was called
    assertTrue(out.toString().contains("makeVisible() was called"));
  }
  
  @Test
  public void restartNewGame() {
    controller.setModel(10, 10, 3, true, 100, 10);
    controller.startProgram();
    controller.restartNewGame();
    //Assert that game is restarted and clear view is called
    assertTrue(out.toString().contains("Clear view called"));
  }
  
  @Test
  public void handleCellClick() {
    controller.setModel(10, 10, 3, true, 100, 10);
    controller.handleCellClick(0, 0);
    //show error message was called for wrong move
    assertTrue(out.toString().contains("show error message was called"));
  }
  
  @Test
  public void handleKeyboardInputs() {
    controller.setModel(10, 10, 3, true, 100, 10);
    controller.startProgram();
    controller.handleKeyboardInputs("p a");
    
    //assert that view is refreshed after keyboard inputs are handled
    assertTrue(out.toString().contains("Refresh view called"));
    assertTrue(out.toString().contains("resetFocus() was called"));
  }
  
  @Test
  public void exitProgram() {
    controller.exitProgram();
    //Assert that the game is now over after exiting
    assertTrue(model.isGameOver());
  }
}