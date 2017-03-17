package testing;

import static org.junit.Assert.*;

import org.junit.Test;

import threedee.MatrixUtils;

public class TestMatrixUtils {
    
    private static double EPS = 0.000001;
    
    @Test
    public void testDet2x2() {
        double det = MatrixUtils.det(1, 2, 3, 4);
        assertEquals(-2.0, det, EPS);
    }
    
    @Test
    public void testDet3x3() {
        double det = MatrixUtils.det(1, 2, -5, -1, 3, 4, 9, -4, 2);
        assertEquals(213.0, det, EPS);
    }
    
    @Test
    public void testDet4x4() {
        double det = MatrixUtils.det(1,2,-5, 6,-1,3,4,-7,9,-4,2,6,1,2,3,4);
        assertEquals(1670.0, det, EPS);
    }
    
    @Test
    public void testDet4x4Binary() {
        double det = MatrixUtils.det(
                1.0, 1.0, 1.0, 0.0,
                0.0, 1.0, 0.0, 1.0,
                1.0, 0.0, 0.0, 1.0,
                0.0, 0.0, 1.0, 1.0);
        assertEquals(det, -3, EPS);
    }
}
