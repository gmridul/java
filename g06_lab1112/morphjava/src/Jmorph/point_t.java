
package Jmorph;


/** creates a point*/
public class point_t {
    
    double x,y;
    /**constructors*/
    public point_t(double _x, double _y) { 
        x = _x;
        y = _y;
    }
    
    public point_t() { x=0; y=0; }
    public point_t(point_t p) { x=p.x; y=p.y; }
    
    /** returns a point which is perpendicular to object's point with equal length of the position vector*/
    public point_t perp() {
        point_t p=new point_t(-y,x);
        return p;
    }
    
    /** returns the square of the length of the position vector*/
    public double length2() {
        return (this.x*this.x+this.y*this.y);
    }
    
    /** returns the length of the position vector*/
    public double length() { 
        return Math.sqrt(length2()); 
    }
    
    /** vector addition of the position vector of the passed point and object's point and stores it in the latter*/
    public point_t addeq(point_t _p) {
        this.x += _p.x; this.y += _p.y;
        return this;
    }
    
    /** vector subtraction of the position vector of the passed point from object's point and stores it in the latter*/
    public point_t subeq(point_t _p) {
        this.x = this.x-_p.x; this.y = this.y-_p.y;
        return this;
    }
    
    /** stores the passed point in the object's point*/
    public point_t eq(point_t _p) {
        this.x=_p.x; this.y=_p.y;
        return this; 
    }
    
    /** returns the vector addition of the passed point and the object's point*/
    public point_t add(point_t rhs) {
        point_t temp = new point_t(this);
        temp.addeq(rhs);
        return temp;
    }
    
    /** returns the vector subtraction of the passed point and the object's point*/
    public point_t sub(point_t rhs) {
        point_t temp = new point_t(this);
        temp.subeq(rhs);
        return temp;
    }
    
}
