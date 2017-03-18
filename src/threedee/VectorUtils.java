package threedee;

import static threedee.MatrixUtils.*;

/**
 * All vectors are assumed to be 3 dimensional.
 */
public class VectorUtils {
    public static final double EPS = 0.000001;
    
    /**
     * @return <code>new double[] {0, 0, 0}</code>
     */
    public static double[] v() {
        return new double[] {0, 0, 0};
    }
    
    public static double[] v(double x, double y, double z) {
        return new double[] {x, y, z};
    }
    
    public static double[] v(double[] v) {
        return new double[] {v[0], v[1], v[2]};
    }
    
    public static double mag(double...v) {
        double res = 0;
        for (int i = 0; i < v.length; i++) {
            res += v[i]*v[i];
        }
        return Math.sqrt(res);
    }
    
    public static double[] normalize(double[] v, double[] res) {
        double mag = mag(v);
        if (mag < EPS) {
            return null;
        } else {
            res[0] = v[0] / mag;
            res[1] = v[1] / mag;
            res[2] = v[2] / mag;
            return res;
        }
    }
    
    public static double dist(double[] v1, double[] v2) {
        return mag(v2[0]-v1[0], v2[1]-v1[1], v2[2]-v1[2]);
    }
    
    public static double[] sub(double[] v1, double[] v2, double[] res) {
        for (int i = 0; i < res.length; i++) {
            res[i] = v1[i] - v2[i];
        }
        return res;
    }
    
    public static double[] add(double[] v1, double[] v2, double[] res) {
        for (int i = 0; i < res.length; i++) {
            res[i] = v1[i] + v2[i];
        }
        return res;
    }
    
    public static double[] mult(double[] v, double scalar, double[] res) {
        res[0] = v[0] * scalar;
        res[1] = v[1] * scalar;
        res[2] = v[2] * scalar;
        return res;
    }
    
    public static double[] neg(double[] v, double[] res) {
        for (int i = 0; i < res.length; i++) {
            res[i] = -v[i];
        }
        return res;
    }
    
    public static double[] copy(double[] v, double[] res) {
        for (int i = 0; i < res.length; i++) {
            res[i] = v[i];
        }
        return res;
    }
    
    public static double min(int axis, double[]...points) {
        double res = points[0][axis];
        for (double[] pt : points) {
            if (pt[axis] < res) {
                res = pt[axis];
            }
        }
        return res;
    }
    
    public static double max(int axis, double[]...points) {
        double res = points[0][axis];
        for (double[] pt : points) {
            if (pt[axis] > res) {
                res = pt[axis];
            }
        }
        return res;
    }
    
    public static double[] max(double[] res, double[]...points) {
        res[0] = max(0, points);
        res[1] = max(1, points);
        res[2] = max(2, points);
        return res;
    }
    
    public static double[] min(double[] res, double[]...points) {
        res[0] = min(0, points);
        res[1] = min(1, points);
        res[2] = min(2, points);
        return res;
    }
    
    public static double[] interpolate(double t, double[] v1, double[] v2, double[] res) {
        add(v1, mult(sub(v2, v1, res), t, res), res);
        return res;
    }
    
    public static double[] cross(double[] v1, double[] v2, double[] res) {
        double x = det(v1[1], v1[2], v2[1], v2[2]);
        double y = -det(v1[0], v1[2], v2[0], v2[2]);
        res[2] = det(v1[0], v1[1], v2[0], v2[1]);
        res[0] = x;
        res[1] = y;
        return res;
    }
    
    /**
     * Returns whether the point p1 is contained within the rectangular prism
     * defined by corners b1, b2.
     */
    public static boolean inBox(double[] pt, double[] b1, double[] b2, double eps) {
        for (int i = 0; i < 3; i++) {
            if (pt[i] < Math.min(b1[i], b2[i]) - eps || pt[i] > Math.max(b1[i], b2[i]) + eps) {
                return false;
            }
        }
        return true;
    }
    
    public static double[] rotateXY(double[] v, double rads, double[] res) {
        double cos = Math.cos(rads);
        double sin = Math.sin(rads);
        double newX = cos*v[0] - sin*v[1];
        res[1] = sin*v[0] + cos*v[1];
        res[0] = newX;
        res[2] = v[2];
        
        return res;
    }
    
    /**
     * See http://mathworld.wolfram.com/Line-PlaneIntersection.html
     */
    public static double[] planeLineIntersection(double[] p1, double[] p2, 
            double[] p3, double[] l1, double[] l2, double[] res) {
        double denomDet = det(1.0, 1.0, 1.0, 0.0,
                p1[0], p2[0], p3[0], l2[0]-l1[0],
                p1[1], p2[1], p3[1], l2[1]-l1[1],
                p1[2], p2[2], p3[2], l2[2]-l1[2]);
        if (Math.abs(denomDet) <= EPS) {
            return null; // plane is perpendicular to ray
        }
        double numDet = det(1.0, 1.0, 1.0, 1.0,
                p1[0], p2[0], p3[0], l1[0],
                p1[1], p2[1], p3[1], l1[1],
                p1[2], p2[2], p3[2], l1[2]);
        double t = - numDet / denomDet;
        if (t < 0) {
            return null; // plane is behind ray
        }
        res[0] = l1[0] + (l2[0] - l1[0])*t;
        res[1] = l1[1] + (l2[1] - l1[1])*t;
        res[2] = l1[2] + (l2[2] - l1[2])*t;
        
        return res;
    }
    
    public static String str(double[] v) {
       return str(v, 2);
    }
    
    /**
     * @param v vector to stringify.
     * @param n number of decimal places to display.
     */
    public static String str(double[] v, int n) {
        return String.format("{%."+n+"f, %."+n+"f, %."+n+"f}", v[0], v[1], v[2]);
    }

}
