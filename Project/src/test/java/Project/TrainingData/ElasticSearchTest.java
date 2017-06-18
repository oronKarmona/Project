package Project.TrainingData;

import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.*;
import DB.ElasticSearchService;

public class ElasticSearchTest 
{

		ElasticSearchService es ; 
		
		
		/***
		 * getting specific protein from the DB
		 * precondition - index and type are exist with protein index to be found
		 * expected - Map<String , Object > diffrent then null
		 */
		@Test
		public void getProteinTrueTest()
		{
			Map<String, Object> result ; 
			es = new ElasticSearchService("proteins","known_structure");
			int proteinIndex = 0;
			result = es.get(proteinIndex);
			es.clientClose();
			assertTrue(result != null );
		}
		
		
		/***
		 * getting specific protein from the DB
		 * precondition - index and type are exist without protein index to be found
		 * expected -  null
		 */
		@Test
		public void getProteinFalseTest()
		{
			Map<String, Object> result ; 
			es = new ElasticSearchService("proteins","known_structure");
			int proteinIndex = -1;
			result = es.get(proteinIndex);
			es.clientClose();
			assertTrue(result == null );
		}
		
		
}
