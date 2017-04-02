package Project.TrainingData;

import ProGAL.geom3d.Point;
/***
 * This class contains the fields and methods for representing a protein/fragment structure
 * @author Oron
 *
 */
public class Structure 
{
	/***
	 * Index of the atom in the protein
	 */
	int atomIndex;
	/***
	 * amino acid's type
	 */
	String type ; 
	/***
	 * String representation of the amino acid
	 */
	String AminoAcid;
	/***
	 * Coordinates of the atom (x,y,z)
	 */
	Point p ; 
	
	
	/***
	 * Copy constructor
	 * @param temp
	 */
	public Structure(Structure temp) {
		this.setAminoAcid(temp.getAminoAcid());
		this.setType(temp.getType());
		this.setIndex(temp.getIndex());
	}
	/***
	 * General Constructor
	 */
	public Structure() {}
	public int getIndex() {
		return atomIndex;
	}
	public void setIndex(int index) {
		this.atomIndex = index;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAminoAcid() {
		return AminoAcid;
	}
	public void setAminoAcid(String aminoAcid) {
		AminoAcid = aminoAcid;
	}

	public Point getP() {
		return p;
	}

	public void setP(Point p) {
		this.p = p;
	}
	
	

}
