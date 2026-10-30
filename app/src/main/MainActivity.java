package dev.sdvq3.graphicscheck;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends Activity {
    private GLSurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        surfaceView = new GLSurfaceView(this);
        surfaceView.setEGLContextClientVersion(2);
        surfaceView.setRenderer(new DiagnosticRenderer());
        setContentView(surfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        surfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        surfaceView.onResume();
    }

    private static final class DiagnosticRenderer implements GLSurfaceView.Renderer {
        private static final String VERTEX_SHADER =
                "attribute vec4 aPosition; void main() { gl_Position = aPosition; }";
        private static final String FRAGMENT_SHADER =
                "precision mediump float; void main() { gl_FragColor = vec4(0.15, 0.85, 0.45, 1.0); }";
        private final FloatBuffer vertices;
        private int program;
        private int positionHandle;

        DiagnosticRenderer() {
            float[] triangle = {
                    0.0f, 0.65f,
                    -0.65f, -0.55f,
                    0.65f, -0.55f
            };
            ByteBuffer bytes = ByteBuffer.allocateDirect(triangle.length * 4);
            bytes.order(ByteOrder.nativeOrder());
            vertices = bytes.asFloatBuffer();
            vertices.put(triangle);
            vertices.position(0);
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glClearColor(0.05f, 0.08f, 0.16f, 1.0f);
            int vertexShader = compileShader(GLES20.GL_VERTEX_SHADER, VERTEX_SHADER);
            int fragmentShader = compileShader(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER);
            program = GLES20.glCreateProgram();
            GLES20.glAttachShader(program, vertexShader);
            GLES20.glAttachShader(program, fragmentShader);
            GLES20.glLinkProgram(program);
            positionHandle = GLES20.glGetAttribLocation(program, "aPosition");
            GLES20.glDeleteShader(vertexShader);
            GLES20.glDeleteShader(fragmentShader);
        }

        private static int compileShader(int type, String source) {
            int shader = GLES20.glCreateShader(type);
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);
            return shader;
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            GLES20.glUseProgram(program);
            vertices.position(0);
            GLES20.glEnableVertexAttribArray(positionHandle);
            GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, vertices);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
            GLES20.glDisableVertexAttribArray(positionHandle);
        }
    }
}
