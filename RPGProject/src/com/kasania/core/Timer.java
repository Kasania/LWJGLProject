package com.kasania.core;

import org.lwjgl.glfw.GLFW;

public class Timer {
	
	private double lastTime;
	
	public void init(){
		lastTime = getTime();
	}

	public double getTime(){
		return GLFW.glfwGetTime();
	}
	public double getLastTime(){
		return lastTime;
	}
	
	public float getDeltaTime(){
		double time = getTime();
		float delta = (float) (time - lastTime);
		lastTime = time;
		return delta;
	}
	
}
