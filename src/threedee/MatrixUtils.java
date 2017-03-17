package threedee;

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
        System.out.println(a11+"\t"+a12+"\t"+a13+"\t"+a14);
        System.out.println(a21+"\t"+a22+"\t"+a23+"\t"+a24);
        System.out.println(a31+"\t"+a32+"\t"+a33+"\t"+a34);
        System.out.println(a41+"\t"+a42+"\t"+a43+"\t"+a44);
        
        return  a11*det(a22, a23, a24, a32, a33, a34, a42, a43, a44) -
                a12*det(a21, a23, a24, a31, a33, a34, a41, a43, a44) +
                a13*det(a21, a22, a24, a31, a32, a34, a41, a42, a44) -
                a14*det(a21, a22, a23, a31, a32, a33, a41, a42, a43);
    }

}