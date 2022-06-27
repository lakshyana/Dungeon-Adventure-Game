import org.junit.Before;
import org.junit.Test;

import controller.Controller;
import controller.Features;
import controller.GuiController;
import model.AdventureGame;
import model.Game;
import model.gameobjects.actors.Player;
import view.IView;

import static org.junit.Assert.assertTrue;

/**
 * This class includes the tests for a view using the mock view and the mock model.
 */
public class MockViewTest {
  private Appendable out;
  private AdventureGame model;
  private IView view;
  private Controller controller;
  
  @Before
  public void setUp() {
    out = new StringBuilder();
    model = new Game(10, 10, 3, true, 100, 10, new Player(1), 10000);
    view = new MockView(model, out);
    controller = new GuiController(view, model);
    controller.playGame();
  }
  
  @Test
  public void makeVisible() {
    //"makeVisible() was called \n"
    view.makeVisible();
    assertTrue(out.toString().contains("makeVisible() was called \n"));
  }
  
  @Test
  public void resetFocus() {
    //"setModel method invoked. Player is location at"
    view.resetFocus();
    assertTrue(out.toString().contains("resetFocus() was called \n"));
  }
  
  @Test
  public void setModel() {
    //"setModel method invoked. Player is location at"
    view.setModel(model);
    assertTrue(out.toString().contains("setModel method invoked. \n Player is location at"));
  }
  
  @Test
  public void setFeatures() {
    view.setModel(model);
    view.setFeatures((Features) controller);
    assertTrue(out.toString().contains("set Features() was called \n"));
    //"set Features() was called \n"
  }
  
  @Test
  public void setPanelFeatures() {
    //"set panel features was called \n"
    view.setModel(model);
    view.setPanelFeatures((Features) controller);
    assertTrue(out.toString().contains("set panel features was called \n"));
  }
  
  @Test
  public void showErrorMessage() {
    //setModel method invoked. Error Message sample:
    view.showErrorMessage("error");
    assertTrue(out.toString().contains("show error message was called \n"));
  }
  
  @Test
  public void refresh() {
    //"Refresh view called \n "
    view.refresh();
    assertTrue(out.toString().contains("Refresh view called \n "));
  }
  
  @Test
  public void clearView() {
    //  "Clear view called \n "
    view.clearView();
    assertTrue(out.toString().contains("Clear view called \n "));
  }
}