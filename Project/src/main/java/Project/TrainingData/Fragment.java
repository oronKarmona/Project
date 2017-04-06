package Project.TrainingData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ProGAL.geom3d.Point;

public class Fragment implements Serializable
{
	String aminoAcid;
	ArrayList<Structure> structure = new ArrayList<Structure>();
	int fragmentIndex;

	/** 
	 * Class constructor
	 * @param aminoAcid
	 * @param structure
	 * @param fragmentIndex
	 */
	public Fragment(String aminoAcid, ArrayList<Structure> structure,
			int fragmentIndex) {
		//System.out.println(App.check(aminoAcid, new ArrayList<Structure>(structure)));
		this.aminoAcid = aminoAcid;
		this.structure = structure;
		this.fragmentIndex = fragmentIndex;
	}
	
	public String getAminoAcid() {
		return aminoAcid;
	}
	public void setAminoAcid(String aminoAcid) {
		this.aminoAcid = aminoAcid;
	}
	public ArrayList<Structure> getStructure() {
		return structure;
	}
	public void setStructure(ArrayList<Structure> structure) {
		this.structure = structure;
	}
	public int getFragmentIndex() {
		return fragmentIndex;
	}
	public void setFragmentIndex(int fragmentIndex) {
		this.fragmentIndex = fragmentIndex;
	}
	/***
	 * Retrieving only the points of the structures
	 * @return list of points
	 */
	public List<Point> getCoordinatesOnly()
	{
		
		List<Point> l = new ArrayList<Point>();
		
		for(Structure s : structure)
			l.add(s.getP());
		
		return l ;
		
	}
	
	
	
}
