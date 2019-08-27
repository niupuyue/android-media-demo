package com.paulniu.media_opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Coder: niupuyue
 * Date: 2019/8/27
 * Time: 19:09
 * Desc: 定义正方形
 * Version:
 */
public class Square {

    // 顶点缓冲区
    private FloatBuffer vertexBuffer;
    // 绘制顺序顶点缓冲区
    private ShortBuffer drawListBuffer;

    // 每个顶点的坐标数
    private static final int COORDS_PER_VERTEX = 3;
    // 正方形的四个顶点坐标
    private static float squareCoords[] = {
            -0.5f, 0.5f, 0.0f,// top left
            -0.5f, -0.5f, 0.0f,// bottom lef
            0.5f, -0.5f, 0.0f,// bottom right
            0.5f, 0.5f, 0.0f// top right
    };
    // 绘制顶点的绘制顺序
    private short drawOrder[] = {0, 1, 2, 0, 2, 3};

    // 构造方法 设置图形的RGB和透明度
    public Square(){
        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // 绘制列表初始化字节缓冲
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(drawOrder.length * 2);
        byteBuffer.order(ByteOrder.nativeOrder());
        drawListBuffer = byteBuffer.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
    }

}
