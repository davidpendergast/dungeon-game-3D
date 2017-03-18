package threedee;

import static threedee.VectorUtils.*;

public class Camera implements Updatable {
    
    public double[] pos;
    public double[] dir;
    
    public double viewWidth;
    public double viewHeight;
    
    // these are derived from pos and dir
    /**
     * In XY plane, points left of direction camera is facing.
     */
    public double[] left = v();
    
    public double[] up = v();
    /**
     * Coordinates on viewing plane.
     */
    public double[] pCenter = v();
    public double[] pLeft = v();
    public double[] pRight = v();
    public double[] pTop = v();
    public double[] pBottom = v();
    public double[] pTopRight = v();
    public double[] pTopLeft = v();
    public double[] pBottomRight = v();
    public double[] pBottomLeft = v();
    
    private double[] dummy = v();
    
    // temporary
    public double moveSpeed = 0.1;    // tile per sec
    public double rotateSpeed = -0.006;  // radians per sec
    
    public Camera(double xPos, double yPos, double zPos, double xDir, double yDir, double zDir, double viewWidth, double viewHeight) {
        this.pos = v(xPos, yPos, zPos);
        this.dir = v(xDir, yDir, zDir);
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        updateDerivedQuantities();
    }
    
    public Camera(double[] pos, double[] dir, double viewWidth, double viewHeight) {
        this.pos = v(pos);
        this.dir = v(dir);
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        updateDerivedQuantities();
    }
    
    private void updateDerivedQuantities() {
        rotateXY(dir, Math.PI/2, left);
        left[2] = 0;
        normalize(left, left);
        
        cross(dir, left, up);
        
        add(pos, dir, pCenter);
        add(pCenter, mult(left, viewWidth/2, dummy), pLeft);
        sub(pCenter, mult(left, viewWidth/2, dummy), pRight);
        add(pCenter, mult(up, viewHeight/2, dummy), pTop);
        sub(pCenter, mult(up, viewHeight/2, dummy), pBottom);
        
        add(pTop, mult(left, viewWidth/2, dummy), pTopLeft);
        sub(pTop, mult(left, viewWidth/2, dummy), pTopRight);
        add(pBottom, mult(left, viewWidth/2, dummy), pBottomLeft);
        sub(pBottom, mult(left, viewWidth/2, dummy), pBottomRight);
        
    }

    @Override
    public void update(double dt, InputState state) {
        if (state.left ^ state.right) {
            int sign = state.left ? -1 : 1;
            VectorUtils.rotateXY(dir, sign * dt *rotateSpeed, dir);
            
            System.out.println("\npos="+str(pos));
            System.out.println("dir="+str(dir));
            System.out.println("top: "+str(pTopLeft)+" "+str(pTop)+" "+str(pTopRight));
            System.out.println("mid: "+str(pLeft)+" "+str(pCenter)+" "+str(pRight));
            System.out.println("bot: "+str(pBottomLeft)+" "+str(pBottom)+" "+str(pBottomRight));
        }
        if (state.forward ^ state.backward) {
            int sign = state.forward ? 1 : -1;
            pos[0] += sign * dt * moveSpeed * dir[0]; //TODO : normalize (x,y)
            pos[1] += sign * dt * moveSpeed * dir[1];
            System.out.println("pos="+str(pos));
        }
        
        updateDerivedQuantities();
    }
    

}
