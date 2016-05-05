package com.bvisiongames.me.settings;

import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.bvisiongames.me.entity.Tank;
import com.bvisiongames.me.screen.MultiGameScreen;

/**
 * Created by ahzji_000 on 12/16/2015.
 */
public class GeneralMethods {

    /**
     * returns a random number with a range.
     * @param min
     * minimum integer number
     * @param max
     * maximum integer number
     */
    //small variables for the method below
    private static int generalRandomRange = 0;
    public synchronized static int GenerateRandom(int min, int max){
            generalRandomRange = (max - min) + 1;
            return (int)(Math.random() * generalRandomRange) + min;
    }

    /**
     * checks whether the point vector vertices are inside a frustum's camera.
     * @param vertices
     * @param frustum
     */
    public static synchronized boolean isInsideCameraView(Vector2[] vertices, Frustum frustum){

        //check vert 1
        if(frustum.pointInFrustum(vertices[0].x, vertices[0].y, 0)){
            return true;
        }
        //check vert 2
        if(frustum.pointInFrustum(vertices[1].x, vertices[1].y, 0)){
            return true;
        }
        //check vert 3
        if(frustum.pointInFrustum(vertices[2].x, vertices[2].y, 0)){
            return true;
        }
        //check vert 4
        if(frustum.pointInFrustum(vertices[3].x, vertices[3].y, 0)){
            return true;
        }

        return false;
    }
    /**
     * checks whether a bounding box is inside a frustum's camera.
     * @param boundingBox
     * @param frustum
     */
    public static synchronized boolean isInsideCameraViewBox(BoundingBox boundingBox, Frustum frustum){

        if(frustum.boundsInFrustum(boundingBox)){
            return true;
        }

        return false;
    }
    /**
     * checks whether a bounding box is inside a frustum's camera.
     * the center is the center of the box.
     * the dimensions are the total width and height of the sprite.
     * @param center
     * @param dimensions
     * @param frustum
     */
    public static synchronized boolean isInsideCameraView(Vector2 center, Vector2 dimensions, Frustum frustum){

        if(frustum.boundsInFrustum(center.x, center.y, 0, dimensions.x / 2, dimensions.y / 2, 0)){
            return true;
        }

        return false;
    }
    /**
     * checks whether a circle is inside a frustum's camera.
     * @param center
     * @param radius
     * @param frustum
     */
    public static synchronized boolean isInsideCameraView(Vector2 center, float radius, Frustum frustum){

        if(frustum.sphereInFrustum(center.x, center.y, 0, radius)){
            return true;
        }

        return false;
    }
    /**
     * converts from body dimensions to pixels dimensions.
     * @param vector2
     * Coordinates.
     * @return
     */
    //small varaibles for the method below
    private static Vector2 convertBodyToPixelsVec = new Vector2(0, 0);
    public synchronized static Vector2 convertFromBodyToPixels(Vector2 vector2){
        convertBodyToPixelsVec.set(vector2.x* ConstantVariables.PIXELS_TO_METERS,
                vector2.y* ConstantVariables.PIXELS_TO_METERS);
        return convertBodyToPixelsVec;
    }
    /**
     * converts from body dimensions to pixels dimensions.
     * @param x, y
     * Coordinates.
     * @return
     */
    public synchronized static Vector2 convertFromBodyToPixels(float x, float y){
        convertBodyToPixelsVec.set(x* ConstantVariables.PIXELS_TO_METERS,
                                    y* ConstantVariables.PIXELS_TO_METERS);
        return convertBodyToPixelsVec;
    }

    /**
     * converts from pixels dimensions to body dimensions.
     * @param vector2
     * Coordinates.
     * @return
     */
    //small variable for the method below
    private static Vector2 convertPixelToBodyVec = new Vector2(0, 0);
    public synchronized static Vector2 convertFromPixelsToBody(Vector2 vector2){
        convertPixelToBodyVec.set(vector2.x/ConstantVariables.PIXELS_TO_METERS,
                vector2.y/ConstantVariables.PIXELS_TO_METERS);
        return convertPixelToBodyVec;
    }
    /**
     * converts from pixels dimensions to body dimensions.
     * @param x, y
     * Coordinates.
     * @return
     */
    public synchronized static Vector2 convertFromPixelsToBody(float x, float y){
        convertPixelToBodyVec.set(x/ConstantVariables.PIXELS_TO_METERS,
                                    y/ConstantVariables.PIXELS_TO_METERS);
        return convertPixelToBodyVec;
    }

    /**
     * check if a point is inside another polygon.
     * @param point
     * the point to be checked if it is inside it.
     * @param polygon
     * polygon that we are checking.
     */
    public static boolean isPointInsidePolygon(Vector2 point, float[] polygon){

        if(Intersector.isPointInPolygon(polygon, 0, polygon.length, point.x, point.y)){
            return true;
        }

        return false;
    }

    //ray cast collision detector methods
    //saves the collision state and is used by the ray cast method
    private static boolean hasCollided = false;
    //callback of ray cast
    private static RayCastCallback rayCastCallback = new RayCastCallback() {
        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
            hasCollided = true;
            return 0;
        }
    };
    /**
     * check if there is a collision between two points in the world.
     */
    public static boolean isThereCollision(Vector2 point1, Vector2 point2){

        //reset the collision variable
        hasCollided = false;

        //initiate the ray caster from the world
        MultiGameScreen.WORLD.rayCast(rayCastCallback, point1, point2);

        //return the result
        return hasCollided;
    }
    //saves the collision state and is used by the ray cast method
    private static boolean hasCollidedTank = false;
    //callback of ray cast
    private static RayCastCallback rayCastCallbackTank = new RayCastCallback() {
        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
            if(fixture.getBody().getUserData() != null
                    && fixture.getBody().getUserData() instanceof Tank
                    && ((Tank)fixture.getBody().getUserData()).tankProperties.tankpilot == Tank.TANKPILOT.MAIN_PLAYER
                    ||
                    fixture.isSensor() ){
                hasCollidedTank = false;
            }else{
                hasCollidedTank = true;
            }
            return 1;
        }
    };
    /**
     * check if there is a collision between two points in the world but excludes tanks.
     */
    public static boolean isThereCollisionTank(Vector2 point1, Vector2 point2){

        //reset the collision variable
        hasCollidedTank = false;

        if(point1 != null && point2 != null){
            //initiate the ray caster from the world and check center
            MultiGameScreen.WORLD.rayCast(rayCastCallbackTank, point1, point2);
        }

        //return the result
        return hasCollidedTank;
    }
    //end of ray collision detector methods


}
