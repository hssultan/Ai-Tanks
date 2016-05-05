#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;

//variable for enabling, disabling the button color, and selected button
uniform int enabled, u_glow, u_selected;

//variable for brightness
uniform float contrast, brightness, u_radius_ratio;

//coordinates
uniform vec2 u_coordinates, u_dimensions;

void main() {
        //get the color sampling
        vec4 color = texture2D(u_texture, v_texCoords);
        //get the radius
        float radius = u_dimensions.x*u_radius_ratio;
        //get the length of the gl frag to the center
        float len = distance( vec2(u_coordinates.x + u_dimensions.x/2.0, u_coordinates.y + u_dimensions.y/2.0),
                                gl_FragCoord.xy );

        //enable and disable color method
        if(enabled == 0){
            float num = (color.r + color.g + color.b) / 3.0;
            color.rgb = vec3(num, num, num);
        }else{
            //end of method and allow for the other effects to apply

            //adjust contrast
            color.rgb *= contrast;
            //end of contrast

            //adjust brightness and add effects
            if(brightness > 0.5){
                if(u_glow == 0){
                    color = mix(color, vec4(1,1,1,1), brightness);
                }else{
                    //add effects
                    if( (gl_FragCoord.x > u_coordinates.x && gl_FragCoord.y > u_coordinates.y)
                        &&
                        (gl_FragCoord.x < u_coordinates.x + u_dimensions.x && gl_FragCoord.y < u_coordinates.y + u_dimensions.y )
                        ){
                        color = mix(color, vec4(1,1,1,1), brightness*smoothstep(radius, 0.0, len) );
                    }
                    //end of effects
                }

            }else if(brightness < 0.5){
                if(u_glow == 0){
                    color = mix(vec4(0,0,0,1), color, brightness);
                }else{
                    //add effects
                    if( (gl_FragCoord.x > u_coordinates.x && gl_FragCoord.y > u_coordinates.y)
                           &&
                         (gl_FragCoord.x < u_coordinates.x + u_dimensions.x && gl_FragCoord.y < u_coordinates.y + u_dimensions.y )
                      ){
                        color = mix(vec4(0,0,0,1), color, brightness*smoothstep(radius, 0.0, len));
                    }
                    //end of effects
                }
            }
            //end of brightness

            //add the selected effect
            if(u_selected == 1){
                if( gl_FragCoord.x >= u_coordinates.x && u_coordinates.x + u_dimensions.x/9.0 >= gl_FragCoord.x ){
                    color.rgb = mix(vec3(1,1,1), color.rgb, (gl_FragCoord.x - u_coordinates.x)/(u_dimensions.x/9.0) );
                    color.a = 1.0;
                }
                if( gl_FragCoord.y >= u_coordinates.y && u_coordinates.y + u_dimensions.x/9.0 >= gl_FragCoord.y ){
                    color.rgb = mix(vec3(1,1,1), color.rgb, (gl_FragCoord.y - u_coordinates.y)/(u_dimensions.x/9.0) );
                    color.a = 1.0;
                }
                if( gl_FragCoord.y <= u_coordinates.y + u_dimensions.y
                        && gl_FragCoord.y >= u_coordinates.y + u_dimensions.y - u_dimensions.x/9.0 ){
                    color.rgb = mix(vec3(1,1,1), color.rgb, ( u_coordinates.y + u_dimensions.y - gl_FragCoord.y)/(u_dimensions.x/9.0) );
                    color.a = 1.0;
                }
                if( gl_FragCoord.x <= u_coordinates.x + u_dimensions.x
                        && gl_FragCoord.x >= u_coordinates.x + u_dimensions.x - u_dimensions.x/9.0 ){
                     color.rgb = mix(vec3(1,1,1), color.rgb, ( u_coordinates.x + u_dimensions.x - gl_FragCoord.x)/(u_dimensions.x/9.0) );
                     color.a = 1.0;
                }
            }
            //end of the selected effect

        }

        gl_FragColor = color;
}