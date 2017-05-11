package Table;

public class TrainingDataEntry extends TableEntry{

	private double m_weightFunctionResult;
	
	public TrainingDataEntry(){
		
	}
	public double getWeightFunctionResult() {
		return m_weightFunctionResult;
	}

	public void setWeightFunctionResult(double m_weightFunctionResult) {
		this.m_weightFunctionResult = m_weightFunctionResult;
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
	
}
