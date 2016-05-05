#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;

//PI
#define PI 3.14159265358979323844

//this variable tells whether color should be added or not
uniform int u_addColor;

//radius and thickness
uniform vec2 u_circle; //vec2 circle contains radius and thickness: radius @ x, thickness @ y

//degree completion
uniform float u_angle, u_startAngle;

//center of the radial progress bar and alpha of each end of the radial progress bar
uniform vec2 u_center, u_alpha; //u_alpha: contains the opacity of each end: startEnd @ x, endEnd @ y

//color gradients
uniform vec3 u_color1; //starting color gradient
uniform vec3 u_color2; //ending color gradient

void main() {
        //get the color sampling
        vec4 color = texture2D(u_texture, v_texCoords);

        //only change the pixels inside the square
        if( gl_FragCoord.x > (u_center.x - u_circle.x) && gl_FragCoord.y > (u_center.y - u_circle.x)
            && (gl_FragCoord.x < (u_center.x + u_circle.x) && gl_FragCoord.y < (u_center.y + u_circle.x) ) ){

            //remove the square extra shaders to draw the circle with thickness
            if( ( pow( gl_FragCoord.x - u_center.x , 2.0) + pow( gl_FragCoord.y - u_center.y , 2.0) ) <= pow(u_circle.x - u_circle.y, 2.0)
                || ( pow( gl_FragCoord.x - u_center.x , 2.0) + pow( gl_FragCoord.y - u_center.y , 2.0) ) >= pow(u_circle.x, 2.0)
             ){
                color.a = 0.0;
            }

            //hide the shade with angle greater than u_angle
            float u_aangle = atan( (gl_FragCoord.y - u_center.y),(gl_FragCoord.x - u_center.x) ),
                    angle_diff = 0.0;
            //convert u_aangle from -PI,PI range to 0,2PI range
            if(u_aangle < 0.0){
                angle_diff = PI*2.0 + u_aangle;
            }else{
                angle_diff = u_aangle;
            }

            //set the start of the radial progress bar and trim it
            angle_diff += u_startAngle;
            angle_diff = ( angle_diff >= PI*2.0 )? angle_diff - PI*2.0 : angle_diff;

            //hide unused shade
            if(u_angle < angle_diff){
                color.a = 0.0;
            }else{
                //else add color gradient
                if(u_addColor == 1){
                    color.rgb = mix(u_color2, u_color1, (u_angle - angle_diff)/u_angle);
                }
                color.a = (color.a != 0.0)? mix(u_alpha.y, u_alpha.x, (u_angle - angle_diff)/u_angle ): color.a;
            }

        }

        gl_FragColor = color;
}