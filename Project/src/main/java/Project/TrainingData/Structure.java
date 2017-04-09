package Project.TrainingData;

import java.io.Serializable;

import ProGAL.geom3d.Point;
/***
 * This class contains the fields and methods for representing a protein/fragment structure
 * @author Oron
 *
 */
public class Structure implements Serializable
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
	 * Residue sequence number 
	 */

	/***
	 * Copy constructor
	 * @param temp
	 */
	public Structure(Structure temp) {
		this.setAminoAcid(temp.getAminoAcid());
		this.setType(temp.getType());
		this.setIndex(temp.getIndex());
		this.setP(new Point(temp.p));
		this.setResidueSequenceNumber(temp.getResidueSequenceNumber());
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
	int residueSequenceNumber;
	public int getResidueSequenceNumber() {
		return residueSequenceNumber;
	}
	public void setResidueSequenceNumber(int residueSequenceNumber) {
		this.residueSequenceNumber = residueSequenceNumber;
	}
	public Point getP() {
		return p;
	}

	public void setP(Point p) {
		this.p = p;
	}
	
	

}
