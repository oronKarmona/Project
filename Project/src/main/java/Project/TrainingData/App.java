package Project.TrainingData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ProGAL.geom3d.Point;

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
    	System.out.println(proteinsDB.size());
    	List<Integer> toRemove = new ArrayList<Integer>();
    	ArrayList<Structure> structure;
    	//d4f4oj_
    	
    	for(Protein protein : proteinsDB)
    	{
    		try{
    			/***
    			 *לדוגמא, תוריד את ההערות ותוכל לרוץ על הפרסר ולראות מתי זה קורה d4f4oj_ זה קורה בקובץ 
    			 */		
//    			if(protein.getAstralID().equals("d4f4oj_"))
//        			structure = FileParser.ReadStructureDateFile(protein.getfolderIndex()+"\\"+protein.getFileName());
    			structure = FileParser.ReadStructureDateFile(protein.getfolderIndex()+"\\"+protein.getFileName());

			}
			catch(Exception e){
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
    	for (Integer remove : toRemove) {
    		proteinsDB.remove(remove);
		}
    	System.out.println(proteinsDB.size());
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


