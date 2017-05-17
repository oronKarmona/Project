package Table;

public class TrainingDataEntry extends TableEntry{

	
	private int HammingDistance;
	public TrainingDataEntry(){
		
	}


	public TrainingDataEntry(int firstProteinIndex, int secondProteinIndex, 
							 int firstFragmentIndex,int secondFragmentIndex){
		
		this.firstProteinIndex = firstProteinIndex;
		this.secondProteinIndex = secondProteinIndex;
		this.firstFragmentIndex = firstFragmentIndex;
		this.secondFragmentIndex = secondFragmentIndex;
		
	}
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
