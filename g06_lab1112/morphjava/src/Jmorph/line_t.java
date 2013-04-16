
package Jmorph;


/** creates a line */
public class line_t {  
    point_t p1,p2;
    /** copies the parameter */
    public line_t(line_t _l) { 
        p1 = _l.p1;
        p2 = _l.p2;
    }
    
    /** creates a line between (x1,y1) and (x2,y2) */
    public line_t(double x1,double y1, double x2, double y2) { 
        p1 = new point_t(x1,y1);
        p2 = new point_t(x2,y2);
    }
    
    /** creates a line between points p1 and p2 */
    public line_t(point_t _p1, point_t _p2) { 
        p1 = new point_t(_p1);
        p2 = new point_t(_p2);
    }

	/** returns the square of length of the line(line is actually line segment) */
    double length2() { 
        return ((p2.x-p1.x)*(p2.x-p1.x))+((p2.y-p1.y)*(p2.y-p1.y));
    }
	
	/** returns the length of the line */
    double length() { 
        return Math.sqrt(length2()); 
    }
}
