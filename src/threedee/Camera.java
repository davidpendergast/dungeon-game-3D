package threedee;

public class Camera implements Updatable {
    public double xPos, yPos, xDir, yDir, xPlane, yPlane;
    
    // temporary
    public double moveSpeed = 3;    // tile per sec
    public double rotateSpeed = 3;  // radians per sec
    
    public Camera(double xPos, double yPos, double xDir, double yDir, double xPlane, double yPlane) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.xDir = xDir;
        this.yDir = yDir;
        this.xPlane = xPlane;
        this.yPlane = yPlane;
    }

    @Override
    public void update(double dt, InputState state) {
        if (state.left) {
            this.xPos -= moveSpeed * dt;
        }
        if (state.right) {
            this.xPos += moveSpeed * dt;
        }
    }
}
