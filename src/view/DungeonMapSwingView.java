package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import controller.Features;
import model.ReadonlyAdventureGame;

/**
 * This class represents a Dungeon map or grid view to show the entire Dungeon representation.
 */
public class DungeonMapSwingView extends JFrame implements IView {
  
  private final boolean viewInCheatMode;
  private final int width;
  private final int height;
  private BoardPanel boardPanel;
  private JScrollPane scrollPane;
  private ReadonlyAdventureGame model;
  
  
  /**
   * Constructor for Dungeon Map View.
   */
  public DungeonMapSwingView(int width, int height) {
    super("Dungeon Map");
    
    this.width = width;
    this.height = height;
    
    this.setSize(width, height);
    this.setBackground(Color.BLACK);
    setLocation(0, 0);
    
    //Adding layout
    this.setLayout(new BorderLayout());
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
    //Start with the menu window view
    //this.container = new JPanel();
    this.viewInCheatMode = false;
  }
  
  /**
   * Make the view visible to start the game session.
   */
  @Override
  public void makeVisible() {
    this.setVisible(true);
  }
  
  /**
   * Reset the focus on the appropriate part of the view that has the keyboard listener attached to
   * it, so that keyboard events will still flow through.
   */
  @Override
  public void resetFocus() {
    //this.setFocusable(true);
    //this.requestFocus();
    this.boardPanel.setFocusable(true);
    this.boardPanel.requestFocus();
  }
  
  /**
   * Get the set of feature callbacks that the view can use.
   * @param f the set of feature callbacks as a Features object
   */
  @Override
  public void setFeatures(Features f) {
    //Set features of the menu bar as well.
  }
  
  @Override
  public void setPanelFeatures(Features f) {
    //boardPanel.setListener(f);
    
    //set up click and keyboard listeners for the board panel
    //BoardPanel boardPanel = this;
    
    //Adding click listener to handle movement of player by click
    setClickListener(f);
    
    //Adding keyboard input listener to handle player actions like move, pickup, shoot etc.
    setKeyboardInputListener(f);
    
    //Reset focus
    this.resetFocus();
    
    //set focusable p
    boardPanel.setFocusable(true);
    //this.requestFocus();
  }
  
  //Helper method to set click listener
  private void setClickListener(Features f) {
    //create the mouse adapter
    MouseAdapter clickAdapter = new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        // arithmetic to convert panel coords to grid coords
        //Converting coordinates by accounting for board panel distance from edge and cell width
        int x = (e.getX() - boardPanel.getDistanceFromEdge()) / (boardPanel.getCellWidth());
        int y = (e.getY() - boardPanel.getDistanceFromEdge()) / (boardPanel.getCellWidth());
        f.handleCellClick(x, y);
      }
    };
    boardPanel.addMouseListener(clickAdapter);
  }
  
  //Helper method to set keyboard input listeners to
  private void setKeyboardInputListener(Features f) {
    List<Integer> pressedKeys = new ArrayList<Integer>();
    //Set<Integer> pressedKeys = new HashSet<>();
    //Set<Character> pressedChars = new HashSet<>();
    
    KeyAdapter keyAdapter = new KeyAdapter() {
      
      /**
       * Invoked when a key has been typed. This event occurs when a key press is followed
       * by a key release.
       */
      @Override
      public void keyTyped(KeyEvent e) {
        super.keyTyped(e);
      }
      
      /**
       * Invoked when a key has been pressed.
       */
      @Override
      public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        
        //Add the key pressed to pressed keys if the key hasn't already been added in this set of
        // commands
        if (! pressedKeys.contains(e.getKeyCode())) {
          pressedKeys.add(e.getKeyCode());
        }
        
      }
      
      /**
       * Invoked when a key has been released.
       */
      @Override
      public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
        
        //Note: For commands needing multiple key presses, the keys are processed once the first
        // key has been released.
        
        //If the keys haven't already been processed before:
        
        try {
          if (pressedKeys.size() > 0) {
            //Iterate over each item in the list of key presses and create the final command
            String command = processKeyPresses();
            
            //Handle command with controller
            String message = f.handleKeyboardInputs(command);
            
            //Update action feedback
            boardPanel.updateActionMessage(message);
            
            //Clear keys and command after command is processed
            pressedKeys.clear();
            
            //pressedKeys.stream().collect(Collectors.toList()).remove(0)
          }
        } catch (ArrayIndexOutOfBoundsException ex) {
          //don't do anything if the key press is not valid.
        }
        
      }
      
      protected String processKeyPresses() {
        //Adding a string builder to detect the command based on key presses
        StringBuilder command = new StringBuilder();
        
        for (int i = 0; i < pressedKeys.size(); i++) {
          
          //if it's the first press
          if (i == 0) {
            
            switch (pressedKeys.get(0)) {
              case KeyEvent.VK_UP:
              case KeyEvent.VK_KP_UP:
                command.append("m n");
                break;
              
              case KeyEvent.VK_DOWN:
              case KeyEvent.VK_KP_DOWN:
                command.append("m s");
                break;
              
              case KeyEvent.VK_RIGHT:
              case KeyEvent.VK_KP_RIGHT:
                command.append("m e");
                break;
              
              case KeyEvent.VK_LEFT:
              case KeyEvent.VK_KP_LEFT:
                command.append("m w");
                break;
              
              case KeyEvent.VK_P:
                command.append("p ");
                break;
              
              case KeyEvent.VK_S:
                command.append("s ");
                break;
              
              default:
                //Do nothing
                break;
            }
          } else {
            switch (pressedKeys.get(i)) {
              case KeyEvent.VK_A:
                command.append("a");
                break;
              
              case KeyEvent.VK_S:
                command.append("s");
                break;
              
              case KeyEvent.VK_D:
                command.append("d");
                break;
              
              case KeyEvent.VK_R:
                command.append("r");
                break;
              
              case KeyEvent.VK_UP:
              case KeyEvent.VK_KP_UP:
                command.append("n");
                break;
              
              case KeyEvent.VK_DOWN:
              case KeyEvent.VK_KP_DOWN:
                command.append("s");
                break;
              
              case KeyEvent.VK_RIGHT:
              case KeyEvent.VK_KP_RIGHT:
                command.append("e");
                break;
              
              case KeyEvent.VK_LEFT:
              case KeyEvent.VK_KP_LEFT:
                command.append("w");
                break;
              
              case KeyEvent.VK_0:
              case KeyEvent.VK_1:
              case KeyEvent.VK_2:
              case KeyEvent.VK_3:
              case KeyEvent.VK_4:
              case KeyEvent.VK_5:
              case KeyEvent.VK_6:
              case KeyEvent.VK_7:
              case KeyEvent.VK_8:
              case KeyEvent.VK_9:
                String distance = KeyEvent.getKeyText(pressedKeys.get(i));
                command.append(String.format("%s ", distance));
                break;
              
              default:
                //Do nothing
                break;
            }
          }
        }
        return command.toString();
      }
      
    };
    //Add key listener to the panel
    boardPanel.addKeyListener(keyAdapter);
    
  }
  
  /**
   * Set the model later in the program.
   * @param g the read-only game model.
   */
  @Override
  public void setModel(ReadonlyAdventureGame g) {
    this.model = g;
    
    if (boardPanel != null && scrollPane != null) {
      //Clear existing components
      this.remove(boardPanel);
      this.remove(scrollPane);
    }
    boardPanel = new BoardPanel(this.width, this.height, g);
    //container.add(boardPanel);
    boardPanel.setPreferredSize(new Dimension(boardPanel.getWidth(), boardPanel.getHeight()));
    
    scrollPane = new JScrollPane(boardPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    
    //scrollPane.setLayout(new BorderLayout());
    scrollPane.setPreferredSize(new Dimension(height - 200, height - 200));
    this.add(scrollPane, BorderLayout.CENTER); //add scroll pane
    this.setVisible(true);
    
  }
  
  /**
   * Transmit an error message to the view, in case the command could not be processed correctly.
   */
  @Override
  public void showErrorMessage(String error) {
    JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
  }
  
  /**
   * Signal the view to draw itself.
   */
  @Override
  public void refresh() {
    boardPanel.updateBoard(model);
    this.repaint();
    this.resetFocus();
  }
  
  /**
   * Clear the existing view before showing new components.
   */
  @Override
  public void clearView() {
    //Clear view not implemented for Map View
    this.refresh();
  }
  
  /**
   * View the Dungeon grid in cheat mode or regular mode showing only the visited cells.
   */
  protected void setViewInCheatMode() {
    boardPanel.setViewInCheatMode(!viewInCheatMode);
    refresh();
  }
}
