import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import model.AdventureGame;
import model.DungeonBuilderKruskal;
import model.Game;
import model.ModifiedKruskal;
import model.gameobjects.Directions;
import model.gameobjects.actors.Player;
import model.gameobjects.actors.Players;
import model.gameobjects.actors.Status;
import model.gameobjects.cell.Cell;
import model.gameobjects.cell.CellTypes;
import model.gameobjects.cell.Treasure;
import random.RandomGenerator;
import random.TrueRandomGenerator;

import static model.gameobjects.cell.Treasure.DIAMONDS;
import static model.gameobjects.cell.Treasure.RUBIES;
import static model.gameobjects.cell.Treasure.SAPPHIRES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class represents the test class for the whole Dungeon adventure game program. The tests
 * assert the player's movements, and checks for both valid and invalid inputs to build the game.
 */
public class GameTest {
  AdventureGame game;
  RandomGenerator trueRandom = new TrueRandomGenerator();
  private Players player;
  private Map<Treasure, Integer> treasureCollected;
  private int rows;
  private int cols;
  private int interconnectivity;
  private boolean isWrapped;
  private RandomGenerator random;
  private int percentOfTreasure;
  private int percentOfMonsters;
  private Cell startPosition;
  private Cell endPosition;
  private Cell[][] dungeonGrid;
  private ModifiedKruskal builder;
  
  @Before
  public void setUp() throws Exception {
    player = new Player(1);
    rows = 9;
    cols = 9;
    interconnectivity = 0;
    isWrapped = false;
    percentOfTreasure = 50;
    percentOfMonsters = 3;
    game = new Game(rows, cols, interconnectivity, isWrapped, percentOfTreasure, percentOfMonsters,
            player);
    dungeonGrid = game.getGridCopy();
    startPosition = game.getStart();
    endPosition = game.getDestination();
  }
  
  /**
   * Tests the player's positions and movement through a dungeon in the game. Atleast 5 movements
   * should be possible in a Dungeon.
   */
  @Test
  public void testPlayerMovements() {
    player = new Player(1);
    game = new Game(rows, cols, interconnectivity, isWrapped, percentOfTreasure, percentOfMonsters,
            player);
    dungeonGrid = game.getGridCopy();
    startPosition = game.getStart();
    endPosition = game.getDestination();
    
    int moves = 0;
    
    //At the start, the player should be in the start cell, which should be of Cave type.
    assertEquals(startPosition.getX(), player.getPositionX());
    assertEquals(startPosition.getY(), player.getPositionY());
    
    //There should be no treasure collected before player starts moving.
    int amount = player.getTreasureCollected().get(DIAMONDS);
    assertEquals(0, amount);
    amount = player.getTreasureCollected().get(RUBIES);
    assertEquals(0, amount);
    
    amount = player.getTreasureCollected().get(SAPPHIRES);
    assertEquals(0, amount);
    
    //Game start and destination nodes should be cave.
    assertTrue(player.getLocation().getType().equals(CellTypes.CAVE));
    assertTrue(startPosition.getType().equals(CellTypes.CAVE));
    assertTrue(endPosition.getType().equals(CellTypes.CAVE));
    
    
    //Take 40 plus moves in the Dungeon.
    for (int i = 0; i < 40; i++) {
      //Check current location if there is any treasure.
      Cell currentLocation = player.getLocation();
      
      try {
        //Collect treasure if available
        player.pickTreasure(RUBIES);
        player.pickTreasure(SAPPHIRES);
        player.pickTreasure(DIAMONDS);
        player.pickArrows(); //pick arrows if available.
      } catch (IllegalArgumentException e) {
        //System.out.println("no treasure found");
      }
      
      
      //Every cell should have at least one available move
      List<Directions> availableMoves = currentLocation.getEntrances();
      
      //Assert at least one move possible.
      assertTrue(availableMoves.size() > 0);
      
      int randomIndex = trueRandom.getRandomInt(0, availableMoves.size() - 1);
      game.movePlayer(availableMoves.get(randomIndex));
      
      moves += 1;
      Cell nextLocation = player.getLocation();
      
      try {
        //Collect treasure if available
        player.pickTreasure(RUBIES);
        player.pickTreasure(SAPPHIRES);
        player.pickTreasure(DIAMONDS);
        player.pickArrows(); //pick arrows if available.
      } catch (IllegalArgumentException e) {
        //System.out.println("no treasure found");
      }
      
      
      availableMoves = nextLocation.getEntrances();
      randomIndex = trueRandom.getRandomInt(0, availableMoves.size() - 1);
      game.movePlayer(availableMoves.get(randomIndex));
      moves += 1;
      
      Cell anotherLocation = player.getLocation();
      
      try {
        //Collect treasure if available
        player.pickTreasure(RUBIES);
        player.pickTreasure(SAPPHIRES);
        player.pickTreasure(DIAMONDS);
        player.pickArrows(); //pick arrows if available.
      } catch (IllegalArgumentException e) {
        //System.out.println("no treasure found");
      }
      
      currentLocation = player.getLocation();
      
      try {
        //Collect treasure if available
        player.pickTreasure(RUBIES);
        player.pickTreasure(SAPPHIRES);
        player.pickTreasure(DIAMONDS);
        player.pickArrows(); //pick arrows if available.
      } catch (IllegalArgumentException e) {
        //System.out.println("no treasure found");
      }
      
      //Available moves
      availableMoves = currentLocation.getEntrances();
      moves += 1;
      randomIndex = trueRandom.getRandomInt(0, availableMoves.size() - 1);
      game.movePlayer(availableMoves.get(randomIndex));
      
      currentLocation = player.getLocation();
      try {
        //Collect treasure if available
        player.pickTreasure(RUBIES);
        player.pickTreasure(SAPPHIRES);
        player.pickTreasure(DIAMONDS);
        player.pickArrows(); //pick arrows if available.
      } catch (IllegalArgumentException e) {
        //System.out.println("no treasure found");
      }
      
      //Available moves
      availableMoves = currentLocation.getEntrances();
      moves += 1;
      randomIndex = trueRandom.getRandomInt(0, availableMoves.size() - 1);
      game.movePlayer(availableMoves.get(randomIndex));
      
      currentLocation = player.getLocation();
      try {
        //Collect treasure if available
        player.pickTreasure(RUBIES);
        player.pickTreasure(SAPPHIRES);
        player.pickTreasure(DIAMONDS);
        player.pickArrows(); //pick arrows if available.
      } catch (IllegalArgumentException e) {
        //System.out.println("no treasure found");
      }
      
      //Available moves
      availableMoves = currentLocation.getEntrances();
      moves += 1;
      randomIndex = trueRandom.getRandomInt(0, availableMoves.size() - 1);
      game.movePlayer(availableMoves.get(randomIndex));
      try {
        //Collect treasure if available
        player.pickTreasure(RUBIES);
        player.pickTreasure(SAPPHIRES);
        player.pickTreasure(DIAMONDS);
        player.pickArrows(); //pick arrows if available.
      } catch (IllegalArgumentException e) {
        //System.out.println("no treasure found");
      }
    }
    
    //Assert that at least 5 moves were made by the player.
    assertTrue(moves >= 5);
    
    //Assert that the player was able to pick the treasures in caves.
    assertTrue(player.getTreasureCollected().values().size() > 0);
    
    //Assert that the player was able to pick the arrows in caves and tunnels.
    assertTrue(player.getArrowCount() > 0);
  }
  
  /**
   * Test that a monster is always found in the end cave and additional monsters will only be found
   * in caves and not tunnels.
   */
  @Test
  public void testMonsterIsAlwaysFoundInEndCave() {
    player = new Player(1);
    game = new Game(6, 6, 0, isWrapped, percentOfTreasure, percentOfMonsters, player);
    assertEquals(Status.ALIVE, game.getDestination().getMonster().getStatus());
    player = new Player(1);
    AdventureGame game1 =
            new Game(6, 6, 0, isWrapped, percentOfTreasure, percentOfMonsters, player);
    assertEquals(Status.ALIVE, game1.getDestination().getMonster().getStatus());
    player = new Player(1);
    AdventureGame game3 =
            new Game(6, 6, 0, isWrapped, percentOfTreasure, percentOfMonsters, player);
    assertEquals(Status.ALIVE, game3.getDestination().getMonster().getStatus());
    
    Cell[][] grid = game.getGridCopy();
    for (int i = 0; i < 6; i++) {
      for (int j = 0; j < 6; j++) {
        if (grid[i][j].getType().equals(CellTypes.TUNNEL)) {
          assertEquals(null, grid[i][j].getMonster());
        }
      }
    }
  }
  
  /**
   * Test small dungeon that may not meet minimum distance criteria.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testEndPositionForSmallDungeon() {
    game = new Game(3, 3, 0, isWrapped, percentOfTreasure, percentOfMonsters, player);
  }
  
  /**
   * Test invalid player object.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNullPlayer() {
    AdventureGame newGame =
            new Game(rows, cols, interconnectivity, isWrapped, percentOfTreasure, percentOfMonsters,
                    null);
  }
  
  /**
   * Test invalid percent of Monster.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidPercentOfMonster() {
    new Game(rows, cols, interconnectivity, isWrapped, percentOfTreasure, - 2, player);
  }
  
  @Test
  public void testDescribeCurrentLocation() {
    player = new Player(1);
    game = new Game(rows, cols, interconnectivity, isWrapped, percentOfTreasure, percentOfMonsters,
            player);
    dungeonGrid = game.getGridCopy();
    startPosition = game.getStart();
    endPosition = game.getDestination();
    if (game.getPlayer().getLocation().getType().equals(CellTypes.CAVE)) {
      assertTrue(game.describeCurrentLocation().toString().contains("Treasures"));
      assertTrue(game.describeCurrentLocation().toString().contains("Location Type"));
      assertTrue(game.describeCurrentLocation().toString().contains("Available Moves"));
    }
    
    if (game.getPlayer().getLocation().getType().equals(CellTypes.TUNNEL)) {
      assertTrue(game.describeCurrentLocation().toString().contains("Location Type"));
      assertTrue(game.describeCurrentLocation().toString().contains("Available Moves"));
    }
    
  }
  
  @Test
  public void testDestinationValid() {
    player = new Player(1);
    game = new Game(9, 9, 0, false, 50, percentOfMonsters, player);
    Cell start = game.getStart();
    Cell end = game.getDestination();
    
    int dist = (Math.abs(start.getX() - end.getX()) + Math.abs(end.getY() - start.getY()));
    //assertEquals(0, dist);
    assertTrue(dist >= 5);
  }
  
  @Test
  public void testDungeonGrid() {
    Cell[][] grid = game.getGridCopy();
    assertEquals(9, game.getGridCopy().length);
  }
  
  /**
   * Test invalid number of rows and columns.
   */
  @Test(expected = IllegalArgumentException.class)
  public void dungeonInvalidRowsColsTest() {
    int r = 3;
    int c = 3;
    int ic = 15;
    ModifiedKruskal builder =
            new DungeonBuilderKruskal(r, c, ic, false, trueRandom, percentOfTreasure,
                    percentOfMonsters);
    builder.buildDungeon();
  }
  
  /**
   * Test invalid number of columns.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDungeonColumns() {
    int r = 6;
    int c = 0;
    int ic = 15;
    ModifiedKruskal builder =
            new DungeonBuilderKruskal(r, c, ic, false, trueRandom, percentOfTreasure,
                    percentOfMonsters);
    builder.buildDungeon();
  }
  
  /**
   * Test invalid number of rows.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDungeonRows() {
    int r = 0;
    int c = 6;
    int ic = 15;
    ModifiedKruskal builder =
            new DungeonBuilderKruskal(r, c, ic, false, trueRandom, percentOfTreasure,
                    percentOfMonsters);
    builder.buildDungeon();
  }
  
  /**
   * Test invalid treasure amount.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidTreasureAmount() {
    int r = 6;
    int c = 6;
    int ic = 2;
    ModifiedKruskal builder =
            new DungeonBuilderKruskal(r, c, ic, false, trueRandom, - 30, percentOfMonsters);
    builder.buildDungeon();
  }
  
  /**
   * Test invalid treasure amount.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidTreasureAmount2() {
    int r = 6;
    int c = 6;
    int ic = 2;
    ModifiedKruskal builder =
            new DungeonBuilderKruskal(r, c, ic, false, trueRandom, 1000, percentOfMonsters);
    builder.buildDungeon();
  }
  
  /**
   * Test invalid interconnectivity.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidInterconnectivity() {
    int r = 6;
    int c = 6;
    int ic = - 30;
    ModifiedKruskal builder =
            new DungeonBuilderKruskal(r, c, ic, false, trueRandom, percentOfTreasure,
                    percentOfMonsters);
    builder.buildDungeon();
  }
  
  /**
   * Test invalid interconnectivity.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testTooHighInterconnectivity() {
    int r = 6;
    int c = 6;
    int ic = 90;
    ModifiedKruskal builder =
            new DungeonBuilderKruskal(r, c, ic, false, trueRandom, percentOfTreasure,
                    percentOfMonsters);
    builder.buildDungeon();
  }
  
  
  /**
   * Test that tunnels aren't assigned treasures.
   */
  @Test
  public void testNoTreasureInTunnels() {
    int r = 6;
    int c = 6;
    int ic = 1;
    Cell[][] grid = game.getGridCopy();
    for (int i = 0; i < r; i++) {
      for (int j = 0; j < c; j++) {
        if (grid[i][j].getType().equals(CellTypes.TUNNEL)) {
          assertEquals(0, grid[i][j].getTreasures().size());
        }
      }
    }
  }
  
  /**
   * Test that the thief can steal the player's treasures.
   */
  @Test
  public void testTreasureStolenByThief() {
    Players testPlayer = new Player(900);
    AdventureGame testGame =
            new Game(10, 10, 40, true, 100, 5, testPlayer, new TrueRandomGenerator());
    
    //Now find a cave with a thief to test treasure getting stolen.
    Cell cellWithTreasure = Arrays.stream(testGame.getGridCopy()).flatMap(Arrays::stream)
            .filter(s -> s.getType().equals(CellTypes.CAVE) && s.getTreasures().get(DIAMONDS) > 0)
            .collect(Collectors.toList()).get(0);
    testPlayer.setLocation(cellWithTreasure);
    
    
    //Pick up treasure first
    testPlayer.pickTreasure(DIAMONDS);
    testPlayer.pickTreasure(RUBIES);
    testPlayer.pickTreasure(SAPPHIRES);
    
    //Assert that the game still ran smoothly without issues.
    //Message shown after picking up treasures.
    assertTrue(testPlayer.getTreasureCollected().get(DIAMONDS) > 0);
    assertTrue(testPlayer.getTreasureCollected().get(SAPPHIRES) > 0);
    assertTrue(testPlayer.getTreasureCollected().get(RUBIES) > 0);
    
    
    //Now find a cave with a thief to test treasure getting stolen.
    Cell cell = Arrays.stream(testGame.getGridCopy()).flatMap(Arrays::stream)
            .filter(s -> s.getType().equals(CellTypes.CAVE) && s.hasThief())
            .collect(Collectors.toList()).get(0);
    
    //get a neighboring cell
    Cell neighborCave = cell.getNeighbors().get(Directions.EAST);
    
    //move the player to the neighboring cave.
    testPlayer.setLocation(neighborCave);
    testGame.movePlayer(Directions.WEST);
    
    //Now, assert that all the treasure was stolen
    assertTrue(testPlayer.getLocation().hasThief());
    assertTrue(testGame.getTreasureStolen().get(DIAMONDS) > 0);
    assertTrue(testGame.getTreasureStolen().get(SAPPHIRES) > 0);
    assertTrue(testGame.getTreasureStolen().get(RUBIES) > 0);
    assertTrue(testGame.getTreasureCollected().get(SAPPHIRES) == 0);
    assertTrue(testGame.getTreasureCollected().get(RUBIES) == 0);
    assertTrue(testGame.getTreasureCollected().get(DIAMONDS) == 0);
    assertTrue(testGame.getTreasureCollected().get(SAPPHIRES) == 0);
    assertTrue(testGame.getTreasureCollected().get(RUBIES) == 0);
    
  }
  
  /**
   * Test that the percentage of treasure criteria is met in caves.
   */
  @Test
  public void testPercentageOfTreasureInCaves() {
    int r = 6;
    int c = 6;
    int ic = 1;
    Cell[][] grid = game.getGridCopy();
    int caveCount = 0;
    int treasureCount = 0;
    for (int i = 0; i < r; i++) {
      for (int j = 0; j < c; j++) {
        if (grid[i][j].getType().equals(CellTypes.CAVE)) {
          caveCount += 1;
          if (grid[i][j].getTreasures().size() != 0) {
            treasureCount += 1;
          }
        }
      }
    }
    int expectedCount = (percentOfTreasure / 100) * caveCount;
    assertTrue(treasureCount >= expectedCount);
    
  }
  
  private boolean reachedDestination(int playerX, int playerY, int destX, int destY) {
    return playerX == destX && playerY == destY;
  }
  
  //Helper method to check dungeon elements.
  private void showFullDungeonGrid(AdventureGame game) {
    Cell[][] grid = game.getGridCopy();
    int rows = game.getRows();
    int cols = game.getColumns();
    
    Appendable log = new StringBuilder();
    for (int x = 0; x < rows; x++) {
      try {
        log.append("Row" + x + ":\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
      for (int y = 0; y < cols; y++) {
        try {
          log.append("Node position " + x + y + " has --> ");
          log.append(grid[x][y].getType() + " has --> ");
          log.append(String.format("Treasures %s has--> ", grid[x][y].getTreasures().toString()));
          log.append(String.format("%s Neighbors %s \n", grid[x][y].getNeighbors().size(),
                  grid[x][y].getNeighbors().toString()));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      try {
        log.append("\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    
    String[] output = log.toString().split("\n");
    String message = String.join("\n", Arrays.copyOfRange(output, 0, output.length));
    //System.out.println(message);
  }
  
  
}