package PCN;

public class Node {
	
	private long m_protein;
	private int m_index;

	public Node(){
		
	}
	public Node(long proteind,int index){
		m_protein = proteind;
		m_index = index;
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
	
	
	
	
}
