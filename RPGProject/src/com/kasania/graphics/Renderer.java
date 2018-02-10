package com.kasania.graphics;

import static org.lwjgl.opengl.GL11.*;
import org.joml.Matrix4f;
import com.kasania.core.Camera;
import com.kasania.core.GameItem;
import com.kasania.graphics.element.Mesh;
import com.kasania.graphics.element.Shader;
import com.kasania.graphics.element.Transformation;
import com.kasania.util.ResourceManager;

public class Renderer {

	private final float FOV = (float) Math.toRadians(60.0f);
	private final float ZNear = 0.01f;
	private final float ZFar = 1000.f;
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

		// Create uniforms for modelView and projection matrices and texture
		shader.createUniform("projectionMatrix");
		shader.createUniform("modelViewMatrix");
		shader.createUniform("texture_sampler");
		// Create uniform for default colour and the flag that controls it
		shader.createUniform("colour");
		shader.createUniform("useColour");
	}

	public void render(Window window, Camera camera, GameItem[] gameItems) {
		clear();

		if (window.isResized()) {
			glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResized(false);
		}

		shader.bind();

		// Update projection Matrix
		Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(),
				ZNear, ZFar);
		shader.setUniform("projectionMatrix", projectionMatrix);

		// Update view Matrix
		Matrix4f viewMatrix = transformation.getViewMatrix(camera);

		shader.setUniform("texture_sampler", 0);
		// Render each gameItem
		for (GameItem gameItem : gameItems) {
			Mesh mesh = gameItem.getMesh();
			// Set model view matrix for this item
			Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
			shader.setUniform("modelViewMatrix", modelViewMatrix);
			// Render the mesh for this game item
			shader.setUniform("colour", mesh.getColour());
			shader.setUniform("useColour", mesh.isTextured() ? 0 : 1);
			mesh.render();
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
