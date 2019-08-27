package com.paulniu.media_opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Coder: niupuyue
 * Date: 2019/8/27
 * Time: 18:59
 * Desc: 定义三角形
 * Version:
 */
public class Triangle {

    private FloatBuffer vertexBuffer;

    // 数组中每个顶点的坐标数(其实就是在三维模型中，我们要有x,y,z三个坐标)
    private static final int COORDS_PER_VERTEX = 3;
    // 声明三角形三个顶点的位置
    private static float triangleCoords[] = {
            0.0f,0.622f,0.0f,// top
            -0.5f,-0.311f,0.0f,// bottom lef
            0.5f,-0.311f,0.0f// bottom right
    };

    // 设置颜色，分别为red,green,blue和alpha
    private float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};

    // 构造方法
    public Triangle(){
        // 为存放三角形坐标，初始化顶点字节缓存
        ByteBuffer bb = ByteBuffer.allocateDirect(4 * triangleCoords.length);// 坐标数*4(float占4个字节)
        // 使用设备的本点字节序
        bb.order(ByteOrder.nativeOrder());
        // 从ByteBuffer创建一个浮点缓冲
        vertexBuffer = bb.asFloatBuffer();
        // 把坐标加入到floatBuffer中
        vertexBuffer.put(triangleCoords);
        // 设置buffer，从第一个坐标开始读
        vertexBuffer.position(0);
    }

}
