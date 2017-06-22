package PCN;

import java.util.ArrayList;

public class Node {
	
	private long m_protein;
	private int m_index;
	private ArrayList<Double> m_meanRmsd; 
	
	public Node(){
		
	}
	public Node(long proteind,int index){
		m_protein = proteind;
		m_index = index;
		m_meanRmsd= new ArrayList<Double>();
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
	public ArrayList<Double> getMeanRmsd() {
		return m_meanRmsd;
	}
	public void setMeanRmsd(ArrayList<Double> m_meanRmsd) {
		this.m_meanRmsd = m_meanRmsd;
	}
	
	
	
	
}
