package Threads;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import DB.ElasticSearchService;
import PCN.NodePCN;
import PCN.Node;
import PCN.WritePCNtoDB;
/**
 * 	Writing the PCN data to ElasticSearch DB
 * @author Oron
 *
 */
public class WritePCNToDBThread extends Thread
{
	/**
	 * Number of the pcn file 
	 */
	private int pcn_file_number ; 
	/**
	 * Name of the PCN file 
	 */
	private String pcn_file_name ; 
	/**
	 * Elastic Seach cilent
	 */
	private ElasticSearchService es ; 
	/***
	 * Constructor
	 * @param pcn_file_name - Name of the PCN file 
	 * @param es - Elastic Seach cilent
	 */
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
				} catch (FileNotFoundException e) {
					
					e.printStackTrace();
				}
			 
		
		}
			
	}
	/***
	 * Parsing the PCN file 
	 * @param pcn_file
	 * @throws FileNotFoundException
	 */
	private void readFile(File pcn_file) throws FileNotFoundException
	{
		NodePCN currentNode = new NodePCN();

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
	        	long number ;
	        	if(line.charAt(0) == '#')
	        	{
	        		words = line.split("#");
	        		try{
	        			number = Long.parseLong(words[1]);
	        			
	        		}catch(Exception e)
	        		{
	        			number = Long.parseLong(words[1].split(" ")[1]);
	        		}
	        		
	        		currentNode.setProteinIndex(number);
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
	
	/**
	 * Writing the parsed data to the database
	 * @param currentNode - data 
	 */
	private void writeToDB(NodePCN currentNode)
	{
		es.addToBulk(currentNode);
	}
}
