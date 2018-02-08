
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.usfirst.frc.team5109.robot;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import java.util.logging.Level;
import java.util.logging.Logger;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.networktables.*;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.DriverStation;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	TalonSRX leftMotor1 =  new TalonSRX(10);
	TalonSRX leftMotor2 =  new TalonSRX(10);
	TalonSRX rightMotor1 =  new TalonSRX(10);//10
	TalonSRX rightMotor2 =  new TalonSRX(1);//10
	Joystick leftJoy = new Joystick(0);
	Joystick rightJoy = new Joystick(1);
	// Solenoids for ....
	Solenoid Solenoid2 = new Solenoid(2);//1
	Solenoid Solenoid1 = new Solenoid(1);
	Solenoid Solenoid3 = new Solenoid(3);
	
	Solenoid Solenoid5 = new Solenoid(5);
	//Solenoids for gear shifting
	Solenoid Solenoid4 = new Solenoid(4);//1
	Compressor compressor;
	boolean lowgear = false;
	Encoder testEncoder = new Encoder(0, 1);
	testEncoder.setDistancePerPulse(5);
	/*testEncoder.setMaxPeriod(.1);
	testEncoder.setMinRate(10);
	testEncoder.setDistancePerPulse(5);
	testEncoder.setReverseDirection(true);
	testEncoder.setSamplesToAverage(7);*/
	int count = testEncoder.get();
	
	double encoderDistance = testEncoder.getDistance();
	//double period = testEncoder.getPeriod();
	double rate = testEncoder.getRate();
	boolean direction = testEncoder.getDirection();
	boolean stopped = testEncoder.getStopped();
	


	//NetworkTable imutable = NetworkTable.getTable("IMU Table");

 

	


	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		compressor = new Compressor(0);
		CameraServer.getInstance().startAutomaticCapture();
		/*NetworkTableInstance table = NetworkTableInstance.getDefault();
		NetworkTableInstance instance = NetworkTableInstance.getDefault();
		NetworkTable rootTable = instance.getTable("");
		System.out.println(rootTable);
		double[] defaultValue = new double[0];
		while(true) {
			double[] areas = table.getNumberArray("area",defaultValue);
			for(double area : areas) {
				System.out.println(area + " ");
			}
			System.out.println();
			Timer.delay(1);
		} */
		//NetworkTable imutable = NetworkTable.getTable("IMU Table");
		//System.out.println(imutable.getNumber("roll", "Error receiving roll"));
	    //System.out.println(imutable.getNumber("pitch", "Error receiving pitch"));
	    //System.out.println(imutable.getNumber("yaw", "Error receiving yaw"));
		//exampleSolenoid.set(true);
		//exampleSolenoid.set(false);
		//c.setClosedLoopControl(true);
		//c.setClosedLoopControl(false);
		
		

	}

	/**
	 * This function is run once each time the robot enters autonomous mode.
	 */
	@Override
	public void autonomousInit() {
		testEncoder.reset();
		//testEncoder.setDistancePerPulse(.01840775);
		testEncoder.setDistancePerPulse(1/360);
	}
	/**
	 * This function is called periodically during autonomous.
	 */
	public void autonomousPeriodic() {
		/*testEncoder.reset();
		double length = testEncoder.getDistance();
		double dist = testEncoder.getDistancePerPulse();
		System.out.println(dist);
		
		while (length <= 1)	{
			leftMotor1.set(ControlMode.PercentOutput, .5);
			System.out.println(length);
		}
		leftMotor1.set(ControlMode.PercentOutput, 0);
		*/
		
		
	}
		/*int automode = 1; //which auto case we r gonna run
		double waitTime = 0.1; //the wait time for each action
		String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		switch(automode) {
		case 1: //right side
			   if(gameData.charAt(0) == 'R'){
			       moveOnYAxis(100);
			       Timer.delay(waitTime);
			       turn90(-90);
			       Timer.delay(waitTime);
			       moveOnYAxis(50);
			       Timer.delay(waitTime);
			       break;
			   } else if(gameData.charAt(0) == 'R') {
				   moveOnYAxis(200);
			       Timer.delay(2*waitTime);
			       turn90(-90);
			       Timer.delay(waitTime);
			       moveOnYAxis(50);
			       Timer.delay(waitTime);
			       break;
			   }
			   else {
				   moveOnYAxis(255);
			       Timer.delay(3*waitTime);
			       turn90(-90);
			       Timer.delay(waitTime);
			       moveOnYAxis(50);
			       Timer.delay(waitTime);
			       break;
			   }
		case 2: //middle
			   if(gameData.charAt(0) == 'R') {
				   moveOnYAxis(100);
				   Timer.delay(waitTime);
				   turn45(45);
				   moveOnYAxis(100);
				   Timer.delay(2/waitTime);
				   break;
			   } else {
				   moveOnYAxis(100);
				   Timer.delay(waitTime);
				   turn45(-45);
				   moveOnYAxis(100);
				   Timer.delay(2/waitTime);
				   break;
			   }
		case 3:
			   if(gameData.charAt(0) == 'L'){
			       moveOnYAxis(100);
			       Timer.delay(waitTime);
			       turn90(90);
			       Timer.delay(waitTime);
			       moveOnYAxis(50);
			       Timer.delay(waitTime);
			       break;
			   } else if(gameData.charAt(0) == 'L') {
				   moveOnYAxis(200);
			       Timer.delay(2*waitTime);
			       turn90(90);
			       Timer.delay(waitTime);
			       moveOnYAxis(50);
			       Timer.delay(waitTime);
			       break;
			   }
			   else {
				   moveOnYAxis(255);
			       Timer.delay(3*waitTime);
			       turn90(90);
			       Timer.delay(waitTime);
			       moveOnYAxis(50);
			       Timer.delay(waitTime);
			       break;
			   }
		}
	}	

	*/
	/**
	 * This function is called once each time the robot enters teleoperated mode.
	 */
	@Override
	public void teleopInit() {
	}

	/**
	 * This function is called periodically during teleoperated mode.
	 */
	@Override
	public void teleopPeriodic() {
		compressor = new Compressor(0);
		leftMotor1.set(ControlMode.PercentOutput, leftJoy.getY());
		leftMotor2.set(ControlMode.PercentOutput, leftJoy.getY());
		rightMotor1.set(ControlMode.PercentOutput, -1 * rightJoy.getY());
		rightMotor2.set(ControlMode.PercentOutput, -1 * rightJoy.getY());
		compressor.setClosedLoopControl(true);
		compressor.start();
		double x = 0;
		double y = 0;
		//NetworkTableEntry xValue = NetworkTableEntry.setDouble(x);
		//NetworkTableEntry yValue = NetworkTableEntry.setDouble(y);
		//in and out for two cylinders using button 2
		if(leftJoy.getRawButton(1)) {
			Solenoid1.set(false);
		} else {
			Solenoid1.set(true);
		}
		if(leftJoy.getRawButton(2)) {
			Solenoid2.set(false);
		} else {
			Solenoid2.set(true);
		}
		
		if(leftJoy.getRawButton(3)) {
			Solenoid3.set(false);
		} else {
			Solenoid3.set(true);
		}
		if(leftJoy.getRawButton(4)) {
			Solenoid4.set(false);
		} else {
			Solenoid4.set(true);
		}
		if(leftJoy.getRawButton(4)) {
			Solenoid5.set(false);
		} else {
			Solenoid5.set(true);
		}
		if (rightJoy.getRawButton(2)) {
			if (lowgear) {
				Solenoid2.set(true);
				Solenoid1.set(true);
				lowgear = false;
			} else {
				Solenoid2.set(false);
				Solenoid1.set(false);
				lowgear = true;
			}
			Timer.delay(.2);	
		}
		
		//while(isOperatorControl() && isEnabled()) {
			
			
		//}
	}

	/**
	 * This function is called periodically during test mode.
	 */
	public void moveOnYAxis(int speed) { //0 - 255 and moves full robot forwards or backwards
		leftMotor1.set(ControlMode.PercentOutput, -1*speed);
		leftMotor2.set(ControlMode.PercentOutput, -1*speed);
		rightMotor1.set(ControlMode.PercentOutput, speed);
		rightMotor2.set(ControlMode.PercentOutput, speed);
	}
	public void turn90(int degree) {//90 or -90 nothing else works, and turns in a perfect right angle
		if (degree == 90) {
			leftMotor1.set(ControlMode.PercentOutput, 25);
			leftMotor2.set(ControlMode.PercentOutput, 25);
			rightMotor1.set(ControlMode.PercentOutput, 25);
			rightMotor2.set(ControlMode.PercentOutput, 25);
		} else if(degree == -90) {
			leftMotor1.set(ControlMode.PercentOutput, -25);
			leftMotor2.set(ControlMode.PercentOutput, -25);
			rightMotor1.set(ControlMode.PercentOutput, 25);
			rightMotor2.set(ControlMode.PercentOutput, 25);
		}
	}
	public void turn45(int degree2) {//45 or -45 nothing else works, and turns in a perfect right angle
		if (degree2 == 45) {
			leftMotor1.set(ControlMode.PercentOutput, 4);
			leftMotor2.set(ControlMode.PercentOutput, 4);
			rightMotor1.set(ControlMode.PercentOutput, -4);
			rightMotor2.set(ControlMode.PercentOutput, -4);
		} else if(degree2 == -45) {
			leftMotor1.set(ControlMode.PercentOutput, -4);
			leftMotor2.set(ControlMode.PercentOutput, -4);
			rightMotor1.set(ControlMode.PercentOutput, 4);
			rightMotor2.set(ControlMode.PercentOutput, 4);
		}
	}
	@Override
	public void testPeriodic() {
		leftMotor1.set(ControlMode.PercentOutput, .5);
		double encoderDistanceRaw = testEncoder.getRaw();
		boolean encoderDirection = testEncoder.getDirection();
		int count = testEncoder.get();
		//System.out.println(encoderDirection);
		//System.out.println(encoderDistanceRaw);
		//System.out.println(count);
		
		//inches per pulse = .0736310in/pulse
		double rate = testEncoder.getRate();
		System.out.println(rate * -1);
		
		
	}
}
