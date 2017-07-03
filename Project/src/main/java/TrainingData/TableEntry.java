package TrainingData;

import java.io.Serializable;

/***
 * 
 * TableEntry
 * abstract class representing an entry in all tables
 *  * @author LITAF
 * 
 */
public abstract class TableEntry implements Serializable{
	
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
	/**
	 * First protein index 
	 */
	protected int firstProteinIndex;
	/***
	 * Second protein index 
	 */
	protected int secondProteinIndex;
	/***
	 * First protein index 
	 */
	protected int firstFragmentIndex;
	/***
	 * Second fragment index 
	 */
	protected int secondFragmentIndex;
	/***
	 * The result of the RMSD 
	 */
	protected double RMSDResult;
	

}
