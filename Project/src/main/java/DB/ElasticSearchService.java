package DB;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ElasticSearchService
{
//
//	private static Long index = (long) 0;
//	private Client client = null;
//	
//	public ElasticSearchService(){
//	
//
//		try {
//			client = TransportClient.builder().build()
//					   .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw new NoNodeAvailableException("[ElasticSearchServiceImpl]: Error occurred while creating DB");
//		}
//		
//	
//	}
//	public Client getClient() {
//		return client;
//	}
//	
//	/**
//	 * Method to add document
//	 *
//	 * @param cardId
//	 *            - unique identifier
//	 * @param shopperData
//	 *            - JSON of {@link ShopperCard}
//	 */
//	public synchronized void add(String TrainingData) {
//	     try 
//	     {	 
//			getClient().prepareIndex("search.index.name","search. index type",index.toString()).	
//					setSource(TrainingData).setRefresh(true).execute().actionGet();
//		 }catch (Exception e) {
//	    	 throw new NoNodeAvailableException("[add]: Error occurred while creating record");
//	     }
//	     index++;
//	}
//	
//	/**
//	* Method to update document
//	 *
//	 * @param cardId
//	 *            - unique identifier
//	 * @param updatedRecord
//	 */
//	public void update(Long cardId, String updatedRecord) {
//	     try {
//			getClient().prepareUpdate("search.index.name", "search.index.type",index.toString()).
//			setDoc(updatedRecord).setRefresh(true).execute().actionGet();
//		}
//	     catch (Exception e) {
//		throw new NoNodeAvailableException("[update]: Error occurred while updating record", e);
//		}
//	}
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