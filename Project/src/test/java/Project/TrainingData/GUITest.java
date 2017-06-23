package Project.TrainingData;

import static org.junit.Assert.*;

import org.junit.Test;

import GUI.ProteinSearch;

public class GUITest 
{
	/***
	 * Checking if the program finds an exist protein  by checking the error label
	 * Expected - error label = ""
	 */
	@Test 
	public void ProteinSearchWindowsFindExistProteinTest()
	{
		String expected = "";
		ProteinSearch p = new ProteinSearch("Test");
		p.setproteinIDtextField("d10gsa1");
		p.pressButton();
		String result = p.getErrorLabel();
		assertTrue(expected.equals(result));
		
	}
	/***
	 * Checking if the program sets an error for a not exist protein  by checking the error label
	 * Expected - error label = "*No protein was found"
	 */
	@Test 
	public void ProteinSearchWindowsNotFindExistProteinTest()
	{
		String expected = "*No protein was found";
		ProteinSearch p = new ProteinSearch("Test");
		p.setproteinIDtextField("d10gsa1as");
		p.pressButton();
		String result = p.getErrorLabel();
		assertTrue(expected.equals(result));
		
	}
	
	/***
	 * Checking if the program sets an error for searching an exist protein with the elasticsearch server is down
	 * Pre condition - the elastic search server must be down, The search protein should be exist in C:\pdbstyle-2.06
	 * Expected - error label =  "*ElasticSearch server is not running!"
	 */
	@Test
	public void ProteinSearchWhenElasticSearchServerIsdownTest()
	{
		String expected = "*ElasticSearch server is not running!";
		ProteinSearch p = new ProteinSearch("Test");
		p.setproteinIDtextField("d10gsa1");
		p.pressButton();
		String result = p.getErrorLabel();
		if(result.equals("")) // server is up -> no error
			assertFalse(expected.equals(result));
		else // server is down -> error
			assertTrue(expected.equals(result));
	}
	
	/***
	 * Checking if the program sets an error for an illegal size of protein index  by checking the error label
	 * Expected - error label = "*No protein was found"
	 */
	@Test 
	public void ProteinSearchWindowsIllegalSizeProteinTest()
	{
		String expected = "*Protein is in too short";
		ProteinSearch p = new ProteinSearch("Test");
		p.setproteinIDtextField("");
		p.pressButton();
		String result = p.getErrorLabel();
		assertTrue(expected.equals(result));
		
	}
	
	/***
	 * Checking if the program returns the data of specified exist protein by index
	 * precondition - the protein exist in the database with its data in the tested index
	 * expected - the real data of the protein  
	 */
	@Test
	public void ProteinSearchWindowFindProteinByIndex()
	{
		String expected = "Campylobacter jejuni mkfetinqesiaklmeifyekvrkdkdlgpifnnaigtsdeewkehkakignfwagmllgegdyngqplkkhldlppfpqeffeiwlklfeeslnivyneemknvilqraqmiashfqnmlykyggh";
		ProteinSearch p = new ProteinSearch("Test");
		p.setproteinIndextextField("2");
		p.pressSerchIndexButton();
		String result = p.getProteinData();
		assertTrue(expected.equals(result));
	}
	
	/***
	 * Checking if the program return error in the error label in case of illegal protein index 
	 * expected - error label  = "Index must be a number between 0 - 30874"
	 */
	@Test
	public void ProteinSearchWindowFindProteinByIllegalIndex()
	{
		String expected = "Index must be a number between 0 - 30874";
		ProteinSearch p = new ProteinSearch("Test");
		p.setproteinIndextextField("-12");
		p.pressSerchIndexButton();
		String result = p.getIndexErrorLabelText();
		assertTrue(expected.equals(result));
	}
	
	/***
	 * Checking if the program return error in the error label in case of illegal protein index 
	 * expected - error label  = "Index must be a number between 0 - 30874"
	 */
	@Test
	public void ProteinSearchWindowFindProteinByIllegalStringIndex()
	{
		String expected = "Index must be a number between 0 - 30874";
		ProteinSearch p = new ProteinSearch("Test");
		p.setproteinIndextextField("text msg");
		p.pressSerchIndexButton();
		String result = p.getIndexErrorLabelText();
		assertTrue(expected.equals(result));
	}
	
	/***
	 * Checking if the program return error in the error label in case of illegal protein index 
	 * expected - error label  = "Index must be a number between 0 - 30874"
	 */
	@Test
	public void ProteinSearchWindowFindProteinByIllegalEmptyStringIndex()
	{
		String expected = "Index must be a number between 0 - 30874";
		ProteinSearch p = new ProteinSearch("Test");
		p.setproteinIndextextField("");
		p.pressSerchIndexButton();
		String result = p.getIndexErrorLabelText();
		assertTrue(expected.equals(result));
	}
}
