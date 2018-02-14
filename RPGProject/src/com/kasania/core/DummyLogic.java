package com.kasania.core;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.kasania.graphics.Renderer;
import com.kasania.graphics.Window;
import com.kasania.graphics.element.Material;
import com.kasania.graphics.element.Mesh;
import com.kasania.graphics.element.PointLight;
import com.kasania.graphics.element.Texture;
import com.kasania.util.OBJLoader;

public class DummyLogic implements Logic {

	private static final float MOUSE_SENSITIVITY = 0.2f;

	private final Vector3f cameraInc;

	private final Renderer renderer;

	private final Camera camera;

	private GameItem[] gameItems;

	private Vector3f ambientLight;

	private PointLight pointLight;

	private static final float CAMERA_POS_STEP = 0.05f;

	public DummyLogic() {
		renderer = new Renderer();
		camera = new Camera();
		cameraInc = new Vector3f(0, 0, 0);
	}

	@Override
	public void init(Window window) throws Exception {
		renderer.init(window);

		float reflectance = 1f;
		Mesh mesh = OBJLoader.loadMesh("/models/bunny.obj");
		Material material = new Material(new Vector4f(0.55f, 0.5f, 0.5f, 0), reflectance);

//		Mesh mesh = OBJLoader.loadMesh("/models/cube.obj");
//		Texture texture = new Texture("/textures/grassblock.png");
//		Material material = new Material(texture, reflectance);

		mesh.setMaterial(material);
		GameItem gameItem = new GameItem(mesh);
		gameItem.setScale(0.5f);
		gameItem.setPosition(0, 0, -2);
		GameItem gameItem2 = new GameItem(mesh);
		gameItem2.setScale(0.5f);
		gameItem2.setPosition(1.5f, 0, -2);
		GameItem gameItem3 = new GameItem(mesh);
		gameItem3.setScale(0.5f);
		gameItem3.setPosition(3, 0, -2);
		gameItems = new GameItem[] { gameItem, gameItem2, gameItem3 };

		ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);
		Vector3f lightColour = new Vector3f(1, 1, 1);
		Vector3f lightPosition = new Vector3f(0, 0, 2);
		float lightIntensity = 10.5f;
		pointLight = new PointLight(lightColour, lightPosition, lightIntensity);
		PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
		pointLight.setAttenuation(att);
	}

	@Override
	public void input(Window window, MouseInput mouseInput) {
		cameraInc.set(0, 0, 0);
		if (window.isKeyPressed(GLFW_KEY_W)) {
			cameraInc.z = -1;
		} else if (window.isKeyPressed(GLFW_KEY_S)) {
			cameraInc.z = 1;
		}
		if (window.isKeyPressed(GLFW_KEY_A)) {
			cameraInc.x = -1;
		} else if (window.isKeyPressed(GLFW_KEY_D)) {
			cameraInc.x = 1;
		}
		if (window.isKeyPressed(GLFW_KEY_Z)) {
			cameraInc.y = -1;
		} else if (window.isKeyPressed(GLFW_KEY_X)) {
			cameraInc.y = 1;
		}
		
//        float lightPos = pointLight.getPosition().z;
//        if (window.isKeyPressed(GLFW_KEY_N)) {
//            this.pointLight.getPosition().z = lightPos + 0.1f;
//        } else if (window.isKeyPressed(GLFW_KEY_M)) {
//            this.pointLight.getPosition().z = lightPos - 0.1f;
//        }
	}

	@Override
	public void update(float interval, MouseInput mouseInput) {
		// Update camera position
		camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP,
				cameraInc.z * CAMERA_POS_STEP);
		Vector3f pos = camera.getPosition();
        this.pointLight.setPosition(pos);

		// Update camera based on mouse
		if (mouseInput.isLeftButtonPressed()) {
			Vector2f rotVec = mouseInput.getDisplVec();
			camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
		}
	}

	@Override
	public void render(Window window) {
		renderer.render(window, camera, gameItems, ambientLight, pointLight);
	}

	@Override
	public void cleanUp() {
		renderer.cleanup();
		for (GameItem gameItem : gameItems) {
			gameItem.getMesh().cleanUp();
		}
	}

}