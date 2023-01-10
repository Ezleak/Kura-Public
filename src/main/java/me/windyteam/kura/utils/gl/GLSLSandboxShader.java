/*
 * Copyright (c) superblaubeere27 2020
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.windyteam.kura.utils.gl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.opengl.GL20.*;

public class GLSLSandboxShader {
    public static int programId;
    public static int timeUniform;
    public static int mouseUniform;
    public static int resolutionUniform;

    public GLSLSandboxShader(String fragmentShaderLocation) throws IOException {
        int program = glCreateProgram();

        glAttachShader(program, createShader("/shaders/passthrough.vsh", GLSLSandboxShader.class.getResourceAsStream("/shaders/passthrough.vsh"), GL_VERTEX_SHADER));
        glAttachShader(program, createShader(fragmentShaderLocation, GLSLSandboxShader.class.getResourceAsStream(fragmentShaderLocation), GL_FRAGMENT_SHADER));

        glLinkProgram(program);

        int linked = glGetProgrami(program, GL_LINK_STATUS);

        // If linking failed
        if (linked == 0) {
            System.err.println(glGetProgramInfoLog(program, glGetProgrami(program, GL_INFO_LOG_LENGTH)));

            throw new IllegalStateException("Shader failed to link");
        }

        programId = program;

        // Setup uniforms
        glUseProgram(program);

        timeUniform = glGetUniformLocation(program, "time");
        mouseUniform = glGetUniformLocation(program, "mouse");
        resolutionUniform = glGetUniformLocation(program, "resolution");

        glUseProgram(0);
    }

    public static void useShader(int width, int height, float mouseX, float mouseY, float time) {
        glUseProgram(programId);

        glUniform2f(resolutionUniform, width, height);
        glUniform2f(mouseUniform, mouseX / width, 1.0f - mouseY / height);
        glUniform1f(timeUniform, time);
    }

    private int createShader(String check, InputStream inputStream, int shaderType) throws IOException {
        int shader = glCreateShader(shaderType);

        glShaderSource(shader, readStreamToString(inputStream));

        glCompileShader(shader);

        int compiled = glGetShaderi(shader, GL_COMPILE_STATUS);

        // If compilation failed
        if (compiled == 0) {
            System.err.println(glGetShaderInfoLog(shader, glGetShaderi(shader, GL_INFO_LOG_LENGTH)));
            System.err.println("Caused by " + check);
            throw new IllegalStateException("Failed to compile shader: " + check);
        }

        return shader;
    }

    private String readStreamToString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        byte[] buffer = new byte[512];

        int read;

        while ((read = inputStream.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, read);
        }

        return new String(out.toByteArray(), StandardCharsets.UTF_8);
    }
}