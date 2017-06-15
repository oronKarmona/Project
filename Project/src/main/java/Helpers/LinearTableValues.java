package Helpers;

import java.util.ArrayList;

public class LinearTableValues 
{
	private	int x ; 
	private	int y ; 
	private double rmsd;
	private ArrayList<Double> power_of_y;
	private ArrayList<Double> power_of_x , total ;
	private ArrayList<String> powersY,powersX,powersXY;
	private int pow ; 
	private String order_of_calculation = "";
	
	public LinearTableValues (int x , int y , double rmsd , int pow)
	{
		this.initiate();
		this.x = x ; this.y = y ; this.rmsd = rmsd ; 
		this.pow = pow;
		
		this.calculate_pows();
	}
	

	@Override
	public String toString()
	{
		String string = "";
		
		for(int i = 1 ; i < power_of_x.size() ; i++)
			string += " " + power_of_x.get(i);
		
		for(int i = 1 ; i < power_of_y.size() ; i++)
			string += " " + power_of_y.get(i);
		
		for(Double value : total)
			string += " " + value;
		
		string += " " + rmsd;
		
		return string;
	
		
		
	}
	
	private void calculate_pows()
	{
		for(int i = 0 ; i <= pow ; i++)
		{
			power_of_x.add(Math.pow(x,i));
			power_of_y.add(Math.pow(y,i));
			// for string
			powersY.add("y^"+i);
			powersX.add("x^"+i);
		}
		
		
		for(int i = 1 ; i <= pow ; i++ )
		{
			for(int j = 1 ; j <= pow ; j++)
			{
				for(int k = 2 ; k<= pow ; k++)
				{
					if((i + j ) == k)
					{
						total.add(power_of_x.get(i) *  power_of_y.get(j));
						powersXY.add(powersX.get(i)+ "*"+ powersY.get(j));
						
					}
				}
			}
		}
	}

	public String getColumnsNames()
	{
		
	
		for(int i = 1 ; i < powersX.size() ; i++)
			order_of_calculation += " "+powersX.get(i);
		
		for(int i = 1 ; i < powersY.size() ; i++)
			order_of_calculation += " "+powersY.get(i);
		
		for(String str : powersXY)
			order_of_calculation += " " + str;
		
		order_of_calculation += " RMSD";
		
		return order_of_calculation;
		
	}
	
	
	private void initiate()
	{
		powersY = new ArrayList<String>();
		powersX = new ArrayList<String>();
		powersXY =new ArrayList<String>();
		total = new ArrayList<Double>();
		power_of_y = new ArrayList<Double>();
		power_of_x = new ArrayList<Double>();
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
	
}
