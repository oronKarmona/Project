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
	 * Protein's fragment (20 aa long each with overlapping)
	 */
	ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	
	
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
	
	public ArrayList<Structure> getStructure() {
		return structure;
	}
	public void setStructure(ArrayList<Structure> structure) {
		this.structure = structure;
	}
	public ArrayList<Fragment> getFragments() {
		return fragments;
	}
	public void setFragments(ArrayList<Fragment> fragments) {
		this.fragments = fragments;
	}

	public String GetFragments(int index)
	{
		if(index < aminoAcids.length()-20)
			return aminoAcids.substring(index,index+20);
		return null;
	}
	public List<Structure> GetFragmentStructure(int index)
	{
		if(index <= structure.size()-20)
			return structure.subList(index, index+20);
		return null;
	}
	/***
	 * This method will create a 20 amino acid long fragments for the protein represented in this class
	 */
	public void DivisionToFragments()
	{
		String aminoStr = new String(this.aminoAcids);
		int ctr = 0 ; 
		List<Structure> s = new ArrayList<Structure>(this.getStructure());
		String temp = "";
		aminoStr = aminoStr.replace("\n", "").replace("\r", "");
		//while the size of the aa acid chain is higher than 20 keep dividing
		while(aminoStr.length()>20)
		{
			//getting the 20 aa 
			for(int i = 0 ; i < 20 ; i++)
			{
				temp += aminoStr.charAt(i);
			}
			
			this.fragments.add(new Fragment(temp,new ArrayList<Structure>(s.subList(0,21)),ctr)); // add to fragment list
			//removing first amino acid details
		/*	if(ctr == 0)
				aminoStr = aminoStr.substring(2);
			else*/
				aminoStr = aminoStr.substring(1);
			s.remove(0);
			temp = "";
			ctr++; // next index
		}
		
		this.fragments.add(new Fragment(aminoStr,new ArrayList<Structure>(s.subList(0,s.size())),ctr)); // add to fragment list the last element
	}

}
