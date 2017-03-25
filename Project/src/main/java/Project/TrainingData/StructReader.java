package Project.TrainingData;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class StructReader {
	
	
	
	/***
	 * Reading specified file of with data of the protein structure
	 * @return structure details in ArrayList<Structure> type
	 */
	@SuppressWarnings("resource")
	public static ArrayList<Structure> ReadFile()
	{
		 Scanner sc2 = null;
		
		 ArrayList<Structure> structure = new ArrayList<Structure>();
		 
		 Structure temp  = new Structure();
		 double[] t = temp.getCoordinates();
		 int ctr = -1 ;
		 
		    try {
		        sc2 = new Scanner(new File("d10gsa1.ent"));
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();  
		    }
		    //reading line
		    while (sc2.hasNextLine()) {
		            Scanner s2 = new Scanner(sc2.nextLine());
		            //reading word in line
		        while (s2.hasNext()) {
		            String s = s2.next();
		            System.out.println(s);
		            
		            switch(ctr)
		            {
		            	
		            	case 1: // atom index 
		            		temp.setIndex(Integer.parseInt(s));
		            		ctr++;
		            		break;
		            	
		            	case 2:  // atom type
		            		if(!s.equals("CA"))
		            		{
		            			ctr = -1;
		            			break;
		            		}
		            		temp.setType(s);
		            		ctr++;
		            		break;
		            		
		            	case 3: // Amino acid related to
		            		temp.setAminoAcid(s);
		            		ctr++;
		            		break;
		            		
		            	case 6: // coordinate x
		 
		            		t[0] = Double.parseDouble(s);
		            		temp.setCoordinates(t);
		            		ctr++;
		            		break;
		            		
		            	case 7: // coordinate y
		            		t = temp.getCoordinates();
		            		t[1] = Double.parseDouble(s);
		            		temp.setCoordinates(t);
		            		ctr++;
		            		break;
		            		
		            	case 8: // coordinate z
		            		t = temp.getCoordinates();
		            		t[2] = Double.parseDouble(s);
		            		temp.setCoordinates(t);
		            		ctr++;
		            		break;	
		            		
		            	case -1: // nothing to use
		            		break;
		            		
		            	default: // not necessary 
		            		ctr++;
		            		break;
		            	
		            	
		            }
		         
		            // if the start of the line is ATOM or TER
		            if(s.equals("ATOM") || s.equals("TER"))
            		{
		            	if(ctr == 13) // if whole line has been read than we have the necessary data
			            	structure.add(new Structure(temp));
		            	
            			ctr = 0 ;
            			ctr++;
            		}
		            
		            
		            if(ctr == -1) // if its nothing to use than skip line
		            	break;
    	
		        }
		      
		    }
		    
		    return structure;
	}

}
