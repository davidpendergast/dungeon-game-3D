package building;

import java.awt.Image;

public interface WorldDrawer {
    public void draw(Image canvas);
    public void move(int xPix, int yPix);
}
