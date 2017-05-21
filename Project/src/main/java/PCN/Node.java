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
	
	
	public long getProtein() {
		return m_protein;
	}
	public void setProtein(long m_currentProtein) {
		this.m_protein = m_currentProtein;
	}
	public int getIndex() {
		return m_index;
	}
	public void setIndex(int m_index) {
		this.m_index = m_index;
	}
	
	
	
	
}
