package PCN;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import DB.ElasticSearchService;
/**
 * reading and parsing the PCN file
 * @author Oron
 *
 */
public class ReadPCNFile {
	/**
	 * Time attribute 
	 */
	static LocalDateTime now = LocalDateTime.now();
	/**
	 * Date attribute
	 */
	static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	/**
	 * Read pcn file and write to elastic search db
	 * @param file
	 * @param index - index to write in elastic 
	 * @param type - type to write in elastic
	 * @return
	 */
	public static boolean Read(File file,String index ,String type){
		
		ElasticSearchService elasticSearchService = new ElasticSearchService(index,type);
		elasticSearchService.setID(elasticSearchService.getCountOfDocInType());
		NodePCN currentNode = new NodePCN();

		String[] words;
		String[] neighborsList;

	    if(!file.exists()){
	    	return false;
	    }
	    
	    
	    
	    String line;
	    try (BufferedReader br = Files.newBufferedReader(file.toPath())) {
	        for (line = null; (line = br.readLine()) != null;) 
	        {
	        if(!line.isEmpty())
	        {
	        	if(line.charAt(0) == '#')
	        	{
	        		words = line.split(" ");
	        		
	        		currentNode.setProteinIndex(Long.parseLong(words[1]));
	        		System.out.println(currentNode.getProteinIndex());
	        		now = LocalDateTime.now();
	    			System.out.println(dtf.format(now)); //2016/11/16 12:08:43
	    			
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
		        		Node node = new Node(Long.parseLong(neighborsList[1]),
		        							Integer.parseInt(neighborsList[2]));
		        		currentNode.neighbors.add(node);
					}
	        		
	        		elasticSearchService.add(currentNode);
	        		currentNode.neighbors.removeAll(currentNode.neighbors);
	        	} 	
	        	        	
	        }
	        }
	    }
	    catch (IOException e) {
			System.out.println("[ReadPCNFile]: error trying to read PCN");
		}
	    
	    return true;
		
		
		
	}
}
