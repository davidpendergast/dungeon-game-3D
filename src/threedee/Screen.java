package threedee;

import static threedee.VectorUtils.*;

import java.awt.Color;

public class Screen {
    public int width, height;
    
    public Screen(int w, int h) {
        width = w;
        height = h;
    }
    
    public int[] update(Camera camera, Dungeon dungeon, int pixels[]) {
        for (int n = 0; n < pixels.length/2; n++) {
            pixels[n] = Color.DARK_GRAY.getRGB();
        }
        for (int n = pixels.length/2; n < pixels.length; n++) {
            pixels[n] = Color.GRAY.getRGB();
        }
        
        double[] pt1 = v();
        double[] pt2 = v();
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int i = y * width + x;
                
                interpolate(x / (double)width, camera.pTopLeft, camera.pTopRight, pt1);
                interpolate(y / (double)height, camera.pTopLeft, camera.pBottomLeft, pt2);
                
                add(pt1, sub(pt2, camera.pTopLeft, pt2), pt1);
                
                for (Rect3D r : dungeon.polygons) {
                    if (r.distanceTo(camera.pos, pt1) > -1) {
                        pixels[i] = r.color;
                    }
                }
            }
        }
        
        return pixels;
    }
}
