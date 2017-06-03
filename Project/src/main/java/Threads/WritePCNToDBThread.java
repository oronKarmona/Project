package Threads;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import DB.ElasticSearchService;
import PCN.Neighbors;
import PCN.Node;
import PCN.WritePCNtoDB;

public class WritePCNToDBThread extends Thread
{
	private int pcn_file_number ; 
	private String pcn_file_name ; 
	private ElasticSearchService es ; 
	
	public WritePCNToDBThread(String pcn_file_name ,ElasticSearchService es )
	{
		this.pcn_file_name = pcn_file_name ; 
		this.es = es ; 
	}
	
	@Override 
	public void run()
	{
		while((pcn_file_number = WritePCNtoDB.getNextFileNumber()) != -1 )
		{
			try {
				readFile(new File(pcn_file_name+pcn_file_number));
			} 
			catch (FileNotFoundException e) {
				System.out.println("File not Found: " + pcn_file_name+pcn_file_number);
				e.printStackTrace();
			}
		}
			
	}
	
	private void readFile(File pcn_file) throws FileNotFoundException
	{
		Neighbors currentNode = new Neighbors();

		String[] words;
		String[] neighborsList;
		String line;
		
	    if(!pcn_file.exists()){
	    	throw  new FileNotFoundException();
	    }
	    
	    
	    
	   
	    try ( BufferedReader br = Files.newBufferedReader(pcn_file.toPath())) {
	        for (line = null; (line = br.readLine()) != null;) 
	        {
	        if(!line.isEmpty())
	        {
	        	if(line.charAt(0) == '#')
	        	{
	        		words = line.split("#");
	        		currentNode.setProteinIndex(Long.parseLong(words[1]));
	        	}
	        	if(line.charAt(0) == '>')
	        	{
	        		line = line.substring(0, line.length()-1);
	        		
	        		//index in current node
	        		words = line.substring(1).split(":");
	        		currentNode.setFragmentIndex(Integer.parseInt(words[0]));
	        		words = words[1].split(",");
	        		
	        		//neighbors of current node
	        		for (String neighbor : words) 
	        		{
	        			
	        			neighborsList = neighbor.split(" ");
		        		
		        		currentNode.neighbors.add(new Node(Long.parseLong(neighborsList[1]),
    							Integer.parseInt(neighborsList[2])));
					}
	        		
	        		writeToDB(currentNode);
	        		currentNode.neighbors.removeAll(currentNode.neighbors);
	        	} 	
	        	        	
	        }
	        }
	    }
	    catch (IOException e) {
			System.out.println("[ReadPCNFile]: error trying to read PCN");
		}
	}
	
	
	private void writeToDB(Neighbors currentNode)
	{
		es.addToBulk(currentNode);
	}
}
