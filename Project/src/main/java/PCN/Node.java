package PCN;

import java.util.ArrayList;
/***
 * Basic node representaion of protein 
 * @author Oron
 *
 */
public class Node {
	/***
	 * protein index 
	 */
	private long m_protein;
	/***
	 * fragment index 
	 */
	private int m_index;
	/***
	 * mean rmsd with neighbor node (as neighbor)
	 */
	private double m_meanRmsd; 
	/***
	 * weight with neighbor node calculated by weight function
	 */
	private double weight;
	/***
	 * general constructor
	 */
	public Node(){
		
	}
	/***
	 * sceond constructor
	 * @param proteind - protein index 
 	 * @param index - fragment index
	 */
	public Node(long proteind,int index){
		m_protein = proteind;
		m_index = index;
		m_meanRmsd= 0;
		weight = 0 ;
	}
	
	
	public long getProteinIndex() {
		return m_protein;
	}
	public void setProteinIndex(long m_currentProtein) {
		this.m_protein = m_currentProtein;
	}
	public int getFragmentIndex() {
		return m_index;
	}
	public void setFragmentIndex(int m_index) {
		this.m_index = m_index;
	}
	public double getMeanRmsd() {
		return m_meanRmsd;
	}
	public void setMeanRmsd(double m_meanRmsd) {
		this.m_meanRmsd = m_meanRmsd;
	}
	public void setWeight(double weight){
		this.weight = weight;
	}
	public double getWeight()
	{
		return this.weight;
	}
	
	
	
}
