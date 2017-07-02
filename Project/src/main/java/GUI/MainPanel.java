package GUI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
 /***
  * MainPanel - home panel - show image.
  * @author Litaf
  *
  */
public class MainPanel extends JPanel{
	
    private Image m_image;
	private Image image;

    public MainPanel(String name) {

		this.setName(name);
		Image image=null;
	       try{
	            image = ImageIO.read(Main.class.getResource("/resources/background.jpeg"));
	        }
	        catch (IOException e){
	            e.printStackTrace();
	        }
		
    	m_image = image;	
    }
	   private void initBackground() {

		   image=null;
	       try{
	            image = ImageIO.read(Main.class.getResource("/resources/panelbackground.jpg"));
	        }
	        catch (IOException e){
	            e.printStackTrace();
	        }				
		}
	@Override
    public void paintComponent(Graphics G) {
        super.paintComponent(G);

        Image scaledImage = m_image.getScaledInstance(this.getWidth(),this.getHeight(),Image.SCALE_SMOOTH);

        G.drawImage(scaledImage,  0, 0, this.getWidth(), this.getHeight(), null);
    }
}
