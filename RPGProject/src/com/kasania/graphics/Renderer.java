package com.kasania.graphics;

import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.system.MemoryUtil;

import com.kasania.graphics.shader.Shader;
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
		
		float[] vertices = new float[] { 
				 0.5f, 0.5f, 0.0f, 
				-0.5f,-0.5f, 0.0f, 
				 0.5f,-0.5f, 0.0f,
				-0.5f, 0.5f, 0.0f };
		//		  x		y	  z
		
		FloatBuffer verticesBuffer = null;
        try {
            verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
            verticesBuffer.put(vertices).flip();

            vaoID = glGenVertexArrays();
            glBindVertexArray(vaoID);

            vboID = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            glBindBuffer(GL_ARRAY_BUFFER, 0);

            glBindVertexArray(0);
        } finally {
            if (verticesBuffer != null) {
                MemoryUtil.memFree(verticesBuffer);
            }
        }
	}
	
	public void render(Window window) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shader.bind();

        // Bind to the VAO
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);

        // Draw the vertices
        glDrawArrays(GL_TRIANGLES, 0, 4);

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
