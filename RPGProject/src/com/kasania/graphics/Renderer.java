package com.kasania.graphics;

import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import com.kasania.core.GameItem;
import com.kasania.graphics.element.Mesh;
import com.kasania.graphics.element.Shader;
import com.kasania.graphics.element.Transformation;
import com.kasania.util.ResourceManager;

public class Renderer {
	
	private final float FOV = (float)Math.toRadians(60.0f);
	private final float ZNear = 0.01f;
	private final float ZFar = 1000.f;
	private Matrix4f projectionMatrix;
	private final Transformation transformation;

	private Shader shader;
	{
		transformation = new Transformation();
	}

	public void init(Window window) throws Exception {
		shader = new Shader();
		shader.createVertexShader(ResourceManager.loadResource("/shaders/vertex.vtx"));
		shader.createFragmentShader(ResourceManager.loadResource("/shaders/fragment.frg"));
		shader.link();
		
		float aspectRatio = (float) window.getWidth()	 / window.getHeight();
		projectionMatrix = new Matrix4f().perspective(FOV, aspectRatio, ZNear, ZFar);
		shader.createUniform("projectionMatrix");
		shader.createUniform("worldMatrix");
		shader.createUniform("texture_sampler");
	}
	
	public void render(Window window, GameItem[] gameItems) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shader.bind();
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), ZNear, ZFar);
        shader.setUniform("projectionMatrix", projectionMatrix);
        shader.setUniform("texture_sampler", 0);
        for(GameItem gameItem : gameItems) {
            // Set world matrix for this item
            Matrix4f worldMatrix = transformation.getWorldMatrix(
                    gameItem.getPosition(),
                    gameItem.getRotation(),
                    gameItem.getScale());
            shader.setUniform("worldMatrix", worldMatrix);
            // Render the mes for this game item
            gameItem.getMesh().render();
        }

        shader.unbind();
    }

	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

    public void cleanup() {
        if (shader != null) {
            shader.cleanUp();
        }
    }
}
