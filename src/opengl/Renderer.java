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
    private int vboId;
    private int vaoId;
    
    public void init() throws Exception {
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("shaders/vertex.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("shaders/fragment.fs"));
        shaderProgram.link();
        
        float[] vertices = new float[] { 
                 0.0f,  0.5f, 0.0f, 
                -0.5f, -0.5f, 0.0f,
                 0.5f, -0.5f, 0.0f 
        };
        
        FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
        verticesBuffer.put(vertices).flip();
        
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);
        
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        MemoryUtil.memFree(verticesBuffer);
        
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        
        // unbind the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        // unbind the VAD
        glBindVertexArray(0);
        
        if (verticesBuffer != null) {
            MemoryUtil.memFree(verticesBuffer);
        }
    }
    
    public void render(Window window) {
        clear();
        
        if (window.getResized()) {
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }
        
        shaderProgram.bind();
        
        // bind the VAO
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        
        // draw the vertices
        glDrawArrays(GL_TRIANGLES, 0, 3);
        
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
        
        glDisableVertexAttribArray(0);
        
        // delete the vbo
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboId);
        
        // delete the vad
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

}
