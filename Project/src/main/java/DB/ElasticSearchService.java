package DB;
//https://medium.com/@adnanxteam/how-to-install-elasticsearch-5-and-kibana-on-homestead-vagrant-60ea757ff8c7
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.get.GetField;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import PCN.Neighbors;
import Table.TrainingDataEntry;
import static org.elasticsearch.common.xcontent.XContentFactory.*;
public class ElasticSearchService
{
	
	private static Long id = (long) 0;
	private static TransportClient client = null;
	private static Gson gson = null;
	private static IndexResponse response;
	private Settings settings;
	private BulkRequestBuilder bulkRequest = null;  
	private String index , type ; 
	
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
			
			bulkRequest = client.prepareBulk() ; 
			
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
	    	 response = client.prepareIndex(index, type, id+"")
	    			 .setSource(gson.toJson(trainingDataEntry)).get();
		 }catch (Exception e) {
	    	 throw new NoNodeAvailableException("[add]: Error occurred while creating record");
	     }
	     id++;
	}
	
	@SuppressWarnings("deprecation")
	public  void add(Neighbors pcnEntry) {
	     try 
	     {	 
	    	 response = client.prepareIndex(index, type, id+"")
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
		
		
		public Map<String, Object> get(int index)
		{
			Map<String, Object> map = null;
			try{
			GetResponse response = client.prepareGet(this.index, type,index + "").get();
			map = response.getSource();
			}catch(Exception e )
			{
				return null;
			}
			return map;
		}
		
		
		
		public void BulkWrite()
		{
			BulkResponse bulkResponse = bulkRequest.get();
			if (bulkResponse.hasFailures()) {
			    // process failures by iterating through each bulk response item
			}
		}
//	
//	/**
//	 * Method to delete shopper Card   record
//	 *
//	 * @param cardId
//	 *            - unique identifier
//	 */
//	public void delete(Long cardId) {
//	    try {
//		 DeleteResponse response = getClient().prepareDelete("search.index.name", "search.index.type" , index.toString()).execute().actionGet();
//		}
//	    catch (Exception e) {
//	    	throw new NoNodeAvailableException("[delete]: Error occurred while updating record", e);
//		}
//	}
//	
//	/**
////	 * Method to perform search
////	 *
////	 * @param searchQuery
////	 *            - search query
////	 * @param sortBuilder
////	 *            - sort builder to perform data sorting
////	 * @return - list of matching records
////	 * @throws Exception
////	 */
//	public List<String> performFieldSearch(QueryBuilder searchQuery, SortBuilder sortBuilder) throws Exception {
//		try {
//	CountResponse countresponse = client.prepareCount(I18NUtility.getMessage("search.index.name"))
//	.setQuery(QueryBuilders.matchAllQuery()).execute().actionGet();
//	int recordCount = (int) countresponse.getCount();
//	
//	SearchResponse response = getClient().prepareSearch(I18NUtility.getMessage("search.index.name"))
//		.setTypes(I18NUtility.getMessage("search.index.type")).setSearchType(SearchType.QUERY_AND_FETCH).setQuery(searchQuery).addSort(sortBuilder). .setSize(recordCount) .execute().actionGet();
//	
//	SearchHit[] searchHits = response.getHits().getHits();
//	List<String> searchResults = new ArrayList<String>();
//	 for (SearchHit searchHit : searchHits) {
//		String searchResult = searchHit.getSourceAsString();
//		searchResults.add(searchResult);
//	       }
//	return searchResults;
//	}
//	catch (Exception e) {
//		throw new NoNodeAvailableException("Error occurred while  searching", e.getCause());
//		}
//	     }
//	}
//	
//	/**
//	 * Builds search query based on search keyword and filed name
//	 *
//	 * @param searchKeyword
//	 *            - search keyword
//	 * @param fieldName
//	 *            - field name on which search has to be performed
//	*/
//	public BoolQueryBuilder buildSearchQuery(String searchKeyword, String fieldName){
//		BoolQueryBuilder searchQuery = QueryBuilders.boolQuery ();
//		searchQuery.must (QueryBuilders.queryString(searchString).phraseSlop(searchString 	.length()).field(fieldName));
//		searchQuery.mustNot(QueryBuilders.termQuery("whetherActive", Boolean.FALSE));
//		return searchQuery;
//	    }
	}