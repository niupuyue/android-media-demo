attribute vec4 position;
uniform mat4 matrix;
varying vec4 vColor;
attribute vec4 color;
void main(){
    gl_Position =matrix * position;
    vColor = color;
}