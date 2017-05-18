package PCN;

import java.util.ArrayList;

public class Neighbors extends Node {

	public ArrayList<Node> neighbors;
	
	public Neighbors(){
		neighbors = new ArrayList<Node>();

	}
	public Neighbors(long proteind,int index){
		super(proteind, index);
		neighbors = new ArrayList<Node>();
	}
}
