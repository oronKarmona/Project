package Project.TrainingData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class App 
{
	public static HashMap<Character,String> map ; 
	
    public static void main( String[] args )
    {
    	Protein p = null;
    	
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
    	
    	try {
			 p = FileParser.ReadAstralDB();
			} 
    	catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
							  }
    	
    	System.out.println(p.getfolderIndex());
    	
    //	p.setStructure(FileParser.ReadStructureDateFile(p.getfolderIndex()+"\\"+p.getFileName()) );
    	p.setStructure(FileParser.ReadStructureDateFile("1g\\d11gsa1.ent") );

    	p.DivisionToFragments();
    	
    	
    }
    
    public static boolean check(String s , ArrayList<Structure> struct)
    {
    	for(Character c : s.toCharArray())
    	{
    		if(!map.get(c).equals(struct.get(0).getAminoAcid()))
    			return false;
    		
    		struct.remove(0);
    	}
		return true;
    	
    }
}


