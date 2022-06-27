import java.io.IOException;

import javax.swing.JFrame;

import controller.Features;
import model.AdventureGame;
import model.ReadonlyAdventureGame;
import view.IView;

/**
 * This is a mock view for testing the Dungeon Adventure Game Interface.
 **/
public class MockView extends JFrame implements IView {
  
  private final Appendable out;
  private ReadonlyAdventureGame model;
  
  /**
   * This is a constructor for the mock view.
   * @param model The game model.
   * @param output Output to store the game outputs.
   */
  public MockView(AdventureGame model, Appendable output) {
    this.model = model;
    this.out = output;
  }
  
  /**
   * Make the view visible to start the game session.
   */
  @Override
  public void makeVisible() {
    try {
      out.append("makeVisible() was called \n");
    } catch (IOException e) {
      e.printStackTrace();
    }
    
  }
  
  /**
   * Reset the focus on the appropriate part of the view that has the keyboard listener attached to
   * it, so that keyboard events will still flow through.
   */
  @Override
  public void resetFocus() {
    try {
      out.append("resetFocus() was called \n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Get the set of feature callbacks that the view can use.
   * @param f the set of feature callbacks as a Features object
   */
  @Override
  public void setFeatures(Features f) {
    
    try {
      out.append("set Features() was called \n");
    } catch (IOException e) {
      e.printStackTrace();
    }
    
  }
  
  /**
   * Set the feature callbacks for the board panel after model instance is created.
   * @param f the set of feature callbacks as a Features object
   */
  @Override
  public void setPanelFeatures(Features f) {
    try {
      out.append("set panel features was called \n");
    } catch (IOException e) {
      e.printStackTrace();
      
    }
  }
  
  /**
   * Set the model.
   * @param g the read-only game model.
   */
  @Override
  public void setModel(ReadonlyAdventureGame g) {
    this.model = g;
    try {
      out.append("setModel method invoked. Player is location at" + this.model.getCurrentLocation()
              + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
    
  }
  
  /**
   * Transmit an error message to the view, in case the command could not be processed correctly.
   */
  @Override
  public void showErrorMessage(String error) {
    try {
      out.append("show error message was called" + error + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Signal the view to draw itself.
   */
  @Override
  public void refresh() {
    try {
      out.append("Refresh view called " + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Clear the current view.
   */
  @Override
  public void clearView() {
    try {
      out.append("Clear view called " + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
}
