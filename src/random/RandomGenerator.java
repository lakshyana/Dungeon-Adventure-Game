package random;

/**
 * This interface represents a random generator to be used for various operations in the game that
 * need a randomly assigned value. To make the values truly random, only one instance of
 * RandomGenerator class will be shared throughout the game.
 */
public interface RandomGenerator {
  
  /**
   * Get a random integer value within a lower bound (inclusive) and an upperbound (inclusive).
   * @param lowerBound Lower bound integer.
   * @param upperBound Upper bound integer.
   * @return a randomly generated integer.
   */
  int getRandomInt(int lowerBound, int upperBound);
  
  /**
   * Set the seed to the random variable.
   * @param seed the seed value.
   */
  void setSeed(long seed);
  
}

// https://stackoverflow.com/questions/35880692/random-numbers-g
// enerated-in-separate-functions-arent-so-random
// Reference: https://cs.stanford.edu/people/eroberts/jtf/
// javadoc/complete/acm/util/RandomGenerator.html