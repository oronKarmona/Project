package Calculation;

public class NeighborsHelper {

	private int m_proteinIndex;
	private int m_fragmentIndex;
	private int m_count = 0;
	private float m_sum = 0;
	
	
	public NeighborsHelper(int proteinI, int fragmentI){
		m_proteinIndex = proteinI;
		m_fragmentIndex = fragmentI;
	}


	public float getSum() {
		return m_sum;
	}
	
	public int getCount() {
		return m_count;
	}

	public synchronized void CalcAvg(float sum){
		m_sum += sum;
		m_count++;
	}
	
}
