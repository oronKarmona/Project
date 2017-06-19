package Calculation;

import java.util.ArrayList;

public class MeanRMSD
{
	private int x , y , power;
	private ArrayList<Double> power_of_x  , power_of_y , power_of_xy, total ; 
	private double[] m_beta;
	private double m_sum;
	private double[] beta;
	
	public void setMeanRMSD( int x,int y , int power, double[] beta)
	{
		this.x = x ;
		this.y = y; 
		this.power = power;
		m_beta = beta;
		m_sum = 0;
		
		
		power_of_x = new ArrayList<>();
		power_of_y = new ArrayList<>();
		power_of_xy = new ArrayList<>();
		total = new ArrayList<>();

		calculate_pows();
		calculate_meanRmsd();
	}
	
	public void calculate_pows()
	{
		for(int i = 1 ; i <= power ; i++)
		{
			power_of_x.add(Math.pow(x,i));
			power_of_y.add(Math.pow(y,i));
		}
		
		
		for(int i = 1 ; i <= power ; i++ )
		{
			for(int j = 1 ; j <= power ; j++)
			{
				for(int k = 2 ; k<= power ; k++)
				{
					if((i + j ) == k)
					{
						total.add(power_of_x.get(i) *  power_of_y.get(j));
						
					}
				}
			}
		}
		
		total.addAll(power_of_x);
		total.addAll(power_of_y);
		total.addAll(power_of_xy);

	}
	
	private void calculate_meanRmsd(){
		
		for(int i=0 ;i< total.size();i++){
			m_sum+=total.get(i)*m_beta[i];
		}

	}
	
	public double getMeanRmsd() {
		return m_sum;
		
	}

}
