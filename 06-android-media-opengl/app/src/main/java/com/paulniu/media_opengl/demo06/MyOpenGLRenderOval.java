package com.paulniu.media_opengl.demo06;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.paulniu.media_opengl.R;
import com.paulniu.media_opengl.demo1.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Coder: niupuyue
 * Date: 2019/8/29
 * Time: 17:35
 * Desc:
 * Version:
 */
public class MyOpenGLRenderOval implements GLSurfaceView.Renderer {

    private Context context;

    private int positionHandler;
    private int colorHandler;
    private int matrixHandler;

    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;

    private int program;

    private float[] projectMatrix = new float[16];
    private float[] viewMatrix = new float[16];
    private float[] mvpMatrix = new float[16];

    private float[] colorCoords = {
            1.0f, 0f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 0f, 1f, 1f
    };
    private float[] vertexCoords ;

    public MyOpenGLRenderOval(Context context) {
        this.context = context;

        // 设置顶点数据
        vertexCoords = createPositions();
        ByteBuffer bb = ByteBuffer.allocateDirect(vertexCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertexCoords);
        vertexBuffer.position(0);

        // 设置颜色
        ByteBuffer cc = ByteBuffer.allocateDirect(colorCoords.length * 4);
        cc.order(ByteOrder.nativeOrder());
        colorBuffer = cc.asFloatBuffer();
        colorBuffer.put(colorCoords);
        colorBuffer.position(0);
    }

    /**
     * 创建圆形图形的所有坐标点
     *
     * @return
     */
    private float[] createPositions() {
        ArrayList<Float> datas = new ArrayList<>();
        datas.add(0.0f);
        datas.add(0.0f);
        datas.add(0.5f);
        float angleSpan = 360f / 360;
        for (int i = 0; i < 360 + angleSpan; i += angleSpan) {
            datas.add((float) (1.0f * Math.sin(i * Math.PI / 180f)));
            datas.add((float) (1.0f * Math.cos(i * Math.PI / 180f)));
            datas.add(0.5f);
        }
        float[] res = new float[datas.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = datas.get(i);
        }
        return res;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        String vertexSource = ShaderUtils.readRawText(context, R.raw.vertex_shader_6);
        String fragmentSource = ShaderUtils.readRawText(context,R.raw.fragment_shader_6);
        program = ShaderUtils.createProgram(vertexSource,fragmentSource);
        if (program > 0){
            positionHandler = GLES20.glGetAttribLocation(program,"position");
            matrixHandler = GLES20.glGetUniformLocation(program,"matrix");
            colorHandler = GLES20.glGetAttribLocation(program,"color");
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        float ratio = (float) i / i1;
        Matrix.frustumM(projectMatrix,0,-ratio,ratio,-1,1,3,7);
        Matrix.setLookAtM(viewMatrix,0,0,0,7f,0f,0f,0f,0f,1f,0f);
        Matrix.multiplyMM(mvpMatrix,0,projectMatrix,0,viewMatrix,0);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glClearColor(0.5f,0.5f,0.5f,1f);

        GLES20.glUseProgram(program);

        GLES20.glEnableVertexAttribArray(positionHandler);
        GLES20.glVertexAttribPointer(positionHandler,3,GLES20.GL_FLOAT,false,0,vertexBuffer);

        GLES20.glEnableVertexAttribArray(colorHandler);
        GLES20.glVertexAttribPointer(colorHandler,4,GLES20.GL_FLOAT,false,0,colorBuffer);

        GLES20.glUniformMatrix4fv(matrixHandler,1,false,mvpMatrix,0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,vertexCoords.length / 3);
    }
}
