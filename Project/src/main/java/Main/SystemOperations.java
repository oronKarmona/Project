package Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import Calculation.WeightFunctionCalculation;
import DB.ElasticSearchService;
import GUI.ClustersPanel;
import GUI.StructurePanel;
import GUI.TrainingDataPanel;
import Helpers.JSONhelper;
import Helpers.LinearRegressionUpdateHelper;
import Helpers.PajekFormatHelper;
import Helpers.WeightedGraphFile;
import PCN.WritePCNtoDB;
import ParallelBFS.CreateClusters;
import Protein.Protein;
import Table.TrainingData;
import TestingData.CalculateRmsdForEntry;
import TestingData.ReadResistencesFile;
import TestingData.TestingEntry;

/***
 * System Operations for the user 
 * @author Oron
 *
 */
public class SystemOperations 
{
	/***
	 * Depth of the BFS defined by the user 
	 * default value is 5 
	 */
	private static int bfsDepth = 5 ;
	/**
	 * Hamming distance threshold for trainingData
	 */
	private static double hammingDistance = 60;
	/**
	 * TrainingData class
	 */
	private static TrainingData trainingData = null;
	/**
	 * CreateClusters class
	 */
	private static CreateClusters bfs = null;
	/**
	 * Enums for progressbar update
	 * @author Oron
	 *
	 */
	public enum panelName{
		Structure , Training ,Cluster , Testing
	}
	/***
	 * Build Protein Structural Data
	 * @param elasticType - the name of the type of the elastic search
	 */
	public static void BuildProteinStructuralData(String elasticType, JProgressBar progressBar)
	{
		ArrayList<Protein>knownStructrePDB = getKnownStructureProteins(panelName.Structure);
		
		ElasticSearchService es = new ElasticSearchService("proteins" , elasticType );
		StructurePanel.updateProgress(0);
		StructurePanel.setParameters(0,knownStructrePDB.size());
		int ctr = 1 ; 
		for(Protein p  : knownStructrePDB)
		{
			StructurePanel.updateProgress(ctr);
			es.add(p);
			ctr++;
		}
		es.clientClose();
	}
	

	/***
	 * Build Training Data
	 * @param elasticType - the name of the type of the elastic search
	 */
	public static void BuildTrainingData(String elasticType, JProgressBar progressBar) 
	{
		ArrayList<Protein>knownStructrePDB = getKnownStructureProteins(panelName.Training);
		TrainingDataPanel.updateProgress(0);
		trainingData = new TrainingData(knownStructrePDB ,elasticType,hammingDistance);
	}
	/***
	 * Create Clusters
	 */
	public static void BuildClusters(String elastic_index,int index, JProgressBar progressBar)
	{
		ArrayList<Protein> knownStructrePDB =  getKnownStructureProteins(panelName.Cluster);

		ArrayList<Protein> uknownStructurePDB =  App.Read_unknown_structure_PDB("proteinsData\\ProteomDB");
		ClustersPanel.updateProgress(0);
		bfs = new CreateClusters(bfsDepth,uknownStructurePDB , knownStructrePDB, 20/3 , "pcn" , "data",
				elastic_index,95);		
		ClustersPanel.setParameters(0, index);
		for(int i = 0; i < index; i++)
		{
				System.out.println("Cluster " + i);
				ClustersPanel.updateProgress(i+1);
				bfs.setClusterStart(i);
				PajekFormatHelper pf = new PajekFormatHelper("cluster", i+"");
				LinearRegressionUpdateHelper helper = new LinearRegressionUpdateHelper("cluster", i, 4, 60, uknownStructurePDB, knownStructrePDB);
		}
	}
	
	/***
	 * Calculate Weight For clusters
	 * @param number_of_clusters - the number of clusters to be created 
	 */
	public static void BuildWeightedCluster(int number_of_clusters)
	{
		for(int i = 0 ; i < number_of_clusters ; i ++)
		{
			WeightFunctionCalculation wf = new WeightFunctionCalculation(i,1000000);
			WeightedGraphFile w = new WeightedGraphFile("cluster",i+"");
		}
	}
	
	/***
	 * Build Testing Data
	 * @param read_file_name - the name of the file with the resistance 
	 * @param cluster_type - the name of the cluster type to be manipulated 
	 * @throws FileNotFoundException 
	 */
	public static void BuildTestingData(String read_file_name, String cluster_type) throws FileNotFoundException
	{
	
			ArrayList<Protein> knownStructrePDB =  getKnownStructureProteins(panelName.Testing);
			CalculateRmsdForEntry c = new CalculateRmsdForEntry( read_file_name, "cluster", cluster_type, knownStructrePDB);
	
	}
	/***
	 * set Bfs depth
	 * @param depth - depth of the bfs  
	 */
	public static void setBfsDepth(int depth)
	{
		bfsDepth = depth ; 
	}
	/***
	 * get Bfs depth
	 * @param depth - depth of the bfs  
	 */
	public static String getBfsDepth()
	{
		return Integer.toString(bfsDepth);
	}

	
	
	/***
	 * SetHamming Distance threshold
	 * @param threshold - threshold defined by user
	 */
	public static void sedHammingDistanceThreshold(double threshold)
	{
		hammingDistance = threshold ; 
	}
	/***
	 * Parses the PCN file to the elasticSearch DB
	 * @throws FileNotFoundException
	 */
	public static void WritePCNtoElastic() throws FileNotFoundException
	{
		 if(!new File("pcn//pcn~0").exists() || !new File("pcn//PDB_Proteom_Map2~0").exists()){
		    	throw  new FileNotFoundException();
		    }
		 ClustersPanel.setParameters(0, 61);
		 ClustersPanel.updateProgress(0);
		 WritePCNtoDB w = new WritePCNtoDB("pcn//PDB_Proteom_Map2~", 61, "pcn", "data", false);
		 ClustersPanel.setParameters(0, 50);
		 ClustersPanel.updateProgress(0);
		 w = new WritePCNtoDB("pcn//pcn~", 50, "pcn", "data", true);
		
	}
	
	/***
	 * GetHamming Distance threshold
	 * @param threshold - threshold defined by user
	 */
	public static String getHammingDistanceThreshold()
	{
		return Double.toString(hammingDistance)+"%";

	}
	/***
	 * Get knownStructre proteins
	 * @return ArrayList with known structure protein
	 */
	private static ArrayList<Protein> getKnownStructureProteins( panelName name )
	{
		ArrayList<Protein> knownStructrePDB = JSONhelper.ReadJsonFile("proteinsData\\Output" , 20 , name);
		return knownStructrePDB;
	}
}
