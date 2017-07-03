package TrainingData;
/**
 * Training data entry which contains the calculation results with the compared protein indices
 * @author Oron
 *
 */
public class TrainingDataEntry extends TableEntry{

	/**
	 * Hamming distance value 
	 */
	private int HammingDistance;
	/**
	 * General Constructor
	 */
	public TrainingDataEntry(){
		
	}

	/**
	 * Constructor
	 * @param firstProteinIndex 
	 * @param secondProteinIndex
	 * @param firstFragmentIndex
	 * @param secondFragmentIndex
	 */
	public TrainingDataEntry(int firstProteinIndex, int secondProteinIndex, 
							 int firstFragmentIndex,int secondFragmentIndex){
		
		this.firstProteinIndex = firstProteinIndex;
		this.secondProteinIndex = secondProteinIndex;
		this.firstFragmentIndex = firstFragmentIndex;
		this.secondFragmentIndex = secondFragmentIndex;
		
	}
	/***
	 * Setting the following parameters 
	 * @param firstProteinIndex
	 * @param secondProteinIndex
	 * @param firstFragmentIndex
	 * @param secondFragmentIndex
	 */
	public void Set(int firstProteinIndex, int secondProteinIndex, 
			 int firstFragmentIndex,int secondFragmentIndex){
					
			this.firstProteinIndex = firstProteinIndex;
			this.secondProteinIndex = secondProteinIndex;
			this.firstFragmentIndex = firstFragmentIndex;
			this.secondFragmentIndex = secondFragmentIndex;
			
	}


	public int getHammingDistance() {
		return HammingDistance;
	}


	public void setHammingDistance(int hammingDistance) {
		HammingDistance = hammingDistance;
	}
	
}
