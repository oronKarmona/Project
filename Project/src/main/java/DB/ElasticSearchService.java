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

import PCN.Vertex;
import PCN.Node;
import ProGAL.proteins.ProteinComplex;
import Project.TrainingData.Protein;
import Table.TrainingDataEntry;
import static org.elasticsearch.common.xcontent.XContentFactory.*;
public class ElasticSearchService
{
	
	private  Long id = (long) -1;
	private static TransportClient client = null;
	private static Gson gson = null;
	private Settings settings;
	private String index , type ; 
	public  BulkProcessor bulkProcessor;
	private String lastDocID = null; 
	
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
	
	public void clientClose()
	{
		this.client.close();
	}
	
	
	

	@SuppressWarnings("deprecation")
	public  void add(TrainingDataEntry trainingDataEntry) {
	     try 
	     {	 
	    	 client.prepareIndex(index, type, id+"")
	    			 .setSource(gson.toJson(trainingDataEntry)).get();
		 }catch (Exception e) {
	    	 throw new NoNodeAvailableException("[add]: Error occurred while creating record");
	     }
	     id++;
	}
	
	
	@SuppressWarnings("deprecation")
	public  void add(Protein protein) {
	     try 
	     {	 
	    	 client.prepareIndex(index, type, id+"")
	    			 .setSource(gson.toJson(protein)).get();
		 }catch (Exception e) {
	    	 throw new NoNodeAvailableException("[add]: Error occurred while creating record");
	     }
	     id++;
	}
	
	@SuppressWarnings("deprecation")
	public  void add(Vertex pcnEntry) {
	     try 
	     {	 
	    	 client.prepareIndex(index, type, id+"")
	    			 .setSource(gson.toJson(pcnEntry)).get();
		 }catch (Exception e) {
	    	 throw new NoNodeAvailableException("[add]: Error occurred while creating record");
	     }
	     id++;
	}
	
	
		public void updateDocument(int hamming,int index) throws IOException, InterruptedException, ExecutionException
		{
	
			UpdateRequest updateRequest = new UpdateRequest(this.index, type,index + "")
			        .doc(jsonBuilder()
			            .startObject()
			                .field("Hamming_Distance", hamming)
			            .endObject());             
			client.update(updateRequest).get();
		}
		
		public void updateDocument(ArrayList<Node> neighbors,String id) throws IOException, InterruptedException, ExecutionException
		{
	
			UpdateRequest updateRequest = new UpdateRequest(this.index, type,id)
			        .doc(jsonBuilder()
			            .startObject()
			                .field("neighbors", neighbors)
			            .endObject());             
			client.update(updateRequest).get();
		}
		
		
		public Map<String, Object> get(int id)
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
		
		public synchronized void addToBulk(TrainingDataEntry trainingDataEntry)
		{

			id++;
			bulkProcessor.add(new IndexRequest(index, type, id+"")
			 .source(gson.toJson(trainingDataEntry)));		
		}
		
		public synchronized void addToBulk(Vertex pcnEntry)
		{

			id++;
			bulkProcessor.add(new IndexRequest(index, type, id+"")
			 .source(gson.toJson(pcnEntry)));		
		}
		
	
		
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
		
		public Vertex SearchPCNDB(long ProteinIndex , int  fragmentIndex )
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
				return fromMaptoNeighbors(results[0].getSource());
			}
			
			
			 }catch(ArrayIndexOutOfBoundsException e)
			 {
				 this.setLastDocID(null);
				 System.out.println(String.format("Protein Index: %d fragmentIndex: %d IS NOT FOUND!", ProteinIndex , fragmentIndex));
			 }
				
			 return null;
		}
		
		public ArrayList<Vertex> SearchForNeighborsInPCN(long ProteinIndex , int  fragmentIndex )
		{
			ArrayList<Vertex> neighbors = new ArrayList<Vertex>();
			
			QueryBuilder qb = QueryBuilders.boolQuery()
		                .must(QueryBuilders.termQuery("neighbors.m_protein",ProteinIndex))
		                .must(QueryBuilders.termQuery("neighbors.m_index",fragmentIndex)) 
		                ;

			 SearchResponse r = client.prepareSearch(this.index)
					 				  .setTypes(this.type)
					 				  .setQuery(qb)
					 				  .execute().actionGet();
			
			 
			 SearchHit[]  results = r.getHits().getHits();
			 Vertex node ; 
			 try{
			for(SearchHit hit :  results)
			{
				this.setLastDocID(hit.getId());
				node =  fromMaptoNeighbors(hit.getSource());
				if(check_exist(ProteinIndex, fragmentIndex, node))
					neighbors.add(new Vertex(node));
				
			}
			
			 }catch(ArrayIndexOutOfBoundsException e)
			 {
				 this.setLastDocID(null);
				 System.out.println(String.format("Protein Index: %d fragmentIndex: %d IS NOT FOUND!", ProteinIndex , fragmentIndex));
			 }
				
			 return neighbors;
		}
		
		private boolean check_exist(long ProteinIndex , int  fragmentIndex ,Vertex node)
		{
			for(Node n : node.neighbors)
			{
				if(n.getProteinIndex() == ProteinIndex && n.getFragmentIndex() == fragmentIndex)
					return true;
			}
			
			return false;
		}
		
		@SuppressWarnings("unchecked")
		public Vertex getVertexAt(int index)
		{ 
			Map<String, Object> map = get(index);
			Vertex neighbors = fromMaptoNeighbors(map);
			
			return neighbors;		 
		}
		
		
		private Vertex fromMaptoNeighbors(Map<String, Object> map )
		{

			 Vertex neighbors = new Vertex();
			 
			 neighbors.setProteinIndex((Integer)map.get("m_protein"));
			 neighbors.setFragmentIndex((Integer)map.get("m_index"));
			 neighbors.setNeighbors((ArrayList<Node>)map.get("neighbors"));
			 neighbors.setNeighbors(this.fromMapToNeighbors(neighbors));
			 
			 return neighbors;
		}
		
		
		@SuppressWarnings("unchecked")
		private ArrayList<Node> fromMapToNeighbors(Vertex neighbors)
		{
			Map<String, Object> nmap;
			ArrayList<Node> nodes = new ArrayList<Node>();
			
			for(int i = 0 ; i < neighbors.getNeighbors().size() ; i++)
			{
				nmap = (Map<String, Object>) neighbors.getNeighbors().get(i);
				if(nmap == null)
					return null;
				nodes.add(new Node( (Integer)nmap.get("m_protein"),(Integer)nmap.get("m_index")));
			}
			
			return nodes;
		}
		
		public long getCountOfDocInType( )
		{
			long ctr  = -1 ;
			SearchResponse searchResponse = client.prepareSearch(this.index).setTypes(this.type).execute().actionGet();
			ctr = searchResponse.getHits().getTotalHits();
			return ctr;
		}

		public String getLastDocID() {
			return lastDocID;
		}

		public void setLastDocID(String lastDocID) {
			this.lastDocID = lastDocID;
		}
		
		public void setID(long id )
		{
			this.id = id ;
			
		}

	}
