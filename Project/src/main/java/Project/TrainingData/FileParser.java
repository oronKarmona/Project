package Project.TrainingData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;

import ProGAL.geom3d.Point;

/***
 * This class contains 2 static methods which will be used to parse the ASTRAL database files
 * one method is used for the properties file 
 * second method is used for the structural file
 * @author Oron
 *
 */
public class FileParser {
	
	/***
	 * This enum will be used as term of simplicity in the file's parser
	 * @author Oron
	 *
	 */
	 enum ProteinEnum {
			astralId , Classification , type , matched , name , TaxId , aminoAcids
		}
	
	/***
	 * Reading specified file with data of the protein structure
	 * @return structure details in ArrayList<Structure> type
	 */
	@SuppressWarnings("resource")
	public static ArrayList<Structure> ReadStructureDateFile(String ProteinPath)
	{
		 Scanner sc2 = null;
		 ArrayList<Structure> structure = new ArrayList<Structure>();
		
		 Structure temp  = new Structure();
		 double[] t = new double[3];
		 int ctr = -1 ;
		 
		    try {
		        sc2 = new Scanner(new File("C:\\pdbstyle-2.06\\"+ProteinPath));
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
		            		ctr++;
		            		break;
		            		
		            	case 7: // coordinate y
		            		
		            		t[1] = Double.parseDouble(s);
		            		ctr++;
		            		break;
		            		
		            	case 8: // coordinate z
		            		
		            		t[2] = Double.parseDouble(s);
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
		            	{
		            		temp.setP(new Point(t));
			            	structure.add(new Structure(temp));
		            	}
            			ctr = 0 ;
            			ctr++;
            		}
		            
		            
		            if(ctr == -1) // if its nothing to use than skip line
		            	break;
    	
		        }
		      
		    }
		    
		    return structure;
	}
	
	
	/***
	 * Reads the Astral Data from the file
	 * By reading the data base it will compose an arrayList of proteins
	 * @return proteins from the database as ArrayList
	 * @throws IOException 
	 */
	public static Protein ReadAstralDB() throws IOException
	{
			Protein p = null;
			ProteinEnum state = null;
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader( new InputStreamReader(new FileInputStream("Astral.txt"),Charset.forName("UTF-8")));
			int c;
			String buffer = "" ; 
			while((c = reader.read()) != -1) 
			{
			  char character = (char) c;
			  System.out.println(buffer);
			  if(character == '>')
			  {
				  // if this is the end of the amino acids and start of new protein
				  if(state == ProteinEnum.valueOf("aminoAcids"))
				  {
					  p.setAminoAcids(buffer.substring(0,buffer.length()-1));
					  buffer = "";
					  
					  //add to arrayList
					  
					  return p ; // return for check purpose
				  }
					  
				  p = new Protein();
				  state = ProteinEnum.valueOf("astralId");
			  }
			  else 
			  {
				  buffer += character;
				  switch(state)
				  {
				  		case astralId:
				  			if(character == ' ')
				  			{
				  				p.setAstralID(buffer.substring(0,buffer.length()-1));
				  				buffer = "";
				  				state = ProteinEnum.valueOf("Classification");
				  			}
				  			break;
				  		
				  		case Classification:
				  			if(character == ' ')
				  			{
				  				p.setClassification(buffer.substring(0,buffer.length()-1));
				  				buffer = "";
				  				state = ProteinEnum.valueOf("type");
				  			}
				  			break;
				  		
				  		case type:
				  			if(character == ' ')
				  			{
				  				p.setType(buffer.substring(0,buffer.length()-1));
				  				buffer = "";
				  				state = ProteinEnum.valueOf("matched");
				  			}
				  			break;
				  		
				  		case matched:
				  			if(character == '{')
				  			{
				  				p.setMatched(buffer.substring(0,buffer.length()-2));
				  				buffer = "";
				  				state = ProteinEnum.valueOf("name");
				  			}
				  			break;
				  			
				  		case name:
				  			if(character == '[')
				  			{
				  				p.setName(buffer.substring(0,buffer.length()-2));
				  				buffer = "";
				  				state = ProteinEnum.valueOf("TaxId");
				  			}
				  			break;
				  			
				  		case TaxId:
				  			if(character == ' ')
				  				buffer = "";
				  			else if(character == '}')
				  			{
				  				p.setTaxId(buffer.substring(0,buffer.length()-2));
				  				buffer = "";
				  				state = ProteinEnum.valueOf("aminoAcids");
				  			}
				  		
				  			
						default:
							break;
				  }
				 
			  }
			  
			}
			
			
			return null;
			
	
	}

}
