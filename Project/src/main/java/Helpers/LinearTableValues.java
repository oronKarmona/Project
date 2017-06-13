package Helpers;

public class LinearTableValues 
{
	private	int x ; 
	private	int y ; 
	private double rmsd;
	public LinearTableValues (int x , int y , double rmsd)
	{
		this.x = x ; this.y = y ; this.rmsd = rmsd ; 
	}
	public double getRmsd()
	{
		return rmsd;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	@Override
	public String toString()
	{
		return String.format("%d %d %lf",x,y,rmsd);
	}
	
	
}
