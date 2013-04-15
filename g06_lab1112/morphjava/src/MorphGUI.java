
import Jmorph.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.util.*;
import javax.swing.filechooser.*;

/**
 * The main GUI class that displays the buttons
 * for loading the two images and inputting the number of Frames
 */
public class MorphGUI extends Component implements ActionListener {
    /**
     * openButton1 loads the first image
     */
    JButton openButton1;
    /**
     * openButton2 loads the second image
     */ 
    JButton openButton2;
    /**
     * morph stands for the load option
     */
    JButton morph;
    /**
     * framesButton gets the value of the number of frames
     */
    JTextField framesButton;
    /**
     * f displays the first jframe that shows the four buttons
     */
    static JFrame init_frame;
    /**
     * image_frame displays the second jframe that shows the images
     */
    static JFrame image_frame;
    /**
     * fc is the filechooser wizard
     */
    JFileChooser fc;
    
    /**
     * the main function that coordinates the process
     */
    public static void main(String[] args) {
	init_frame = new JFrame("Select the image files");
	init_frame.add (new MorphGUI());
        init_frame.addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
	init_frame.pack();
        init_frame.setVisible(true);
	
	image_frame = new JFrame("Image Morph");
	image_frame.addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
    }

    /**
     * the constructor function of MorphGUI
     * builds the four buttons in the f jpanel
     */
    public MorphGUI() {
	fc = new JFileChooser("../images");
	openButton1 = new JButton("Select source image");
        openButton1.addActionListener(this);
	openButton2 = new JButton("Select destination image");
        openButton2.addActionListener(this);
	morph = new JButton("Load");
	morph.addActionListener(this);
	framesButton = new JTextField("numFrames");
	framesButton.addActionListener(this);
	JPanel buttonPanel = new JPanel();
        buttonPanel.add(openButton1);
	buttonPanel.add(openButton2);
	buttonPanel.add(morph);
	buttonPanel.add(framesButton);
	init_frame.add(buttonPanel, BorderLayout.PAGE_START);
	}

    /**
     * action listener for jpanel init_frame
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == openButton1) {
	    int returnVal = fc.showOpenDialog(MorphGUI.this);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
		openButton1.setLabel(file.getName());
            }
	}
	if (e.getSource() == openButton2) {
	    int returnVal = fc.showOpenDialog(MorphGUI.this);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
		openButton2.setLabel(file.getName());
            }
	}

	if (e.getSource() == morph)
	    {
		init_frame.setVisible(false);
		image_frame.add(new Image("../images/" + openButton1.getLabel(), "../images/" + openButton2.getLabel(), Integer.parseInt(framesButton.getText())));
		image_frame.pack();
		image_frame.setVisible(true);
	    }
    }
}

/**
 * class responsible for handling events for the second jpanel that displays the images
 */
class Image extends Component implements MouseListener, KeyListener{
    /**
     * the first image
     */
    BufferedImage img1;
    /**
     * the second image
     */
    BufferedImage img2;
    /**
     * the number of frames
     */
    int numFrames;
    /**
     * stores the location of the x-coordinates of the left-clicks done on the image
     */
    ArrayList<Integer> leftXClicks = new ArrayList<Integer>();
    /**
     * stores the location of the y-coordinates of the left-clicks done on the image
     */
    ArrayList<Integer> leftYClicks = new ArrayList<Integer>();
    /**
     * stores the location of the x-coordinates of the right-clicks done on the image
     */
    ArrayList<Integer> rightXClicks = new ArrayList<Integer>();
    /**
     * stores the location of the y-coordinates of the right-clicks done on the image
     */
    ArrayList<Integer> rightYClicks = new ArrayList<Integer>();
    
    /**
     * overrides the paint method of the Component class
     */
    public void paint(Graphics g) {
	Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(img1, 0, 0, null);
	g2d.drawImage(img2, img1.getWidth(), 0, null);
	Color red = new Color (204,0,0);
	g2d.setStroke(new BasicStroke(4));
	g2d.setColor(red);
	for (int i = 0, size = leftXClicks.size(); i + 1 < size; i += 2)
	    {
		g2d.drawLine (leftXClicks.get(i), leftYClicks.get(i), leftXClicks.get(i+1), leftYClicks.get(i+1));
	    }

	Color green = new Color (0,204,0);
	g2d.setColor(green);
	for (int i = 0, size = rightXClicks.size(); i + 1 < size; i += 2)
	    {
		g2d.drawLine (rightXClicks.get(i), rightYClicks.get(i), rightXClicks.get(i+1), rightYClicks.get(i+1));
	    }

    }

    /**
     * the constructor of the Image class
     * takes as input the path for the two images
     * and the number of the frames required
     */
    public Image(String imgPath1, String imgPath2, int _numFrames) {
       try {
           img1 = ImageIO.read(new File(imgPath1));
	   img2 = ImageIO.read(new File(imgPath2));
	   numFrames = _numFrames;
	   addMouseListener(this);
	   addKeyListener(this);
	   setFocusable(true);
       } catch (IOException e) {
	   System.out.println("image not found");
       }

    }

    /**
     * overrides the getPreferredSize function of the Component class
     */
    public Dimension getPreferredSize() {
        if (img1 == null || img2 == null) {
             return new Dimension(100,100);
        } else {
	    return new Dimension(img1.getWidth(null)+img2.getWidth(null), img1.getHeight(null));
       }
    }
    
    /**
     * don't do anything when the mouse is pressed
     */
    public void mousePressed(MouseEvent e) {
    }
    /**
     * don't do anything when the mouse is released
     */
    public void mouseReleased(MouseEvent e) {
    }
    /**
     * don't do anything when the mouse enters the jpanel
     */
    public void mouseEntered(MouseEvent e) {
    }
    /**
     * don't do anything when the mouse leaves the jpanel
     */    
    public void mouseExited(MouseEvent e) {
    }
    
    /**
     * captures the moused clicked event
     */
    public void mouseClicked(MouseEvent e) {
	if (e.getButton() == 1)
	    {
		leftXClicks.add(e.getX());
		leftYClicks.add(e.getY());
	    }

	if (e.getButton() == 3)
	    {
		rightXClicks.add(e.getX());
		rightYClicks.add(e.getY());
	    }

	repaint();
    }

    /**
     * captures the key typed event
     */
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'm') {
	    Vector <line_t> srclines = new Vector <line_t> (), dstlines = new Vector <line_t> ();
	    for (int i = 0, size = leftXClicks.size(); i+1 < size; i += 2) {
		srclines.add (new line_t (leftXClicks.get(i), leftYClicks.get(i), leftXClicks.get(i+1), leftYClicks.get(i+1)));
	    }
	    for (int i = 0, size = rightXClicks.size(); i+1 < size; i += 2) {
		dstlines.add (new line_t (rightXClicks.get(i), rightYClicks.get(i), rightXClicks.get(i+1), rightYClicks.get(i+1)));
	    }
	    morph M = new morph ();
	    BufferedImage timage = new BufferedImage (img1.getWidth(), img1.getHeight(), BufferedImage.TYPE_INT_ARGB);
	    for (int i = 0; i < numFrames; ++i) {
		try {
		    M.morph_frame (i, numFrames, srclines, dstlines, img1, img2, timage);
		}
		catch (java.io.IOException ioe)
		    {
			System.out.println(ioe);
		    }
	    }
	}   
    }

    /**
     * don't do anything when the key is pressed
     */
    public void keyPressed(KeyEvent e) {
    }

    /**
     * don't do anything when the key is released
     */
    public void keyReleased(KeyEvent e) {
    }

}
