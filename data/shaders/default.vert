attribute vec4 a_position;
attribute vec4 a_normal;

uniform mat4 u_projView;
uniform mat4 u_model;

varying vec4 v_color;

void main() {
	v_color = vec4(1, 0, 0, 1);
	gl_Position = u_projView * u_model * a_position;
}
