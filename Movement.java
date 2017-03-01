package org.usfirst.frc.team5109.robot;


import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.GyroBase;

public class Movement extends Thread {
    GyroBase gyro;
    BuiltInAccelerometer accelerometer;
    
    
    //used for location tracking
    //saved values
    long previousSampleTime;
    double xSpeed;
    double ySpeed;
    double xAcceleration;
    double yAcceleration;
    double xLocation;
    double yLocation;
    
    //tmp variables
    long sampleTime;
    double newXAcceleration;
    double newYAcceleration;
    long timeChange;
    double newXSpeed;
    double newYSpeed;
    double xOffset;
    double yOffset;
    double angle;
    
    
    
    public void run() {
        gyro = new ADXRS450_Gyro();
        gyro.calibrate();
        accelerometer = new BuiltInAccelerometer();
        
        //enabling location data
        previousSampleTime = System.currentTimeMillis();
        xSpeed = 0;
        ySpeed = 0;
        xAcceleration = 0;
        yAcceleration = 0;
        xLocation = 10;
        yLocation = 10;
        movement();
    }
    
    
    public void movement() {
        
        //gets x data from accelerometer
        sampleTime = System.currentTimeMillis();
        newXAcceleration = accelerometer.getX();
        //converts g force into m/s^2
        newXAcceleration *= 9.8;
        //gets y data from accelerometer
        newYAcceleration = accelerometer.getY();
        //converts g force into m/s^2
        newYAcceleration *= 9.8;
        //calculates time change since last sample
        timeChange = sampleTime - previousSampleTime;
        //uses time change and acceleration to calculate new speeds
        newXSpeed = (((newXAcceleration + xAcceleration)/2) * timeChange) + xSpeed;
        newYSpeed = (((newYAcceleration + yAcceleration)/2) * timeChange) + ySpeed;
        //uses new speeds to calculate distance moved (averages the current and previous speed for accuracy in calculating distance travelled)
        xOffset = ((newXSpeed + xSpeed)/2) * sampleTime;
        yOffset = ((newYSpeed + ySpeed)/2) * sampleTime;
        angle = gyro.getAngle();
        //converts to radians
        angle = angle * 2 * Math.PI / 180;
        xLocation += xOffset * Math.sin(angle) + yOffset * Math.sin(angle);
        yLocation += yOffset * Math.sin(angle) + xOffset * Math.sin(angle);
        
        previousSampleTime = sampleTime;
        xAcceleration = newXAcceleration;
        yAcceleration = newYAcceleration;
        xSpeed = newXSpeed;
        ySpeed = newYSpeed;
        /*
        System.out.println("\n\n\n\n");
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                if (x == (int)xLocation && y == yLocation) {
                    System.out.print("/");
                }
                else {
                    System.out.print("-");
                }
            }
            System.out.println();
        }
        */
        movement();
    }
}