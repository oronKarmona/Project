package Project.TrainingData;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;

import Table.TrainingData;



public class App 
{
	public static HashMap<Character,String> map ; 
	public static void main( String[] args )
    {
		ArrayList<Protein> proteinsDB;
		long startTime = System.currentTimeMillis();

		//***************  init DB *****************************//
		 
//		proteinsDB = (ArrayList<Protein>) FileParser.ReadWholePDB();
//    	
		//***************  save DB ******************************//
		
    	//JSONhelper.WriteObject(proteinsDB); // writing the pdb as json file
    	
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
	public static void animate(String status,double currentPosition,int totalNumber )
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
    	map = new HashMap<Character,String>();
    	map.put('a', "ALA");
    	map.put('r', "ARG");
    	map.put('n', "ASN");
    	map.put('d', "ASP");
    	map.put('c', "CYS");
    	map.put('e', "GLU");
    	map.put('q', "GLN");
    	map.put('g', "GLY");
    	map.put('h', "HIS");
    	map.put('i',"ILE");
    	map.put('l',"LEU");
    	map.put('k',"LYS");
    	map.put('m',"MET");
    	map.put('f',"PHE");
    	map.put('p',"PRO");
    	map.put('s',"SER");
    	map.put('t',"THR");
    	map.put('w',"TRP");
    	map.put('y',"TYR");
    	map.put('v',"VAL");
    	
    	for(Character c : aminoAcids.toCharArray())
    	{
    		if(!map.get(c).equals(struct.get(0).getAminoAcid()))
    			return false;
    		
    		struct.remove(0);
    	}
		return true;
    	
    }
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


