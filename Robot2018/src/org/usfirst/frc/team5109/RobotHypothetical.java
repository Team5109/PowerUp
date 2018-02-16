
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
	TalonSRX rightMotor1 =  new TalonSRX(4);
	TalonSRX rightMotor2 =  new TalonSRX(1);
	TalonSRX leftMotor1 =  new TalonSRX(6);//10
	TalonSRX leftMotor2 =  new TalonSRX(10);//10
	TalonSRX rightElevatorMotor = new TalonSRX(5);
	TalonSRX leftElevatorMotor = new TalonSRX(2);
	Joystick leftJoy = new Joystick(0);
	Joystick rightJoy = new Joystick(1);
	Joystick operator = new Joystick(2);
	// Solenoids for gear shifting
	Solenoid Solenoid2 = new Solenoid(2);//1
	Solenoid Solenoid1 = new Solenoid(1);
	// Anand's Solenoids, 0 is used for clamping, 3 is used for extending
	Solenoid Solenoid0 = new Solenoid(0);
	boolean clamped = false;
	Solenoid Solenoid3 = new Solenoid(3);
	boolean extended = false;
	
	Solenoid Solenoid5 = new Solenoid(5);
	//Solenoids for gear shifting
	Solenoid Solenoid4 = new Solenoid(4);//1
	Compressor compressor;
	boolean lowgear = false;
	Encoder rightEncoder = new Encoder(0, 1, false); 
	Encoder leftEncoder = new Encoder(8, 9, false);
	double motorSpeed = 0.1;//Motorspeed is used for auto start and the starting speed driveStraight method goes, turn it negative for leftMotor to go straight
	int counter = 0;
	//NetworkTable imutable = NetworkTable.getTable("IMU Table");

 

	


	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		compressor = new Compressor(0);
		CameraServer.getInstance().startAutomaticCapture();
		leftEncoder.setDistancePerPulse(1);
		rightEncoder.setDistancePerPulse(1);
		/*.0184
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
		//NetworkTable imutable = NetworkTable.getSubTable("IMU Table");
		//System.out.println(imutable.getEntry("roll"));
	    //System.out.println(imutable.getEntry("pitch"));
	    //System.out.println(imutable.getEntry("yaw"));
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
		leftEncoder.reset();
		rightEncoder.reset();
		leftMotor1.set(ControlMode.PercentOutput, -motorSpeed);
		leftMotor2.set(ControlMode.PercentOutput, -motorSpeed);
		rightMotor1.set(ControlMode.PercentOutput, motorSpeed);
		rightMotor2.set(ControlMode.PercentOutput, motorSpeed);
	}
	/**
	 * This function is called periodically during autonomous.
	 */
	//The encoders must be reset after EVERY SINGLE METHOD RUN - usually done inside the method
	public void autonomousPeriodic() {
	
	}
		
	@Override
	public void teleopInit() {
		leftEncoder.reset();
		rightEncoder.reset();
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
		leftElevatorMotor.set(ControlMode.PercentOutput, operator.getY());
		//rightElevatorMotor.set(ControlMode.PercentOutput, -1*operator.getY());
		int leftCount = leftEncoder.get();
		int rightCount = rightEncoder.get();
		System.out.println("left: " + leftCount);
		System.out.println("right: " + rightCount);
		compressor.setClosedLoopControl(true);
		compressor.start();
		double x = 0;
		double y = 0;
		//NetworkTableEntry xValue = NetworkTableEntry.setDouble(x);
		//NetworkTableEntry yValue = NetworkTableEntry.setDouble(y);
		//in and out for two cylinders using button 2
		
		/*if(leftJoy.getRawButton(3)) {
			Solenoid3.set(false);
		} else {
			Solenoid3.set(true);
		}*/
		/*if(leftJoy.getRawButton(4)) {
			Solenoid4.set(false);
		} else {
			Solenoid4.set(true);
		}*/
		/*if(leftJoy.getRawButton(4)) {
			Solenoid5.set(false);
		} else {
			Solenoid5.set(true);
		}*/
		if (rightJoy.getRawButton(2)) {
			if (lowgear == true) {
				Solenoid2.set(true);
				Solenoid1.set(true);
				lowgear = false;
				Timer.delay(.001);
			} else {
				Solenoid2.set(false);
				Solenoid1.set(false);
				lowgear = true;
				Timer.delay(.001);
			}
		}
		if (rightJoy.getRawButton(1)) {
			if(clamped == false) {
				Solenoid0.set(true);
				clamped = true;
				Timer.delay(.001);
			}
			else {
				Solenoid0.set(false);
				clamped = false;
				Timer.delay(.001);
			}
		} 
		if (rightJoy.getRawButton(3)) {
			if(extended == false) {
				Solenoid3.set(true);
				extended = true;
				Timer.delay(.001);
			}
			else {
				Solenoid3.set(false);
				extended = false;
				Timer.delay(.001);
			}
		}
		//while(isOperatorControl() && isEnabled()) {
			
			*++++++++
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
	
	//130.3797294 pulses per inch
	//.0076699 inches per pulse

	public void driveStraight(double distanceFeet) { 
		double leftspeed = motorSpeed; //motorSpeed is declared at the stop, Changes the motors to go at that speed 
		double rightspeed = motorSpeed; 
		int leftCount = leftEncoder.get(); 
		int rightCount = rightEncoder.get();
		double averageCount = (leftCount + rightCount) * 0.5; 	//Find the distance the robot has gone 
		double distancePulse = distanceFeet * 1564.556753; 	//Convert the feet the user wants to go into encoder pulses 
		double dstraight = leftCount - rightCount; //Find the distance difference between the left and right sides 
		counter = 0;
		//Conditionals to determine whether to slow down the left or right sides to correct errors in driving straight and when to stop
		leftMotor1.set(ControlMode.PercentOutput, - leftspeed);
		leftMotor2.set(ControlMode.PercentOutput, - leftspeed);
		rightMotor1.set(ControlMode.PercentOutput,  rightspeed);
		rightMotor2.set(ControlMode.PercentOutput,  rightspeed);
		while (averageCount < distancePulse || counter > 150) {
			leftCount = leftEncoder.get();
			rightCount = rightEncoder.get();
			averageCount = (leftCount + rightCount) * 0.5;
			System.out.println("left: " + leftCount);									//Print out your encoder values
			System.out.println("right: " + rightCount);
			dstraight = leftCount - rightCount;											//dstraight is the difference of encoder values. Depending on value, used to know whether motors are ahead or behind.
			if ( dstraight ==  0) {
				;
			}
			else if (dstraight >= 1) {													//change motor speed to straighten out bot
				rightspeed = rightspeed - 0.02;
			}
			else if (dstraight <= -1) {
				leftspeed = leftspeed - 0.02;
			}
			leftMotor1.set(ControlMode.PercentOutput, - leftspeed);
			leftMotor2.set(ControlMode.PercentOutput, - leftspeed);
			rightMotor1.set(ControlMode.PercentOutput,  rightspeed);
			rightMotor2.set(ControlMode.PercentOutput,  rightspeed);
			System.out.println("dstraight: " + dstraight);
			Timer.delay(0.1);
			counter += 1; 
			
		}
		stopMotors();
		System.out.println(distanceFeet + ":feet has passed");
		System.out.println(counter);
		counter = 0;
	}
	
	//true is turn 90 right, false is turn 90 left
	public void turn90 (boolean right) {
		int leftCount = leftEncoder.get(); 
		int rightCount = rightEncoder.get();
		counter = 0;
		if (right == true) {
			System.out.println("I have turned right");
			while (((leftCount - rightCount)*0.5) < 4194.192964 || counter > 100) {			//tests the average of the encoders against 90 degrees, with a failsafe of 10 seconds which is 100 loops
				leftMotor1.set(ControlMode.PercentOutput, -motorSpeed);					//turn right by 90 degrees
				leftMotor2.set(ControlMode.PercentOutput, -motorSpeed);
				rightMotor1.set(ControlMode.PercentOutput, -motorSpeed);
				rightMotor2.set(ControlMode.PercentOutput, -motorSpeed); 
				leftCount = leftEncoder.get(); 											//updates encoder values
				rightCount = rightEncoder.get();
				Timer.delay(0.1);
				counter += 1;
				}
			stopMotors();
			System.out.println(counter);
			counter = 0;
		}
		else {
			System.out.println("I have turned left");
			while (((rightCount - leftCount)*0.5) < 4194.192964 || counter > 100) {		//tests average of encoders against 90 degrees, failsafe of 10 secons which is 100 loops
				leftMotor1.set(ControlMode.PercentOutput, motorSpeed);					//turn left by 90 degrees
				leftMotor2.set(ControlMode.PercentOutput, motorSpeed);
				rightMotor1.set(ControlMode.PercentOutput, motorSpeed);
				rightMotor2.set(ControlMode.PercentOutput, motorSpeed); 
				leftCount = leftEncoder.get(); 											//update encoder vaules
				rightCount = rightEncoder.get();
				Timer.delay(0.1);
				counter += 1;
			}
			stopMotors();
			System.out.println(counter);												//print value of counter to know how many cycles needed
			counter = 0;																//reset counter
		}
		
	}
	/*public void turnDegreesRight (double turnDegree) {
		int leftCount = leftEncoder.get(); 
		int rightCount = rightEncoder.get();
		counter = 0;
		double turnRevised = turnDegree/360;
		double turnRevisedInPulses = turnRevised * 
			while (((leftCount - rightCount)*0.5) < turnRevisedInPulses || counter > 100) {			//tests for the average value of encoders and if they are below the required value, failsafe ending at 100 loops
				leftMotor1.set(ControlMode.PercentOutput, -motorSpeed);
				leftMotor2.set(ControlMode.PercentOutput, -motorSpeed);
				rightMotor1.set(ControlMode.PercentOutput, -motorSpeed);
				rightMotor2.set(ControlMode.PercentOutput, -motorSpeed); 
				leftCount = leftEncoder.get(); 
				rightCount = rightEncoder.get();
				Timer.delay(0.1);
				counter += 1;
				}
			stopMotors();
			System.out.println(counter);
			counter = 0;
		}
	public void turnDegreesLeft (double turnDegree) {
		int leftCount = leftEncoder.get(); 
		int rightCount = rightEncoder.get();
		counter = 0;
		double turnRevised = turnDegree/360;
		double turnRevisedInPulses = turnRevised * 
			while (((rightCount - leftCount)*0.5) < turnRevisedInPulses || counter > 100) {
				leftMotor1.set(ControlMode.PercentOutput, motorSpeed);
				leftMotor2.set(ControlMode.PercentOutput, motorSpeed);
				rightMotor1.set(ControlMode.PercentOutput, motorSpeed);
				rightMotor2.set(ControlMode.PercentOutput, motorSpeed); 
				leftCount = leftEncoder.get(); 
				rightCount = rightEncoder.get();
				Timer.delay(0.1);
				counter += 1;
				}
			stopMotors();
			System.out.println(counter);
			counter = 0;
		}
		*/
	//Only use stopMotors to end a auto Method most of the time
	public void stopMotors () {
		leftMotor1.set(ControlMode.PercentOutput, 0);
		leftMotor2.set(ControlMode.PercentOutput, 0);
		rightMotor1.set(ControlMode.PercentOutput, 0);
		rightMotor2.set(ControlMode.PercentOutput, 0); 
		leftEncoder.reset();
		rightEncoder.reset();
	}
	public void goBackwards(double distanceFeet) {
		int leftCount = leftEncoder.get();
		int rightCount = rightEncoder.get();
		counter = 0;
		System.out.println("left: " + leftCount);
		System.out.println("right: " + rightCount);
		double leftspeed = motorSpeed; //motorSpeed is declared at the stop, Changes the motors to go at that speed 
		double rightspeed = motorSpeed;
		double averageCount = -(leftCount + rightCount) * 0.5; 	//Find the distance the robot has gone 
		System.out.println("left: " + leftCount);
		System.out.println("right: " + rightCount);
		double distancePulse = distanceFeet * 1564.556753; 	//Convert the feet the user wants to go into encoder pulses 
		double dstraight = leftCount + rightCount; //Find the distance difference between the left and right sides 
		//Conditionals to determine whether to slow down the left or right sides to correct errors in driving straight and when to stop
		leftMotor1.set(ControlMode.PercentOutput, leftspeed);
		leftMotor2.set(ControlMode.PercentOutput, leftspeed);
		rightMotor1.set(ControlMode.PercentOutput, - rightspeed);
		rightMotor2.set(ControlMode.PercentOutput, - rightspeed);
		while (averageCount < distancePulse || counter > 100) {
			leftCount = leftEncoder.get();
			rightCount = rightEncoder.get();
			averageCount = -(leftCount + rightCount) * 0.5;
			dstraight = leftCount + rightCount;
			if ( dstraight ==  0) {
				;
			}
			else if (dstraight >= 1) {
				rightspeed = rightspeed - 0.02;
			}
			else if (dstraight <= -1) {
				leftspeed = leftspeed - 0.02;
			}
			leftMotor1.set(ControlMode.PercentOutput, leftspeed);
			leftMotor2.set(ControlMode.PercentOutput, leftspeed);
			rightMotor1.set(ControlMode.PercentOutput, - rightspeed);
			rightMotor2.set(ControlMode.PercentOutput, - rightspeed);
			System.out.println("dstraight: " + dstraight);
			Timer.delay(0.1);
			counter += 1;
		}
		stopMotors();
		System.out.println(distanceFeet + ":feet has passed");
		System.out.println(counter);
		counter = 0;
	}
	public void Clamp(boolean clampBoolean) {
		if(clampBoolean == true) {
			Solenoid0.set(true);
			Timer.delay(.001);
		}
		else {
			Solenoid0.set(false);
			Timer.delay(.001);
		}
	}
	public void Extend(boolean extendBoolean) {
		if(extendBoolean == true) {
			Solenoid3.set(true);
			Timer.delay(.001);
		}
		else {
			Solenoid3.set(false);
			Timer.delay(.001);
		}
	}
	public void Elevator(boolean ElevatorBoolean) {
		//not done 
		leftElevatorMotor.set(ControlMode.PercentOutput, operator.getY());
		rightElevatorMotor.set(ControlMode.PercentOutput, -1*operator.getY());
		
	}
	public void tipTest(double imuPitch) {					
		double tipPoint = 0;								//the value at when the robot will tip
		double speedChange = 0;								//the speed the motor will change to so it doesn't fall
		if(imuPitch > tipPoint) {
			leftMotor1.set(ControlMode.PercentOutput, speedChange);
			leftMotor2.set(ControlMode.PercentOutput, speedChange);
			rightMotor1.set(ControlMode.PercentOutput, speedChange);
			rightMotor2.set(ControlMode.PercentOutput, speedChange);
		}
	}
	
	@Override
	public void testPeriodic() {
		
	}
}

