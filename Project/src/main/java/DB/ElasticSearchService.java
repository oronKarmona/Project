package DB;
//https://medium.com/@adnanxteam/how-to-install-elasticsearch-5-and-kibana-on-homestead-vagrant-60ea757ff8c7
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.get.GetField;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.DisMaxQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import Helpers.LinearTableValues;
import PCN.NodePCN;
import PCN.Node;
import ProGAL.proteins.ProteinComplex;
import Protein.Protein;
import Table.TrainingDataEntry;
import static org.elasticsearch.common.xcontent.XContentFactory.*;
/***
 * This class creates a client and manipulates the elastic search data base with the methods sets inside it
 * @author Oron
 *
 */
public class ElasticSearchService
{
	/***
	 * default document id
	 */
	private  Long id = (long) -1;
	/***
	 * client to be used for communication
	 */
	private static TransportClient client = null;
	/***
	 * Gson object for coverting objects to JSON
	 */
	private static Gson gson = null;
	/***
	 * settings object
	 */
	private Settings settings;
	/***
	 * index and type for the elastic search db 
	 */
	private String index , type ; 
	/***
	 * for asynchronous writes
	 */
	public  BulkProcessor bulkProcessor;
	/***
	 * Last doc id used - specific use inside methods
	 */
	private String lastDocID = null; 
	
	/***
	 * constructor
	 * @param index - index to access 
	 * @param type - type to access
	 */
	public ElasticSearchService(String index , String type){
	
		this.index = index; 
		this.type = type;
		
		try {
			 settings = Settings.builder()
			        .put("cluster.name", "elasticsearch")
			        .put("client.transport.sniff", true)
			        .build();
		
			client = new PreBuiltTransportClient(settings)
			        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
			
			client.prepareBulk(); 
			
			 bulkProcessor = BulkProcessor.builder(
			        client,  
			        new BulkProcessor.Listener() {
						@Override
						public void beforeBulk(long executionId,
								BulkRequest request) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void afterBulk(long executionId,
								BulkRequest request, BulkResponse response) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void afterBulk(long executionId,
								BulkRequest request, Throwable failure) {
							// TODO Auto-generated method stub
							
						} 
			        })
			         
			        .setFlushInterval(TimeValue.timeValueSeconds(5))
			        .setConcurrentRequests(1) 
			        .setBackoffPolicy(
			            BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3)) 
			        .build();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new NoNodeAvailableException("[ElasticSearchServiceImpl]: Error occurred while creating DB");
		}
		gson = new Gson();
	
	}
//	public TransportClient getClient() {
//		return client;
//	}
	/***
	 * close client
	 */
	public void clientClose()
	{
		this.client.close();
	}
	
	
	

	/***
	 * adding a document with a training data object
	 * @param trainingDataEntry
	 */
	public  void add(TrainingDataEntry trainingDataEntry) {
		id++;
	     try 
	     {	 
	    	 client.prepareIndex(index, type, id+"")
	    			 .setSource(gson.toJson(trainingDataEntry)).get();
		 }catch (Exception e) {
	    	 throw new NoNodeAvailableException("[add]: Error occurred while creating record");
	     }
	     
	}
	
	
	/***
	 * adding a document with proteing object
	 * @param protein
	 */
	public  void add(Protein protein) {
		 id++;
	     try 
	     {	 
	    	 client.prepareIndex(index, type, id+"")
	    			 .setSource(gson.toJson(protein)).get();
		 }catch (Exception e) {
	    	 throw new NoNodeAvailableException("[add]: Error occurred while creating record");
	     }
	    
	}
	
	/***
	 * adding a document with NodePCN object
	 * @param pcnEntry
	 */
	public  void add(NodePCN pcnEntry) {
		id++;
	     try 
	     {	 
	    	 client.prepareIndex(index, type, id+"")
	    			 .setSource(gson.toJson(pcnEntry)).get();
		 }catch (Exception e) {
	    	 throw new NoNodeAvailableException("[add]: Error occurred while creating record");
	     }
	     
	}
	
		/***
		 * Update exist document with new hamming distance at id_to_update
		 * @param hamming
		 * @param id_to_update
		 * @throws IOException
		 * @throws InterruptedException
		 * @throws ExecutionException
		 */
		public void updateDocument(int hamming,int id_to_update) throws IOException, InterruptedException, ExecutionException
		{
	
			UpdateRequest updateRequest = new UpdateRequest(this.index, type,id_to_update + "")
			        .doc(jsonBuilder()
			            .startObject()
			                .field("Hamming_Distance", hamming)
			            .endObject());             
			client.update(updateRequest).get();
		}
		
		/***
		 * update document with an array of double values such as weights 
		 * @param values
		 * @param id_to_update
		 * @param name - name of the values to be shown in the document
		 */
		public void updateDocument(Double[] values , int id_to_update, String name)
		{
			UpdateRequest updateRequest;
			try {
				updateRequest = new UpdateRequest(this.index, type,id_to_update + "")
				.doc(jsonBuilder()
				    .startObject()
				        .field(name, values)
				    .endObject());
				
				client.update(updateRequest).get();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}             
	
		}
		
		/***
		 * UPdate an existing document with an arrayList of nodes 
		 * @param neighbors 
		 * @param id
		 * @throws IOException
		 * @throws InterruptedException
		 * @throws ExecutionException
		 */
		public void updateDocument(ArrayList<Node> neighbors,String id) throws IOException, InterruptedException, ExecutionException
		{
	
			UpdateRequest updateRequest = new UpdateRequest(this.index, type,id)
			        .doc(jsonBuilder()
			            .startObject()
			                .field("neighbors", neighbors)
			            .endObject());             
			client.update(updateRequest).get();
		}
		
		/***
		 * return a proteing at a given proteinIndex as saved in the type
		 * @param ProteinIndex
		 * @return Map<String, Object> with the protein data
		 */
		public synchronized Map<String, Object> getProtein(int ProteinIndex )
		{
			 QueryBuilder qb = QueryBuilders.boolQuery()
		                .must(QueryBuilders.matchQuery("ProteinIndex", ProteinIndex));
 
			 SearchResponse r = client.prepareSearch(this.index)
					 				  .setTypes(this.type)
					 				  .setQuery(qb)
					 				  .execute().actionGet();
			 
			 
			 SearchHit[]  results = r.getHits().getHits();
			 
			 try{
					if( results[0].getSource() != null)
					{
						return (results[0].getSource());
					}
					
					
					 }catch(ArrayIndexOutOfBoundsException e)
					 {
						 System.out.println(String.format("Protein Index: %d IS NOT FOUND!", ProteinIndex ));
					 }
						
					 return null;
		}
		/***
		 * return a proteing with a given AstralID as saved in the type
		 * @param AstralID
		 * @return Map<String, Object> with the protein data
		 */
		public synchronized Map<String, Object> getProtein(String AstralID )
		{
			 QueryBuilder qb = QueryBuilders.boolQuery()
		                .must(QueryBuilders.matchQuery("astralID", AstralID));
 
			 SearchResponse r = client.prepareSearch(this.index)
					 				  .setTypes(this.type)
					 				  .setQuery(qb)
					 				  .execute().actionGet();
			 
			 
			 SearchHit[]  results = r.getHits().getHits();
			 
			 try{
					if( results[0].getSource() != null)
					{
						return (results[0].getSource());
					}
					
					
					 }catch(ArrayIndexOutOfBoundsException e)
					 {
						 System.out.println(String.format("Protein: %s IS NOT FOUND!", AstralID ));
					 }
						
					 return null;
		}
		
		/***
		 * return document from the index - type set in this object by a given id
		 * @param id - id to retreive 
		 * @return Map<String, Object> with the document data
		 */
		public synchronized Map<String, Object> get(int id)
		{
			Map<String, Object> map = null;
			try{
			GetResponse response = client.prepareGet(this.index, type,id + "").get();
			map = response.getSource();
			}catch(Exception e )
			{
				return null;
			}
			return map;
		}
		/***
		 * add to bulk for asynchronous writing
		 * @param trainingDataEntry
		 */
		public synchronized void addToBulk(TrainingDataEntry trainingDataEntry)
		{

			id++;
			bulkProcessor.add(new IndexRequest(index, type, id+"")
			 .source(gson.toJson(trainingDataEntry)));		
		}
		/***
		 * add to bulk for asynchronous writing
		 * @param pcnEntry
		 */
		public synchronized void addToBulk(NodePCN pcnEntry)
		{

			id++;
			bulkProcessor.add(new IndexRequest(index, type, id+"")
			 .source(gson.toJson(pcnEntry)));		
		}
		/***
		 * add to bulk for asynchronous writing
		 * @param values
		 */
		public synchronized void addToBulk(LinearTableValues values)
		{

			id++;
			bulkProcessor.add(new IndexRequest(index, type, id+"")
			 .source(gson.toJson(values)));		
		}
		
	
		/***
		 * search for training data results with given protein and fragments
		 * @param firstProteinIndex
		 * @param secondProteinIndex
		 * @param firstFragmentIndex
		 * @param secondFragmentIndex
		 * @return
		 */
		public SearchHit[] SearchTrainingDataDB(int firstProteinIndex , int  secondProteinIndex , int firstFragmentIndex , int secondFragmentIndex)
		{
			 QueryBuilder qb = QueryBuilders.boolQuery()
		                .must(QueryBuilders.matchQuery("firstProteinIndex", firstProteinIndex))
		                .must(QueryBuilders.matchQuery("secondProteinIndex", secondProteinIndex))
		                		.must(QueryBuilders.matchQuery("firstFragmentIndex", firstFragmentIndex))
				                .must(QueryBuilders.matchQuery("secondFragmentIndex", secondFragmentIndex));
			 
			 SearchResponse r = client.prepareSearch(this.index)
					 				  .setTypes(this.type)
					 				  .setQuery(qb)
					 				  .execute().actionGet();
			 
			 
			 SearchHit[]  results = r.getHits().getHits();
			 
			 return results;
			 
		}
		/***
		 * search the pcn with a given protein and fragment indices
		 * @param ProteinIndex
		 * @param fragmentIndex
		 * @return node in the pcn of the corresponding details
		 */
		public NodePCN SearchPCNDB(long ProteinIndex , int  fragmentIndex )
		{
			 QueryBuilder qb = QueryBuilders.boolQuery()
		                .must(QueryBuilders.matchQuery("m_protein", ProteinIndex))
		                .must(QueryBuilders.matchQuery("m_index", fragmentIndex));
			 
			 SearchResponse r = client.prepareSearch(this.index)
					 				  .setTypes(this.type)
					 				  .setQuery(qb)
					 				  .execute().actionGet();
			 
			 
			 SearchHit[]  results = r.getHits().getHits();
			 
			 try{
			if( results[0].getSource() != null)
			{
				this.setLastDocID(results[0].getId());
				return fromMapToVertex(results[0].getSource());
			}
			
			
			 }catch(ArrayIndexOutOfBoundsException e)
			 {
				 this.setLastDocID(null);
				 System.out.println(String.format("Protein Index: %d fragmentIndex: %d IS NOT FOUND!", ProteinIndex , fragmentIndex));
			 }
				
			 return null;
		}
		/***
		 * Searches for unlisted neighbors in the pcn of specific node given as protein index and fragment index
		 * @param ProteinIndex
		 * @param fragmentIndex
		 * @return arrayList of the unlisted neighbors
		 */
		public ArrayList<NodePCN> SearchForNeighborsInPCN(long ProteinIndex , int  fragmentIndex )
		{
			ArrayList<NodePCN> neighbors = new ArrayList<NodePCN>();
			
			QueryBuilder qb = QueryBuilders.boolQuery()
		                .must(QueryBuilders.termQuery("neighbors.m_protein",ProteinIndex))
		                .must(QueryBuilders.termQuery("neighbors.m_index",fragmentIndex)) 
		                ;

			 SearchResponse r = client.prepareSearch(this.index)
					 				  .setTypes(this.type)
					 				  .setQuery(qb)
					 				  .execute().actionGet();
			
			 
			 SearchHit[]  results = r.getHits().getHits();
			 NodePCN node ; 
			 try{
			for(SearchHit hit :  results)
			{
				this.setLastDocID(hit.getId());
				node =  fromMapToVertex(hit.getSource());
				if(check_exist(ProteinIndex, fragmentIndex, node))
					neighbors.add(new NodePCN(node));
				
			}
			
			 }catch(ArrayIndexOutOfBoundsException e)
			 {
				 this.setLastDocID(null);
				 System.out.println(String.format("Protein Index: %d fragmentIndex: %d IS NOT FOUND!", ProteinIndex , fragmentIndex));
			 }
				
			 return neighbors;
		}
		/***
		 * checks if a protein and a fragment exist in the node's neighbors list
		 * @param ProteinIndex
		 * @param fragmentIndex
		 * @param node
		 * @return true if exist , false otherwise
		 */
		private boolean check_exist(long ProteinIndex , int  fragmentIndex ,NodePCN node)
		{
			for(Node n : node.neighbors)
			{
				if(n.getProteinIndex() == ProteinIndex && n.getFragmentIndex() == fragmentIndex)
					return true;
			}
			
			return false;
		}
		
		/***
		 * Retrieve a vertex at a given id 
		 * @param index
		 * @return NodePCN from a given id 
		 */
		public NodePCN getVertexAt(int index)
		{ 
			Map<String, Object> map = get(index);
			NodePCN vertex = fromMapToVertex(map);
			
			return vertex;		 
		}
		
		/***
		 * converts the Map<String, Object> to NodePCN object
		 * @param map
		 * @return 
		 */
		private NodePCN fromMapToVertex(Map<String, Object> map )
		{
			ArrayList<Double> mean_rmsd = new ArrayList<Double>();
			ArrayList<Double> weight = new ArrayList<Double>();
			 NodePCN neighbors = new NodePCN();
			 
			 neighbors.setProteinIndex((Integer)map.get("m_protein"));
			 neighbors.setFragmentIndex((Integer)map.get("m_index"));
			 neighbors.setNeighbors((ArrayList<Node>)map.get("neighbors"));
			 try{
			 mean_rmsd = (ArrayList<Double>)map.get("mean_rmsd");
			 weight = (ArrayList<Double>)map.get("weight");
			 }catch(Exception e )
			 {
				 
			 }
			 neighbors.setNeighbors(this.fromMapToNeighbors(neighbors, mean_rmsd,weight));
			 
			 return neighbors;
		}
		
		
		/***
		 * converts the Map<String , Object> to arrayList of nodes with weights
		 * @param neighbors
		 * @param mean_rmsd
		 * @param weight
		 * @return
		 */
		private ArrayList<Node> fromMapToNeighbors(NodePCN neighbors, ArrayList<Double> mean_rmsd,ArrayList<Double>weight)
		{
			Map<String, Object> nmap , rmap;
			ArrayList<Node> nodes = new ArrayList<Node>();
			for(int i = 0 ; i < neighbors.getNeighbors().size() ; i++)
			{
				nmap = (Map<String, Object>) neighbors.getNeighbors().get(i);
				if(nmap == null)
					return null;
				nodes.add(new Node( (Integer)nmap.get("m_protein"),(Integer)nmap.get("m_index")));
				if(!(mean_rmsd == null) )
					nodes.get(i).setMeanRmsd(mean_rmsd.get(i));
				if(!(weight==null) )
					if(!weight.isEmpty())
					{
					try{
					nodes.get(i).setWeight(weight.get(i));
					}catch(Exception e)
				{
						System.out.checkError();
				}
					}
			}
			
			return nodes;
		}
		/***
		 * return count of documents in a type 
		 * @return count of documents in a type 
		 */
		public long getCountOfDocInType( )
		{
			long ctr  = -1 ;
			SearchResponse searchResponse = client.prepareSearch(this.index).setTypes(this.type).execute().actionGet();
			ctr = searchResponse.getHits().getTotalHits();
			return ctr;
		}
		/***
		 * getLastDocID
		 * @return
		 */
		public String getLastDocID() {
			return lastDocID;
		}
		/***
		 * setLastDocID
		 * @param lastDocID
		 */
		public void setLastDocID(String lastDocID) {
			this.lastDocID = lastDocID;
		}
		
		/***
		 * Set the id to start from 
		 * @param id
		 */
		public void setID(long id )
		{
			this.id = id ;
			
		}

	}
