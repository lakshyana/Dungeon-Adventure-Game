package random;

import java.util.Random;

/**
 * Class to generate random values without seed. Represents a random generator to be used for
 * various operations in the game that need a randomly assigned value. To make the values truly
 * random, only one instance of RandomGenerator class will be shared throughout the game.
 */
public class TrueRandomGenerator implements RandomGenerator {
  
  private final Random random;
  
  /**
   * Constructor for true random generator.
   */
  public TrueRandomGenerator() {
    this.random = new Random();
  }
  
  /**
   * Get a random integer value within a lower bound (inclusive) and an upperbound (inclusive).
   * @param lowerBound Lower bound integer.
   * @param upperBound Upper bound integer.
   * @return a randomly generated integer.
   */
  @Override
  public int getRandomInt(int lowerBound, int upperBound) {
    int currentValue;
    currentValue = lowerBound + random.nextInt(upperBound + 1 - lowerBound);
    return currentValue;
  }
  
  /**
   * Set the seed to the random variable.
   */
  @Override
  public void setSeed(long seed) {
    random.setSeed(seed);
  }
  
}
