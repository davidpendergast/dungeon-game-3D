package threedee;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Dungeon {
    
    public List<Rect3D> polygons = new ArrayList<Rect3D>();
    
    public Dungeon() {
        Rect3D r1 = new Rect3D(
                VectorUtils.v(10, -20, 3),
                VectorUtils.v(10, 40, 3),
                VectorUtils.v(10, 40, 0));
        r1.color = Color.RED.getRGB();
        
        Rect3D r2 = new Rect3D(
                VectorUtils.v(6, 0, 0),
                VectorUtils.v(7, 0, 0),
                VectorUtils.v(7, 1, 0));
        r2.color = Color.GREEN.getRGB();
        
        polygons.add(r1);
        polygons.add(r2);
    }

}
