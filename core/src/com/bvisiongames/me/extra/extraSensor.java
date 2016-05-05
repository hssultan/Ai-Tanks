package com.bvisiongames.me.extra;

import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * Created by ahzji_000 on 2/11/2016.
 */
public class extraSensor {

    /**
     * spinner sensor reporter interface.
     */
    public interface SpinnerSensorInterface{
        void reportBegin(Fixture bodyFixture, Fixture alienFixture);
        void reportEnd(Fixture bodyFixture, Fixture alienFixture);
    }
    /**
     * The sensors class below are for sensor bodies that are not attached to the tank entity
     * body.
     *//*
    public class ExternalSensors{

        public EmptyCircleAreaSensor emptyCircleSensor;

        /**
         * initiator
         *//*
        public ExternalSensors(){

            emptyCircleSensor = new EmptyCircleAreaSensor();

        }

        /**
         * this class sets up a sensor body that is used for detecting object bodies around
         * a circular body shape.
         *//*
        public class EmptyCircleAreaSensor{

            //body variables
            public Body movingBody,
                    centerBody,
                    middleBody;

            //joint variable
            public RevoluteJoint revoluteJoint;
            //joint def variable
            public RevoluteJointDef revoluteJointDef;

            //saves the spinning state of this sensor
            public boolean spinning = false;

            //interface for reporting collision begin and end
            public SpinnerSensorInterface spinnerSensorInterface;

            //sensor responder
            public EmptyCircleAreaPointsSearcher searcher = new EmptyCircleAreaPointsSearcher();

            //boolean for enabling or disabling automatic stop when an empty area is found
            public boolean automaticStop = true;

            /**
             * initiator
             *//*
            public EmptyCircleAreaSensor(){

                //circle shape
                CircleShape circleShape = new CircleShape();
                circleShape.setRadius(TankIntelligi.this.tank.getDiagonal()/(2* ConstantVariables.PIXELS_TO_METERS));
                PolygonShape polygonShape = new PolygonShape();
                polygonShape.setAsBox(95/ConstantVariables.PIXELS_TO_METERS, circleShape.getRadius());
                //fixture def for circle
                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = circleShape;
                fixtureDef.density = 0;
                fixtureDef.friction = 0;
                fixtureDef.isSensor = true;
                fixtureDef.filter.categoryBits = ConstantVariables.Box2dVariables.SENSOR;
                fixtureDef.filter.maskBits = ConstantVariables.Box2dVariables.FLASH_LIGHT;
                fixtureDef.filter.groupIndex = 1;
                //fixture def for the polygon
                FixtureDef middleFixture = new FixtureDef();
                middleFixture.shape = polygonShape;
                middleFixture.density = 0.1f;
                middleFixture.friction = 0;
                middleFixture.isSensor = true;
                middleFixture.filter.categoryBits = ConstantVariables.Box2dVariables.SENSOR;
                middleFixture.filter.maskBits = ConstantVariables.Box2dVariables.FLASH_LIGHT;
                middleFixture.filter.groupIndex = 1;

                //create the moving body circle
                BodyDef movingDef = new BodyDef();
                movingDef.position.set(0, 0);
                movingDef.type = BodyDef.BodyType.DynamicBody;
                movingDef.bullet = true;
                this.movingBody = MultiGameScreen.WORLD.createBody(movingDef);
                this.movingBody.setUserData(new BodyIdentity(this, EXTERNALSENSOR.SEARCH_EMPTY_NEARBY_AREA_SENSOR));
                this.movingBody.createFixture(fixtureDef);

                //create the center body circle
                BodyDef centerDef = new BodyDef();
                centerDef.type = BodyDef.BodyType.KinematicBody;
                centerDef.position.set(TankIntelligi.this.tank.body.getPosition());
                this.centerBody = MultiGameScreen.WORLD.createBody(centerDef);
                this.centerBody.createFixture(fixtureDef);

                //middle body sensor
                BodyDef middleDef = new BodyDef();
                middleDef.position.set(0, 0);
                middleDef.type = BodyDef.BodyType.DynamicBody;
                this.middleBody = MultiGameScreen.WORLD.createBody(middleDef);
                this.middleBody.setUserData(new BodyIdentity(this, EXTERNALSENSOR.SEARCH_EMPTY_NEARBY_ROUTE_SENSOR));
                this.middleBody.createFixture(middleFixture);

                //attach the middle body to the moving body
                RevoluteJointDef revoluteJointDef1 = new RevoluteJointDef();
                revoluteJointDef1.bodyA = this.movingBody;
                revoluteJointDef1.bodyB = this.middleBody;
                revoluteJointDef1.localAnchorB.set(-95 / ConstantVariables.PIXELS_TO_METERS, 0);
                MultiGameScreen.WORLD.createJoint(revoluteJointDef1);

                //attach the middle body to the center body
                this.revoluteJointDef = new RevoluteJointDef();
                this.revoluteJointDef.bodyA = this.centerBody;
                this.revoluteJointDef.bodyB = this.middleBody;
                this.revoluteJointDef.enableMotor = true;
                this.revoluteJointDef.maxMotorTorque = 50000;
                this.revoluteJointDef.localAnchorB.set(95/ ConstantVariables.PIXELS_TO_METERS, 0);
                revoluteJoint = (RevoluteJoint)MultiGameScreen.WORLD.createJoint(this.revoluteJointDef);

            }


            //action methods
            /**
             * spin the moving circle.
             * @param speed
             * speed at which the revolter is going to be spinning at.
             * speed is measured in rotation of radian. (radians per second)
             *//*
            public void spin(float speed){
                if(speed != revoluteJoint.getMotorSpeed()){
                    revoluteJoint.setMotorSpeed(speed);
                    this.spinning = true;
                }
            }
            /**
             * this method makes the join stop spinning.
             *//*
            public void seizeSpin(){
                if(revoluteJoint.getMotorSpeed() != 0){
                    revoluteJoint.setMotorSpeed(0);
                    this.spinning = false;
                }
            }
            //end of action methods


            //listener methods
            /**
             * reports when moving body collides with another fixture.
             * @param bodyFixture
             * @param alienFixture
             *//*
            public void reportCollisionBegin(Fixture bodyFixture, Fixture alienFixture){

                if(automaticStop){
                    switch (searcher.searchtype){
                        case ESCAPE_POINT_SEARCH_POINT:
                            searcher.reportFreePointBegin(bodyFixture, alienFixture);
                            break;
                    }
                }

                //report the collision begin if spinner interface is not null
                if(this.spinnerSensorInterface != null){
                    this.spinnerSensorInterface.reportBegin(bodyFixture, alienFixture);
                }

            }
            /**
             * reports when moving body end colliding with a fixture.
             * @param bodyFixture
             * @param alienFixture
             *//*
            public void reportCollisionEnd(Fixture bodyFixture, Fixture alienFixture){

                if(automaticStop){
                    switch (searcher.searchtype){
                        case ESCAPE_POINT_SEARCH_POINT:
                            searcher.reportFreePointEnd(bodyFixture, alienFixture);
                            break;
                    }
                }

                //report the collision begin if spinner interface is not null
                if(this.spinnerSensorInterface != null){
                    this.spinnerSensorInterface.reportEnd(bodyFixture, alienFixture);
                }

            }
            //end of listener methods


            //setters
            /**
             * disable automatic stop.
             *//*
            public void disableAutomaticStop(){
                if(this.automaticStop != false){
                    this.automaticStop = false;
                }
            }
            /**
             * enable automatic stop.
             *//*
            public void enableAutomaticStop(){
                if(this.automaticStop != true){
                    this.automaticStop = true;
                }
            }
            /**
             * set the spinner collision interface listener.
             * @param spinnerSensorInterface
             *//*
            public void setSpinnerSensorInterface(SpinnerSensorInterface spinnerSensorInterface){
                this.spinnerSensorInterface = spinnerSensorInterface;
            }
            /**
             * position to place the spinner's center at.
             * @param position
             *//*
            public void setPosition(Vector2 position){
                if(position.x != this.centerBody.getPosition().x || position.y != this.centerBody.getPosition().y){
                    this.centerBody.setTransform(position, 0);
                }
            }
            /**
             * position to place the spinner's center at.
             * @param x, y
             *//*
            public void setPosition(float x, float y){
                if(x != this.centerBody.getPosition().x || y != this.centerBody.getPosition().y){
                    this.centerBody.setTransform(x, y, 0);
                }
            }
            //end of setters


            //getters

            //end of getters


            /**
             * public class that is used to identify each body object and return a reference to this parent class.
             *//*
            public class BodyIdentity{

                //saves the reference to this parent class
                public EmptyCircleAreaSensor emptyCircleAreaSensor;
                //saves which sensor it is
                public EXTERNALSENSOR externalsensor;

                /**
                 * initiator
                 *//*
                public BodyIdentity(EmptyCircleAreaSensor emptyCircleAreaSensor, EXTERNALSENSOR externalsensor){

                    this.emptyCircleAreaSensor = emptyCircleAreaSensor;
                    this.externalsensor = externalsensor;

                }

            }

        }

        /**
         * this class contains the methods that are used in emptycircle area sensor class.
         *//*
        public class EmptyCircleAreaPointsSearcher{

            //saves the search type
            //public SEARCHTYPE searchtype = SEARCHTYPE.NONE;

            //counts the number of collided bodies to the sensor
            public int reportCount = 0;

            //setters
            /**
             * set the search type.
             *//*
            public void setSearchtype(SEARCHTYPE searchtype){
                if(this.searchtype != searchtype){
                    this.searchtype = searchtype;
                    this.reportCount = 0;
                }
            }*/
            //end of setters


            //getters

            //end of getters


            //listener methods
    /*
            /**
             * report the begin collision used to search for free point.
             * @param bodyFixture
             * is the spinner sensors
             * @param alienFixture
             * other fixtures
             *//*
            public void reportFreePointBegin(Fixture bodyFixture, Fixture alienFixture){

                if(  alienFixture.getBody().getUserData() == null
                        || (alienFixture.getBody().getUserData() != null
                        && !(alienFixture.getBody().getUserData() instanceof Tank
                        && alienFixture.getBody().getUserData() == TankIntelligi.this.tank))
                        ){

                    //and do not sense sensors
                    if( alienFixture.getUserData() == null ||
                            ( alienFixture.getUserData() != null && !(alienFixture.getUserData() instanceof NEARBYSENSOR) ) ){
                        //add to the report count of how many bodies has collided with it
                        this.reportCount++;
                    }

                }else{

                    //if the report count is zero then stop the spinner and tell the move to free zone to start
                    //moving to the destination point
                    if(reportCount == 0){

                        //seize the spinner sensor
                        emptyCircleSensor.seizeSpin();
                        //update the destination point to the moving body sensor
                        TankIntelligi.this.action.destinationPoint.set(emptyCircleSensor.movingBody.getPosition());
                        //inform the move to free zone to start moving
                        TankIntelligi.this.action.moveToFreeZone.lock();
                        setSearchtype(SEARCHTYPE.NONE);

                    }

                }

            }
            /**
             * report the end collision used to search for free point.
             * @param bodyFixture
             * is the spinner sensors
             * @param alienFixture
             * other fixtures
             *//*
            public void reportFreePointEnd(Fixture bodyFixture, Fixture alienFixture){

                if(  alienFixture.getBody().getUserData() == null
                        || (alienFixture.getBody().getUserData() != null
                        && !(alienFixture.getBody().getUserData() instanceof Tank
                        && alienFixture.getBody().getUserData() == TankIntelligi.this.tank))
                        ){

                    //and do not sense sensors
                    if( alienFixture.getUserData() == null ||
                            ( alienFixture.getUserData() != null && !(alienFixture.getUserData() instanceof NEARBYSENSOR) ) ){
                        //remove from report count
                        if(reportCount > 0){
                            this.reportCount--;
                        }
                    }

                    //if the report count is zero then stop the spinner and tell the move to free zone to start
                    //moving to the destination point
                    if(reportCount == 0){

                        //seize the spinner sensor
                        emptyCircleSensor.seizeSpin();
                        //update the destination point to the moving body sensor
                        TankIntelligi.this.action.destinationPoint.set(emptyCircleSensor.movingBody.getPosition());
                        //inform the move to free zone to start moving
                        TankIntelligi.this.action.moveToFreeZone.lock();
                        setSearchtype(SEARCHTYPE.NONE);

                    }


                }

            }
            //end of listeners methods

        }

    }
    */

}
