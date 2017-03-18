package threedee;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Texture {
    public int[] pixels;
    public String loc;
    public int width;
    public int height;
    
    public Texture(String location) {
        this.loc =location;
        load();
    }
    
    public int getColor(double x, double y) {
        int ix = (int)(x * width);
        int iy = (int)(y * height);
        
        if (ix >= 0 && ix < width && iy >= 0 && iy < height) {
            return pixels[iy * width + ix];
        } else {
            return 0;
        }
    }
    
    public int getColor(int x, int y) {
        return pixels[y*width + x];
    }
    
    private void load() {
        try {
            BufferedImage image = ImageIO.read(new File(loc));
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
