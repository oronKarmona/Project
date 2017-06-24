package TestingData;
/***
 * An entry for presenting 
 * protein 1 | protein 2 | their resistence 
 * for further use it contains attributes for index and fragment of the proteins and their rmsd value
 * @author Oron
 *
 */
public class TestingEntry
{
	/**
	 * protein 1 
 	 * protein 2 
	 */
	private int p1 , p2 ; 
	/***
	 * index and fragment of the protein
	 */
	private int protein1_index , protein1_fragment;
	private int protein2_index , protein2_fragment;
	/**
	 * resistence of the 2 proteins 
	 */
	private double resistence ; 
	/***
	 * rmsd value of the 2 proteins
	 */
	private double rmsd ; 
	
	/**
	 * Astral ID
	 */
	private String astralId1,astralId2;
	/**
	 * Constructor
	 * @param p1 - first protein 
	 * @param p2 - second protein 
	 * @param resistence - resistences
	 */
	public TestingEntry(int p1 , int p2 , double resistence )
	{
		this.p1 = p1 ; 
		this.p2 = p2 ; 
		this.resistence = resistence;
	}
	public int getP1() {
		return p1;
	}
	public void setP1(int p1) {
		this.p1 = p1;
	}
	public int getP2() {
		return p2;
	}
	public void setP2(int p2) {
		this.p2 = p2;
	}
	public double getResistence() {
		return resistence;
	}
	public void setResistence(double resistence) {
		this.resistence = resistence;
	}
	public int getProtein1_index() {
		return protein1_index;
	}
	public void setProtein1_index(int protein1_index) {
		this.protein1_index = protein1_index;
	}
	public int getProtein1_fragment() {
		return protein1_fragment;
	}
	public void setProtein1_fragment(int protein1_fragment) {
		this.protein1_fragment = protein1_fragment;
	}
	public int getProtein2_index() {
		return protein2_index;
	}
	public void setProtein2_index(int protein2_index) {
		this.protein2_index = protein2_index;
	}
	public int getProtein2_fragment() {
		return protein2_fragment;
	}
	public void setProtein2_fragment(int protein2_fragment) {
		this.protein2_fragment = protein2_fragment;
	}
	public double getRmsd() {
		return rmsd;
	}
	public void setRmsd(double rmsd) {
		this.rmsd = rmsd;
	}
	public String getAstralId1() {
		return astralId1;
	}
	public void setAstralId1(String astralId1) {
		this.astralId1 = astralId1;
	}
	public String getAstralId2() {
		return astralId2;
	}
	public void setAstralId2(String astralId2) {
		this.astralId2 = astralId2;
	}
	
	
}
