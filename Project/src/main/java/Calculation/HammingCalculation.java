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

	private double m_threshold;
	private static int m_error;

	public HammingCalculation(double threshold) throws Exception{
		
		if(threshold<0)
		{
			throw new Exception(String.format("Unable to proceed, threshold is incorrect: %f",threshold));
		}
		m_threshold = threshold;
	    m_error = (int) Math.round((m_threshold/100)*20);

	}
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
	
	private void validateInputs(String s1, String s2) {
	    if (s1.length() != s2.length()) {
	      throw new IllegalArgumentException();
	    }
	}
	private boolean checkThreshold(int hammingDistance) {
		if(hammingDistance > m_error){
			return false;
		}
		return true;
	}
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
