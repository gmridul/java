
import Jmorph.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.util.*;
import javax.swing.filechooser.*;

public class MorphGUI extends Component implements ActionListener {
    JButton openButton1, openButton2, morph;
    JTextField framesButton;    
    static JFrame f, g;
    JFileChooser fc;
    
    public static void main(String[] args) {
	f = new JFrame("Select the image files");
	f.add (new MorphGUI());
        f.addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
	f.pack();
        f.setVisible(true);
	
	g = new JFrame("Image Morph");
	g.addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
    }

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
	f.add(buttonPanel, BorderLayout.PAGE_START);
	}

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
		f.setVisible(false);
		g.add(new Image("../images/" + openButton1.getLabel(), "../images/" + openButton2.getLabel(), Integer.parseInt(framesButton.getText())));
		g.pack();
		g.setVisible(true);
	    }
    }

    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = MorphGUI.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

}

class Image extends Component implements MouseListener, KeyListener{
    
    BufferedImage img1;
    BufferedImage img2;
    int numFrames;
    ArrayList<Integer> leftXClicks = new ArrayList<Integer>();
    ArrayList<Integer> leftYClicks = new ArrayList<Integer>();
    ArrayList<Integer> rightXClicks = new ArrayList<Integer>();
    ArrayList<Integer> rightYClicks = new ArrayList<Integer>();
    
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

    public Dimension getPreferredSize() {
        if (img1 == null || img2 == null) {
             return new Dimension(100,100);
        } else {
	    return new Dimension(img1.getWidth(null)+img2.getWidth(null), img1.getHeight(null));
       }
    }
    
    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
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

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

}
