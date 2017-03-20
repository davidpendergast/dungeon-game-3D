package opengl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class Renderer {
    
    private ShaderProgram shaderProgram;
    
    public void init() throws Exception {
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("shaders/vertex.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("shaders/fragment.fs"));
        shaderProgram.link();
    }
    
    public void render(Window window, Mesh mesh) {
        clear();
        
        if (window.getResized()) {
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }
        
        shaderProgram.bind();
        
        // bind the VAO
        glBindVertexArray(mesh.getVaoId());
        glEnableVertexAttribArray(0);   // enable index lookups
        glEnableVertexAttribArray(1);   // enable color
        // draw the vertices
        glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
        
        // restore the state
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
        
        shaderProgram.unbind();
    }
    
    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
    
    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }

}
