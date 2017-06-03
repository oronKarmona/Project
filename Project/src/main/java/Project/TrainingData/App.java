package Project.TrainingData;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import BFS.BFS;
import Calculation.MeanRMSD;
import DB.ElasticSearchService;
import Helpers.FileParser;
import Helpers.JSONhelper;
import Helpers.NeighborsHelper;
import Helpers.PCNpdbParser;
import Helpers.UpdateHamming;
import PCN.PCNCorrector;
import PCN.ReadPCNFile;
import PCN.WritePCNtoDB;
import ParallelBFS.ParallelBFS;
import Table.TrainingData;
import testing.CreateSequenceFile;

//WritePCNtoDB pcn2db = new WritePCNtoDB("1//PDB_Proteom_Map2~",61,"pcn","data");
//MeanRMSD m = new MeanRMSD("proteins","trainingdata",8);
// TrainingData trainingData = new TrainingData(proteinsDB);
/***
 * Main class
 * @author oron
 *
 */
public class App 
{
	
	public static void main( String[] args )
    {
		ArrayList<Protein> knownStructrePDB = new ArrayList<Protein>(), uknownStructurePDB;
		long startTime = System.currentTimeMillis();
		
//		ElasticSearchService es = new ElasticSearchService("protein","known_structure");
		knownStructrePDB = App.Read_knowStructuralPDB_files("Output" , 20 );
		uknownStructurePDB =  App.Read_unknown_structure_PDB("1//ProteomDB");
//		
//		PCNCorrector p = new PCNCorrector();
	    
//		BFS bfs = new BFS(3,uknownStructurePDB , knownStructrePDB, 20/3);
//		bfs.runBFS();
		
		ParallelBFS bfs = new ParallelBFS(4,uknownStructurePDB , knownStructrePDB, 20/3 , "pcn" , "data");
		bfs.InitiateBFS(0);
	   
		System.out.println("Total Time: " + (System.currentTimeMillis()-startTime)/(60*1000));
    }
	
	
	
	public static Protein fromMapToProtein(Map<String, Object> map)
	{
		Protein p = new Protein();
		p.setProteinIndex((Integer)map.get("ProteinIndex"));
		p.setAstralID((String)map.get("astralID"));
		p.setType("type");
		p.setAminoAcids((String) map.get("aminoAcids"));
		p.setFragment_count((Integer)map.get("fragment_count"));
		
		
		
		return p ;
	}
	
	
	
	public static ArrayList<Protein> read_Whole_ASTRAL_and_structural_data()
	{
		ArrayList<Protein> proteinsDB = (ArrayList<Protein>) FileParser.ReadWholePDB();
		
		return proteinsDB;
	}
	
	public static void Write_PDBtoJSON_files(ArrayList<Protein> proteinsDB , int numberOfFiles , String FileName)
	{
		//(proteinsDB,20,"Output"); // default
		JSONhelper.WriteObject(proteinsDB,numberOfFiles,FileName); // writing the pdb as json file
	}
	
	public static ArrayList<Protein> Read_knowStructuralPDB_files(String fileName , int amount )
	{
		// ("Output" , 20 ) default
		ArrayList<Protein>  proteinsDB = JSONhelper.ReadJsonFile(fileName , amount); //reading the pdb from json files
		return proteinsDB;
	}
	
	public static ArrayList<Protein> Read_unknown_structure_PDB(String FileName)
	{
		ArrayList<Protein> proteinDB = null;
		//("1//ProteomDB")
		   try {
			   
			  proteinDB  = PCNpdbParser.ParseFile(FileName);
			   
			} catch (Exception e) {
				e.printStackTrace();
			}
		   
		   return proteinDB;
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
    		if(!App.check(p.getAminoAcids(), new ArrayList<Structure>(p.getStructure())))
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


