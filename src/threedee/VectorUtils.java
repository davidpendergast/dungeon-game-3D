package threedee;

import static threedee.MatrixUtils.*;

/**
 * All vectors are assumed to be 3 dimensional.
 */
public class VectorUtils {
    public static final double EPS = 0.000001;
    
    public static double mag(double...v) {
        double res = 0;
        for (int i = 0; i < v.length; i++) {
            res += v[i]*v[i];
        }
        return Math.sqrt(res);
    }
    
    public static double dist(double[] v1, double[] v2) {
        return mag(v2[0]-v1[0], v2[1]-v1[1], v2[2]-v1[2]);
    }
    
    public static void sub(double[] v1, double[] v2, double[] res) {
        for (int i = 0; i < res.length; i++) {
            res[i] = v1[i] - v2[i];
        }
    }
    
    public static void add(double[] v1, double[] v2, double[] res) {
        for (int i = 0; i < res.length; i++) {
            res[i] = v1[i] + v2[i];
        }
    }
    
    public static void neg(double[] v, double[] res) {
        for (int i = 0; i < res.length; i++) {
            res[i] = -v[i];
        }
    }
    
    public static void copy(double[] v, double[] res) {
        for (int i = 0; i < res.length; i++) {
            res[i] = v[i];
        }
    }
    
    public static void planeLineIntersection(double[] p1, double[] p2, 
            double[] p3, double[] l1, double[] l2, double[] res) {
        double denomDet = det(1.0, 1.0, 1.0, 0.0,
                p1[0], p2[0], p3[0], l2[0]-l1[0],
                p1[1], p2[1], p3[1], l2[1]-l1[1],
                p1[2], p2[2], p3[2], l2[2]-l1[2]);
        if (Math.abs(denomDet) <= EPS) {
            return; // err
        }
        double numDet = det(1.0, 1.0, 1.0, 1.0,
                p1[0], p2[0], p3[0], l1[0],
                p1[1], p2[1], p3[1], l1[1],
                p1[2], p2[2], p3[2], l1[2]);
        double t = - numDet / denomDet;
        res[0] = l1[0] + (l2[0] - l1[0])*t;
        res[1] = l1[1] + (l2[1] - l1[1])*t;
        res[2] = l1[2] + (l2[2] - l1[2])*t;
    }

}
