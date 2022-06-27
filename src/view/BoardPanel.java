package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.ReadonlyAdventureGame;
import model.gameobjects.Directions;
import model.gameobjects.actors.Status;
import model.gameobjects.cell.Cell;
import model.gameobjects.cell.CellTypes;
import model.gameobjects.cell.Smell;
import model.gameobjects.cell.Treasure;

/**
 * This class represents a board panel of the view for the Dungeon Adventure game. The panel
 * contains the contents of the Dungeon grid, which is shown in the Dungeon Map View.
 */
public class BoardPanel extends JPanel {
  private static int WIDTH;
  private static int HEIGHT;
  private final int DISTFROMEDGE;
  private final int CELLWIDTH;
  private final int CELLHEIGHT;
  private final int extraSpace;
  private final BufferedImage[][] grid;
  private ReadonlyAdventureGame model;
  private boolean viewInCheatMode;
  private String displayMessage;
  private String actionFeedback;
  private Image[] playerInfoIcons;
  private String[] treasureDescription;
  
  /**
   * Constructor for the board panel.
   * @param model the tictactoe game model.
   * @param width the width of the view window for dimension configuration.
   * @param height the height of the view window for dimension configuration.
   */
  public BoardPanel(int width, int height, ReadonlyAdventureGame model) {
    this.model = model;
    this.setLayout(null);
    this.setBackground(Color.black);
    
    CELLWIDTH = width / 12;
    CELLHEIGHT = width / 12;
    DISTFROMEDGE = height / 30;
    extraSpace = 10;
    WIDTH = CELLWIDTH * (model.getColumns() + extraSpace) + DISTFROMEDGE;
    HEIGHT = CELLHEIGHT * (model.getRows() + extraSpace) + DISTFROMEDGE;
    
    //this.grid = new JLabel[model.getRows()][model.getColumns()];
    this.grid = new BufferedImage[model.getRows()][model.getColumns()];
    this.playerInfoIcons = new BufferedImage[9];
    //Set preferred size to inform scroll pane if scrolling is needed.
    this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    viewInCheatMode = false;
    //setGridComponents(); //add containers to the grid(empty)
    displayMessage = getDisplayMessage();
    actionFeedback = "";
    treasureDescription = new String[7];
    setPlayerDescription();
    this.setFocusable(true);
    this.requestFocus();
  }
  
  /**
   * Get the width of the board panel.
   * @return the width of the panel.
   */
  public int getWidth() {
    return WIDTH;
  }
  
  /**
   * Get the height of the board panel.
   * @return the height of the board panel.
   */
  public int getHeight() {
    return HEIGHT;
  }
  
  
  /**
   * Paint the Jpanel component to display the Dungeon grid.
   * @param g Graphics object.
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setColor(Color.white);
    
    //Get the Dungeon grid
    Cell[][] board = model.getGridCopy();
    
    //Reset the player description icons
    
    //// draw vertical and two horizontal grid lines.
    for (int c = 0; c <= model.getColumns(); c++) {
      //draw vertical lines
      Stroke stroke = new BasicStroke(4f);
      g2d.setStroke(stroke);
      g2d.drawLine(DISTFROMEDGE + (c * CELLWIDTH), DISTFROMEDGE, DISTFROMEDGE + (c * CELLWIDTH),
              DISTFROMEDGE + (model.getRows() * CELLHEIGHT));
      //Add containers
    }
    
    for (int r = 0; r <= model.getRows(); r++) {
      
      //Draw horizontal line
      g2d.drawLine(DISTFROMEDGE, DISTFROMEDGE + (r * CELLHEIGHT),
              DISTFROMEDGE + (model.getColumns() * CELLWIDTH), DISTFROMEDGE + (r * CELLHEIGHT));
    }
    
    
    g2d.setFont(new Font("Arial", Font.PLAIN, 20));
    // iterate over board, draw components accordingly
    g2d.setPaint(Color.WHITE);
    //this.grid = new JPanel[model.getRows()][model.getColumns()];
    
    //Draw each cell of the Dungeon
    drawDungeonComponents(board, g2d);
    
  }
  
  
  //Helper method to draw the Dungeon board elements
  private void drawDungeonComponents(Cell[][] board, Graphics2D g2d) {
    //Reset the treasure info
    setTreasureInfo();
    
    //int x = DISTFROMEDGE + (WIDTH - (extraSpace * CELLWIDTH));
    //int y = DISTFROMEDGE;
    
    // Draw treasure info
    int distFromEdge = this.DISTFROMEDGE * 2;
    int x = distFromEdge + (WIDTH - (extraSpace * CELLWIDTH));
    int y = 2 * distFromEdge;
    int d = distFromEdge;
    int dh = distFromEdge / 2;
    
    //treasure collected and stolen
    g2d.drawString(treasureDescription[0], x + d + dh / 2, y);
    g2d.drawString(treasureDescription[1], x + (2 * d) + dh / 2, y);
    g2d.drawString(treasureDescription[2], x + (3 * d) + dh / 2, y);
    g2d.drawString(treasureDescription[3], x + (4 * d + dh), y);
    g2d.drawString(treasureDescription[4], x + (6 * d) - dh / 2, y);
    g2d.drawString(treasureDescription[5], x + (7 * d), y);
    g2d.drawString(treasureDescription[6], x + (8 * d) + dh / 2, y);
    
    //Draw total treasure stolen and collected
    //player
    g2d.drawImage(playerInfoIcons[0], (x + (2 * distFromEdge) - (distFromEdge / 2)), 0, CELLWIDTH,
            (CELLHEIGHT / 2), null);
    //Diamonds collected
    g2d.drawImage(playerInfoIcons[1], x + d, y - d, null);
    //Rubies collected
    g2d.drawImage(playerInfoIcons[2], x + (2 * distFromEdge), y - d, distFromEdge / 2,
            distFromEdge / 2, null);
    //Sapphires collected
    g2d.drawImage(playerInfoIcons[3], x + (3 * distFromEdge), y - d, distFromEdge / 2,
            distFromEdge / 2, null);
    //Arrows collected
    g2d.drawImage(playerInfoIcons[4], x + (4 * distFromEdge), y - (d + dh), null);
    //Thief
    g2d.drawImage(playerInfoIcons[5], x + (6 * distFromEdge), 0, (CELLWIDTH / 2),
            10 + CELLHEIGHT / 2, null);
    //Diamonds collected by thief
    g2d.drawImage(playerInfoIcons[1], x + (6 * distFromEdge) - (distFromEdge / 2), y - d,
            distFromEdge / 2, distFromEdge / 2, null);
    //Rubies collected by thief
    g2d.drawImage(playerInfoIcons[2], x + (7 * distFromEdge), y - d, distFromEdge / 2,
            distFromEdge / 2, null);
    //Sapphires collected by thief
    g2d.drawImage(playerInfoIcons[3], x + (8 * distFromEdge), y - d, distFromEdge / 2,
            distFromEdge / 2, null);
    
    //Draw a line to separate the dungeon grid with the text region
    int x1 = distFromEdge + (WIDTH - (extraSpace * CELLWIDTH));
    int y1 = 0;
    int x2 = distFromEdge + (WIDTH - (extraSpace * CELLWIDTH));
    int y2 = distFromEdge + (HEIGHT - (extraSpace * CELLHEIGHT));
    g2d.drawLine(x1, y1, x2, y2);
    
    //Set grid components
    setGridComponents(board, g2d);
    
    displayMessage = getDisplayMessage();
    
    
    //display message output for the current cell and any recent actions.
    drawString(g2d, displayMessage, x1 + distFromEdge, y1 + (5 * distFromEdge));
    
    //Display action feedback message if any.
    drawString(g2d, actionFeedback, x1 + distFromEdge, y1 + (3 * distFromEdge));
    
  }
  
  
  protected void updateBoard(ReadonlyAdventureGame game) {
    if (game == null) {
      throw new IllegalArgumentException("Game cannot be null");
    }
    this.model = game;
    repaint();
  }
  
  /**
   * Protected method to update action message in the game.
   * @param message the message to display in the view.
   */
  protected void updateActionMessage(String message) {
    actionFeedback = message;
  }
  
  //Helper method to get the display messages for the Dungeon game.
  private String getDisplayMessage() {
    //DISPLAY MESSAGES
    StringBuilder out = new StringBuilder();
    //Output player location
    if (model.getPlayerStatus().equals(Status.ALIVE)) {
      String smell = "";
      if (model.getCurrentLocation().getSmellUnits() == 1) {
        smell = "\nSMELL:\nYou smell something slightly pungent nearby.\n";
      }
      if (model.getCurrentLocation().getSmellUnits() > 1) {
        smell = "\nSMELL:\nYou smell something terribly pungent nearby.\n";
      }
      out.append(smell);
      
      if (model.getCurrentLocation().isNextToPit()) {
        out.append("\nPIT WARNING!:\nThere is a pit nearby.\n");
      }
      if (model.getCurrentLocation().hasThief()) {
        out.append("\nTHIEF ALERT!: You were mugged by a thief!\n");
      }
      
      //Current location
      out.append(String.format("\nCURRENT LOCATION: \nYou are in %s %d%d.\n",
              model.getCurrentLocation().getType().toString().toLowerCase(),
              model.getCurrentLocation().getX(), model.getCurrentLocation().getY()));
      //Entrances
      out.append("\nENTRANCES:\nEntrances can be found at ");
      for (Directions dir : model.getCurrentLocation().getNeighbors().keySet()) {
        out.append(dir.toString().charAt(0)).append(", ");
      }
      out.append("\n");
      if (model.getCurrentLocation().getArrows() > 0) {
        out.append(String.format("\nARROWS:\nYou find %d arrow(s) here\n",
                model.getCurrentLocation().getArrows()));
      }
      if (! model.getCurrentLocation().getTreasures().isEmpty()) {
        out.append(String.format(
                "\nTREASURE:\nYou find %d rubies, %d sapphires, & %d diamonds " + "here\n",
                model.getCurrentLocation().getTreasures().get(Treasure.RUBIES),
                model.getCurrentLocation().getTreasures().get(Treasure.SAPPHIRES),
                model.getCurrentLocation().getTreasures().get(Treasure.DIAMONDS)));
      }
    }
    
    //If player has reached the destination and won 
    if (model.getPlayerStatus().equals(Status.WON)) {
      out.append("\nYou are at the destination. You won!\n");
    }
    
    if (model.getPlayerStatus().equals(Status.DEAD)) {
      if (model.getCurrentLocation().getType().equals(CellTypes.PIT)) {
        out.append("\nGAME RESULT:\nYou fell into a pit!\nBetter luck next time\n");
      } else if (model.getCurrentLocation().getMonster() != null
              && model.getCurrentLocation().getMonster().getStatus() == Status.ALIVE) {
        out.append("\nGAME RESULT:\nChomp, chomp, chomp, you are eaten by an Otyugh!"
                + "\n Better luck next time\n");
      }
    }
    return out.toString();
  }
  
  
  //Helper method to draw the text messages for the game, showing details like current location,
  // arrows and treasure found, etc.
  protected void drawString(Graphics2D g, String text, int x, int y) {
    int lineHeight = g.getFontMetrics().getHeight();
    for (String line : text.split("\n")) {
      g.drawString(line, x, y += lineHeight);
    }
  }
  
  /**
   * View the Dungeon grid in cheat mode or regular mode showing only the visited cells.
   * @param mode Mode to view the dungeon grid.
   */
  protected void setViewInCheatMode(boolean mode) {
    this.viewInCheatMode = mode;
  }
  
  
  private void setTreasureInfo() {
    StringBuilder out = new StringBuilder();
    
    //get treasure collected and stolen
    out.append(model.getTreasureCollected().get(Treasure.DIAMONDS) + " ");
    out.append(model.getTreasureCollected().get(Treasure.RUBIES) + " ");
    out.append(model.getTreasureCollected().get(Treasure.SAPPHIRES) + " ");
    out.append(model.getArrowCount() + " ");
    out.append(model.getTreasureStolen().get(Treasure.DIAMONDS) + " ");
    out.append(model.getTreasureStolen().get(Treasure.RUBIES) + " ");
    out.append(model.getTreasureStolen().get(Treasure.SAPPHIRES) + " ");
    treasureDescription = out.toString().split(" ");
  }
  
  //Helper method to set the icons for the panel showing treasure collected by
  // player and treasure stolen by thieves.
  private void setPlayerDescription() {
    int distFromEdge = this.DISTFROMEDGE * 2;
    
    playerInfoIcons = new Image[9];
    
    URL urlPlayer = this.getClass().getResource("img/player.png");
    URL urlTreasureD = this.getClass().getResource("img/diamond.png");
    URL urlTreasureR = this.getClass().getResource("img/ruby.png");
    URL urlTreasureS = this.getClass().getResource("img/sapphire.png");
    URL urlArrow = this.getClass().getResource("img/arrows.png");
    URL urlThief = this.getClass().getResource("img/thief.png");
    
    //int x = DISTFROMEDGE + (WIDTH - (extraSpace * CELLWIDTH));
    //int y = DISTFROMEDGE;
    
    Image imgPlayer = new ImageIcon(urlPlayer).getImage();
    Image imgD = new ImageIcon(urlTreasureD).getImage();
    Image imgR = new ImageIcon(urlTreasureR).getImage();
    Image imgS = new ImageIcon(urlTreasureS).getImage();
    Image imgA = new ImageIcon(urlArrow).getImage();
    Image imgThief = new ImageIcon(urlThief).getImage();
    
    //player
    playerInfoIcons[0] = imgPlayer.getScaledInstance(CELLWIDTH, CELLHEIGHT / 2, Image.SCALE_FAST);
    
    //Diamonds collected
    playerInfoIcons[1] =
            imgD.getScaledInstance(distFromEdge / 2, distFromEdge / 2, Image.SCALE_FAST);
    //Rubies collected
    playerInfoIcons[2] =
            imgR.getScaledInstance(distFromEdge / 2, distFromEdge / 2, Image.SCALE_FAST);
    //Sapphires collected
    playerInfoIcons[3] =
            imgS.getScaledInstance(distFromEdge / 2, distFromEdge / 2, Image.SCALE_FAST);
    //Arrows collected
    playerInfoIcons[4] = imgA.getScaledInstance(distFromEdge, distFromEdge, Image.SCALE_FAST);
    
    //Thief
    playerInfoIcons[5] =
            imgThief.getScaledInstance((CELLWIDTH / 2), 10 + CELLHEIGHT / 2, Image.SCALE_FAST);
  }
  
  //Helper method to set the dungeon grid components.
  private void setGridComponents(Cell[][] board, Graphics2D g2d) {
    //this.grid = new JLabel[model.getRows()][model.getColumns()];
    
    ////get the appropriate url for the cell
    //Cell[][] board = model.getGrid();
    
    for (int r = 0; r < model.getRows(); r++) {
      for (int c = 0; c < model.getColumns(); c++) {
        
        Cell cell = board[r][c];
        int x = Math.round(DISTFROMEDGE + ((c) * CELLHEIGHT));
        int y = Math.round(DISTFROMEDGE + ((r) * CELLWIDTH));
        
        if (cell.isVisited() || this.viewInCheatMode) {
          setCellComponents(r, c, cell, g2d);
          g2d.drawImage(grid[r][c].getScaledInstance(CELLWIDTH, CELLHEIGHT, 0), x, y, CELLWIDTH,
                  CELLHEIGHT, null);
          //grid[r][c].getScaledInstance(CELLWIDTH, CELLHEIGHT,0);
        }
      }
    }
  }
  
  
  //Helper method to set the components of each cell in the Dungeon grid.
  private void setCellComponents(int r, int c, Cell cell, Graphics2D g2d) {
    //get the appropriate url for the cell
    //Cell[][] board = model.getGrid();
    
    //Filter neighbors to set appropriate url for image of the cell
    String entrances =
            cell.getNeighbors().keySet().stream().map(k -> k.name().substring(0, 1)).sorted()
                    .collect(Collectors.joining(""));
    //component 1: Image
    //Add an image icon to jlabel
    URL urlCell = this.getClass().getResource(String.format("img/%s.png", entrances));
    URL urlPit = this.getClass().getResource(String.format("img/pit.png"));
    URL urlthief = this.getClass().getResource(String.format("img/thief.png"));
    URL urlPitWarning = this.getClass().getResource(String.format("img/pitwarning.png"));
    URL urlTreasure = this.getClass().getResource("img/treasure.png");
    URL urlArrow = this.getClass().getResource("img/arrows.png");
    URL urlMonsterOtyugh = this.getClass().getResource("img/otyugh.png");
    URL urlWoundedOtyugh = this.getClass().getResource("img/woundedmonster.png");
    URL urlPlayer = this.getClass().getResource("img/player.png");
    URL urlsmell1 = this.getClass().getResource("img/stench01.png");
    URL urlsmell2 = this.getClass().getResource("img/stench02.png");
    
    BufferedImage combined = null;
    try {
      BufferedImage imgCell = ImageIO.read(urlCell);
      BufferedImage imgPit = ImageIO.read(urlPit);
      BufferedImage imgThief = ImageIO.read(urlthief);
      BufferedImage imgPitWarning = ImageIO.read(urlPitWarning);
      BufferedImage imgTreasure = ImageIO.read(urlTreasure);
      BufferedImage imgArrow = ImageIO.read(urlArrow);
      BufferedImage imgMonsterO = ImageIO.read(urlMonsterOtyugh);
      BufferedImage imgMonsterWounded = ImageIO.read(urlWoundedOtyugh);
      BufferedImage imgPlayer = ImageIO.read(urlPlayer);
      BufferedImage imgSmell1 = ImageIO.read(urlsmell1);
      BufferedImage imgSmell2 = ImageIO.read(urlsmell2);
      
      
      //Cell
      combined = new BufferedImage(CELLWIDTH, CELLHEIGHT, BufferedImage.TYPE_INT_RGB);
      
      Graphics g = combined.getGraphics();
      
      g.drawImage(imgCell, 0, 0, CELLWIDTH, CELLHEIGHT, null);
      
      //Pit
      if (cell.getType().equals(CellTypes.PIT)) {
        g.drawImage(imgPit, (combined.getWidth() / 4), (combined.getHeight() / 4),
                combined.getWidth() / 2, combined.getHeight() / 2, null);
      }
      
      //Pit warning
      if (cell.isNextToPit()) {
        g.drawImage(imgPitWarning, 1 * (combined.getWidth() / 3) + 5, (combined.getHeight() / 3),
                combined.getWidth() / 4, combined.getHeight() / 4, null);
      }
      
      //Treasure
      if (cell.getTreasures().values().stream().reduce(0, Integer::sum) > 0) {
        g.drawImage(imgTreasure, 2 * (combined.getWidth() / 3), (combined.getHeight() / 3),
                imgTreasure.getWidth() / 12, imgTreasure.getHeight() / 12, null);
      }
      
      //Treasure: Arrow
      if (cell.getArrows() > 0) {
        g.drawImage(imgArrow, 2 * (combined.getWidth() / 3), (combined.getHeight() / 3),
                imgTreasure.getWidth() / 12, imgTreasure.getHeight() / 12, null);
      }
      
      //Treasure: Monster
      if (cell.getMonster() != null) {
        if (cell.getMonster().getStatus().equals(Status.ALIVE)) {
          g.drawImage(imgMonsterO, (combined.getWidth() / 2) + 15 - (combined.getWidth() / 4),
                  (combined.getWidth() / 2), imgMonsterO.getWidth() / 6,
                  imgMonsterO.getHeight() / 6, null);
        } else {
          g.drawImage(imgMonsterWounded, (combined.getWidth() / 2) + 15 - (combined.getWidth() / 4),
                  (combined.getWidth() / 2), imgMonsterWounded.getWidth() / 10,
                  imgMonsterWounded.getHeight() / 10, null);
        }
      }
      
      if (cell.hasPlayer()) {
        //Player
        g.drawImage(imgPlayer, 0, 0, imgPlayer.getWidth() / 6, imgPlayer.getHeight() / 6, null);
      }
      
      if (cell.getThief() != null) {
        //Player
        g.drawImage(imgThief, 0, 0, imgThief.getWidth() / 6, imgThief.getHeight() / 6, null);
      }
      
      //Smell too pungent
      if (cell.getSmellLevel().equals(Smell.PUNGENT) && cell.getSmellUnits() > 1) {
        g.drawImage(imgSmell2, 0, 0, combined.getWidth() - 10, combined.getHeight() - 10, null);
      }
      if (cell.getSmellLevel().equals(Smell.PUNGENT) && cell.getSmellUnits() == 1) {
        //Treasure: Smell1
        g.drawImage(imgSmell1, 0, 0, combined.getWidth() - 10, combined.getHeight() - 10, null);
      }
      
      g.dispose();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    grid[r][c] = combined;
  }
  
  //Package private method to get distance from the edge
  int getDistanceFromEdge() {
    return DISTFROMEDGE;
  }
  
  //Package private method to get cell height
  int getCellHeight() {
    return CELLHEIGHT;
  }
  
  //Package private method to get cell wdith
  int getCellWidth() {
    return CELLWIDTH;
  }
  
  //private method to get an image component.
  private JLabel getImageComponent(BufferedImage img, int x, int y, int width, int height) {
    BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics g = newImg.getGraphics();
    g.drawImage(img, 0, 0, width, height, null);
    ImageIcon imc = new ImageIcon(newImg);
    JLabel jLabel = new JLabel(imc);
    jLabel.setLayout(null);
    jLabel.setBounds(x, y, width, height);
    g.dispose();
    return jLabel;
  }
  
}
