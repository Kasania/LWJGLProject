package com.kasania.core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.glfw.Callbacks.*;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import com.kasania.graphics.Window;

public class GameManager implements Runnable {

	public static final int TARGET_FPS = 120;
	public static final int TARGET_UPS = 60;;

	private final Thread gameLoopThread;

	private final Timer timer;
	private final Window window;
	private final Logic logic;
	private final MouseInput mouseInput;

	public GameManager(String TITLE, int WIDTH, int HEIGHT, boolean vSync, Logic logic) {
		gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
		window = new Window(TITLE, WIDTH, HEIGHT, vSync);
		this.logic = logic;
		timer = new Timer();
		mouseInput = new MouseInput();
	}

	public void start() {
		gameLoopThread.start();
	}

	private void init() throws Exception {

		window.init();
		timer.init();
		mouseInput.init(window);
		logic.init(window);
	}

	private void loop() {
		float deltaTime;
		float accumulator = 0f;
		float interval = 1f / TARGET_UPS;

		boolean running = true;
		while (running && !window.windowShouldClose()) {
			deltaTime = timer.getDeltaTime();
			accumulator += deltaTime;

			input();

			while (accumulator >= interval) {
				update(interval);
				accumulator -= interval;
			}

			render();

			if (!window.isvSync()) {
				sync();
			}
		}

	}

	@Override
	public void run() {
		try{
			init();
			loop();
			dispose();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			cleanUp();
		}
	}

	private void dispose() {
		glfwFreeCallbacks(window.getHandle());
		glfwDestroyWindow(window.getHandle());
		glfwTerminate();
	}

	private void sync() {
		float loopSlot = 1f / TARGET_FPS;
		double endTime = timer.getLastTime() + loopSlot;
		while (timer.getTime() < endTime) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException ie) {
			}
		}
	}

	private void input() {
		mouseInput.input(window);
		logic.input(window,mouseInput);
	}

	private void update(float interval) {
		logic.update(interval,mouseInput);
	}

	private void render() {
		logic.render(window);
		window.update();
	}
	
	private void cleanUp(){
		logic.cleanUp();
	}

}
