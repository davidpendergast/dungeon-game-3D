package testing;

import static org.junit.Assert.*;
import static threedee.VectorUtils.*;

import org.junit.Test;


public class TestVectorUtils {
    
    @Test
    public void testPlaneLineIntersection() {
        double[] p1 = {0, 1, 0};
        double[] p2 = {1, 0, 0};
        double[] p3 = {0, 0, 1};
        double[] l1 = {0, 0, 0};
        double[] l2 = {1, 1, 1};
        
        double[] res = {0, 0, 0};
        double[] expected = {1/3.0, 1/3.0, 1/3.0};
        
        planeLineIntersection(p1, p2, p3, l1, l2, res);
        
        assertArrayEquals(expected, res, EPS);
    }
    
    @Test
    public void testRotateXY() {
        double[] v = {1,1,1};
        rotateXY(v, Math.PI, v);
        assertArrayEquals(new double[] {-1,-1, 1}, v, EPS);
    }
    
    @Test
    public void testStr() {
        double[] v = v();
        assertEquals("{0.0, 0.0, 0.0}", str(v, 1));
    }
    
    private static void assertArrayEquals(double[] expected, double[] res, double eps) {
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], res[i], eps);
        }
    }

}
