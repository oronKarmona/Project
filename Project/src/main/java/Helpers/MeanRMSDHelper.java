package Helpers;

public class MeanRMSDHelper 
{
	
	private int countOfValues = 0 ;
	private double totalRmsd = 0 ;
	
	
	public void addValue(double rmsd)
	{
		this.totalRmsd += rmsd ; 
		this.countOfValues++;
	}
	
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
