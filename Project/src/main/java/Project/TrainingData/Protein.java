package Project.TrainingData;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ProGAL.geom3d.Point;
/***
 * This class represent protein  properties with its structure
 * @author Oron
 *
 */
public class Protein implements Serializable{
	
	/***
	 * Proteins Index in the DB (serial)
	 */
	int ProteinIndex;
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

	
	
	
	/***
	 * Setting the amino acid String without spaces
	 * @param aminoAcids - amino acid String as saved in Astral DB
	 */
	public void setAminoAcids(String aminoAcids) {
		this.aminoAcids =  aminoAcids.replace("\n", "").replace("\r", "");;
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
	
	/***
	 * Retrieving only the points of the structures from a specific fragment
	 * @return list of points
	 */
	public List<Point> getFragmentCoordinates(int i)
	{	
		List<Point> l = new ArrayList<Point>();
		try{
		for(Structure s : GetFragmentStructure(i))
			l.add(s.getP());
		}catch(NullPointerException e){
			System.out.println(this.astralID);
		}
		return l ;
		
	}
	


	/***
	 * This method return the String of the fragment in specified index
	 * @param index - index of the wanted fragment
	 * @return String of the fragment with 20 aa long
	 */
	public String GetFragments(int index)
	{
		if(index < aminoAcids.length()-20)
			return aminoAcids.substring(index,index+20);
		return null;
	}
	/***
	 * Returns the structural data of the aa Fragment
	 * @param index - index of the Fragment
	 * @return - List<Structure> of the Fragment
	 */
	public List<Structure> GetFragmentStructure(int index)
	{
		if(index <= structure.size()-20)
			return structure.subList(index, index+20);
		return null;
	}
	
	public int getProteinIndex() {
		return ProteinIndex;
	}
	public void setProteinIndex(int proteinIndex) {
		ProteinIndex = proteinIndex;
	}
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
	
	public ArrayList<Structure> getStructure() {
		return structure;
	}
	public void setStructure(ArrayList<Structure> structure) {
		this.structure = structure;
	}
}
