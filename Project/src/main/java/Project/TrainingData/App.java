package Project.TrainingData;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Calculation.MultipleLinearRegression;
import Calculation.PolynomialRegression;
import DB.ElasticSearchService;
import GUI.Main;
import Helpers.StructureParser;
import Helpers.JSONhelper;
import Helpers.LinearRegressionUpdateHelper;
import Helpers.PCNpdbParser;
import Helpers.PajekFormatHelper;
import Helpers.ReadXYregression;
import Jama.Matrix;
import PCN.NodePCN;
import PCN.WritePCNtoDB;

import Table.TrainingData;
import ParallelBFS.CreateClusters;
import ToRemove.BFS;

//WritePCNtoDB pcn2db = new WritePCNtoDB("1//PDB_Proteom_Map2~",61,"pcn","data");
//MeanRMSD m = new MeanRMSD("proteins","trainingdata",8);
// TrainingData trainingData = new TrainingData(proteinsDB);
/*
 * Main class
 * @author oron
 *
 */
public class App 
{
	private static Matrix beta;
	
	public static void main( String[] args )
    {
		ArrayList<Protein> uknownStructurePDB,knownStructrePDB;
		long startTime = System.currentTimeMillis();


//		 Main main = new Main();
//	      main.setBounds(200, 100, 700, 550);
//	      main.setVisible(true);
//	   

		knownStructrePDB = App.Read_knowStructuralPDB_files("Output" , 20 );

//		writeProteinsToDB("proteins","known_structure",knownStructrePDB);

		uknownStructurePDB =  App.Read_unknown_structure_PDB("1//ProteomDB");
		LinearRegressionUpdateHelper helper = new LinearRegressionUpdateHelper("cluster", 1, 4, 60, uknownStructurePDB, knownStructrePDB);
//		
//	      TrainingData training = new TrainingData(knownStructrePDB);
//		//LinearSystemSolution xy = new LinearSystemSolution();
//	
//		
//        double[] x = { 10, 20, 40, 80, 160, 200 };
//        double[] y = { 100, 350, 1500, 6700, 20160, 40000 };
//        PolynomialRegression regression = new PolynomialRegression(x, y, 3);
//        beta = regression.getBeta();
		

	
//		for(int i = 0 ; i <= 1000 ; i++)
//		{
//			System.out.println("Cluster " + i);
//			CreateClusters bfs = new CreateClusters(3,uknownStructurePDB , knownStructrePDB, 20/3 , "pcn" , "data",
//										"cluster",i+"",95);
//				bfs.startBFS(i);
////				bfs.flushBulk();
////				PajekFormatHelper pf = new PajekFormatHelper("cluster", i+"");

//				
//		}
		
		
		
		System.out.println("Total Time: " + (System.currentTimeMillis()-startTime)/(60*1000));
    }
	
	public void calculateBetaLinearRegression()
	{
		try {
			LinearSystemSolution xy = new LinearSystemSolution();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		ReadXYregression rxy = new ReadXYregression("xyValues");
		System.out.println("Calculating regression...");
		MultipleLinearRegression regression = new MultipleLinearRegression(rxy.getMatrixX() , rxy.getRMSD());
        beta = regression.getBeta();
        System.out.println("Saving to file...");
		JSONhelper.writeCoefficientsRegression(beta, "regression_coefficients");
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
	
	
	public static void writeProteinsToDB(String index , String type , ArrayList<Protein> proteinDB)
	{
		ElasticSearchService es = new ElasticSearchService(index , type );
		for(Protein p  : proteinDB)
			es.add(p);
		es.clientClose();
	}
	
	public static ArrayList<Protein> read_Whole_ASTRAL_and_structural_data()
	{
		ArrayList<Protein> proteinsDB = (ArrayList<Protein>) StructureParser.ReadWholePDB();
		
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
	/*
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
    /*
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
	/*
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
    
    
    /*
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
