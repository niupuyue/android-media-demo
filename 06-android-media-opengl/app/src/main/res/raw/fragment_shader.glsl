precision mediump float;// 声明我们使用中等精度 float
uniform vec4 af_Color;// 用于在java代码中传递颜色数值
void main(){
    gl_FragColor = af_Color;// gl_FragColor内置变量，OpenGL在填充颜色时使用该变量
}