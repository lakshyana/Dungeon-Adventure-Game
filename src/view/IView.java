package view;

import controller.Features;
import model.ReadonlyAdventureGame;

/**
 * This class represents the interface for the view classes in an adventure game with graphical
 * interface.
 */
public interface IView {
  
  /**
   * Make the view visible to start the game session.
   */
  void makeVisible();
  
  /**
   * Reset the focus on the appropriate part of the view that has the keyboard listener attached to
   * it, so that keyboard events will still flow through.
   */
  void resetFocus();
  
  /**
   * Get the set of feature callbacks that the view can use.
   * @param f the set of feature callbacks as a Features object
   */
  void setFeatures(Features f);
  
  /**
   * Set the feature callbacks for the board panel after model instance is created.
   * @param f the set of feature callbacks as a Features object
   */
  void setPanelFeatures(Features f);
  
  /**
   * Set the model.
   * @param g the read-only game model.
   */
  void setModel(ReadonlyAdventureGame g);
  
  /**
   * Transmit an error message to the view, in case the command could not be processed correctly.
   */
  void showErrorMessage(String error);
  
  
  //void addContent(Component component, String layout);
  
  /**
   * Signal the view to draw itself.
   */
  void refresh();
  
  /**
   * Clear the current view.
   */
  void clearView();
}
