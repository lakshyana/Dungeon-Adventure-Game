package model.gameobjects.actors;

/**
 * This class represents the status of a player or a monster in a game. A player can be alive, dead,
 * or have won the game by reaching the destination alive, and a monster may be alive, wounded, or
 * dead.
 */
public enum Status {
  ALIVE, WOUNDED, DEAD, WON
}
