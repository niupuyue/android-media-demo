// 设置我们使用中等精确度
precision mediump float;
// 声明变量用于传递颜色数值
varying vec4 vColor;
void main() {
    // 将af_Color的颜色属性值赋值给gl_FragColor的内置变量
    gl_FragColor = vColor;
}
