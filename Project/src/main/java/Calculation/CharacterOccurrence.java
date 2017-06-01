package Calculation;

import org.springframework.util.StringUtils;


public class CharacterOccurrence {

	private double m_factor = 0;
	
	public CharacterOccurrence(double factor){
		m_factor = factor;
	}
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
		
		return false;
		
	}
}
