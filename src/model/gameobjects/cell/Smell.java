package model.gameobjects.cell;

/**
 * This class represents the smell that may exist in a cell due to a smelly monster like Otyugh. A
 * cell close to a monster's location will have a pungent smell. The intensity of the smell will be
 * determined based on the proximity of the cell to the monster.
 */
public enum Smell {
  NONE, PUNGENT
}
