#version 330

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 inColor;

out vec3 exColor;

uniform mat4 worldMatrix;
uniform mat4 projectionMatrix;

void main()
{
    gl_Position = vec4(position, 1.0);
    exColor = inColor;
}