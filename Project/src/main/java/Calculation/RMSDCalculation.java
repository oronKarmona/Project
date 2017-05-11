package Calculation;

import java.util.List;

import ProGAL.geom3d.Point;
import ProGAL.geom3d.superposition.RMSD;
/***
 * This class contains one static method that calls the RMSD method 
 * @author Oron
 *
 */
public class RMSDCalculation {
	
	/***
	 * Calls the RMSD calculation method 
	 * @param protein1 - List<Point> of protein 1 
	 * @param protein2 - List<Point> of protein 2
	 * @return the RMSD value 
	 */
	public  double Calculate(List<Point> protein1 ,List<Point> protein2){
		
		return RMSD.getRMSD(protein1, protein2);		
	}

}
