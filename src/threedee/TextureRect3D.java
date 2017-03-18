package threedee;

public class TextureRect3D extends Rect3D {
    
    Texture texture;

    public TextureRect3D(Texture texture, double[] p1, double[] p2, double[] p3) {
        super(p1, p2, p3);
        this.texture = texture;
    }

}
