package Table;
/***
 * 
 * @author ליטף
 * TableEntry
 * abstract class representing an entry in all tables
 * 
 */
public abstract class TableEntry {
	
	public int getFirstProteinIndex() {
		return firstProteinIndex;
	}

	public void setFirstProteinIndex(int firstProteinIndex) {
		this.firstProteinIndex = firstProteinIndex;
	}

	public int getSecondProteinIndex() {
		return secondProteinIndex;
	}

	public void setSecondProteinIndex(int secondProteinIndex) {
		this.secondProteinIndex = secondProteinIndex;
	}

	public int getFirstFragmentIndex() {
		return firstFragmentIndex;
	}

	public void setFirstFragmentIndex(int firstFragmentIndex) {
		this.firstFragmentIndex = firstFragmentIndex;
	}

	public int getSecondFragmentIndex() {
		return secondFragmentIndex;
	}

	public void setSecondFragmentIndex(int secondFragmentIndex) {
		this.secondFragmentIndex = secondFragmentIndex;
	}

	public double getRMSDResult() {
		return RMSDResult;
	}

	public void setRMSDResult(double RMSD) {
		RMSDResult = RMSD;
	}
	
	protected int firstProteinIndex;
	protected int secondProteinIndex;
	protected int firstFragmentIndex;
	protected int secondFragmentIndex;
	protected double RMSDResult;
	

}
