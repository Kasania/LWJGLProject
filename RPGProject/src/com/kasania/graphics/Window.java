package com.kasania.graphics;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

public class Window {

	private long windowHandle;
	private int WIDTH;
	private int HEIGHT;
	private String Title;
	private boolean vSync;
	private boolean resized;


	public Window(String Title, int WIDTH, int HEIGHT, boolean vSync) {
		this.Title = Title;
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		this.vSync = vSync;

	}

	public void init() {
		GLFWErrorCallback.createPrint(System.err);

		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		glfwDefaultWindowHints(); // optional, the current window hints are
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

		windowHandle = glfwCreateWindow(WIDTH, HEIGHT, "TEST", NULL, NULL);
		if (windowHandle == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}
		glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
			this.WIDTH = width;
			this.HEIGHT = height;
			this.setResized(true);
		});

		glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
				glfwSetWindowShouldClose(window, true); // We will detect this
														// in the rendering loop
			}
		});

		GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(windowHandle, (vidMode.width() - WIDTH) / 2, (vidMode.height() - HEIGHT) / 2);

		glfwMakeContextCurrent(windowHandle);
		if (vSync)
			glfwSwapInterval(1);
		glfwShowWindow(windowHandle);

		GL.createCapabilities();
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glEnable(GL_DEPTH_TEST);
		glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );
	}

	public long getHandle() {
		return windowHandle;

	}

	public void setClearColor(float r, float g, float b, float alpha) {
		glClearColor(r, g, b, alpha);
	}

	public boolean isKeyPressed(int keyCode) {
		return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
	}

	public boolean windowShouldClose() {
		return glfwWindowShouldClose(windowHandle);
	}

	public String getTitle() {
		return Title;
	}

	public int getWidth() {
		return WIDTH;
	}

	public int getHeight() {
		return HEIGHT;
	}

	public boolean isResized() {
		return resized;
	}

	public void setResized(boolean resized) {
		this.resized = resized;
	}

	public boolean isvSync() {
		return vSync;
	}

	public void setvSync(boolean vSync) {
		this.vSync = vSync;
	}

	public void update() {
		glfwSwapBuffers(windowHandle);
		glfwPollEvents();
	}
}
