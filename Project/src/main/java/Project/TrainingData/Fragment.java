package Project.TrainingData;

import java.util.ArrayList;

public class Fragment 
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
	
	
	
	
}
