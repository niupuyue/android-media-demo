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
            -1f,0f,// 左下角
            1f,0f,// 右下角
            0f,1f// 顶角
    };

    // 设置颜色，分别为red,green,blue和alpha
    private float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};

    // 构造方法
    public Triangle(){
        // 为存放三角形坐标，初始化顶点字节缓存
        ByteBuffer bb = ByteBuffer.allocateDirect(4 * triangleCoords.length);// 分配内存空间
        // 内存bit的排序方式和本地机器一致
        bb.order(ByteOrder.nativeOrder());
        // 转换成float的buffer，因为这里我们存放的是float类型的顶点
        vertexBuffer = bb.asFloatBuffer();
        // 将数据放入内存中
        vertexBuffer.put(triangleCoords);
        // 把索引指针指向开头位置
        vertexBuffer.position(0);
    }

}
