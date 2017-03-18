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
    
    public int colorAt(double x, double y) {
        if (x >= 0 && x < 1 && y >= 0 && y < 1) {
            return new Color((int)(255*(1-y)), (int)(255*(x)), 0).getRGB();
        } else {
            return -1;
        }
    }
    
    /**
     * Returns the (distance, color) from l1 to the rectangle along the ray that 
     * goes through l2, if such a ray intersects the rectangle. Otherwise,
     * -1 is returned.
     */
    public double[] distAndColorOnRay(double[] l1, double[] l2, double[] res) {
        double[] intersect = v();
        intersect = planeLineIntersection(p1, p2, p3, l1, l2, intersect);
        
        if (intersect == null) {
            return null;
        }
        
        double tDist = distFromPointToLine(intersect, p1, p2);
        double rDist = distFromPointToLine(intersect, p2, p3);
        double bDist = distFromPointToLine(intersect, p3, p4);
        double lDist = distFromPointToLine(intersect, p4, p1);
        
//        double[] b2 = max(v(), p1, p2, p3, p4);
//        double[] b1 = min(v(), p1, p2, p3, p4);
//        
//        if (VectorUtils.inBox(intersect, b1, b2, EPS)) {
//            res[0] = dist(l1, intersect);
//            res[1] = color;
//            
//            return res;
//        } else {
//            return null;
//        }
        
        if (tDist + bDist > dist(p1,p4) + EPS) {
            return null;
        } else if (rDist + lDist > dist(p1,p2) + EPS) {
            return null;
        }
        
        res[0] = dist(l1, intersect);
        res[1] = colorAt(lDist/dist(p1,p2), tDist/dist(p1,p4));
        
        return res;
//        
        // gotta find where on the rect the intersection is
//        double[] v1 = sub(p2, p1, v());
//        double[] v2 = sub(p3, p2, v());
//        double[] v3 = sub(intersect, p1, v());
        
//        double[] ans = {0, 0};
        
        //ans = MatrixUtils.solveEqn(v1[0], v2[0], v1[1], v2[1], v3[0], v3[1], ans); // uhh just trust me
//        if (ans == null) {
//            return null;
//        }
//        
//        int color = colorAt(ans[0], ans[1]);
//        if (color == -1) {
//            return null;
//        } else {
//            res[0] = dist(l1, intersect);
//            res[1] = color;
//            return res;
//        }
    }
}
