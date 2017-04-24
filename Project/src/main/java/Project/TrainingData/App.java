package Project.TrainingData;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;

import Table.TrainingData;


/***
 * Main class
 * @author oron
 *
 */
public class App 
{
	public static void main( String[] args )
    {
		ArrayList<Protein> proteinsDB;
		long startTime = System.currentTimeMillis();

		//***************  init DB *****************************//
		 
		proteinsDB = (ArrayList<Protein>) FileParser.ReadWholePDB();
    	
		//***************  save DB ******************************//
		
    	JSONhelper.WriteObject(proteinsDB); // writing the pdb as json file
    	
		//***************  read DB ******************************//
    	
	    proteinsDB = JSONhelper.ReadJsonFile(); //reading the pdb from json files
		
	    System.out.println("Total Time: " + (System.currentTimeMillis()-startTime)/(60*1000));
	      
	      //checking the match between aminoacid string to its structure properties
	      //checkAmino(proteinsDB);
	    
		//***************  training ******************************//

	    TrainingData trainingData = new TrainingData(proteinsDB);
	    
		//***************  Hamming ******************************//
	    	

    }
	
	/***
	 * This method checks the number of classifications there are among the proteins data
	 * @param proteins - arrayList of proteins
	 * @return number of classifications available 
	 */
	public static int CheckNumberOfClassifications(ArrayList<Protein> proteins)
	{
		ArrayList<String> classes = new ArrayList<String>();
    	
    	for(Protein p : proteins)
    	{
    		if(!classes.contains(p.getClassification()))
    			classes.add(p.getClassification());
    	}
    	
    	return classes.size();
    /*	System.out.println(classes.size()+" classes:");
    	for(String s : classes)
    	{
    		System.out.println(s);
    	} */
	}
    /***
     * Showing percentage animation through the console
     * @param status - message to be shown 
     * @param currentPosition - current position from 0 to total 
     * @param totalNumber - the amount of data to be processed
     */
	public  static void animate(String status,double currentPosition,int totalNumber )
	{
			
			DecimalFormat df = new DecimalFormat("###.#");
			double percent = (currentPosition/totalNumber)*100;
			if(currentPosition % (totalNumber/100) == 0 || currentPosition == totalNumber)
				System.out.println(status + df.format(percent) + "% ");

		   
	}
	/***
	 * This method confirms that the amino acids String is identical to the structural list
	 * @param aminoAcids - String of amino acids
	 * @param struct - ArrayList of the structural data 
	 * @return true for identical and false otherwise
	 */
    public static boolean check(String aminoAcids , ArrayList<Structure> struct)
    {
    	
    	
    	for(Character c : aminoAcids.toCharArray())
    	{
    		if(!c.equals(aaStringToChar(struct.get(0).getAminoAcid())))
    			return false;
    		
    		struct.remove(0);
    	}
		return true;
    	
    }
    
    /**
     * Convert 3 char amino acid representation to 1 char representation
     * @param AminoAcid - 3 char amino acid
     * @return - 1 char amino acid representation
     */
    public static char aaStringToChar(String AminoAcid)
    {
    	HashMap<String,Character> map = new HashMap<String,Character>();
    	map.put("ALA",'a');
    	map.put("ASX",'b');
    	map.put("CYS",'c');
    	map.put("ASP",'d');
    	map.put("GLU",'e');
    	map.put("PHE",'f');
    	map.put("GLY",'g');
    	map.put("HIS",'h');
    	map.put("ILE",'i');
    	map.put("XLE",'j');
    	map.put("LYS",'k');
    	map.put("LEU",'l');
    	map.put("MET",'m');
    	map.put("ASN",'n');
    	map.put("PYL",'o');
    	map.put("PRO",'p');
    	map.put("GLN",'q');
    	map.put("ARG",'r');
    	map.put("SER",'s');
    	map.put("THR",'t');
    	map.put("SEC",'u');
    	map.put("VAL",'v');
    	map.put("TRP",'w');
    	map.put("XAA",'x');
    	map.put("TYR",'y');
    	map.put("GLX",'z');
    	map.put("UNK",'?'); // for unknown amino acid
    	map.put("XXX",'_'); // for broken links in the protein
    	
    	return map.get(AminoAcid);
    }
    
    
    /***
     * Verifying the amino acid to its structure
     * @param proteinsDB
     */
    public static void checkAmino(ArrayList<Protein> proteinsDB)
    {
    	int diff = 0 , notEqual = 0 ;
    	for(Protein p  : proteinsDB)
    	{
    		try
    		{
    		if(p.getAstralID().equals("d1ux8a_"))
    			System.out.print("dsg");
    		if(!App.check(p.getAminoAcids(), new ArrayList<Structure>(p.structure)))
    			System.out.println(p.getAstralID());   // if an aminoacid is different than the structural data
    			diff++;
    		}
    			catch(Exception e ) // if the aminoacid string is mismatch to the structural data
    		{
    				System.out.println(p.getAstralID());
    				System.out.println(p.getAminoAcids().length());
    				System.out.println(p.getStructure().size());
    				System.out.println();
    				notEqual++;
    		}
    		
    	}
    	System.out.println("Differenct in an amino acid: " +diff);
		System.out.println("Mismatch in size " +notEqual);
    }
}


