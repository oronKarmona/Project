package Calculation;

import java.util.List;

import ProGAL.geom3d.Point;
import ProGAL.geom3d.superposition.RMSD;

public class RMSDCalculation {
	
	
	public static double Calculate(List<Point> protein1 ,List<Point> protein2){
		
		return RMSD.getRMSD(protein1, protein2);		
	}

}
