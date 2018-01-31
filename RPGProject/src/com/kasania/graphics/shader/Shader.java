package com.kasania.graphics.shader;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
	private final int programID;

	private int vertexShaderID;
	private int fragmentShaderID;

	public Shader() throws Exception {
		programID = glCreateProgram();
		if (programID == 0) {
			throw new Exception("Shader Program Create Error.");
		}

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
