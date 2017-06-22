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
		p.update_TextField("d10gsa1");
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
		p.update_TextField("d10gsa1as");
		p.pressButton();
		String result = p.getErrorLabel();
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
		p.update_TextField("");
		p.pressButton();
		String result = p.getErrorLabel();
		assertTrue(expected.equals(result));
		
	}

}
