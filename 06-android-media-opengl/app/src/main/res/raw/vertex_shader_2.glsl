// 用于在java代码中获取属性
attribute vec4 av_Position;
void main() {
    // 将av_Position的顶点属性值赋值给gl_Position的内置变量
    gl_Position = av_Position;
}
