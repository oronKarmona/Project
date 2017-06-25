package Helpers;

public class ResistanceRMSD{
	
	private String m_protein1;
	
	public String getProtein1() {
		return m_protein1;
	}

	public void setProtein1(String m_protein1) {
		this.m_protein1 = m_protein1;
	}

	public String getFragment1() {
		return m_fragment1;
	}

	public void setFragment1(String m_fragment1) {
		this.m_fragment1 = m_fragment1;
	}

	public String getProtein2() {
		return m_protein2;
	}

	public void setProtein2(String m_protein2) {
		this.m_protein2 = m_protein2;
	}

	public String getFragment2() {
		return m_fragment2;
	}

	public void setFragment2(String m_fragment2) {
		this.m_fragment2 = m_fragment2;
	}

	public String getR() {
		return m_R;
	}

	public void setR(String m_R) {
		this.m_R = m_R;
	}

	public String getRmsd() {
		return m_rmsd;
	}

	public void setRmsd(String m_rmsd) {
		this.m_rmsd = m_rmsd;
	}

	private String m_fragment1;
	private String m_protein2;
	private String m_fragment2;
	private String m_R;
	private String m_rmsd;
	
	public ResistanceRMSD(String p1,String f1, String p2,String f2, 
			String R, String rmsd){

		m_protein1 = p1;
		m_fragment1 =f1;
		m_protein2 =p2;
		m_fragment2 =f2;
		m_R =R;
		m_rmsd = rmsd;
		
	}
}