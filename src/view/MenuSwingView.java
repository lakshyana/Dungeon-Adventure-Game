package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import controller.Features;
import model.ReadonlyAdventureGame;

/**
 * This class represents the game menu for the Dungeon game, displays the menu bar, and input
 * options to create a new Dungeon.
 */
public class MenuSwingView extends JFrame implements IView {
  
  //Dungeon Map View
  private final DungeonMapSwingView mapView;
  //Buttons
  private final JButton getStartedButton;
  private final JButton exitButton;
  //Text display
  private final JTextPane text; //text content
  private final Font labelFont;
  private final Font buttonFont;
  private final JMenu configMenu;
  private final JMenu gameMenu;
  private final JMenu dungeonMapMenu; //JMenu
  //Window size
  int width;
  int height;
  URL url;
  ImageIcon imgIcon;
  //Jpanel
  private JPanel content;
  private JPanel panel; //panel: start button container
  //Image container
  private JLabel jLabel;
  private JButton submitButton;
  //Inputs
  private JTextField rowInput;
  private JTextField colInput;
  private JTextField interconnect;
  private JTextField percentTreasure;
  private JTextField percentMonsters;
  private JTextField wrap;
  private JMenuItem rows;
  private JMenuItem cols;
  private JMenuItem wrapping;
  private JMenuItem intercon;
  private JMenuItem treasure;
  private JMenuItem monsters;
  private JMenuItem newgame;
  private JMenuItem restart;
  private JMenuItem exit;
  private JMenuItem dungeonMap;
  private JMenuItem cheatmode; //Jmenu items
  
  /**
   * Constructor for the menu window showing options to start a game, and build a Dungeon.
   */
  public MenuSwingView() {
    super("Game Menu"); //title
    
    //Update the window size based on the current screen size
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    this.width = (int) screenSize.getWidth();
    this.height = (int) screenSize.getHeight();
    //Fonts
    Font titleFont = new Font("Arial", Font.PLAIN, 50);
    this.buttonFont = new Font("Arial", Font.PLAIN, 30);
    this.labelFont = new Font("Arial", Font.PLAIN, 20);
    Font menuFont = new Font("Helvetica", Font.BOLD, 15);
    
    //set size of the window
    this.setSize(width, height); //dynamically set the size of the window
    
    //Exit on close
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    
    //Adding layout
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    
    //Menu bar
    //Menu bar items
    //Menubar
    JMenuBar mb = new JMenuBar();
    this.setJMenuBar(mb);
    gameMenu = new JMenu("Game");
    gameMenu.setFont(menuFont);
    
    configMenu = new JMenu("Dungeon Configuration");
    configMenu.setFont(menuFont);
    
    dungeonMapMenu = new JMenu("Dungeon Map");
    dungeonMapMenu.setFont(menuFont);
    
    mb.add(configMenu);
    mb.add(gameMenu);
    mb.add(dungeonMapMenu);
    this.setJMenuBar(mb);
    
    //Adding design
    content = new JPanel();
    content.setBackground(Color.BLACK); //background color
    
    //Component 1: Jlabel with image
    
    //Add an image icon to jlabel
    url = this.getClass().getResource("img/dungeon-gate.png");
    //img = ImageIO.read(url);
    imgIcon = new ImageIcon(url);
    this.jLabel = new JLabel(imgIcon);
    //jLabel.setLayout(new GridLayout(1,1, 10, 10));
    //jLabel.setIcon(imgIcon);
    content.add(jLabel); //, BorderLayout.NORTH
    
    //Component 2: Title text
    text = new JTextPane();
    text.setBackground(Color.BLACK);
    text.setForeground(Color.WHITE);
    text.setLayout(new BorderLayout(1, 1));
    text.setFont(titleFont);
    text.setText("WELCOME TO THE DUNGEON \n ADVENTURE GAME! ");
    
    StyledDocument doc = text.getStyledDocument();
    SimpleAttributeSet center = new SimpleAttributeSet();
    StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
    doc.setParagraphAttributes(0, doc.getLength(), center, false);
    
    //Add title to view
    content.add(text); //, BorderLayout.CENTER
    
    //Component 3: Start button panel
    panel = new JPanel(); //new BorderLayout()
    panel.setBackground(Color.BLACK);
    
    getStartedButton = new JButton("Get Started");
    getStartedButton.setFont(buttonFont);
    getStartedButton.setActionCommand("Get Started Button");
    panel.add(getStartedButton);
    
    // exit button
    exitButton = new JButton("Exit Game");
    exitButton.setFont(buttonFont);
    exitButton.setActionCommand("Exit Button");
    panel.add(exitButton);
    
    //add to view
    content.add(panel); //, BorderLayout.SOUTH
    
    //this.pack();
    this.setContentPane(content);
    this.setVisible(true);
    
    
    //Dungeon Map View
    this.mapView = new DungeonMapSwingView(width, height);
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
    this.mapView.resetFocus();
    //this.setFocusable(true);
    //this.requestFocus();
  }
  
  /**
   * Get the set of feature callbacks that the view can use.
   * @param f the set of feature callbacks as a Features object
   */
  @Override
  public void setFeatures(Features f) {
    //Get started with the game
    getStartedButton.addActionListener(l -> displayDungeonInputs(f));
    
    // exit program is tied to the exit button
    exitButton.addActionListener(l -> f.exitProgram());
    
    mapView.setFeatures(f);
  }
  
  /**
   * Set the model later in the program.
   * @param g the read-only game model.
   */
  @Override
  public void setModel(ReadonlyAdventureGame g) {
    //Set the model
    
    //set map view model
    this.mapView.setModel(g);
    
    configMenu.removeAll();
    //Update Menu items
    rows = new JMenuItem("Rows: ");
    cols = new JMenuItem("Columns: ");
    wrapping = new JMenuItem("Wrapping: ");
    intercon = new JMenuItem("Interconnectivity: ");
    treasure = new JMenuItem("Percent of Treasure: ");
    monsters = new JMenuItem("Percent of Monsters: ");
    
    configMenu.add(rows);
    configMenu.add(cols);
    configMenu.add(wrapping);
    configMenu.add(intercon);
    configMenu.add(treasure);
    configMenu.add(monsters);
    
    newgame = new JMenuItem("Start New Game (Same Dungeon)");
    restart = new JMenuItem("Restart New Game (New Dungeon)");
    exit = new JMenuItem("Exit Game");
    //Show the new game (with same Dungeon) option
    gameMenu.removeAll();
    gameMenu.add(newgame);
    //Show the new game option
    gameMenu.add(restart);
    //Show the exit option
    gameMenu.add(exit);
    
    //Dungeon Map
    dungeonMapMenu.removeAll();
    dungeonMap = new JMenuItem("Open Dungeon Map");
    cheatmode = new JMenuItem("Toggle cheat mode");
    dungeonMap.setActionCommand("Display Dungeon");
    cheatmode.setActionCommand("Toggle cheatmode");
    dungeonMapMenu.add(dungeonMap);
    dungeonMapMenu.add(cheatmode);
    
  }
  
  /**
   * Set the feature callbacks for the board panel after model instance is created.
   * @param f the set of feature callbacks as a Features object
   */
  @Override
  public void setPanelFeatures(Features f) {
    //Add action listeners to the dungeon map
    this.mapView.setPanelFeatures(f);
    
    // restart the game with same settings
    newgame.addActionListener(l -> f.restartSameGame());
    
    // restart the game with new Dungeon
    restart.addActionListener(l -> displayDungeonInputs(f));
    
    // exit the game
    exit.addActionListener(l -> f.exitProgram());
    
    //Show the Dungeon
    dungeonMap.addActionListener(l -> {
      //Make the dungeon map visible
      this.mapView.makeVisible();
    });
    
    //Toggle cheatmode
    cheatmode.addActionListener(l -> {
      //Enable to disable cheatmode
      this.mapView.setViewInCheatMode();
    });
  }
  
  /**
   * Transmit an error message to the view, in case the command could not be processed correctly.
   */
  @Override
  public void showErrorMessage(String error) {
    JOptionPane.showMessageDialog(this.mapView, error, "Error", JOptionPane.ERROR_MESSAGE);
  }
  
  /**
   * Signal the view to draw itself.
   */
  @Override
  public void refresh() {
    //Repaint Dungeon Map after move or user input.
    this.mapView.refresh();
    //this.repaint();
  }
  
  /**
   * Clear the existing view before showing new components.
   */
  @Override
  public void clearView() {
    //Reset view
    this.remove(content);
    content = new JPanel();
    this.validate();
    this.repaint();
    content.setBackground(Color.BLACK); //background color
    content.setLayout(new BorderLayout());
  }
  
  //Helper method to update the menu screen to show inputs fields to create a new Dungeon.
  private void displayDungeonInputs(Features f) {
    //clearView();
    
    //replace seed value to create a new Dungeon
    f.restartNewGame();
    
    //component 1: Image
    //Add an image icon to jlabel
    url = this.getClass().getResource("img/build_dungeon.gif");
    //img = ImageIO.read(url);
    imgIcon = new ImageIcon(new ImageIcon(url).getImage()
            .getScaledInstance((int) Math.round(0.5 * width), (int) Math.round(0.45 * height),
                    Image.SCALE_DEFAULT));
    this.jLabel = new JLabel(imgIcon);
    jLabel.setLayout(new GridLayout(1, 1));
    content.add(jLabel, BorderLayout.NORTH); //, BorderLayout.NORTH
    
    //Component 2: Text JLabel
    text.setText("Now let's build a random Dungeon. \n Enter the desired settings: ");
    content.add(text, BorderLayout.CENTER); //, BorderLayout.CENTER
    
    //Component 3: Input fields
    //panel container
    panel = new JPanel(); //new BorderLayout()
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(Color.WHITE);
    
    //Title
    JLabel title = new JLabel(); //Label
    title.setFont(new Font("Arial", Font.BOLD, 20));
    panel.add(title);
    
    //Row input
    JLabel rowDisplay = new JLabel("\nRows: (Eg: 6)"); //Label
    rowDisplay.setFont(labelFont);
    rowInput = new JTextField("6", 10);//row input
    panel.add(rowDisplay);
    panel.add(rowInput);
    
    //Col input
    JLabel colDisplay = new JLabel("Columns: (Eg: 6)"); //Label
    colDisplay.setFont(labelFont);
    colInput = new JTextField("6", 10);//column input
    panel.add(colDisplay);
    panel.add(colInput);
    
    //Interconnectivity input
    JLabel interconDisplay = new JLabel("Interconnectivity: (Eg: 0)"); //Label
    interconDisplay.setFont(labelFont);
    interconnect = new JTextField("0", 10);//column input
    panel.add(interconDisplay);
    panel.add(interconnect);
    
    //Percent of treasure and arrows input
    JLabel treasureDisplay = new JLabel("Percent of treasure: (Eg: 50)"); //Label
    treasureDisplay.setFont(labelFont);
    percentTreasure = new JTextField("15", 10);//column input
    panel.add(treasureDisplay);
    panel.add(percentTreasure);
    
    //Percent of monsters
    JLabel monsterDisplay = new JLabel("Percent of Monsters: (Eg: 20)"); //Label
    monsterDisplay.setFont(labelFont);
    percentMonsters = new JTextField("10", 10);//column input
    panel.add(monsterDisplay); //label
    panel.add(percentMonsters); //input
    
    //wrapped
    JLabel wrappedDisplay = new JLabel("Wrapping Dungeon? : (Eg: false)"); //Label
    wrappedDisplay.setFont(labelFont);
    wrap = new JTextField("false", 10);//column input
    panel.add(wrappedDisplay); //label
    panel.add(wrap); //input
    
    //add to view
    JScrollPane scrollPane =
            new JScrollPane(panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setPreferredSize(new Dimension(600, 600));
    
    content.add(scrollPane, BorderLayout.EAST);
    //content.add(panel, BorderLayout.EAST);
    
    //Buttons
    //panel container
    JPanel buttons = new JPanel(); //new BorderLayout()
    buttons.setBackground(Color.black);
    
    // submit button
    submitButton = new JButton("Start New Game");
    submitButton.setBackground(Color.black);
    submitButton.setFont(buttonFont);
    submitButton.setActionCommand("Submit Button");
    buttons.add(submitButton);
    
    //exit button
    exitButton.setBackground(Color.BLACK);
    buttons.add(exitButton);
    
    content.add(buttons, BorderLayout.SOUTH); //, BorderLayout.PAGE_END
    
    //Add content to window
    this.setContentPane(content);
    
    //Action events setup
    
    //Process the inputs for building a Dungeon
    submitButton.addActionListener(l -> {
      boolean valid = validateGameInputs();
      
      //Submit entered inputs
      java.util.List<String> gameInputs = new ArrayList<>();
      gameInputs = List.of(rowInput.getText(), colInput.getText(), wrap.getText(),
              interconnect.getText(), percentTreasure.getText(), percentMonsters.getText());
      
      //Note: the process game inputs will initialize the model if given valid inputs.
      if (! valid || ! f.processGameInputs(gameInputs)) {
        title.setText("Invalid or Missing Inputs! Please try again and resubmit");
        title.setForeground(Color.pink);
        this.repaint();
        
      } else {
        rows.setText(rows.getText() + rowInput.getText());
        cols.setText(cols.getText() + colInput.getText());
        wrapping.setText(wrapping.getText() + wrap.getText());
        intercon.setText(intercon.getText() + interconnect.getText());
        treasure.setText(treasure.getText() + percentTreasure.getText());
        monsters.setText(monsters.getText() + percentMonsters.getText());
        submitButton.setVisible(false);
      }
    });
    
  }
  
  //Validate inputs
  private boolean validateGameInputs() {
    StringBuilder errorText = new StringBuilder();
    boolean valid = true;
    if (rowInput.getText().length() == 0) {
      valid = false;
      rowInput.setBackground(Color.pink);
    }
    
    if (colInput.getText().length() == 0) {
      valid = false;
      colInput.setBackground(Color.pink);
    }
    
    if (interconnect.getText().length() == 0) {
      valid = false;
      interconnect.setBackground(Color.pink);
    }
    
    if (percentTreasure.getText().length() == 0) {
      valid = false;
      percentTreasure.setBackground(Color.pink);
    }
    
    if (percentMonsters.getText().length() == 0) {
      valid = false;
      percentMonsters.setBackground(Color.PINK);
    }
    
    if (wrap.getText().length() == 0) {
      valid = false;
      wrap.setBackground(Color.pink);
    }
    
    // Show the errorText in a message box, or in a label, or ...
    return valid;
  }
  
  
}
