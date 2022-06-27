import org.junit.Before;
import org.junit.Test;

import random.MockRandomGenerator;
import random.RandomGenerator;
import random.TrueRandomGenerator;

import static org.junit.Assert.assertTrue;

/**
 * Test for checking the random class methods. Testing if the values output are truly random in case
 * of True Random Generator class, and will give the same sequence of ints in case of Mock Random
 * Generator class.
 */
public class RandomGeneratorTest {
  
  private RandomGenerator mockRandom;
  private RandomGenerator trueRandom;
  
  @Before
  public void setUp() throws Exception {
    mockRandom = new MockRandomGenerator(9000);
    trueRandom = new TrueRandomGenerator();
  }
  
  /**
   * Test get random integer with lower and upper bound.
   */
  @Test
  public void getRandomInt() {
    int num1 = trueRandom.getRandomInt(1, 5);
    assertTrue(1 <= num1 && num1 <= 5);
    
    num1 = trueRandom.getRandomInt(1, 5);
    assertTrue(1 <= num1 && num1 <= 5);
    
    int num2 = trueRandom.getRandomInt(5, 10);
    assertTrue(5 <= num2 && num2 <= 10);
    
    num2 = trueRandom.getRandomInt(5, 10);
    assertTrue(5 <= num2 && num2 <= 10);
    
    num2 = trueRandom.getRandomInt(60, 100);
    assertTrue(60 <= num2 && num2 <= 100);
    
    
    num2 = trueRandom.getRandomInt(60, 100);
    assertTrue(60 <= num2 && num2 <= 100);
    
    num2 = trueRandom.getRandomInt(70, 80);
    assertTrue(70 <= num2 && num2 <= 80);
    
    num2 = trueRandom.getRandomInt(70, 80);
    assertTrue(70 <= num2 && num2 <= 80);
  }
  
  
  /**
   * Test mock ran.
   */
  @Test
  public void mockRandomTest() {
    int num;
    num = mockRandom.getRandomInt(0, 10);
    //System.out.println((num));
    assertTrue(0 <= num && num <= 10);
    
    num = mockRandom.getRandomInt(0, 15);
    //System.out.println((num));
    assertTrue(0 <= num && num <= 15);
    
    num = mockRandom.getRandomInt(0, 20);
    //System.out.println((num));
    assertTrue(0 <= num && num <= 20);
    
    
  }
}