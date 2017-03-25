package Project.TrainingData;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class StructReader {
	
	
	
	
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
		    
		    while (sc2.hasNextLine()) {
		            Scanner s2 = new Scanner(sc2.nextLine());
		        while (s2.hasNext()) {
		            String s = s2.next();
		            System.out.println(s);
		            
		            switch(ctr)
		            {
		            	case 1:
		            		temp.setIndex(Integer.parseInt(s));
		            		ctr++;
		            		break;
		            	
		            	case 2:
		            		if(!s.equals("CA"))
		            		{
		            			ctr = -1;
		            			break;
		            		}
		            		temp.setType(s);
		            		ctr++;
		            		break;
		            		
		            	case 3:
		            		temp.setAminoAcid(s);
		            		ctr++;
		            		break;
		            	case 6:
		 
		            		t[0] = Double.parseDouble(s);
		            		temp.setCoordinates(t);
		            		ctr++;
		            		break;
		            		
		            	case 7:
		            		t = temp.getCoordinates();
		            		t[1] = Double.parseDouble(s);
		            		temp.setCoordinates(t);
		            		ctr++;
		            		break;
		            		
		            	case 8:
		            		t = temp.getCoordinates();
		            		t[2] = Double.parseDouble(s);
		            		temp.setCoordinates(t);
		            		ctr++;
		            		break;	
		            		
		            	case -1:
		            		break;
		            		
		            	default:
		            		ctr++;
		            		break;
		            	
		            	
		            }
		         
		            
		            if(s.equals("ATOM") || s.equals("TER"))
            		{
		            	if(ctr == 13)
			            {
			            	structure.add(new Structure(temp));
			            }
		            	
            			ctr = 0 ;
            			ctr++;
            		}
		            
		            
		            
		            if(ctr == -1)
		            	break;
    	
		        }
		      
		    }
		    
		    return structure;
	}

}
