package Helpers;

import java.util.ArrayList;

public class LinearTableValues 
{
	private	int x ; 
	private	int y ; 
	private double rmsd;
	private ArrayList<Double> power_of_y;
	private ArrayList<Double> power_of_x ;
	private ArrayList<String> powersY,powersX,total;
	private int pow ; 
	public LinearTableValues (int x , int y , double rmsd , int pow)
	{
		powersY = new ArrayList<String>();
		powersX = new ArrayList<String>();
		total = new ArrayList<String>();
		this.x = x ; this.y = y ; this.rmsd = rmsd ; 
		this.pow = pow;
		power_of_y = new ArrayList<Double>();
		power_of_x = new ArrayList<Double>();
		this.calculate_pows();
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
	
	private void calculate_pows()
	{
		for(int i = 0 ; i <= pow ; i++)
		{
			power_of_x.add(Math.pow(x,i));
			power_of_y.add(Math.pow(y,i));
			powersY.add("y^"+i);
			powersX.add("x^"+i);
		}
		
		for(String px : powersX)
		{
			for(String py: powersY)
			{
				total.add(px + "*" + py);
			}
		}
		
		System.out.println();
	}
	
	
}
