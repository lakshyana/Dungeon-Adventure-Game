package random;

import java.util.Random;

/**
 * Class to generate random values with seed. Represents a random generator to be used for various
 * operations in the battle that need a randomly assigned value. For example, while assigning player
 * abilities, generating equipment bag with gears, weapons, etc. The mock random generator is a
 * pseudo random generator that will always produce the same sequence of integers for a particular
 * seed value.
 */
public class MockRandomGenerator implements RandomGenerator {
  private final Random random;
  
  /**
   * Constructor for mock random generator. Takes in a seed integer to return the same set of
   * integers in the program to facilitate testing.
   */
  public MockRandomGenerator(int seed) {
    this.random = new Random(seed);
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
   * @param seed the seed value.
   */
  @Override
  public void setSeed(long seed) {
    random.setSeed(seed);
  }
  
  
}
