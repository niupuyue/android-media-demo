package com.paulniu.media_opengl.demo10;

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
 * Date: 2019/9/5
 * Time: 18:57
 * Desc: 绘制球体
 * Version:
 */
public class MyOpenGLRenderSphere implements GLSurfaceView.Renderer {

    private Context mContext;

    private int program;
    private FloatBuffer vertexBuffer;
    private float step = 5f;
    private int vSize;

    private int positionHandler;
    private int materHandler;

    private float[] mViewMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    public MyOpenGLRenderSphere(Context context) {
        this.mContext = context;
        float[] dataPosition = createBallPos();
        ByteBuffer bb = ByteBuffer.allocateDirect(dataPosition.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(dataPosition);
        vertexBuffer.position(0);
        vSize = dataPosition.length / 3;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        String vertexSource = ShaderUtils.readRawText(mContext, R.raw.vertex_shader_10);
        String fragmentSource = ShaderUtils.readRawText(mContext, R.raw.fragment_shader_10);
        program = ShaderUtils.createProgram(vertexSource, fragmentSource);
        if (program > 0){
            positionHandler =  GLES20.glGetAttribLocation(program,"vPosition");
            materHandler = GLES20.glGetUniformLocation(program,"vMatrix");
        }
    }

    // 创建球体的坐标位置
    private float[] createBallPos() {
        // 球以(0,0,0)为中心，以R为半径的任意一点的坐标
        // (R * cos(a) * sin(b),y0 = R * sin(a),R * cos(a) * cos(b))
        // 其中a为圆心到点的线段与xz平面的夹角，b为原型到点的线段xz平面的投影与z轴的夹角
        ArrayList<Float> data = new ArrayList<>();
        float r1, r2;
        float h1, h2;
        float sin, cos;
        for (int i = -90; i < 90 + step; i += step) {
            r1 = (float) Math.cos(i * Math.PI / 180);
            r2 = (float) Math.cos((i + step) * Math.PI / 180);
            h1 = (float) Math.sin(i * Math.PI / 180.0);
            h2 = (float) Math.sin((i + step) * Math.PI / 180.0);
            // 固定纬度, 360 度旋转遍历一条纬线
            float step2 = step * 2;
            for (float j = 0.0f; j < 360.0f + step; j += step2) {
                cos = (float) Math.cos(j * Math.PI / 180.0);
                sin = -(float) Math.sin(j * Math.PI / 180.0);

                data.add(r2 * cos);
                data.add(h2);
                data.add(r2 * sin);
                data.add(r1 * cos);
                data.add(h1);
                data.add(r1 * sin);
            }
        }
        float[] f = new float[data.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = data.get(i);
        }
        return f;
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        //计算宽高比
        float ratio=(float)width/height;
        //设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1, 1, 3, 20);
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 1.0f, -10.0f, -4.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,mViewMatrix,0);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glUseProgram(program);
        GLES20.glUniformMatrix4fv(materHandler,1,false,mMVPMatrix,0);
        GLES20.glEnableVertexAttribArray(positionHandler);
        GLES20.glVertexAttribPointer(positionHandler,3,GLES20.GL_FLOAT,false,0,vertexBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,vSize);
        GLES20.glDisableVertexAttribArray(positionHandler);
    }
}
