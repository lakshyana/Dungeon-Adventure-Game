import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import controller.ConsoleController;
import controller.Controller;
import model.AdventureGame;
import model.Game;
import model.gameobjects.Directions;
import model.gameobjects.actors.Monster;
import model.gameobjects.actors.Monsters;
import model.gameobjects.actors.Player;
import model.gameobjects.actors.Players;
import model.gameobjects.actors.Status;
import model.gameobjects.cell.Cell;
import model.gameobjects.cell.CellTypes;
import model.gameobjects.cell.Smell;
import model.gameobjects.cell.Treasure;
import random.MockRandomGenerator;
import random.RandomGenerator;
import random.TrueRandomGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class represents the test class for the whole Dungeon adventure game program, that's run
 * through the controller. The tests assert the player's movements, and checks for both valid and
 * invalid inputs to build the game. In addition, the tests also check the functionalities to allow
 * a player to move through the Dungeon, pick up treasure and arrows, shoot a monster, and reach the
 * end cave.
 */
public class ConsoleControllerTest {
  
  AdventureGame game;
  Controller gameController;
  
  @Before
  public void setUp() {
    Players player = new Player(60);
    game = new Game(9, 9, 0, false, 50, 3, player);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testFailingAppendable() {
    //Testing failing appendable.
    StringReader input = new StringReader("S 2 N"); //"6 6 0 false 30 30"
    Appendable gameLog = new FailingAppendable();
    Controller c = new ConsoleController(input, gameLog, game);
    c.playGame();
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testingNullGame() {
    gameController = new ConsoleController(new StringReader("M N"), new StringBuilder(), null);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidModel() {
    AdventureGame g = new Game(- 1, 6, 0, false, 10, 10, new Player(1));
    StringReader input = new StringReader("M N");
    Appendable gameLog = new StringBuilder();
    Controller c = new ConsoleController(input, gameLog, g);
    c.playGame();
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidPlayer() {
    AdventureGame g = new Game(6, 6, 0, false, 10, 10, null);
    StringReader input = new StringReader("M N");
    Appendable gameLog = new StringBuilder();
    Controller c = new ConsoleController(input, gameLog, g);
    c.playGame();
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testingNullReadable() {
    gameController = new ConsoleController(null, new StringBuilder(),
            new Game(6, 6, 0, false, 10, 10, new Player(1)));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testingNullAppendable() {
    gameController = new ConsoleController(new StringReader("6 6 0 false 30 30"), null,
            new Game(6, 6, 0, false, 10, 10, new Player(1)));
  }
  
  /**
   * This test tests for shooting of an arrow, and validates that an arrow will only attack a
   * monster when the distance is exact. When exact, the arrow will wound the monster at first hit,
   * and kill the monster at second hit.
   */
  @Test
  public void testArrowHits() {
    
    Players testPlayer = new Player(900);
    AdventureGame testGame =
            new Game(6, 6, 25, false, 100, 100, testPlayer, new MockRandomGenerator(3000));
    Directions dir = testGame.getStart().getNeighbors().keySet().iterator().next();
    Cell monsterPosition = testGame.getStart().getNeighbors().get(dir);
    
    //Test that the smell at the cave one position away from a monster has a pungent smell.
    assertEquals(Smell.PUNGENT, testPlayer.getLocation().getSmellLevel());
    
    assertTrue(testPlayer.getLocation().getSmellUnits() > 1);
    
    //Test that a monster is present in the destination cave cell
    assertEquals(Status.ALIVE, testGame.getDestination().getMonster().getStatus());
    
    //Assert that player has 3 arrows at the beginning of the game
    assertEquals(3, testPlayer.getArrowCount());
    
    //Assert that neighboring cell has a monster
    assertEquals(Status.ALIVE, monsterPosition.getMonster().getStatus());
    
    //Now shoot at this cell where a monster is present but not the exact distance.
    // Assert that the monster is not attacked as the distance is not exact.
    
    testGame.shootArrow(dir, 2);
    
    //Assert that there is a monster present at this location where before shooting an arrow
    assertEquals(Status.ALIVE, monsterPosition.getMonster().getStatus());
    
    //assert that the arrow count is now decreased
    assertEquals(2, testPlayer.getArrowCount());
    
    //assert that the monster is still alive
    assertEquals(Status.ALIVE, monsterPosition.getMonster().getStatus());
    
    //Now shoot the arrow at the exact distance.
    String messageFromShot = testGame.shootArrow(dir, 1);
    
    //assert that the arrow count is now decreased to 1
    assertEquals(1, testPlayer.getArrowCount());
    
    //assert that the monster is wounded when attacked at the exact location.
    assertEquals(Status.WOUNDED, monsterPosition.getMonster().getStatus());
    assertEquals("You shoot an arrow into the darkness\n" + "You hear a great howl in the"
            + " distance.", messageFromShot);
    
    //Now shoot the arrow again at the exact distance to check that the monster gets killed at
    // the second hit.
    messageFromShot = testGame.shootArrow(dir, 1);
    
    //assert that the arrow count is now decreased to 0
    assertEquals(0, testPlayer.getArrowCount());
    assertEquals(
            "You shoot an arrow into the darkness\n" + "You hear a great howl in the distance.\n"
                    + "You killed a monster nearby.\n"
                    + "You are out of arrows, explore more to find more", messageFromShot);
    
    //assert that the monster is dead when attacked at the exact location.
    assertEquals(Status.DEAD, monsterPosition.getMonster().getStatus());
  }
  
  /**
   * This test tests that a player is killed when it enters a cell with a Monster that is fully
   * alive.
   */
  @Test
  public void testPlayerGettingKilled() {
    Players testPlayer = new Player(900);
    AdventureGame testGame =
            new Game(6, 6, 25, false, 100, 100, testPlayer, new MockRandomGenerator(2000));
    Directions dir = testGame.getStart().getNeighbors().keySet().iterator().next();
    Cell monsterPosition = testGame.getStart().getNeighbors().get(dir);
    
    //Assert that the player is alive at the beginning
    assertEquals(Status.ALIVE, testGame.getPlayer().getStatus());
    
    //Assert that there is a monster present at this location where before shooting an arrow
    assertEquals(Status.ALIVE, monsterPosition.getMonster().getStatus());
    
    //Move the player towards the monster's location
    testGame.movePlayer(dir);
    
    //Assert that the player gets killed by the Monster
    assertEquals(Status.DEAD, testGame.getPlayer().getStatus());
    
  }
  
  /**
   * This test tests that a player the player has a 50% chance of survival when it enters a cell
   * with monster that is wounded with one arrow hit.
   */
  @Test
  public void testPlayerEnteringWoundedMonsterCell() {
    List<Status> playerStatus = new ArrayList<>();
    RandomGenerator random = new TrueRandomGenerator();
    
    for (int i = 0; i < 5; i++) {
      Players testPlayer = new Player(900);
      
      Monsters testMonster = new Monster("Otyugh", 1000);
      AdventureGame testGame = new Game(20, 20, 30, false, 10, 10, testPlayer, random);
      
      //First, find a crooked tunnel (with entrances in asymmetrical directions) and a cave
      // neighboring the entrance in crooked direction
      List<Cell> cells = Arrays.stream(testGame.getGridCopy()).flatMap(Arrays::stream)
              .filter(s -> s.getType().equals(CellTypes.CAVE)).collect(Collectors.toList());
      
      //Select a cave neighboring a crooked tunnel, which has a neighboring cave to the  west and
      // and a neighboring cave to the south
      Cell caveWithCrookedTunnel = cells.stream()
              .filter(t -> ((t.getNeighbors().containsKey(Directions.EAST)) && (t.getNeighbors()
                      .get(Directions.EAST).getType().equals(CellTypes.TUNNEL)) && (t.getNeighbors()
                      .get(Directions.EAST).getNeighbors().containsKey(Directions.SOUTH))
                      && (t.getNeighbors().get(Directions.EAST).getNeighbors().get(Directions.SOUTH)
                      .getType().equals(CellTypes.CAVE)))).collect(Collectors.toList()).get(0);
      
      //Crooked tunnel's southern neighbor with a monster
      Cell neighborCave = caveWithCrookedTunnel.getNeighbors().get(Directions.EAST).getNeighbors()
              .get(Directions.SOUTH);
      
      //move the player to this tunnel for testing the crooked arrow
      testPlayer.setLocation(caveWithCrookedTunnel);
      //add a monster to the location for testing the crooked arrow
      testMonster.setLocation(neighborCave);
      neighborCave.setMonster(testMonster);
      
      //Assert there is a monster in the crooked tunnel's neighboring cave
      assertEquals(Status.ALIVE, neighborCave.getMonster().getStatus());
      
      //Now shoot an arrow at this monster from the cave before the crooked tunnel.
      String messageFromShot = testGame.shootArrow(Directions.EAST, 1);
      
      //Now the same monster should be wounded if the arrow successfully went in a crooked direction
      // through the tunnel.
      assertEquals(Status.WOUNDED, neighborCave.getMonster().getStatus());
      
      assertEquals(
              "You shoot an arrow into the darkness\n" + "You hear a great howl in the distance.",
              messageFromShot);
      
      //Now Move the player towards the monster's location with the wounded monster
      
      testGame.movePlayer(Directions.EAST);
      testGame.movePlayer(Directions.SOUTH);
      playerStatus.add(testPlayer.getStatus());
    }
    
    //Assert that the player may be dead or alive after running the sequence of test with the
    // player entering a cell with wounded monster 20 times.
    assertTrue(playerStatus.contains(Status.ALIVE) && playerStatus.contains(Status.DEAD));
    //This asserts that the list of player's statuses from multiple runs contains alive and also
    // dead players.
    
  }
  
  /**
   * This tests picking of arrows and treasures by a player.
   */
  @Test
  public void testArrowPicking() {
    Players testPlayer = new Player(900);
    AdventureGame testGame =
            new Game(6, 6, 1, false, 100, 10, testPlayer, new TrueRandomGenerator());
    
    //Test that the arrow count is 3 by default and treasures collected is 0 by default.
    assertTrue(testPlayer.getArrowCount() == 3);
    assertTrue(testPlayer.getTreasureCollected().get(Treasure.RUBIES) == 0);
    assertTrue(testPlayer.getTreasureCollected().get(Treasure.DIAMONDS) == 0);
    assertTrue(testPlayer.getTreasureCollected().get(Treasure.SAPPHIRES) == 0);
    
    
    //Pick arrow and treasures
    testPlayer.pickArrows();
    testPlayer.pickTreasure(Treasure.RUBIES);
    testPlayer.pickTreasure(Treasure.DIAMONDS);
    testPlayer.pickTreasure(Treasure.SAPPHIRES);
    
    //Test that the arrow collected is more than the default value and the treasures collected is
    // now more than 0 after collecting treasures and arrows.
    assertTrue(testPlayer.getArrowCount() > 3);
    assertTrue(testPlayer.getTreasureCollected().get(Treasure.RUBIES) > 0);
    assertTrue(testPlayer.getTreasureCollected().get(Treasure.DIAMONDS) > 0);
    assertTrue(testPlayer.getTreasureCollected().get(Treasure.SAPPHIRES) > 0);
    assertTrue(testPlayer.getArrowCount() > 0);
  }
  
  /**
   * This tests the crooked arrow movement through a tunnel.
   */
  @Test
  public void testCrookedArrow() {
    Players testPlayer = new Player(900);
    
    Monsters testMonster = new Monster("Otyugh", 1000);
    AdventureGame testGame =
            new Game(20, 20, 30, false, 10, 10, testPlayer, new TrueRandomGenerator());
    
    //First, find a crooked tunnel (with entrances in asymmetrical directions) and a cave
    // neighboring the entrance in crooked direction
    List<Cell> cells = Arrays.stream(testGame.getGridCopy()).flatMap(Arrays::stream)
            .filter(s -> s.getType().equals(CellTypes.CAVE)).collect(Collectors.toList());
    
    //Select a cave neighboring a crooked tunnel, which has a neighboring cave to the  west and
    // and a neighboring cave to the south
    Cell caveWithCrookedTunnel = cells.stream()
            .filter(t -> ((t.getNeighbors().containsKey(Directions.EAST)) && (t.getNeighbors()
                    .get(Directions.EAST).getType().equals(CellTypes.TUNNEL)) && (t.getNeighbors()
                    .get(Directions.EAST).getNeighbors().containsKey(Directions.SOUTH))
                    && (t.getNeighbors().get(Directions.EAST).getNeighbors().get(Directions.SOUTH)
                    .getType().equals(CellTypes.CAVE)))).collect(Collectors.toList()).get(0);
    
    // Crooked tunnel
    Cell crookedTunnel = caveWithCrookedTunnel.getNeighbors().get(Directions.EAST); //neighboring
    
    //Crooked tunnel's southern neighbor with a monster
    Cell neighborCave = caveWithCrookedTunnel.getNeighbors().get(Directions.EAST).getNeighbors()
            .get(Directions.SOUTH);
    
    //move the player to this tunnel for testing the crooked arrow
    testPlayer.setLocation(caveWithCrookedTunnel);
    //add a monster to the location for testing the crooked arrow
    testMonster.setLocation(neighborCave);
    neighborCave.setMonster(testMonster);
    
    //Assert there is a monster in the crooked tunnel's neighboring cave
    assertEquals(Status.ALIVE, neighborCave.getMonster().getStatus());
    
    //Now shoot an arrow at this monster from the cave before the crooked tunnel.
    testGame.shootArrow(Directions.EAST, 1);
    
    //Now the same monster should be wounded if the arrow successfully went in a crooked direction
    // through the tunnel.
    assertEquals(Status.WOUNDED, neighborCave.getMonster().getStatus());
  }
  
  /**
   * This tests that the arrow travels in a straight direction through a cave.
   */
  @Test
  public void testArrowTravelsStraightInCaves() {
    Players testPlayer = new Player(900);
    
    Monsters testMonster = new Monster("Otyugh", 1000);
    AdventureGame testGame =
            new Game(20, 20, 30, false, 10, 10, testPlayer, new TrueRandomGenerator());
    showSimpleDungeonGrid(testGame);
    
    //First, find a cave with another neighoring cave along the same directon
    // neighboring the entrance in crooked direction
    List<Cell> cells = Arrays.stream(testGame.getGridCopy()).flatMap(Arrays::stream)
            .filter(s -> s.getType().equals(CellTypes.CAVE)).collect(Collectors.toList());
    
    //Select a cave neighboring another cave in the east direction.
    Cell caveWithAdjacentCave = cells.stream()
            .filter(t -> ((t.getNeighbors().containsKey(Directions.EAST)) && (t.getNeighbors()
                    .get(Directions.EAST).getType().equals(CellTypes.CAVE))))
            .collect(Collectors.toList()).get(0);
    
    //Cave to the east with a monster
    Cell neighborCave = caveWithAdjacentCave.getNeighbors().get(Directions.EAST);
    
    //move the player to this cave with the adjacent cave for testing
    testPlayer.setLocation(caveWithAdjacentCave);
    //add a monster to the location for testing the crooked arrow
    testMonster.setLocation(neighborCave);
    neighborCave.setMonster(testMonster);
    
    //Assert there is a monster in the neighboring cave in the same direction
    assertEquals(Status.ALIVE, neighborCave.getMonster().getStatus());
    
    //Now shoot an arrow at this monster from the cave with another cave in the east, which has a
    // monster.
    testGame.shootArrow(Directions.EAST, 1);
    
    //Now the same monster should be wounded if the arrow successfully went in a straight direction
    // through the cave.
    assertEquals(Status.WOUNDED, neighborCave.getMonster().getStatus());
  }
  
  
  /**
   * Testing that a cave with no surrounding monster (at least 2 positions away) has no pungent
   * smell.
   */
  @Test
  public void testNoPungentSmellInCellNotNearMonster() {
    Players testPlayer = new Player(900);
    //Note: this game has no monsters from the end cave and the cave next to the start node.
    AdventureGame testGame =
            new Game(20, 20, 30, false, 10, 0, testPlayer, new TrueRandomGenerator());
    
    //First, find a cell that doesn't have any neighbors with a living monster
    Cell filteredCave = Arrays.stream(testGame.getGridCopy()).flatMap(Arrays::stream)
            .filter(s -> ((s.getType().equals(CellTypes.CAVE)) && (
                    Math.abs(s.getX() - testGame.getStart().getX()) > 5) && (
                    Math.abs(s.getY() - testGame.getDestination().getY()) > 5)))
            .collect(Collectors.toList()).get(0);
    
    //Assert that this cave has no pungent smell as it doesn't have any monster.
    assertEquals(Smell.NONE, filteredCave.getSmellLevel());
    assertEquals(0, filteredCave.getSmellUnits());
  }
  
  /**
   * Testing that a cave with a surrounding monster (within 2 positions away) has a pungent smell.
   */
  @Test
  public void testPungentSmellExistsInCellNearMonster() {
    Players testPlayer = new Player(900);
    //Note: this game has no monsters from the end cave and the cave next to the start node.
    Monsters testMonster = new Monster("Otyugh", 1000);
    AdventureGame testGame =
            new Game(20, 20, 40, false, 10, 0, testPlayer, new TrueRandomGenerator());
    
    //Get the neighbor one position away from the destination cell, which always
    // has a monster. Additional filters for
    List<Cell> cellsOnePositionAway = Arrays.stream(testGame.getGridCopy()).flatMap(Arrays::stream)
            .filter(s -> (((Math.abs(s.getX() - testGame.getDestination().getX()) == 0) && (
                    Math.abs(s.getY() - testGame.getDestination().getY()) == 1)) || (
                    (Math.abs(s.getX() - testGame.getDestination().getX()) == 1) && (
                            Math.abs(s.getY() - testGame.getDestination().getY()) == 0))))
            .collect(Collectors.toList());
    
    Cell neighborCaveOnePositionAway = cellsOnePositionAway.stream()
            .filter(s -> (s.getNeighbors().containsValue(testGame.getDestination())))
            .collect(Collectors.toList()).get(0);
    
    //Get the neighbor two positions away from the destination cell, which always has a monster.
    Cell neighborCaveTwoPositionAway =
            neighborCaveOnePositionAway.getNeighbors().entrySet().iterator().next().getValue();
    
    //Assert that the cell one position away from the dehas a pungent smell
    assertEquals(Smell.PUNGENT, neighborCaveOnePositionAway.getSmellLevel());
    
    //Assert that the cell two positions away has a pungent smell
    assertEquals(Smell.PUNGENT, neighborCaveTwoPositionAway.getSmellLevel());
  }
  
  /**
   * This test creates an example game with a Monster in a specific location to test the slaying of
   * a monster with an arrow and other moves.
   */
  @Test
  public void testValidActionsControllerSlayingAMonster() {
    Players testPlayer = new Player(900);
    //Add  a monster to the neighboring cave of the start position.
    Monsters testMonster = new Monster("Otyugh", 1000);
    AdventureGame testGame =
            new Game(20, 20, 30, false, 100, 0, testPlayer, new TrueRandomGenerator());
    //System.out.println(testGame);
    showSimpleDungeonGrid(testGame);
    
    //First, find a crooked tunnel (with entrances in asymmetrical directions) and a cave
    // neighboring the entrance in crooked direction
    List<Cell> cells = Arrays.stream(testGame.getGridCopy()).flatMap(Arrays::stream)
            .filter(s -> s.getType().equals(CellTypes.CAVE)).collect(Collectors.toList());
    
    //Select a cave neighboring a crooked tunnel, which has a neighboring cave to the  west and
    // and a neighboring cave to the south
    Cell caveWithCrookedTunnel = cells.stream()
            .filter(t -> ((t.getNeighbors().containsKey(Directions.EAST)) && (t.getNeighbors()
                    .get(Directions.EAST).getType().equals(CellTypes.TUNNEL)) && (t.getNeighbors()
                    .get(Directions.EAST).getNeighbors().containsKey(Directions.SOUTH))
                    && (t.getNeighbors().get(Directions.EAST).getNeighbors().get(Directions.SOUTH)
                    .getType().equals(CellTypes.CAVE)))).collect(Collectors.toList()).get(0);
    
    // Crooked tunnel
    Cell crookedTunnel = caveWithCrookedTunnel.getNeighbors().get(Directions.EAST); //neighboring
    //Crooked tunnel's southern neighbor with a monster
    Cell neighborCave = caveWithCrookedTunnel.getNeighbors().get(Directions.EAST).getNeighbors()
            .get(Directions.SOUTH);
    
    //move the player to this tunnel for testing the crooked arrow
    testPlayer.setLocation(caveWithCrookedTunnel);
    //add a monster to the location for testing the crooked arrow
    testMonster.setLocation(neighborCave);
    neighborCave.setMonster(testMonster);
    
    StringReader input = new StringReader(
            "S 1 E S 1 E P ruby P arrow P diamond P sapphire M W " + "M N M S M E q");
    Appendable gameLog = new StringBuilder();
    Controller c = new ConsoleController(input, gameLog, testGame);
    c.playGame();
    String[] output = gameLog.toString().split("\n");
    String message = String.join("\n", Arrays.copyOfRange(output, 0, output.length));
    
    //System.out.println("output of this game");
    //System.out.println(message);
    
    //Asserting messsages showing treasure found
    assertTrue(message.contains("You find") && message.contains("arrow(s) here"));
    assertTrue(message.contains("You find") && message.contains("rubies") && message.contains(
            "sapphires") && message.contains("diamonds here"));
    
    //Message showing entrances
    assertTrue(message.contains("Entrances can be found at"));
    
    //Message showing action menu
    assertTrue(message.contains("Move, Pickup, or Shoot (M-P-S)?"));
    //Message showing options for shoot
    //assertTrue(message.contains("No. of caves?"));
    assertTrue(message.contains("Where to?"));
    //Message shown after attacking a monster
    assertTrue(message.contains("You hear a great howl in the distance."));
    //Message shown after killing a monster
    assertTrue(message.contains("You killed a monster nearby."));
    
    //Message shown after picking up treasures.
    assertTrue(message.contains("You pick up Sapphires."));
    assertTrue(message.contains("You pick up Diamonds."));
    assertTrue(message.contains("You pick up Rubies."));
    assertTrue(message.contains("You pick up arrows."));
    
  }
  
  /**
   * Test the controller with invalid inputs.
   */
  @Test
  public void testInvalidActionsController() {
    Players testPlayer = new Player(900);
    //Add  a monster to the neighboring cave of the start position.
    Monsters testMonster = new Monster("Otyugh", 1000);
    AdventureGame testGame =
            new Game(10, 10, 30, false, 100, 0, testPlayer, new TrueRandomGenerator());
    showSimpleDungeonGrid(testGame);
    
    //Testing with invalid moves in between.
    StringReader input =
            new StringReader("x P @ P ruby # P arrow 3 P diamond P " + "sapphire " + "q");
    Appendable gameLog = new StringBuilder();
    Controller c = new ConsoleController(input, gameLog, testGame);
    c.playGame();
    String[] output = gameLog.toString().split("\n");
    String message = String.join("\n", Arrays.copyOfRange(output, 0, output.length));
    
    
    //Check for invalid input messages
    assertTrue(message.contains("Invalid input, please try again"));
    
    //Assert that the game still ran smoothly without issues.
    //Message shown after picking up treasures.
    assertTrue(message.contains("You pick up Sapphires."));
    assertTrue(message.contains("You pick up Diamonds."));
    assertTrue(message.contains("You pick up Rubies."));
    assertTrue(message.contains("You pick up arrows."));
    
    //Message shown after quitting game.
    assertTrue(message.contains("Quitting the game."));
    
  }
  
  
  /**
   * This test tests a player getting killed at the destination cave.
   */
  @Test
  public void testPlayerGettingKilledAtEndCave() {
    Players testPlayer = new Player(900);
    AdventureGame testGame =
            new Game(10, 10, 100, true, 100, 0, testPlayer, new TrueRandomGenerator());
    //System.out.println(testGame);
    showSimpleDungeonGrid(testGame);
    
    int endX = testGame.getDestination().getX();
    int endY = testGame.getDestination().getY();
    
    Directions dir1 = null;
    Directions dir2 = null;
    int startX = 0;
    int startY = 0;
    
    if (endX > 3) {
      startX = endX - 3;
      dir1 = Directions.SOUTH;
    } else if (endX <= 3) {
      startX = endX + 3;
      dir1 = Directions.NORTH;
    }
    
    if (endY > 3) {
      startY = endY - 3;
      dir2 = Directions.EAST;
    } else if (endY <= 3) {
      startY = endY + 3;
      dir2 = Directions.WEST;
    }
    
    //First, find a cell that is five positions away from the destination cave
    Cell start = testGame.getGridCopy()[startX][startY];
    testPlayer.setLocation(start); //set the player to this location for testing
    
    //Now test with 5 moves to see if the player reaches the destination.
    //dir1 = testGame.getStart().getNeighbors().keySet().iterator().next();
    StringReader input = new StringReader(
            String.format("m %s m %s m %s m %s m %s m %s q", dir1, dir1, dir1, dir2, dir2, dir2));
    Appendable gameLog = new StringBuilder();
    Controller c = new ConsoleController(input, gameLog, testGame);
    c.playGame();
    String[] output = gameLog.toString().split("\n");
    String message = String.join("\n", Arrays.copyOfRange(output, 0, output.length));
    
    //Assert that the player is killed after reaching the destination cave with a living monster
    assertTrue(message.contains("Chomp, chomp, chomp, you are eaten by an Otyugh!"));
    assertEquals(Status.DEAD, testPlayer.getStatus());
  }
  
  /**
   * This test tests that a player the player can reach the destination cave.
   */
  @Test
  public void testPlayerReachingDestination() {
    Players testPlayer = new Player(900);
    //Add  a monster to the neighboring cave of the start position.
    AdventureGame testGame =
            new Game(10, 10, 100, true, 100, 0, testPlayer, new TrueRandomGenerator());
    
    //For simplicity we'll only keep the monster in the end cave alive so the player doesn't die
    // before completing the test.
    List<Monsters> monsters = Arrays.stream(testGame.getGridCopy()).flatMap(Arrays::stream)
            .filter(s -> s.getMonster() != null
                    && s.getMonster().getLocation().getID() != testGame.getDestination().getID())
            .map(m -> m.getMonster()).collect(Collectors.toList());
    
    for (Monsters monster : monsters) {
      Cell loc = monster.getLocation();
      monster.setStatus(Status.DEAD);
      loc.setMonster(null);
    }
    
    int endX = testGame.getDestination().getX();
    int endY = testGame.getDestination().getY();
    
    Directions dir1 = null;
    Directions dir2 = null;
    int startX = 0;
    int startY = 0;
    
    
    if (endX > 3) {
      startX = endX - 3;
      dir1 = Directions.SOUTH;
    } else if (endX <= 3) {
      startX = endX + 3;
      dir1 = Directions.NORTH;
    }
    
    if (endY > 3) {
      startY = endY - 3;
      dir2 = Directions.EAST;
    } else if (endY <= 3) {
      startY = endY + 3;
      dir2 = Directions.WEST;
    }
    
    //First, find a cell that is five positions away from the destination cave
    Cell start = testGame.getGridCopy()[startX][startY];
    testPlayer.setLocation(start); //set the player to this location for testing
    
    //Now test with 5 moves to see if the player reaches the destination.
    //dir1 = start.getNeighbors().keySet().iterator().next();
    StringReader input = new StringReader(
            String.format("m %s m %s m %s m %s m %s S 1 %s S 1 %s m " + "%s q", dir1, dir1, dir1,
                    dir2, dir2, dir2, dir2, dir2));
    Appendable gameLog = new StringBuilder();
    Controller c = new ConsoleController(input, gameLog, testGame);
    c.playGame();
    String[] output = gameLog.toString().split("\n");
    String message = String.join("\n", Arrays.copyOfRange(output, 0, output.length));
    
    //Assert that the reaches the destination cave with after shooting a monster twice.
    assertTrue(message.contains("You are at the destination and survived the monster. You won!"));
    assertEquals(true, testGame.isGameOver());
    
  }
  
  private void showSimpleDungeonGrid(AdventureGame game) {
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
          log.append("NP" + x + y + "-->");
          log.append(grid[x][y].getType().toString() + "--> ");
          if (! grid[x][y].getTreasures().isEmpty()) {
            log.append(String.format("Has Treasure "));
          }
          if (grid[x][y].getArrows() > 0) {
            log.append(String.format("Has "));
          }
          if (grid[x][y].getMonster() != null) {
            log.append(String.format("Monster Present-%s", grid[x][y].getMonster().getName()));
          }
          if (grid[x][y].getSmellUnits() != 0) {
            log.append(String.format("Smell-%s-", grid[x][y].getSmellUnits()));
          }
          
          log.append(String.format("%s has Neighbors in NSEW-%s%s%s%s \t",
                  grid[x][y].getNeighbors().size(), grid[x][y].getN(), grid[x][y].getS(),
                  grid[x][y].getE(), grid[x][y].getW()));
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
  }
  
}