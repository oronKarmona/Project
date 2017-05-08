 package Project.TrainingData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;



import org.apache.commons.io.FileUtils;

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
	public static int proteinCount = 1;
	 enum ProteinEnum {
			astralId , Classification , type , matched , name , TaxId , aminoAcids
		}
	


		@SuppressWarnings("unchecked")
		/***
		 * Reading specified file with data of the protein structure
		 * @return structure details in ArrayList<Structure> type, returns null if the structure is not valid or not found
		 */
		public static ArrayList<Structure> ReadStructureDateFile(String ProteinPath)
		{	
			 ArrayList<Structure> structure = new ArrayList<Structure>();
			 Structure temp  = new Structure();
			 List<String> lines = null;
			 int residueNumber = -1 ;
			 File file = new File("C:\\pdbstyle-2.06\\"+ProteinPath);
	    	
			 if(!file.exists()){
	    		//file not found
	    		return null;
			 }
	    	
			 try {
				lines = FileUtils.readLines(file);
			 } catch (IOException e) {
				return null;
			 }
			
		    for (String line : lines) {
		        //reading line
				   if(line.contains("not valid"))
					   return null;
		    	if(line.contains("ATOM")){
		    		
		    		if(line.substring(13,17).contains("CA"))
		    		{
		    		temp.setIndex(Integer.parseInt(line.substring(6,11).replaceAll(" ","")));
		    		temp.setAminoAcid(line.substring(17,20).replaceAll(" "," "));
		    		temp.setP(new Point(Double.parseDouble(line.substring(30,38).replaceAll(" ","")),
			    						Double.parseDouble(line.substring(38,46).replaceAll(" ","")),
			    						Double.parseDouble(line.substring(46,54).replaceAll(" ",""))));
		    		temp.setResidueSequenceNumber(Integer.parseInt(line.substring(22, 26).replaceAll(" ", "")));
					    		
				    		if(residueNumber!= -1 && residueNumber + 1 < temp.getResidueSequenceNumber()) // if there is a missing link between the two residues
					   		{
					   			Structure missing = new Structure();
					   			missing.setAminoAcid("XXX");
					   			missing.setIndex(-1);
					   			missing.setP(new Point(0,0,0));
					   			missing.setResidueSequenceNumber(-1);
					   			structure.add(new Structure(missing));
					   		}
				    		
					   		if(residueNumber != temp.getResidueSequenceNumber()) // if there is no representation of this chain
					   		{
					    			residueNumber = temp.getResidueSequenceNumber();
					    			structure.add(new Structure(temp));
					    	}
					   		
					   		
			    		
		    		}
		    	}
		    	
			}
		    
		    //fill the missing coordinates of the missing links as average of the neighbors 
		    for(int i = 0 ; i < structure.size() ; i ++)
		    {
		    	if(structure.get(i).getAminoAcid().equals("XXX"))
		    	{
		    		structure.get(i).setP( new Point( 
		    											(structure.get(i-1).getP().x() + structure.get(i+1).getP().x() )/ 2, 
		    											(structure.get(i-1).getP().y() + structure.get(i+1).getP().y() )/ 2,
		    											(structure.get(i-1).getP().z() + structure.get(i+1).getP().z() )/ 2 ));
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
	public static List<Protein> ReadAstralDB() throws IOException
	{
	Protein protein = null;
	List<Protein> pr = new ArrayList<Protein>();
	ProteinEnum state = null;
	@SuppressWarnings("resource")
	BufferedReader reader = new BufferedReader( new InputStreamReader(new FileInputStream("Astral.txt"),Charset.forName("UTF-8")));
	int c;
	String buffer = "" ; 
	while((c = reader.read()) != -1) 
	{
	  char character = (char) c;
	  if(character == '>')
	  {
		  // if this is the end of the amino acids and start of new protein
		  if(state == ProteinEnum.valueOf("aminoAcids"))
		  {
			  protein.setAminoAcids(buffer.substring(0,buffer.length()-1));
			  buffer = "";
			  pr.add(protein);
		  }
			  
		  protein = new Protein();
		  state = ProteinEnum.astralId;
		  protein.setProteinIndex(proteinCount);
		  proteinCount++;
	  }
	  else 
	  {
		  buffer += character;
		  switch(state)
		  {
		  		case astralId:
		  			if(character == ' ')
		  			{
		  				protein.setAstralID(buffer.substring(0,buffer.length()-1));
		  				buffer = "";
		  				state = ProteinEnum.Classification;
		  			}
		  			break;
		  		
		  		case Classification:
		  			if(character == ' ')
		  			{
		  				protein.setClassification(buffer.substring(0,buffer.length()-1));
		  				buffer = "";
		  				state = ProteinEnum.type;
		  			}
		  			break;
		  		
		  		case type:
		  			if(character == ' ')
		  			{
		  				protein.setType(buffer.substring(0,buffer.length()-1));
		  				buffer = "";
		  				state = ProteinEnum.matched;
		  			}
		  			break;
		  		
		  		case matched:
		  			if(character == '{')
		  			{
		  				protein.setMatched(buffer.substring(0,buffer.length()-2));
		  				buffer = "";
		  				state = ProteinEnum.name;
		  			}
		  			break;
		  			
		  		case name:
		  			if(character == '[')
		  			{
		  				protein.setName(buffer.substring(0,buffer.length()-2));
		  				buffer = "";
		  				state = ProteinEnum.TaxId;
		  			}
		  			break;
		  			
		  		case TaxId:
		  			if(character == ' ')
		  				buffer = "";
		  			else if(character == '}')
		  			{
		  				protein.setTaxId(buffer.substring(0,buffer.length()-2));
		  				buffer = "";
		  				state = ProteinEnum.aminoAcids;
		  			}
		  		
		  			
				default:
					break;
		  }
		 
	  }
	  
	}
	
	
	return pr;
	
	
	}
	/***
	 * This methods reads the whole PDB file and attaches to each protein its structure
	 * @return PDB in form of ArrayList<Protein>
	 */
	public static List<Protein> ReadWholePDB()
	{
		List<Protein> proteinsDB = null;
    	System.out.println("Initialising ProteinDB...");
    	try {
    		proteinsDB = FileParser.ReadAstralDB();
    	}
    	catch (IOException e)
    	{
			e.printStackTrace();
    	}
    	System.out.println("Initial ProteinDB size: "+proteinsDB.size());
    	//List<Integer> toRemove = new ArrayList<Integer>();
    	List<Protein> proteinToRemove = new ArrayList<Protein>();

    	ArrayList<Structure> structure;
    	//d4f4oj_
    	
    	double currentPosition = 0 ; 
    	System.out.println("Reading structure data...");
    
    	for(Protein protein : proteinsDB)
    	{
    		//status for console
    		App.animate("Reading proteins structure data : ",currentPosition++,proteinsDB.size());
   
    		try{
    				
    				structure = FileParser.ReadStructureDateFile(protein.getfolderIndex()+"\\"+protein.getFileName());
    				String aa = "";
    				if(structure != null)
    				{
    						
		    				//create the amino acid chain from the PDB file
    						String t = "";
    						try{
		    				for(Structure s : structure)
		    				{
		    					t = s.getAminoAcid();
		    					aa += App.aaStringToChar(t);
		    				}
    						}catch(Exception e )
    						{
    							System.out.println(protein.getAstralID());
    							System.out.println(t);
    						}
		    				
//		    				System.out.println(aa.equals(protein.getAminoAcids()));
		    				protein.setAminoAcids(aa);
//		    				if(!App.check(protein.getAminoAcids(), new ArrayList<Structure>(structure)))
//		    					System.out.println(protein.getAstralID());
		    				
		    				
    				}
			}
			catch(Exception e){
				e.printStackTrace();
				structure = null;
				System.out.println(protein.getAstralID());
   			}
    		if(structure == null){
    			
    			proteinToRemove.add(protein);
    		}
    		else{
    			//initiate protein structure
    			protein.setStructure(structure);

    		//protein.DivisionToFragments();
    		}
    	}
    	//if there is any problem
    	System.out.println("Number of Proteins to be removed : "+proteinToRemove.size());
    	
    	currentPosition = 0 ;
    	for (Protein p : proteinToRemove) {
    		//status for console
    		App.animate("Removing problematic proteins : ",currentPosition++,proteinToRemove.size());
    		proteinsDB.remove(p);
		}

    	System.out.println("Proteins DB size after removing: "+proteinsDB.size());
    	
    	
    	return proteinsDB;
	}

}
