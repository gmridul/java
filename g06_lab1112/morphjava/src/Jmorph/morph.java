package Jmorph;

import java.util.*;
import java.awt.Color;
import java.awt.image.*;
import javax.imageio.*;
import java.io.File;
import java.io.IOException;
        

/**This class performs all the morphing operations*/
public class morph {
    
    /** calculates the distance of foot of the perpendicular of point p from one end-point of the lin l*/
    public double compute_u(line_t l, point_t p) { 
        point_t pq = new point_t(l.p2.x - l.p1.x, l.p2.y - l.p1.y);
        point_t xp = new point_t(p.x - l.p1.x, p.y - l.p1.y);
        double u = (pq.x * xp.x + pq.y * xp.y) / l.length2();
        return u;
    }
    
    /** calculates the length of the perpendicular from p to line l */
    public double compute_v(line_t l, point_t p) {
        point_t pq = new point_t(l.p2.sub(l.p1));
        point_t xp = new point_t(p.sub(l.p1));
        point_t ppq = pq.perp();
        double v = (ppq.x * xp.x + ppq.y * xp.y) / l.length();
        return v;
    }
    /** find the point X' by going u distance along the line and then v distance perpendicular to line. */
    public point_t calc_dst_pixel(double u, double v, line_t l) {
        point_t qp = new point_t(l.p2.sub(l.p1));
        point_t pqp = qp.perp();
        double length = l.length();
        point_t X = new point_t();
        X.x = (l.p1.x + u * qp.x + v * pqp.x / length);
        X.y = (l.p1.y + u * qp.y + v * pqp.y / length);
        return X;
    }
    
    /** using all the above fucntions it morphs the image to the image which is decided by the framenum/totalframes*/
    public void morph_frame(int framenum, int totalframes,Vector<line_t> srclines, Vector<line_t> dstlines, BufferedImage simage, BufferedImage dimage, BufferedImage timage) throws IOException {
        float t = (float) (framenum) / (float) (totalframes);
        Vector<line_t> tmplines = new Vector<line_t>(); 
        for (int lnum = 0; lnum< srclines.size(); lnum++ ) {
            point_t p1 = new point_t(((1.0-t)*srclines.elementAt(lnum).p1.x + t * dstlines.elementAt(lnum).p1.x), 
                                        ((1.0-t)*srclines.elementAt(lnum).p1.y + t * dstlines.elementAt(lnum).p1.y));
            point_t p2 = new point_t(((1.0-t)*srclines.elementAt(lnum).p2.x + t * dstlines.elementAt(lnum).p2.x), 
                                        ((1.0-t)*srclines.elementAt(lnum).p2.y + t * dstlines.elementAt(lnum).p2.y));

            line_t tline = new line_t(p1,p2);
            tmplines.addElement(tline);
        }

        double a = 0.5;
        double b = 1.25;
        double p = 1.0;

        for (int row = 0; row < simage.getHeight(); row++) {
            for (int col = 0; col < simage.getWidth(); col++) {
                point_t temppt = new point_t(col, row);
                double wsum = 0;
                point_t sdsum = new point_t(0,0);
                point_t ddsum = new point_t(0,0);

                for (int lnum = 0; lnum< srclines.size(); lnum++) {
                    line_t tl = new line_t(tmplines.elementAt(lnum));
                    double u = compute_u(tl, temppt);
                    double v = compute_v(tl, temppt);

                    point_t sX = new point_t(calc_dst_pixel(u, v, srclines.elementAt(lnum)));
                    point_t sDi = new point_t(sX.sub(temppt));

                    point_t dX = new point_t(calc_dst_pixel(u, v, dstlines.elementAt(lnum)));
                    point_t dDi = new point_t(dX.sub(temppt));
	    
	    
        	    double dist = 0.0;
                    if (u >= 1.0) {
                        point_t p2x = new point_t(tl.p2.sub(temppt));
                        dist = p2x.length();
                    } else if (u <= 0.0) {
                        point_t p1x = new point_t(tl.p1.sub(temppt));
                        dist = p1x.length();
                    } else {
                        if(v<0) { v=-v;}
                        dist = v;
                    }

                    double weight = Math.pow((Math.pow(tl.length(), p) / (a + dist)), b);
                    sdsum.x += sDi.x * weight;
                    sdsum.y += sDi.y * weight;
                    ddsum.x += dDi.x * weight;
                    ddsum.y += dDi.y * weight;
                    wsum += weight;

                }

                point_t srcpt = new point_t((temppt.x + sdsum.x / wsum),(temppt.y + sdsum.y / wsum));	
                point_t dstpt = new point_t((temppt.x + ddsum.x / wsum),(temppt.y + ddsum.y / wsum));
	
	
                Color srccolor, dstcolor;
                        if ((srcpt.x > 0 && srcpt.x < simage.getWidth()) && (srcpt.y > 0 && srcpt.y < simage.getHeight())) {
                            srccolor = new Color(simage.getRGB((int) srcpt.x, (int)srcpt.y), true);
                        } else {
                            srccolor = new Color(simage.getRGB(col, row), true);
                        }

                        if ((dstpt.x > 0 && dstpt.x < simage.getWidth()) && (dstpt.y > 0 && dstpt.y < simage.getHeight())) {
                            dstcolor = new Color(dimage.getRGB((int)dstpt.x, (int)dstpt.y), true);
                        } else {
                            dstcolor = new Color(dimage.getRGB(col, row), true);
                        }

                        int r = (int) ((1 - t) * srccolor.getRed() + t * dstcolor.getRed());
                        int g = (int) ((1 - t) * srccolor.getGreen() + t * dstcolor.getGreen());
                        int bl = (int) ((1 - t) * srccolor.getBlue() + t * dstcolor.getBlue());
                        int al = (int) ((1 - t) * srccolor.getAlpha() + t * dstcolor.getAlpha());
                       /** sets the rgb value to the temp image */
                        timage.setRGB(col, row, ((al << 24) | (r << 16) | (g << 8) | bl));

            }
        }

        System.out.println("Morph Complete: "+framenum);
        saveImage save = new saveImage(timage, framenum); 
    }
}

