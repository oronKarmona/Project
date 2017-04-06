package Project.TrainingData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ProGAL.geom3d.Point;
import ProGAL.geom3d.superposition.RMSD;

public class App 
{
	public static HashMap<Character,String> map ; 
	
    @SuppressWarnings("static-access")
	public static void main( String[] args )
    {
    	List<Protein> pr = null;
    	
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
			 pr = FileParser.ReadAstralDB();
			} 
    	catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
							  }
    	List<Point> struct = null ;
    	int i = 0 ;
    	
    	for(Protein p : pr)
    	{
    		i++;
	    	System.out.println(p.getfolderIndex());
	    	try {
				CAwriter.write(p.getfolderIndex()+"\\"+p.getFileName());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	p.setStructure(FileParser.ReadStructureDateFile(p.getfolderIndex()+"\\"+p.getFileName()) );
	   
	
	    	p.DivisionToFragments();
	    	
	    	try {
	    		if(i == 1)
	    		{
	    			struct = p.getCoordinatesOnly();
	    			struct.remove(128);
	    			struct.remove(127);
	    			Writer.write(struct,"p"+i);
	    		}	
	    		else
	    			Writer.write(p.getCoordinatesOnly(),"p"+i);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	
    	}
    	
    	
    	RMSD r = new RMSD();
    	List<Point> temp = r.optimalSuperposition(struct, pr.get(1).getCoordinatesOnly()).transform(struct);
    	System.out.println(r.getRMSD(pr.get(0).fragments.get(20).getCoordinatesOnly(), pr.get(2).fragments.get(50).getCoordinatesOnly()));
    	
    	
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


