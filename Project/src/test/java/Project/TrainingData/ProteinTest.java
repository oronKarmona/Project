package Project.TrainingData;

import static org.junit.Assert.*;

import org.junit.Test;

import Protein.Protein;

public class ProteinTest 
{

	/***
	 * Checking the number of fragments in size of 20 aa in a given protein 
	 * precondition - the protein object should obtain aa string longer than 20 aa
	 * expected result - count of fragments =  size of aa string - 20 
	 */
	@Test
	public void CorrectFragmentCountTest()
	{
		Protein p = new Protein();
		p.setAminoAcids("mkfetinqesiaklmeifyekvrkdkdlgpifnnaigtsdeewkehkakignfwagmllgegdyngqplkkhldlppfpqeffeiwlklfeeslnivyneemknvilqraqmiashfqnmlykyggh");
		int expected = p.getAminoAcids().length() - 19 ;
		int result = p.getFragment_count();
		assertTrue(expected == result);
	}
	
	/***
	 * Checking for fragments in the protein in case of aa string is smaller than 20 
	 * precondition - the protein should obtain an aa string shorter than 20 aa
	 * expected result - count of fragments = 0 
	 */
	@Test
	public void ZeroFragmentCountTest()
	{
		Protein p = new Protein();
		p.setAminoAcids("a");
		int expected = 0;
		int result = p.getFragment_count();
		assertTrue(expected == result);
	}
	
	/***
	 * Checking the division to 20 amino acid long by obtaining the first fragment 
	 * precondition - protein has aa string longer than 20 aa
	 * expected result - first fragment is equal to the first 20 aa of the aa string
	 */
	@Test
	public void LegalDivisiontoFragmentTest()
	{
		Protein p = new Protein();
		p.setAminoAcids("mkfetinqesiaklmeifyekvrkdkdlgpifnnaigtsdeewkehkakignfwagmllgegdyngqplkkhldlppfpqeffeiwlklfeeslnivyneemknvilqraqmiashfqnmlykyggh");
		String expected = p.getAminoAcids().substring(0,20);
		String result = p.GetFragments(0);
		assertTrue(expected.equals(result));
	}
	
	/***
	 * Checking the division to 20 amino acid long by obtaining a not exist fragment
	 * precondition - protein has aa string longer than 20 aa
	 * expected result - null object
	 */
	@Test
	public void IllegalDivisiontoFragment1Test()
	{
		Protein p = new Protein();
		p.setAminoAcids("mkfetinqesiaklmeifyekvrkdkdlgpifnnaigtsdeewkehkakignfwagmllgegdyngqplkkhldlppfpqeffeiwlklfeeslnivyneemknvilqraqmiashfqnmlykyggh");
		String expected = null;
		String result = p.GetFragments(p.getFragment_count() + 1);
		assertTrue(expected ==  result);
	}
	
	/***
	 * Checking the division to 20 amino acid long by obtaining a not exist fragment
	 * precondition - protein has aa string less than 20 aa
	 * expected result - null object
	 */
	@Test
	public void IllegalDivisiontoFragment2Test()
	{
		Protein p = new Protein();
		p.setAminoAcids("1");
		String expected = null;
		String result = p.GetFragments(1);
		assertTrue(expected ==  result);
	}
}
