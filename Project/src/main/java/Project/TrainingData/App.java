package Project.TrainingData;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class App 
{
	public static HashMap<Character,String> map ; 
	public static void main( String[] args )
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
    			/***
    			 *הקובץ שמופיע בבדיקה כאן הוא קובץ מאוד מסריח . הוא המקרה הכי קיצוני שכל הקואורדינטות צמודות אחת לשנייה
    			 */		
    			if(protein.getAstralID().equals("d1uf2i1"))
    			{
        			structure = FileParser.ReadStructureDateFile(protein.getfolderIndex()+"\\"+protein.getFileName());
        			
    			}
    			else
    				structure = FileParser.ReadStructureDateFile(protein.getfolderIndex()+"\\"+protein.getFileName());

			}
			catch(Exception e){
				structure = null;
				System.out.println(protein.astralID);
   			}
    		if(structure == null){
    			//toRemove.add(proteinsDB.get(proteinsDB.indexOf(protein)).ProteinIndex);
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
}


