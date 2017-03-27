package Project.TrainingData;

import ProGAL.geom3d.Point;

public class Structure 
{
	int index;
	String type ; 
	String AminoAcid;
	Point p ;  // representation of x,y,z as point
	double[] coordinates = new double[3];
	
	/***
	 * Copy constructor
	 * @param temp
	 */
	public Structure(Structure temp) {
		this.setAminoAcid(temp.getAminoAcid());
		this.setCoordinates(temp.getCoordinates());
		this.setType(temp.getType());
		this.setIndex(temp.getIndex());
	}
	
	public Structure() {}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAminoAcid() {
		return AminoAcid;
	}
	public void setAminoAcid(String aminoAcid) {
		AminoAcid = aminoAcid;
	}
	public double[] getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(double[] coordinates) {
		
		this.coordinates = coordinates;
		
		if(this.coordinates.length == 3)
			p = new Point(this.coordinates);
	}

	public Point getP() {
		return p;
	}

	public void setP(Point p) {
		this.p = p;
	}
	
	

}
