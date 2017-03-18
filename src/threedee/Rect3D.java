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
        p4 = v();
        add(sub(p1, p2, p4), p3, p4);
    }
    
    public int colorAt () {
        return 0;
    }
    
    /**
     * Returns the distance from l1 to the rectangle along the ray that 
     * goes through l2, if such a ray intersects the rectangle. Otherwise,
     * -1 is returned.
     */
    public double distanceTo(double[] l1, double[] l2) {
        double[] intersect = v();
        intersect = planeLineIntersection(p1, p2, p3, l1, l2, intersect);
        
        if (intersect == null) {
            return -1;
        }
        
        double[] b1 = min(v(), p1, p2, p3, p4);
        double[] b2 = max(v(), p1, p2, p3, p4);
        
        if (inBox(intersect, b1, b2, 0.0001)) { // TODO: this doesn't generally work
            return dist(l1, intersect);
        } else {
            return -1;
        }
    }
    
}
