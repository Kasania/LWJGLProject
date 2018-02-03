package com.kasania.core;

import static org.lwjgl.opengl.GL11.*;

import org.joml.Vector3f;

public class Camera {

	private Vector3f position;
	private Vector3f rotation;

	public Camera() {
		position = new Vector3f(0, 0, 0);
		rotation = new Vector3f(0, 0, 0);
	}

	public Camera(Vector3f postion, Vector3f rotation) {
		this.position = postion;
		this.rotation = rotation;
	}

	public void setPosition(float x, float y, float z) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void movePosition(float x, float y, float z) {
		if (z != 0) {
			position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * z;
			position.z += (float) Math.cos(Math.toRadians(rotation.y)) * z;
		}
		if (x != 0) {
			position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * x;
			position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * x;
		}
		position.y += y;
	}

	public void setRotation(float x, float y, float z) {
		this.rotation.x = x;
		this.rotation.y = y;
		this.rotation.z = z;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void moveRotation(float x, float y, float z) {
		rotation.x += x;
		rotation.y += y;
		rotation.z += z;
	}

}
