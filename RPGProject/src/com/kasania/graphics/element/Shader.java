package com.kasania.graphics.element;

import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

public class Shader {
	private final int programID;

	private int vertexShaderID;
	private int fragmentShaderID;

	private final Map<String, Integer> uniforms;

	public Shader() throws Exception {
		programID = glCreateProgram();
		if (programID == 0) {
			throw new Exception("Shader Program Create Error.");
		}
		uniforms = new HashMap<>();
	}

	public void createUniform(String uniformName) throws Exception {

		int uniformLocation = glGetUniformLocation(programID, uniformName);
		if (uniformLocation < 0) {
			throw new Exception("Could not find uniform:" + uniformName);
		}
		uniforms.put(uniformName, uniformLocation);
	}

	public void setUniform(String uniformName, Matrix4f value) {
		// Dump the matrix into a float buffer
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer fb = stack.mallocFloat(16);
			value.get(fb);
			glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
		}

	}

	public void setUniform(String uniformName, int value) {
		glUniform1i(uniforms.get(uniformName), value);
	}

	public void createVertexShader(String code) throws Exception {

		vertexShaderID = createShader(code, GL_VERTEX_SHADER);
	}

	public void createFragmentShader(String code) throws Exception {
		fragmentShaderID = createShader(code, GL_FRAGMENT_SHADER);
	}

	protected int createShader(String code, int type) throws Exception {
		int shaderID = glCreateShader(type);
		if (shaderID == 0) {
			throw new Exception("Shader Create Error. Type : " + type);
		}

		glShaderSource(shaderID, code);
		glCompileShader(shaderID);
		if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == 0) {
			throw new Exception("Compiling Shader Error code: " + glGetShaderInfoLog(shaderID, 1024));
		}
		glAttachShader(programID, shaderID);
		return shaderID;
	}

	public void link() throws Exception {
		glLinkProgram(programID);
		if (glGetProgrami(programID, GL_LINK_STATUS) == 0) {
			throw new Exception("Linking Shader Error code: " + glGetProgramInfoLog(programID, 1024));
		}

		if (vertexShaderID != 0) {
			glDetachShader(programID, vertexShaderID);
		}
		if (fragmentShaderID != 0) {
			glDetachShader(programID, fragmentShaderID);
		}

		glValidateProgram(programID);
		if (glGetProgrami(programID, GL_VALIDATE_STATUS) == 0) {
			System.err.println("Validating Shader Warning code: " + glGetProgramInfoLog(programID, 1024));
		}

	}

	public void bind() {
		glUseProgram(programID);
	}

	public void unbind() {
		glUseProgram(0);
	}

	public void cleanUp() {
		unbind();
		if (programID != 0) {
			glDeleteProgram(programID);
		}
	}

}
