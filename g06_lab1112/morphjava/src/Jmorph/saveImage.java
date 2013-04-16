package Jmorph;

import java.io.File;
import java.awt.image.*;
import javax.imageio.*;

/** This class saves image "timage"  corresponding to framenum*/
public class saveImage {
	/** the constructor performs the task of saving the image*/
	public saveImage(BufferedImage timage, int framenum) {
		String name = new String();
        	name = String.format("../images/test_%02d.png",framenum);	       
		/** saves the intermediate image
		*/
        	try {
	        	File f = new File(name);
	            ImageIO.write(timage,"png",f);
	        }
        	catch(Exception ex) {
	            System.out.println("unable to save image");
        	}
	}
}
