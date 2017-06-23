package Project.TrainingData;

import static org.junit.Assert.*;

import org.junit.Test;

import Calculation.HammingCalculation;
/***
 * This class will check edge cases of the hamming distance calculation
 * @author Oron
 *
 */
public class HammingCalculationTest {

	HammingCalculation hd = null;
	
	/***
	 * Testing the hamming distance between 2 equalt strings
	 * Expected result is true
	 */
	@Test
	public void GoodDistanceCheckTest()
	{
		String string1 = "abcdefghijklmnopq";
		String string2 = "abcdefghijklmnopq";
		
		try {
			hd = new HammingCalculation(60);
			assertTrue(hd.Calculate(string1, string2));
		} catch (Exception e) {
			
			assertTrue(false);
		}
	}
	
	/***
	 * Testing the hamming distance between 2 equalt strings
	 * Expected result is true
	 */
	@Test
	public void BadDistanceCheckTest()
	{
		String string1 = "abcdefghijklmnopq";
		String string2 = "ppppppppppppppppp";
		
		try {
			hd = new HammingCalculation(60);
			assertFalse(hd.Calculate(string1, string2));
		} catch (Exception e) {
			
			assertTrue(false);
		}
	}
	
	
	/***
	 * Tests the case of negative threshold
	 */
	@Test
	public void TestBadThreshold()
	{
		try{
			hd = new HammingCalculation(-1);
			assertTrue(false);
		} catch(Exception e )
		{
			assertTrue(true);
		}
	}

}
