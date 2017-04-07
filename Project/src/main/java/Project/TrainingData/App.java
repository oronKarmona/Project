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
    	try {
    		proteinsDB = FileParser.ReadAstralDB();
    	}
    	catch (IOException e)
    	{
			e.printStackTrace();
    	}
    	int ctr = 0 ;
    	System.out.println(proteinsDB.size());
    	List<Integer> toRemove = new ArrayList<Integer>();
    	ArrayList<Structure> structure;
    	//d4f4oj_
    	
    	double currentPosition = 0 ; 
    	System.out.println("Reading structure data...");
    	for(Protein protein : proteinsDB)
    	{
    		
    		App.animate(currentPosition++,proteinsDB.size());
   
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
    			toRemove.add(proteinsDB.indexOf(protein));
    			//proteinsDB.remove(proteinsDB.indexOf(protein));
    		}
    		else{
    			//initiate protein structure
    			protein.setStructure(structure);

    		//protein.DivisionToFragments();
    		}
    	}
    	//if there is any problem
    	System.out.println("Number of problematic Proteins:"+ctr);
    	System.out.println("Number of Proteins to be removed (not valid): "+toRemove.size());
    	
    	for (Integer remove : toRemove) 
    	{
    		proteinsDB.remove(remove);
		}
    	System.out.println(proteinsDB.size());
    }
    
	public static void animate(double currentPosition,int totalNumber )
	{
			
			DecimalFormat df = new DecimalFormat("###.#");
			double percent = (currentPosition/totalNumber)*100;
			if(currentPosition % (totalNumber/100) == 0 || currentPosition == totalNumber)
				System.out.println("Processing: " + df.format(percent) + "% ");

		   
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


