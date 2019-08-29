package com.paulniu.media_opengl.demo1;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.paulniu.media_opengl.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Coder: niupuyue
 * Date: 2019/8/27
 * Time: 18:29
 * Desc:
 * Version:
 */
public class MyGLSurfaceViewRendered implements GLSurfaceView.Renderer {

    private Context context;
    private final float[] vertexData = {
            0.0f,0.322f,// top
            -0.5f,-0.311f,// bottom lef
            0.5f,-0.311f// bottom right
    };

    private FloatBuffer vertexBuffer;
    private int program;
    private int position;
    private int color;

    public MyGLSurfaceViewRendered(Context context) {
        this.context = context;
        vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(ShaderUtils.fBuffer(vertexData));
        vertexBuffer.position(0);
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        String vertexSource = ShaderUtils.readRawText(context, R.raw.vertex_shader);
        String fragmentSource = ShaderUtils.readRawText(context, R.raw.fragment_shader);
        program = ShaderUtils.createProgram(vertexSource, fragmentSource);
        if (program > 0) {
            position = GLES20.glGetAttribLocation(program, "av_Position");
            color = GLES20.glGetUniformLocation(program, "af_Color");
        }
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glUseProgram(program);
        GLES20.glUniform4f(color, 1f, 0f, 0f, 1f);
        GLES20.glEnableVertexAttribArray(position);

        GLES20.glVertexAttribPointer(position, 2, GLES20.GL_FLOAT, false, 8, vertexBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }


}
