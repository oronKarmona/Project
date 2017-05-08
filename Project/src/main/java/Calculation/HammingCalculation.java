package Calculation;
/***
 * 
 * @author ליטף
 * HammingCalculation
 * initiate the wanted threshold
 * Calculate mathid: return true if the fragments passes the predetermined threshold
 * otherwise - return false
 * 
 */

public class HammingCalculation implements Cloneable
{

	/***
	 * Threshold for the Hamming calculation 
	 */
	private double m_threshold;
	/***
	 * The error value for the calculation
	 */
	private static int m_error; // (1 - threshold) in percent representation 

	/***
	 * constructor
	 * @param threshold - defined by user
	 * @throws Exception
	 */
	public HammingCalculation(double threshold) throws Exception{
		
		if(threshold<0)
		{
			throw new Exception(String.format("Unable to proceed, threshold is incorrect: %f",threshold));
		}
		m_threshold = threshold;
	    m_error = (int) Math.round(((100 - m_threshold)/100)*20); // top error 

	}
	/***
	 * Calculating the hamming distance between 2 different protein strings
	 * @param protein1 - String 1  
	 * @param protein2 - String 2 
	 * @return - if the hamming distance is below threshold return false, true otherwise
	 */
	public boolean Calculate(String protein1 ,String protein2){
		
	    validateInputs(protein1, protein2);

	    int hammingDistance = 0;
	    int stringLength = protein1.length();

	    for (int i = 0; i < stringLength; i++) {
	      if (protein1.charAt(i) != protein2.charAt(i)) {
	        hammingDistance++;
	      }
	    }
	    return checkThreshold(hammingDistance);
	}
	
	/***
	 * Checking the length of the Strings
	 * @param s1 - String 1 
	 * @param s2 - String 2 
	 */
	private void validateInputs(String s1, String s2) {
	    if (s1.length() != s2.length()) {
	      throw new IllegalArgumentException();
	    }
	}
	/***
	 * Checking the validation of the threshold
	 * @param hammingDistance - result of the calculation
	 * @return - true if legal and false otherwise
	 */
	private boolean checkThreshold(int hammingDistance) {
		if(hammingDistance >= m_error){
			return false;
		}
		return true;
	}
	
	/***
	 * Cloning the object
	 */
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
