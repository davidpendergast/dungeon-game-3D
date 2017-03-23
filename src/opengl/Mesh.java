package opengl;

public interface Mesh {
    
    public int getVaoId();
    
    public int getVertexCount();
    
    public void render();
    
    public void cleanup();

}
