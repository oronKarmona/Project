package GUI;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/***
 * Main JFrame - initialize all panels and UI settings
 * Control the replacment of the panels
 * @author ליטף
 *
 */
public class Main extends JFrame {
	
//	   private JFrame Frame;

	private MainPanel m_mainPanel;
	private JMenuBar menuBar;
	private ParamsSettingsPanel m_paramsSettings ;
	private AboutPanel m_aboutPanel;
	private ProteinSearch m_proteinSearch;
	private ClusterSearch m_clusterSearch;
	//private ClusterCreate m_clusterCreate;
	private PredictiveWatch m_predictiveWatch;
	private TrainingDataPanel m_trainingMenu;
	private StructurePanel m_structureMenu;
	private ClustersPanel m_clustersMenu;
	private TestingPanel m_testingMenu;

	   public Main(){
		  
	      prepareGUI();
	      initPanels();
		  initMenu();
		  
	      changePanel(m_mainPanel);

	   }

	   private void initPanels() {
		   		 
		   //home
	        m_mainPanel = new MainPanel("main");
	       //about
	       m_aboutPanel = new AboutPanel("about");
	       //hamming
		   m_paramsSettings= new ParamsSettingsPanel("hamming");
		  
		   //Run App
		   m_trainingMenu = new TrainingDataPanel("training");
		   m_structureMenu = new StructurePanel("structure");
		   m_clustersMenu = new ClustersPanel("clusters");
		   m_testingMenu = new TestingPanel("testing");
		   
		   //protein data
		   m_proteinSearch = new ProteinSearch("protein search");
		   //cluster
		   m_clusterSearch = new ClusterSearch("cluster search");

		   //cluter watch
		   m_predictiveWatch = new PredictiveWatch("predictive watch");

		  setLayout(new BorderLayout());

		 
	}

	private void prepareGUI()
	{
		
//		setBackground(new Color(153, 255, 204));

      ImageIcon icon = new ImageIcon("proteinIcon.png");
      setIconImage(icon.getImage());
      setName("protein connectivity network");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
	}
	   public void initMenu(){

		   menuBar= new JMenuBar();

	      //create menus
		  settings();
		  runApp();
		  proteins();
		  cluster();
		  predictiveWatch();
	      menuBar.add(Box.createHorizontalGlue());
	      about();
		  home();
		   
	      //add menubar to the frame
	      setJMenuBar(menuBar);
	          
	   }
	   
	   private void runApp() {
		   
		   final JMenu RunMenu = new JMenu("Start DB's"); 
			  
		      JMenuItem trainingMenu = new JMenuItem("Create Training Data");
		      trainingMenu.addActionListener(new MenuItemListener(m_trainingMenu));
		      JMenuItem structureMenu = new JMenuItem("Create Structure Data");
		      structureMenu.addActionListener(new MenuItemListener(m_structureMenu));

		      JMenuItem clustersMenu = new JMenuItem("Create Clusters");
		      clustersMenu.addActionListener(new MenuItemListener(m_clustersMenu));
		      
		      JMenuItem testingMenu = new JMenuItem("Create Testing Data");
		      testingMenu.addActionListener(new MenuItemListener(m_testingMenu));

		     
		      RunMenu.add(trainingMenu);
		      RunMenu.addSeparator();
		      RunMenu.add(structureMenu);
		      RunMenu.addSeparator();
		      RunMenu.add(clustersMenu);
		      RunMenu.addSeparator();
		      RunMenu.add(testingMenu);
		      
		      
		      menuBar.add(RunMenu);
		
	}

	private void predictiveWatch() {

		   final JMenu proteinsMenu = new JMenu("Predict"); 
		  
	      JMenuItem predictiveWatchhMenu = new JMenuItem("Predictive Watch");
	      predictiveWatchhMenu.addActionListener(new MenuItemListener(m_predictiveWatch));

	     
	      proteinsMenu.add(predictiveWatchhMenu);
	      //settingsMenu.addSeparator();
	      
	      menuBar.add(proteinsMenu);
	   }

	private void proteins() {

		   final JMenu proteinsMenu = new JMenu("Proteins"); 
			  
		      JMenuItem proteinSearchMenu = new JMenuItem("Find Protein");
		      proteinSearchMenu.addActionListener(new MenuItemListener(m_proteinSearch));

		     
		      proteinsMenu.add(proteinSearchMenu);
		      //settingsMenu.addSeparator();
		      
		      menuBar.add(proteinsMenu);
		   
		
	}
	   private void cluster(){
		   final JMenu proteinsMenu = new JMenu("Cluters"); 
			  
		      JMenuItem proteinSearchMenu = new JMenuItem("Find cluter");
		      proteinSearchMenu.addActionListener(new MenuItemListener(m_clusterSearch));
		      proteinsMenu.add(proteinSearchMenu);
		      
		    
		      menuBar.add(proteinsMenu);
		   
		
	   }

	private void home() {
		   
		   final JMenu homeMenu = new JMenu();
		   ImageIcon home = new ImageIcon("homeIcon.png");
		   Image image = home.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
		   homeMenu.setIcon(new ImageIcon(image));		   
		   homeMenu.addMenuListener(new MenuListener() {
			
			@Override
			public void menuSelected(MenuEvent e) {
		        changePanel(m_mainPanel);

			}
			
			@Override
			public void menuDeselected(MenuEvent e) {				
			}
			
			@Override
			public void menuCanceled(MenuEvent e) {
			}
		});
		   
		   menuBar.add(homeMenu);

	}

	private void settings() {
		   
		  final JMenu settingsMenu = new JMenu("Settings"); 
		  
	      JMenuItem paramsMenuItem = new JMenuItem("Parameters");
	      paramsMenuItem.addActionListener(new MenuItemListener(m_paramsSettings));
	      
	      settingsMenu.add(paramsMenuItem);
//	      settingsMenu.addSeparator();
//	      settingsMenu.add(bfsMenuItem);
	      
	      //add menu to menubar
	      menuBar.add(settingsMenu);
	}



	private void about() {
		final JMenu aboutMenu = new JMenu("About");
		aboutMenu.addMenuListener(new MenuListener() {
			
			@Override
			public void menuSelected(MenuEvent e) {
				changePanel(m_aboutPanel);
				
			}
			
			@Override
			public void menuDeselected(MenuEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void menuCanceled(MenuEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
        menuBar.add(aboutMenu);       

		
	}

	class MenuItemListener implements ActionListener {
		
	    private JPanel panel;
	    private MenuItemListener(JPanel pnl) {
	        this.panel = pnl;
	    }
	    @Override

	    public void actionPerformed(ActionEvent e) {            
	        changePanel(panel);

	      }    
	   }
	
	private void changePanel(JPanel panel) {
	    getContentPane().removeAll();
	    if(panel.getName().equals("main") || panel.getName().equals("about"))
	    {
	    	getContentPane().add(panel, BorderLayout.CENTER);
	    }
	    else
	    {
		    getContentPane().add(panel,BorderLayout.WEST);

	    }
	    getContentPane().doLayout();
	    update(getGraphics());
	}

}
	