#version 400

attribute vec4 a_Position;
uniform mat4 u_Matrix;

void main()
{
    gl_Position = u_Matrix * a_Position;
    gl_PointSize = 5.0;
}
