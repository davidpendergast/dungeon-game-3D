package threedee;

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
        
        return pixels;
    }
}
