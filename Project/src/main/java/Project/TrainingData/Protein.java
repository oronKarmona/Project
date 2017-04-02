package Project.TrainingData;


import java.util.ArrayList;
import java.util.List;

import ProGAL.geom3d.Point;
/***
 * This class represent protein  properties with its structure
 * @author Oron
 *
 */
public class Protein {
	
	/***
	 * Protein's asrtral ID
	 */
	String astralID;
	/***
	 * Protein's classification
	 */
	String classification;
	/***
	 * Protein's type
	 */
	String type;
	/***
	 * Protein's matched type
	 */
	String matched;
	/***
	 * Protein's name
	 */
	String name;
	/***
	 * Protein's TaxId
	 */
	String TaxId;
	/***
	 * Protein's amino acid chain
	 */
	String aminoAcids;
	/***
	 * Protein's structure
	 */
	ArrayList<Structure> structure = new ArrayList<Structure>();
	
	
	public String getAstralID() {
		return astralID;
	}
	public void setAstralID(String astralID) {
		this.astralID = astralID;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMatched() {
		return matched;
	}
	public void setMatched(String matched) {
		this.matched = matched;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTaxId() {
		return TaxId;
	}
	public void setTaxId(String taxId) {
		TaxId = taxId;
	}
	public String getAminoAcids() {
		return aminoAcids;
	}
	public void setAminoAcids(String aminoAcids) {
		this.aminoAcids = aminoAcids;
	}
	
	/***
	 * Retreiving the folder index as written in the structural database
	 * @return folder index
	 */
	public String getfolderIndex()
	{
		return this.astralID.substring(this.astralID.length()- 5 , this.astralID.length() - 3);
	}
	
	/***
	 * Retrieving the protein's structures file name
	 * @return protein's structures file name
	 */
	public String getFileName()
	{
		return this.astralID+".ent";
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
