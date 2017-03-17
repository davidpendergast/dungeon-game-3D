package threedee;

import static threedee.MatrixUtils.*;
import static threedee.VectorUtils.*;

import java.awt.Color;

/**
 * Basic unit of rendering, a rectangle in 3D space
 */
public class Rect3D {
    double[] p1, p2, p3, p4;
    int color = Color.PINK.getRGB();
    
    public Rect3D (double[] p1, double[] p2, double[] p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        p4 = new double[] {0,0,0};
        
    }
    
    public int colorAt () {
        return 0;
    }
    
    /**
     * Returns the distance from l1 to the rectangle along the ray that 
     * goes through l2, if such a ray intersects the rectangle. Otherwise,
     * -1 is returned.
     * 
     * See http://mathworld.wolfram.com/Line-PlaneIntersection.html
     */
    public double distanceTo(double[] l1, double[] l2) {
       return 0;
    }

}
