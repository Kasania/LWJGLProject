package com.kasania.graphics;

import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.system.MemoryUtil;

import com.kasania.graphics.element.Mesh;
import com.kasania.graphics.element.Shader;
import com.kasania.util.ResourceManager;

public class Renderer {

	private int vboID;
	private int vaoID;

	private Shader shader;
	{
	}

	public void init() throws Exception {
		shader = new Shader();
		shader.createVertexShader(ResourceManager.loadResource("/vertex.vtx"));
		shader.createFragmentShader(ResourceManager.loadResource("/fragment.frg"));
		shader.link();
	}
	
	public void render(Window window, Mesh mesh) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shader.bind();

        // Bind to the VAO
        glBindVertexArray(mesh.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, mesh.getVertexCount(),GL_UNSIGNED_INT, 0);
        
        
        // Draw the vertices
//        glDrawArrays(GL_TRIANGLES, 0, 4);

        // Restore state
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        shader.unbind();
    }

	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

    public void cleanup() {
        if (shader != null) {
            shader.cleanUp();
        }

        glDisableVertexAttribArray(0);

        // Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboID);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoID);
    }
}
