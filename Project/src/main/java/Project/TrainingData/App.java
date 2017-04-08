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
    	int ctr = 0 ;
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
				ctr ++;
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
    	//System.out.println("Number of Proteins to be removed : "+toRemove.size());
    	System.out.println("Number of Proteins to be removed : "+proteinToRemove.size());

    	int indexToBeRemoved = 0; // the index of the protein that needs to be removed
    	currentPosition = 0 ;
//    	for (Integer remove : toRemove) 
//    	{
//    		//status for console
//    		App.animate("Removing problematic proteins : ",currentPosition++,toRemove.size());
//    		
//    		//finding the protein by its index in the ArrayList
//    		//after finding the index of the marked protein its index in the arrayList is saved
//    		for(int i = 0 ; i < proteinsDB.size();i++)
//    			if(proteinsDB.get(i).ProteinIndex == remove)
//    				indexToBeRemoved = i ;
//    		
//    		//Removing the protein from the ArrayList by the found index from the 'for' loop
//    		proteinsDB.remove(indexToBeRemoved);
//		}
    	
    	for (Protein p : proteinToRemove) {
			
    		proteinsDB.remove(p);
		}
    	System.out.println("Proteins DB size after removing: "+proteinsDB.size());

    }
    
	public static void animate(String status,double currentPosition,int totalNumber )
	{
			
			DecimalFormat df = new DecimalFormat("###.#");
			double percent = (currentPosition/totalNumber)*100;
			if(currentPosition % (totalNumber/100) == 0 || currentPosition == totalNumber)
				System.out.println(status + df.format(percent) + "% ");

		   
	}
    public static boolean check(String s , ArrayList<Structure> struct)
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
    	
    	for(Character c : s.toCharArray())
    	{
    		if(!map.get(c).equals(struct.get(0).getAminoAcid()))
    			return false;
    		
    		struct.remove(0);
    	}
		return true;
    	
    }
}


