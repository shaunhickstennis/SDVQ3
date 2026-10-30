package dev.sdvq3.graphicscheck;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public final class MainActivity extends Activity {
    private GLSurfaceView surface;
    private final DiagnosticRenderer renderer = new DiagnosticRenderer();
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        surface = new GLSurfaceView(this);
        surface.setEGLContextClientVersion(2);
        surface.setRenderer(renderer);
        setContentView(surface);
    }
    @Override protected void onPause() { super.onPause(); surface.onPause(); }
    @Override protected void onResume() { super.onResume(); surface.onResume(); }
    @Override public boolean onTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) { renderer.toggle(); return true; }
        return super.onTouchEvent(e);
    }
    private static final class DiagnosticRenderer implements GLSurfaceView.Renderer {
        private volatile boolean alternate = false;
        private int program, position, color;
        private final FloatBuffer vertices = ByteBuffer.allocateDirect(9 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        DiagnosticRenderer() { vertices.put(new float[]{-0.7f,-0.6f,0f, 0.7f,-0.6f,0f, 0f,0.75f,0f}).position(0); }
        void toggle() { alternate = !alternate; }
        private static int shader(int kind, String src) {
            int s = GLES20.glCreateShader(kind);
            GLES20.glShaderSource(s, src);
            GLES20.glCompileShader(s);
            int[] status = new int[1]; GLES20.glGetShaderiv(s, GLES20.GL_COMPILE_STATUS, status, 0);
            if (status[0] == 0) throw new IllegalStateException(GLES20.glGetShaderInfoLog(s));
            return s;
        }
        @Override public void onSurfaceCreated(javax.microedition.khronos.egl.EGLConfig config) {
            int vs = shader(GLES20.GL_VERTEX_SHADER, "attribute vec3 aPosition; void main(){gl_Position=vec4(aPosition,1.0);}");
            int fs = shader(GLES20.GL_FRAGMENT_SHADER, "precision mediump float; uniform vec4 uColor; void main(){gl_FragColor=uColor;}");
            program = GLES20.glCreateProgram(); GLES20.glAttachShader(program, vs); GLES20.glAttachShader(program, fs);
            GLES20.glLinkProgram(program);
            int[] status = new int[1]; GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, status, 0);
            if (status[0] == 0) throw new IllegalStateException(GLES20.glGetProgramInfoLog(program));
            GLES20.glDeleteShader(vs); GLES20.glDeleteShader(fs);
            position = GLES20.glGetAttribLocation(program, "aPosition");
            color = GLES20.glGetUniformLocation(program, "uColor");
        }
        @Override public void onSurfaceChanged(javax.microedition.khronos.opengles.GL10 unused, int w, int h) { GLES20.glViewport(0, 0, w, h); }
        @Override public void onDrawFrame(javax.microedition.khronos.opengles.GL10 unused) {
            GLES20.glClearColor(0.06f,0.08f,0.12f,1f); GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            GLES20.glUseProgram(program);
            if (alternate) GLES20.glUniform4f(color,0.95f,0.6f,0.18f,1f);
            else GLES20.glUniform4f(color,0.25f,0.85f,0.55f,1f);
            GLES20.glEnableVertexAttribArray(position);
            GLES20.glVertexAttribPointer(position,3,GLES20.GL_FLOAT,false,0,vertices);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,3);
            GLES20.glDisableVertexAttribArray(position);
        }
    }
}
