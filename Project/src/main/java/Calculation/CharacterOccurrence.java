package Calculation;

import org.springframework.util.StringUtils;

/**
 * checking the occurence of letters in a String 
 * @author Oron
 *
 */
public class CharacterOccurrence {

	private double m_factor = 0;
	/***
	 * constructor
	 * @param factor - factor for possible occurences
	 */
	public CharacterOccurrence(double factor){
		m_factor = factor;
	}
	/***
	 * calculate occurence
	 * @param protein - the string to be checked
	 * @return - true in case of occurence less than factor , false otherwise
	 */
	public boolean Calculate(String protein){
		int count = 0 ;
		char letter;
		String proteinStr = protein.toUpperCase();
		for(int i=0;i<28;i++)
		{
			if(i == 26 )
				letter = '_';
			else if(i == 27)
				letter = '?';
			else 
				letter = (char)('A'+i);
			
			count= StringUtils.countOccurrencesOf(proteinStr,Character.toString(letter));
			
			if(count >= m_factor){
				return false;
			}
		}
		
		return true;
		
	}
}
