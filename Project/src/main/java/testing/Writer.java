package testing;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import ProGAL.geom3d.Point;

public class Writer 
{
	public static void write(List<Point> list,String path) throws IOException
	{
		FileOutputStream fout = new FileOutputStream(path+".txt");
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(list);
	}
	
	@SuppressWarnings("unchecked")
	public static void Read() throws IOException
	{
		ObjectInputStream objectinputstream = null;
		try {
			FileInputStream streamIn = new FileInputStream("p1.txt");
		     objectinputstream = new ObjectInputStream(streamIn);
		    List<Point> readCase = (List<Point>) objectinputstream.readObject();
		    for(Point p : readCase)
		    	System.out.println(p);
		    
		    System.out.println("ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
			 streamIn = new FileInputStream("p2.txt");
		     objectinputstream = new ObjectInputStream(streamIn);
		    List<Point> readCase2 = (List<Point>) objectinputstream.readObject();
		    for(Point p : readCase2)
		    	System.out.println(p);
		 
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
		    if(objectinputstream != null){
		        objectinputstream .close();
		    } 
		}
	}
	

}
