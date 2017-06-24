package Helpers;
/***
 * Helper calculation for the average rmsd
 * @author Oron
 *
 */
public class MeanRMSDHelper 
{
	/***
	 * count of all values measured
	 */
	private int countOfValues = 0 ;
	/***
	 * total rmsd values summed
	 */
	private double totalRmsd = 0 ;
	
	/**
	 * adding value
	 * @param rmsd
	 */
	public void addValue(double rmsd)
	{
		this.totalRmsd += rmsd ; 
		this.countOfValues++;
	}
	
	/***
	 * calculate avergae
	 * @return average rmsd
	 */
	public double calculateAverageRMSD()
	{
		if(this.countOfValues != 0)
			return (this.totalRmsd / this.countOfValues);
		else 
			return 0 ;
	}
	
	public double getRMSD()
	{
		return this.totalRmsd;
	}
}
