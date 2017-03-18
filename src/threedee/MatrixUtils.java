package threedee;

import static threedee.VectorUtils.EPS;

public class MatrixUtils {
    
    public static double det(
            double a11, double a12,
            double a21, double a22) {
        return a11*a22 - a12*a21;
    }
    
    public static double det(
            double a11, double a12, double a13,
            double a21, double a22, double a23,
            double a31, double a32, double a33) {
        return  a11*det(a22, a23, a32, a33) - 
                a12*det(a21, a23, a31, a33) +
                a13*det(a21, a22, a31, a32);
    }
    
    public static double det(
            double a11, double a12, double a13, double a14,
            double a21, double a22, double a23, double a24,
            double a31, double a32, double a33, double a34,
            double a41, double a42, double a43, double a44) {
        
        return  a11*det(a22, a23, a24, a32, a33, a34, a42, a43, a44) -
                a12*det(a21, a23, a24, a31, a33, a34, a41, a43, a44) +
                a13*det(a21, a22, a24, a31, a32, a34, a41, a42, a44) -
                a14*det(a21, a22, a23, a31, a32, a33, a41, a42, a43);
    }
    
    
    /**
     * solves [c1|c2|c3]*x = y for x
     */
    public static double[] solveEqn(double[] c1, double[] c2, double[] c3, double[] y, double[] res) {
        double D = det( 
                c1[0], c2[0], c3[0],
                c1[1], c2[1], c3[1],
                c1[2], c2[2], c3[2]);
        if (Math.abs(D) < VectorUtils.EPS) {
            return null;
        }
        double Dx = det(
                y[0], c2[0], c3[0],
                y[1], c2[1], c3[1],
                y[2], c2[2], c3[2]);
        double Dy = det( 
                c1[0], y[0], c3[0],
                c1[1], y[1], c3[1],
                c1[2], y[2], c3[2]);
        double Dz = det( 
                c1[0], c2[0], y[0],
                c1[1], c2[1], y[1],
                c1[2], c2[2], y[2]);
        res[0] = Dx / D;
        res[1] = Dy / D;
        res[2] = Dz / D;
        return res;
    }
    
    /**
     * Solves the system for x1, x2:
     *  ax1 + bx2 = y1
     *  cx1 + dx2 = y2
     */
    public static double[] solveEqn(double a, double b, double c, double d, double y1, double y2, double[] res) {
        if (Math.abs(y1) < EPS && Math.abs(y2) > EPS) {
            res[0] = c / y2;
            res[1] = 0.5;
            return res;
        }
        
        double D = det(a,b,c,d);
        if (Math.abs(D) < EPS) {
            return null;
        }
        double Dx = det(y1,b,y2,d);
        double Dy = det(a,y1,c,y2);
        res[0] = Dx / D;
        res[1] = Dy / D;
        return res;
    }

}
