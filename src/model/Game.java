package model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import model.gameobjects.Directions;
import model.gameobjects.actors.Monster;
import model.gameobjects.actors.Monsters;
import model.gameobjects.actors.Players;
import model.gameobjects.actors.Status;
import model.gameobjects.actors.Thief;
import model.gameobjects.actors.Thieves;
import model.gameobjects.cell.Cell;
import model.gameobjects.cell.CellTypes;
import model.gameobjects.cell.Location;
import model.gameobjects.cell.Smell;
import model.gameobjects.cell.Treasure;
import random.RandomGenerator;
import random.TrueRandomGenerator;

/**
 * Represents a single dungeon adventure game with one player and a dungeon comprising a network of
 * caves and tunnels. This is the model that handles all the operations required to run the game.
 * The operations include allowing: both wrapping and non-wrapping dungeons to be created with
 * different degrees of interconnectivity provide support for at least three types of treasure:
 * diamonds, rubies, and sapphires treasure to be added to a specified percentage of caves. For
 * example, the client should be able to ask the model to add treasure to 20% of the caves and the
 * model should add a random treasure to at least 20% of the caves in the dungeon. A cave can have
 * more than one treasure. A player to enter the dungeon at the start provide a description of the
 * player that, at a minimum, includes a description of what treasure the player has collected
 * provide a description of the player's location that at the minimum includes a description of
 * treasure in the room and the possible moves (north, east, south, west) that the player can make
 * from their current location a player to move from their current location a player to pick up
 * treasure that is located in their same location.
 */
public class Game implements AdventureGame {
  private final RandomGenerator random;
  private final Cell startPosition;
  private final Cell endPosition;
  private final Players player;
  private Dungeon dungeonGrid;
  private boolean gameOver;
  private int endCaveNotFoundCount;
  private Thieves[] thieves;
  
  
  /**
   * The default constructor for a Dungeon game.
   * @param rows Number of rows in the desired Dungeon.
   * @param cols Number of columns in the desired Dungeon.
   * @param interconnectivity The degree of interconnectivity of the desired Dungeon.
   * @param isWrapped Whether the Dungeon is of wrapping type or not.
   * @param percentOfTreasure The percentage of caves with treasures.
   * @param player The player that will enter the Dungeon to explore.
   * @param seed The seed value to set seed for the random variable.
   */
  public Game(int rows, int cols, int interconnectivity, boolean isWrapped, int percentOfTreasure,
              int percentOfMonsters, Players player, int seed) {
    
    validateGameInputs(rows, cols, interconnectivity, isWrapped, percentOfTreasure,
            percentOfMonsters, player);
    this.player = player;
    this.random = new TrueRandomGenerator();
    random.setSeed(seed);
    
    //Sets the dungeon grid.
    setDungeon(rows, cols, interconnectivity, isWrapped, random, percentOfTreasure,
            percentOfMonsters);
    this.endCaveNotFoundCount = 0;
    startPosition = getStartNode();
    endPosition = getEndNode(startPosition);
    movePlayerTo(startPosition);
    moveMonsterTo(new Monster("Otyugh", 1), endPosition);
    setMonsters(); //Set additional monsters to random caves.
    setPits(percentOfMonsters);
    setThieves(percentOfMonsters);
    gameOver = false;
    
  }
  
  
  /**
   * The default constructor for the text-based Dungeon game.
   * @param rows Number of rows in the desired Dungeon.
   * @param cols Number of columns in the desired Dungeon.
   * @param interconnectivity The degree of interconnectivity of the desired Dungeon.
   * @param isWrapped Whether the Dungeon is of wrapping type or not.
   * @param percentOfTreasure The percentage of caves with treasures.
   * @param player The player that will enter the Dungeon to explore.
   */
  public Game(int rows, int cols, int interconnectivity, boolean isWrapped, int percentOfTreasure,
              int percentOfMonsters, Players player) {
    validateGameInputs(rows, cols, interconnectivity, isWrapped, percentOfTreasure,
            percentOfMonsters, player);
    this.player = player;
    this.random = new TrueRandomGenerator();
    
    //Sets the dungeon grid.
    setDungeon(rows, cols, interconnectivity, isWrapped, random, percentOfTreasure,
            percentOfMonsters);
    this.endCaveNotFoundCount = 0;
    startPosition = getStartNode();
    endPosition = getEndNode(startPosition);
    movePlayerTo(startPosition);
    moveMonsterTo(new Monster("Otyugh", 1), endPosition);
    setMonsters(); //Set additional monsters to random caves.
    setPits(percentOfMonsters);
    setThieves(percentOfMonsters);
    gameOver = false;
    
  }
  
  /**
   * Default constructor for game with graphical view before the model settings are provided.
   */
  public Game() {
    this.player = null;
    this.random = null;
    //Sets the dungeon grid.
    this.dungeonGrid = null;
    this.endCaveNotFoundCount = 0;
    startPosition = null;
    endPosition = null;
    gameOver = false;
  }
  
  /**
   * The constructor for testing the Dungeon game.
   * @param rows Number of rows in the desired Dungeon.
   * @param cols Number of columns in the desired Dungeon.
   * @param interconnectivity The degree of interconnectivity of the desired Dungeon.
   * @param isWrapped Whether the Dungeon is of wrapping type or not.
   * @param percentOfTreasure The percentage of caves with treasures.
   * @param player The player that will enter the Dungeon to explore. //* @param dungeon The
   *         dungeon to use in the test game.
   * @param random The mock or true random instance for testing.
   */
  public Game(int rows, int cols, int interconnectivity, boolean isWrapped, int percentOfTreasure,
              int percentOfMonsters, Players player, RandomGenerator random) {
    
    validateGameInputs(rows, cols, interconnectivity, isWrapped, percentOfTreasure,
            percentOfMonsters, player);
    this.player = player;
    
    this.random = random;
    setDungeon(rows, cols, interconnectivity, isWrapped, random, percentOfTreasure,
            percentOfMonsters);
    this.endCaveNotFoundCount = 0;
    startPosition = getStartNode();
    endPosition = getEndNode(startPosition);
    
    movePlayerTo(startPosition);
    
    moveMonsterTo(new Monster("Otyugh", 80), endPosition);
    Directions dir = startPosition.getNeighbors().keySet().iterator().next();
    Cell monsterPosition = startPosition.getNeighbors().get(dir);
    //Add a monster to the neighbor of start cave for testing.
    moveMonsterTo(new Monster("Otyugh", 100), monsterPosition);
    setMonsters(); //Set additional monsters to random caves.
    //setPits(percentOfMonsters);
    setThieves(percentOfMonsters);
  }
  
  /**
   * Get the randomly selected start position in this game.
   * @return Map of Integers for x and y position.
   */
  @Override
  public Map<Integer, Integer> getStartPosition() {
    return Map.of(this.startPosition.getX(), this.startPosition.getY());
  }
  
  
  /**
   * Get the random selected end position in this game, that meets the min distance criteria.
   * @return Map of Integers for x and y position.
   */
  @Override
  public Map<Integer, Integer> getEndPosition() {
    return Map.of(this.endPosition.getX(), this.endPosition.getY());
  }
  
  /**
   * Get the player in this game.
   * @return the player.
   */
  @Override
  public Players getPlayer() {
    return this.player;
  }
  
  
  /**
   * Check if the game is over.
   * @return true or false.
   */
  @Override
  public boolean isGameOver() {
    return gameOver;
  }
  
  //Set if the game is over.
  private void setGameOver(boolean gameOver) {
    this.gameOver = gameOver;
  }
  
  /**
   * Get the start Cave cell in this game.
   * @return the cell of cave type chosen as the start.
   */
  @Override
  public Cell getStart() {
    return this.startPosition;
  }
  
  /**
   * Get the destination Cave cell in this game.
   * @return the cell of cave type chosen as the destination.
   */
  @Override
  public Cell getDestination() {
    return this.endPosition;
  }
  
  /**
   * Get the interconnectivity of the Dungeon.
   * @return the degree of interconnectivity.
   */
  @Override
  public int getInterconnectivity() {
    return this.dungeonGrid.getInterconnectivity();
  }
  
  /**
   * Get the rows in the dungeon.
   * @return number of rows.
   */
  @Override
  public int getRows() {
    return this.dungeonGrid.getRows();
  }
  
  /**
   * Get the columns in the dungeon.
   * @return number of columns.
   */
  @Override
  public int getColumns() {
    return this.dungeonGrid.getColumns();
  }
  
  /**
   * Get player's location.
   * @return list of x and y coordinates.
   */
  @Override
  public List<Integer> getPlayerLocation() {
    return List.of(player.getPositionX(), player.getPositionY());
  }
  
  /**
   * Get the status of the player.
   * @return status of the player, alive, dead, winner.
   */
  @Override
  public Status getPlayerStatus() {
    return this.player.getStatus();
  }
  
  /**
   * Get if the dungeon is wrapped.
   * @return if the dungeon is wrapped or not.
   */
  @Override
  public boolean isWrapped() {
    return this.dungeonGrid.isWrapped();
  }
  
  /**
   * Get the percent of caves in the dungeon with treasure.
   * @return percentage.
   */
  @Override
  public int getPercentOfTreasure() {
    return this.dungeonGrid.getPercentOfTreasure();
  }
  
  /**
   * Get the Dungeon grid copy.
   * @return the Dungeon grid copy.
   */
  @Override
  public Cell[][] getGridCopy() {
    return this.dungeonGrid.getGridCopy();
  }
  
  /**
   * Get the copy of the current location of the player.
   * @return the copy of current location of the player.
   */
  @Override
  public Cell getCurrentLocation() {
    return new Location(player.getLocation());
  }
  
  /**
   * Get the copy of the cell at a location in Dungeon grid.
   * @param x the x coordinate.
   * @param y the y coordinate.
   */
  //@Override
  private Cell getCellAt(int x, int y) {
    return this.dungeonGrid.getGrid()[x][y];
  }
  
  /**
   * Move the player to a chosen direction.
   * @param direction direction to move the player to.
   */
  @Override
  public void movePlayer(Directions direction) {
    Cell currentLoc = player.getLocation();
    if (currentLoc.getNeighbors().containsKey(direction) && player.getStatus()
            .equals(Status.ALIVE)) {
      //movePlayerTo(dungeonGrid.getCellAt(x, y));
      //Get neighbor in this direction
      movePlayerTo(currentLoc.getNeighbors().get(direction));
      
      //Check if the new location has a monster. If so, then the player will die if the Monster
      // is Alive, and if it is wounded, it has 50% chance of survival.
      Cell currentCell = player.getLocation();
      
      if (currentCell.getMonster() != null) {
        //if non-null monster is present
        if (currentCell.getMonster().getStatus() == Status.ALIVE) {
          player.setStatus(Status.DEAD);
          setGameOver(true);
        }
        if (currentCell.getMonster().getStatus() == Status.WOUNDED) {
          //50% chance of survival.
          int val = random.getRandomInt(1, 100);
          if (val <= 50) {
            player.setStatus(Status.ALIVE);
          } else {
            player.setStatus(Status.DEAD);
            setGameOver(true);
          }
        }
      }
      
      if (currentCell.getType().equals(CellTypes.PIT)) {
        player.setStatus(Status.DEAD);
        setGameOver(true);
      }
      
      if (currentCell.hasThief()) {
        currentCell.getThief().stealTreasure(player.getTreasureCollected());
        player.resetTreasure();
      }
      
      if (currentCell.getID() == endPosition.getID() && player.getStatus().equals(Status.ALIVE)) {
        player.setStatus(Status.WON);
        setGameOver(true);
      }
    } else if (player.getStatus().equals(Status.DEAD)) {
      // Don't do anything if the player is dead
    } else {
      throw new IllegalArgumentException(
              "This direction doesn't have an entrance in the player's" + " current location.");
    }
  }
  
  
  /**
   * Move player to a cell in the Dungeon by x and y coordinate.
   */
  @Override
  public void movePlayerTo(int y, int x) {
    if (x > dungeonGrid.rows || y > dungeonGrid.columns) {
      throw new IllegalArgumentException("Cannot move player. Invalid x or y coordinate.");
    }
    ///get the cell at this location
    Cell location = getCellAt(x, y);
    
    if (this.player.getStatus() == Status.ALIVE) {
      if (this.player.getLocation() != null) {
        if (! this.getCurrentLocation().getNeighbors().containsValue(location)) {
          throw new IllegalArgumentException("Illegal Move");
        } else {
          this.player.getLocation().setPlayer(false);
        }
      }
      this.player.setLocation(location);
      location.setVisited(true);
      location.setPlayer(true);
      
      //Check if the new location has a monster. If so, then the player will die if the Monster
      // is Alive, and if it is wounded, it has 50% chance of survival.
      Cell currentCell = player.getLocation();
      
      if (currentCell.getMonster() != null) {
        //if non-null monster is present
        if (currentCell.getMonster().getStatus() == Status.ALIVE) {
          player.setStatus(Status.DEAD);
          setGameOver(true);
        }
        if (currentCell.getMonster().getStatus() == Status.WOUNDED) {
          //50% chance of survival.
          int val = random.getRandomInt(1, 100);
          if (val <= 50) {
            player.setStatus(Status.ALIVE);
          } else {
            player.setStatus(Status.DEAD);
            setGameOver(true);
          }
        }
      }
      
      if (currentCell.getType().equals(CellTypes.PIT)) {
        player.setStatus(Status.DEAD);
        setGameOver(true);
      }
      
      if (currentCell.hasThief()) {
        currentCell.getThief().stealTreasure(player.getTreasureCollected());
        player.resetTreasure();
      }
      
      if (currentCell.equals(endPosition) && player.getStatus() == Status.ALIVE) {
        player.setStatus(Status.WON);
        setGameOver(true);
      }
    }
    //do nothing if player is dead
  }
  
  /**
   * Move player to a cell in the Dungeon.
   */
  @Override
  public void movePlayerTo(Cell location) {
    if (this.player.getStatus() == Status.ALIVE) {
      if (this.player.getLocation() != null) {
        if (! this.getCurrentLocation().getNeighbors().containsValue(location)) {
          throw new IllegalArgumentException("Illegal Move");
        } else {
          this.player.getLocation().setPlayer(false);
        }
      }
      this.player.setLocation(location);
      location.setVisited(true);
      location.setPlayer(true);
      
      //Check if the new location has a monster. If so, then the player will die if the Monster
      // is Alive, and if it is wounded, it has 50% chance of survival.
      Cell currentCell = player.getLocation();
      
      if (currentCell.getMonster() != null) {
        //if non-null monster is present
        if (currentCell.getMonster().getStatus() == Status.ALIVE) {
          player.setStatus(Status.DEAD);
          setGameOver(true);
        }
        if (currentCell.getMonster().getStatus() == Status.WOUNDED) {
          //50% chance of survival.
          int val = random.getRandomInt(1, 100);
          if (val <= 50) {
            player.setStatus(Status.ALIVE);
          } else {
            player.setStatus(Status.DEAD);
            setGameOver(true);
          }
        }
      }
      
      if (currentCell.getType().equals(CellTypes.PIT)) {
        player.setStatus(Status.DEAD);
        setGameOver(true);
      }
      
      if (currentCell.hasThief()) {
        currentCell.getThief().stealTreasure(player.getTreasureCollected());
        player.resetTreasure();
      }
      
      if (currentCell.equals(endPosition) && player.getStatus() == Status.ALIVE) {
        player.setStatus(Status.WON);
        setGameOver(true);
      }
    }
    //do nothing if player is dead
  }
  
  //Helper method to move the monster to a cell in the Dungeon and update the smell accordingly.
  private void moveMonsterTo(Monsters monster, Cell location) {
    monster.setLocation(location);
    location.setMonster(monster);
    updateSmell(location, 1);
  }
  
  private void updateSmell(Cell locationOfMonster, int multiplier) {
    //Set very pungent smell in neighbors 1 position away from the current cell.
    //Get the number of cells with alive or wounded monster
    
    //long firstCount = locationOfMonster.getNeighbors().values().stream()
    //        .filter(i -> (i.getMonster().getStatus() != Status.DEAD)).count();
    
    for (Map.Entry<Directions, Cell> entry : locationOfMonster.getNeighbors().entrySet()) {
      Cell firstNeighbor = entry.getValue();
      firstNeighbor.setSmell(Smell.PUNGENT, 2 * multiplier);
      //Set less pungent smell in neighbors 2 positions away from the current cell and 1
      // position away from this neighbor.
      Map<Directions, Cell> secondNeighbors = firstNeighbor.getNeighbors();
      secondNeighbors.remove(locationOfMonster);
      
      for (Map.Entry<Directions, Cell> secondEntry : secondNeighbors.entrySet()) {
        Cell secondNeighbor = secondEntry.getValue();
        //If second neighbor already has some smell, then make it'll get more smelly,
        //the units will be appended to existing units
        secondNeighbor.setSmell(Smell.PUNGENT, 1 * multiplier);
      }
    }
  }
  
  /**
   * Get the treasure stolen by the thief in this game.
   * @return the treasure collected by the thief.
   */
  @Override
  public Map<Treasure, Integer> getTreasureStolen() {
    Map<Treasure, Integer> total = new HashMap<>();
    total.put(Treasure.DIAMONDS, 0);
    total.put(Treasure.RUBIES, 0);
    total.put(Treasure.SAPPHIRES, 0);
    if (thieves != null) {
      for (Thieves thief : thieves) {
        total.put(Treasure.DIAMONDS,
                total.get(Treasure.DIAMONDS) + thief.getTreasureCollected().get(Treasure.DIAMONDS));
        total.put(Treasure.RUBIES,
                total.get(Treasure.RUBIES) + thief.getTreasureCollected().get(Treasure.RUBIES));
        total.put(Treasure.SAPPHIRES, total.get(Treasure.SAPPHIRES) + thief.getTreasureCollected()
                .get(Treasure.SAPPHIRES));
      }
    }
    return total;
  }
  
  /**
   * Get the treasure collected by the player in this game.
   * @return the treasure collected by the player.
   */
  @Override
  public Map<Treasure, Integer> getTreasureCollected() {
    return player.getTreasureCollected();
  }
  
  /**
   * Get the arrows collected by the player in this game.
   * @return the arrows collected by the player.
   */
  @Override
  public Integer getArrowCount() {
    return player.getArrowCount();
  }
  
  
  /**
   * Shoot an arrow at the Monster, if present towards the given direction and at the exact
   * distance.
   * @param direction direction to shoot.
   * @param distance distance measured by the number of cave that an arrow travels.
   * @return output of the shooting.
   */
  @Override
  public String shootArrow(Directions direction, int distance) {
    
    if (distance < 1 || player.getArrowCount() <= 0) {
      throw new IllegalArgumentException("Player has no arrow left or the distance is invalid.");
    }
    
    String out = shoot(direction, distance);
    return out;
  }
  
  private String shoot(Directions direction, int distance) {
    StringBuilder output = new StringBuilder();
    if (player.getStatus() == Status.DEAD) {
      return "";
    }
    output.append("You shoot an arrow into the darkness");
    Cell currentLoc = player.getLocation(); //The shooting location
    Directions incomingDir = direction;
    //Directions reverseDir = direction.getReverse(); //Reverse direction
    int distChecked = 0;
    
    Cell neighbor = currentLoc.getNeighbors().get(direction);
    
    CellTypes type;
    while (distChecked != distance) {
      type = neighbor.getType();
      
      if (type == CellTypes.CAVE) {
        distChecked += 1; //If it's a cave type, the distance is counted.
        
        //get the neighbor in the same direction.
        if (distChecked != distance && neighbor.getNeighbors().containsKey(direction)) {
          currentLoc = neighbor;
          neighbor = neighbor.getNeighbors().get(direction);
        } else if (distChecked != distance && ! neighbor.getNeighbors().containsKey(direction)) {
          //player.decreaseNumArrows();
          break;
        }
      }
      if (type == CellTypes.TUNNEL) {
        //if it's a tunnel, get the other neighbor that is not the neighbor where the arrow was
        // thrown
        if (distChecked != distance) {
          Map<Directions, Cell> neighbors = new HashMap<>();
          neighbors.putAll(neighbor.getNeighbors()); //First get a copy of neighbors
          neighbors.remove(
                  incomingDir.getReverse()); //Then remove the neighbor where the arrow came from.
          //break;
          currentLoc = neighbor;
          incomingDir = neighbors.keySet().iterator().next();
          neighbor = neighbors.get(incomingDir); //The arrow will pass
        }
        //The cell that is not the neighbor the arrow came from.
        //distChecked += 1; //distance not counted for tunnels
      }
      
      //Check if the current location has a monster
      if (distChecked == distance) {
        if (neighbor.getMonster() != null && neighbor.getMonster().getStatus() != Status.DEAD) {
          
          //Monster is attacked
          neighbor.getMonster().takeHit();
          output.append("\nYou hear a great howl in the distance.");
          
          if (neighbor.getMonster().getStatus() == Status.DEAD) {
            output.append("\nYou killed a monster nearby.");
            //neighbor.setMonster(null);
            //If the monster is dead, update smell in neighboring cells.
            updateSmell(neighbor, - 1);
          }
        }
      }
    }
    player.decreaseNumArrows();
    if (player.getArrowCount() < 1) {
      output.append("\nYou are out of arrows, explore more to find more");
    }
    return output.toString();
  }
  
  /**
   * Get a description of the current location of player.
   * @return Map with descriptions of Type of cell, location, treasures, & available moves.
   */
  @Override
  public Map<String, List> describeCurrentLocation() {
    int x = player.getPositionX();
    int y = player.getPositionY();
    Cell currentLoc = dungeonGrid.getCellAt(x, y);
    CellTypes locType = currentLoc.getType();
    
    if (locType.equals(CellTypes.CAVE)) {
      return Map.of("Location Type:", List.of("Cave"), "Treasures",
              List.of(currentLoc.getTreasures()), "Available Moves",
              List.of(currentLoc.getNeighbors().keySet()));
    } else {
      return Map.of("Location Type:", List.of("Tunnel"), "Available Moves",
              List.of(currentLoc.getNeighbors().keySet()));
    }
  }
  
  //Get the start cell.
  private Cell getStartNode() {
    List<Cell> caves = getCaveCells();
    int randomIndex = random.getRandomInt(0, caves.size() - 1);
    return caves.get(randomIndex);
  }
  
  //Get the end cell.
  private Cell getEndNode(Cell startLocation) {
    boolean endLocationFound = false;
    Cell endLocation = null;
    List<Cell> caves = getCaveCells();
    int numChecks = 0;
    boolean conditionMet = false;
    while (! conditionMet) {
      int randomIndex = random.getRandomInt(0, caves.size() - 1);
      endLocation = caves.get(randomIndex);
      //Check if it's a valid end location based on manhattan distance.
      endLocationFound = isDestinationValid(startLocation, endLocation);
      numChecks += 1;
      if ((endLocationFound) || (numChecks >= caves.size())) {
        conditionMet = true;
        
      }
    }
    
    if (! endLocationFound) {
      endCaveNotFoundCount += 1;
      
      if (endCaveNotFoundCount > 5) {
        throw new IllegalArgumentException("No end location that meets the required distance of 5 "
                + "could be found. Please try again, or try to create a bigger sized Dungeon.");
      }
      return getEndNode(getStart());
    }
    return endLocation;
  }
  
  //https://stackoverflow.com/questions/55398562/how-to-efficiently-calculate-distance-in-toroidal-
  // space/55398869
  //Check if the destination is
  
  /**
   * Check if the destination is valid.
   * @param startLocation start cell.
   * @param endLocation end cell.
   * @return true or false.
   */
  public boolean isDestinationValid(Cell startLocation, Cell endLocation) {
    int dist = 0;
    if (! dungeonGrid.isWrapped()) {
      dist = Math.abs(startLocation.getX() - endLocation.getX()) + Math.abs(
              startLocation.getY() - endLocation.getY());
    }
    if (dungeonGrid.isWrapped()) {
      int distX = Math.abs(startLocation.getX() - endLocation.getX());
      if (distX > (dungeonGrid.getRows() / 2)) {
        distX = dungeonGrid.getRows() - distX;
      }
      int distY = Math.abs(startLocation.getY() - endLocation.getY());
      if (distY > (dungeonGrid.getColumns() / 2)) {
        distY = dungeonGrid.getColumns() - distY;
      }
      dist = (int) Math.ceil(Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2)));
    }
    return dist >= 5;
  }
  
  //Get all the cells of cave type.
  private List<Cell> getCaveCells() {
    //Get all the caves
    return Arrays.stream(dungeonGrid.getGrid()).flatMap(Arrays::stream)
            .filter(s -> s.getType().equals(CellTypes.CAVE)).collect(Collectors.toList());
  }
  
  //Add a dungeon to the game.
  private void setDungeon(int rows, int cols, int interconnectivity, boolean isWrapped,
                          RandomGenerator random, int percentOfTreasure, int percentOfMonsters) {
    ModifiedKruskal builder =
            new DungeonBuilderKruskal(rows, cols, interconnectivity, isWrapped, random,
                    percentOfTreasure, percentOfMonsters);
    this.dungeonGrid = builder.buildDungeon();
  }
  
  //Set additional objects in random cave and tunnel cells in the dungeon. Note: one Monster is
  // already set in the end Cave during initialization.
  private void setMonsters() {
    //Get all the cave cells
    List<Cell> cells = Arrays.stream(this.dungeonGrid.getGrid()).flatMap(Arrays::stream)
            .filter(s -> s.getType().equals(CellTypes.CAVE)).collect(Collectors.toList());
    
    //Remove start and end caves from this list
    cells.remove(startPosition);
    cells.remove(endPosition);
    
    //get the number of cells to assign the arrows.
    if (this.dungeonGrid.getPercentOfMonsters() != 0) {
      int numCells =
              (int) Math.ceil((cells.size() * this.dungeonGrid.getPercentOfMonsters()) / 100);
      numCells -= 1; //Decrease 1 from number as one monster is already in end cave.
      
      //Assign monsters to random caves apart from start cell, which shouldn't have any monster,
      // and end cave, which already has one monster.
      for (int i = 0; i < numCells; i++) {
        int randomIndex = random.getRandomInt(0, cells.size() - 1);
        Cell cell = cells.get(randomIndex);
        
        //assign a monster to this cave.
        moveMonsterTo(new Monster("Otyugh", i + 2), cell); //ID starting from 2.
        cells.remove(cell); //remove from the list of cells to assign monsters.
      }
    }
  }
  
  
  /**
   * Overriding the toString output.
   * @return The formatted layout of the dungeon grid.
   */
  @Override
  public String toString() {
    // Using Java stream API to save code:
    return Arrays.stream(this.dungeonGrid.getGrid())
            .map(row -> " " + Arrays.stream(row).map(p -> p == null ? " " : p.toFormattedString())
                    .collect(Collectors.joining(" | "))).collect(Collectors.joining(
                    "\n------------------------------------------------"
                            + "--------------------------\n"));
  }
  
  //Validate all the inputs provided.
  private void validateGameInputs(int rows, int cols, int interconnectivity, boolean isWrapped,
                                  int percentOfTreasure, int percentOfMonsters, Players player)
          throws IllegalArgumentException {
    if (rows < 1 || cols < 1) {
      throw new IllegalArgumentException("Rows and columns cannot be 0 or negative.");
    } else if ((rows == 1 && cols < 6) || (rows < 6 && cols == 1) || (rows == 2 && cols < 3)) {
      throw new IllegalArgumentException("Invalid rows or columns. Not enough nodes in the graph."
              + " To meet the requirement of minimum distance to be at least 5 nodes.");
    }
    if (interconnectivity < 0) {
      throw new IllegalArgumentException("Interconnectivity cannot be negative.");
    }
    
    if (interconnectivity > 0 && isWrapped) {
      int maximumEdges = 2 * rows * cols;
      if (interconnectivity > maximumEdges) {
        throw new IllegalArgumentException(
                "Invalid degree of interconnectivity for the given row\" \n"
                        + "                + \" and column values for wrapping dungeon.");
      }
    }
    if (interconnectivity > 0 && ! isWrapped) {
      int maximumNumEdges = 2 * rows * cols - rows - cols;
      if (interconnectivity > maximumNumEdges - (rows * cols - 1)) {
        throw new IllegalArgumentException("Invalid degree of interconnectivity for the given row"
                + " and column values for non-wrapping dungeon.");
      }
    }
    if (percentOfTreasure < 0 || percentOfTreasure > 100) {
      throw new IllegalArgumentException(
              "The percent of treasure cannot be negative or more than" + " 100");
    }
    if (percentOfMonsters < 0 || percentOfMonsters > 100) {
      throw new IllegalArgumentException(
              "The percent of monsters cannot be negative or more than" + " 100");
    }
    if (player == null) {
      throw new IllegalArgumentException("The player cannot be null.");
    }
    
  }
  
  //Add a thief to a random cave
  private void setThieves(int percentOfThieves) {
    //Get all the caves
    List<Cell> caveCells = Arrays.stream(dungeonGrid.getGrid()).flatMap(Arrays::stream)
            .filter(s -> s.getType().equals(CellTypes.CAVE)).collect(Collectors.toList());
    
    if (percentOfThieves != 0) {
      //get the number of caves to assign pits (assigning by percentage of monsters)
      int numCavesWithThieves = (int) Math.ceil((caveCells.size() * percentOfThieves) / 100);
      
      //Remove start and end caves from this list
      caveCells.remove(startPosition);
      caveCells.remove(endPosition);
      
      thieves = new Thieves[numCavesWithThieves];
      for (int i = 0; i < numCavesWithThieves; i++) {
        int upperbound = caveCells.size() - 1;
        int randomIndex = 0;
        if (upperbound > 0) {
          randomIndex = random.getRandomInt(0, upperbound);
          Cell cave = caveCells.get(randomIndex);
          Thieves thief = new Thief(i);
          cave.setThief(thief);
          thief.setLocation(cave);
          thieves[i] = cave.getThief();
          caveCells.remove(cave);
        }
        
      }
    }
    
  }
  
  //Set treasure to caves in the dungeon.
  private void setPits(int percentOfPits) {
    int pitInt = 0;
    
    //Get all the caves
    List<Cell> caveCells = Arrays.stream(this.dungeonGrid.getGrid()).flatMap(Arrays::stream)
            .filter(s -> s.getType().equals(CellTypes.CAVE)).collect(Collectors.toList());
    if (percentOfPits != 0) {
      //get the number of caves to assign pits (assigning by percentage of monsters)
      int numCavesWithPits = (int) Math.ceil((caveCells.size() * percentOfPits) / 100);
      
      //Remove start and end caves from this list
      caveCells.remove(startPosition);
      caveCells.remove(endPosition);
      
      //Assign random caves as pit type
      for (int i = 0; i < numCavesWithPits; i++) {
        int randomIndex = random.getRandomInt(0, caveCells.size() - 1);
        Cell cave = caveCells.get(randomIndex);
        //Assign this cave as pit type
        cave.setType(CellTypes.PIT);
        for (Cell neighbor : cave.getNeighbors().values()) {
          neighbor.setAsPitNeighbor(true);
        }
        caveCells.remove(cave);
      }
    }
    
  }
  
  
}
