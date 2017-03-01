package org.usfirst.frc.team5109.robot;

import edu.wpi.first.wpilibj.Encoder;

public class Shifting extends Thread{
    /*
     * want to switch up/down at 943 pulses per second
     * 
     */
    public int lowGearRatio;
    public int highGearRatio;
    public boolean isLowGear;

    public void run() {
        Encoder rightDrivetrain = new Encoder(4, 5);
        rightDrivetrain.setDistancePerPulse(1);
        isLowGear = true;
        while (true) {
           if (rightDrivetrain.getRate() > 943 && isLowGear) {
               //shift up
               System.out.println("Shifted Up");
           }
           else if (rightDrivetrain.getRate() < 943 && !isLowGear) {
               //shift down
               System.out.println("Shifted Down");
           }
        }
    }
}

/*
package org.usfirst.frc.team5109.robot;


import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;

/**
 * This is a demo program showing the use of the RobotDrive class.
 * The SampleRobot class is the base of a robot application that will automatically call your
 * Autonomous and OperatorControl methods at the right time as controlled by the switches on
 * the driver station or the field controls.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're inexperienced,
 * don't. Unless you know what you are doing, complex code will be much more difficult under
 * this system. Use IterativeRobot or Command-Based instead if you're new.
 */
/*
public class Robot extends SampleRobot {
    
    Victor leftDrive1;
    Victor leftDrive2;
    Victor rightDrive1;
    Victor rightDrive2;
    
    Joystick leftStick;
    Joystick rightStick;
    Encoder rightDrivetrain;
    
    boolean lowGear;
  

   public Robot() {
        leftDrive1 = new Victor(0);
        leftDrive2 = new Victor(1);
        rightDrive1 = new Victor(2);
        rightDrive2 = new Victor(3);
        leftStick = new Joystick(0);
        rightStick = new Joystick(1);
        rightDrivetrain = new Encoder(4, 5);
        lowGear = true;
        
    }


     //Drive left & right motors for 2 seconds then stop
 
    public void autonomous() {
        //starts positioning system
        (new Movement()).start();
        
        //print statement to check threading
        for (int i = 0; i < 100; i++) {
            System.out.println("*********************");
        }
        
        //auto testing of encoder
        rightDrivetrain.reset();
        while (rightDrivetrain.get() < 100) {
            rightDrive1.set(.5);
            rightDrive2.set(.5);
        }
    }

   
    public void operatorControl() {
       leftDrive1.set(leftStick.getY());
       leftDrive2.set(leftStick.getY());
       rightDrive1.set(rightStick.getX());
       rightDrive2.set(rightStick.getX());
    }


    public void test() {
        
    }
}
*/