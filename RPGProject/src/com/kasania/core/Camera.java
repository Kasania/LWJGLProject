package com.kasania.core;

import static org.lwjgl.opengl.GL11.*;

public class Camera {
	
	private float x;
	private float y;
	private float z;
	private float rx;
	private float ry;
	private float rz;
	
	private float FoV;
	private float aspect;
	private float nearField;
	private float farField;
	
	
	public Camera(float fov, float aspect, float near, float far){
		x = 0;
		y = 0;
		z = 0;
		rx = 0;
		ry = 0;
		rz = 0;
		
		this.FoV = fov;
		this.aspect = aspect;
		this.nearField = near;
		this.farField = far;
	}
	
	private void initProjection(){
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		
	}

}
