attribute vec4 av_Position; // 用于在Java代码中获取属性
void main(){
    gl_Position = av_Position;// gl_Position是内置变量，OpenGL绘制顶点就是根据这个值绘制的。所以我们需要将java代码中的值赋值给它
}