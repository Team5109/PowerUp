package org.usfirst.frc.team5109.robot;

//Latest code as of 4/11/18

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
    TalonSRX leftMotor1 =  new TalonSRX(6); //6, 10, 5, 4
    TalonSRX leftMotor2 =  new TalonSRX(10);
    TalonSRX rightMotor1 =  new TalonSRX(5);//10
    TalonSRX rightMotor2 =  new TalonSRX(4);//10
    TalonSRX rightElevatorMotor = new TalonSRX(2);
	TalonSRX leftElevatorMotor = new TalonSRX(1);
    TalonSRX scalar = new TalonSRX(0);
    TalonSRX intakeBags = new TalonSRX(8);
	Joystick leftJoy = new Joystick(0);
	Joystick rightJoy = new Joystick(1);
	Joystick operator = new Joystick(2);
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
    Encoder rightEncoder = new Encoder(0, 1, true); //true
    Encoder leftEncoder = new Encoder(8, 9, false); //false
    double  leftspeed = 0;
	double rightspeed = 0;
	long idealright = 0;
	long idealleft = 0;
	int Counter = 0;
    
    @Override
    public void robotInit() {
    	CameraServer.getInstance().startAutomaticCapture();
    	compressor = new Compressor(0);
   	 leftEncoder.setDistancePerPulse(1);
   	 rightEncoder.setDistancePerPulse(1);   	 
    }

    
    @Override
    public void autonomousInit() {
    	leftEncoder.reset();
    	rightEncoder.reset();
   	    }  
    public void autonomousPeriodic() {
    int startpos = 2; //startpos 1 is far right, startpos 2 is middle right, startpos 3 is far left
    	String gameData = DriverStation.getInstance().getGameSpecificMessage();
		if(gameData.length() > 0) {
			if (startpos == 1) {
				if(gameData.charAt(0) == 'R') {
					rightAuto();
					}
				else {
					unluckyAuto();
				}
			}
			else if(startpos == 2) {
				if(gameData.charAt(0) == 'R') {
					middleAutoR2();
				}
				else {
					middleAutoL();
				}
			}
			else if(startpos == 3) {
				if(gameData.charAt(0) == 'L') {
					leftAuto();
				}
				else {
					unluckyAuto();
				}
			}
		}
		leftElevatorMotor.set(ControlMode.PercentOutput, -.4);
		rightElevatorMotor.set(ControlMode.PercentOutput, .4);		 
	   	 System.out.println("right " + rightEncoder.get());
	   	 System.out.println("left " + leftEncoder.get());
    }
  	 
    
    @Override
    public void teleopInit() {
    	rightEncoder.reset();
    	leftEncoder.reset();
   		 }    

    public void teleopPeriodic() {		
    			leftMotor1.set(ControlMode.PercentOutput, leftJoy.getY());
    			leftMotor2.set(ControlMode.PercentOutput, leftJoy.getY());
    			rightMotor1.set(ControlMode.PercentOutput, -1 * rightJoy.getY());
    			rightMotor2.set(ControlMode.PercentOutput, -1 * rightJoy.getY());
    			leftElevatorMotor.set(ControlMode.PercentOutput, -operator.getY());
    			rightElevatorMotor.set(ControlMode.PercentOutput, operator.getY());
    			int leftCount = leftEncoder.get();
    			int rightCount = rightEncoder.get();
    			boolean ejc = false;
    			System.out.println("right: " + rightCount);
    			System.out.println("left: " + leftCount);
    			if(operator.getRawButton(3) == true) {
    				scalar.set(ControlMode.PercentOutput, .5);
    			}
    			else if(operator.getRawButton(2) == true) {
    				scalar.set(ControlMode.PercentOutput, -.5);
    			}
    			else {
    				scalar.set(ControlMode.PercentOutput, 0);
    			}
    			
    			if(operator.getRawButton(7) == true) {
    					intakeBags.set(ControlMode.PercentOutput, -1);
    			}
    			else {
    				if(operator.getRawButton(8) == true) {
    					intakeBags.set(ControlMode.PercentOutput, 1);
    				}
    				else {
    					intakeBags.set(ControlMode.PercentOutput, 0);
    				}
    			}
    			
    			if (operator.getRawButton(9)) {
    				if (ejc = false) {
    					Solenoid2.set(false);
    					ejc = true;
    				} else {
    					Solenoid2.set(true);
    					ejc = false;
    				}
    			}
    			if (operator.getRawButton(8)) {
    				if (ejc = true) {
    					Solenoid2.set(false);
    					ejc = false;
    				}else {
    					Solenoid2.set(false);
    					ejc = false;
    				}
    			}
    			if (rightJoy.getRawButton(2)) {
    				if (lowgear == true) {
    					
    					Solenoid1.set(true);
    					lowgear = false;
    					Timer.delay(.08);
    				} else {
    					
    					Solenoid1.set(false);
    					lowgear = true;
    					Timer.delay(.08);
    				}
    			}
    			if (rightJoy.getRawButton(1) == true) {
    					Solenoid0.set(true);
    					
    					Timer.delay(.08);
    				}
    				else {
    					Solenoid0.set(false);
    					
    					Timer.delay(.08);
    				}
    			
    			if (leftJoy.getRawButton(1)) {
    				if(extended == false) {
    					Solenoid3.set(true);
    					extended = true;
    					Timer.delay(.08);
    				}
    				else {
    					Solenoid3.set(false);
    					extended = false;
    					Timer.delay(.08);
    				}
    			}
    		}

    @Override
    public void testInit() {    
    	Counter = 0;
    }

    public void testPeriodic() {
    	
    	
    }


public void driveStraight2 (double distance, int speed) {
	double encoder_counts = distance * 1564.556753;
	long leftCount = leftEncoder.get();
	long rightCount = rightEncoder.get();
	if(leftCount <= encoder_counts && rightCount < encoder_counts) {	
		leftMotor1.set(ControlMode.PercentOutput, -.5);
		leftMotor2.set(ControlMode.PercentOutput, -.5);
		rightMotor1.set(ControlMode.PercentOutput, .5);
		rightMotor2.set(ControlMode.PercentOutput, .5);
		 
	}
	else {
		leftMotor1.set(ControlMode.PercentOutput, 0);
		leftMotor2.set(ControlMode.PercentOutput, 0);
		rightMotor1.set(ControlMode.PercentOutput, 0);
		rightMotor2.set(ControlMode.PercentOutput, 0);
		leftEncoder.reset();
		rightEncoder.reset();
		Counter ++;
	}
}
public void driveStraight(double distance, int speed) {   //distance should be in feet, speed should be in encoder counts per cycle (40 is our base)
	double encoder_counts = distance * 1564.556753;
	double Acceleration = 0.03;
	long leftCount = leftEncoder.get();
	long rightCount = rightEncoder.get();
	long leftChange = leftCount - idealleft;
	long rightChange = rightCount - idealright;
	idealleft = leftEncoder.get();
	idealright = rightEncoder.get();
		if(leftCount <= encoder_counts && rightCount < encoder_counts) {	
			if (leftChange == speed) {
			}
			else if (leftChange >= speed) {
				leftspeed = leftspeed - Acceleration;
			}
			else if (leftChange <= speed) {
				leftspeed = leftspeed + Acceleration;	
			}
			if (rightChange == speed) {
			}
			else if (rightChange >= speed) {
				rightspeed = rightspeed - Acceleration;
			}
			else if (rightChange <= speed) {
				rightspeed = rightspeed + Acceleration;	
			}
			if (leftspeed >= 0.7) {
				leftspeed = 0.7;
			}
			if (rightspeed >= 0.7) {
				rightspeed = 0.7;
			}
			leftMotor1.set(ControlMode.PercentOutput, -leftspeed);
			leftMotor2.set(ControlMode.PercentOutput, -leftspeed);
			rightMotor1.set(ControlMode.PercentOutput, rightspeed);
			rightMotor2.set(ControlMode.PercentOutput, rightspeed);
			 
		}
		else {
			leftMotor1.set(ControlMode.PercentOutput, 0);
			leftMotor2.set(ControlMode.PercentOutput, 0);
			rightMotor1.set(ControlMode.PercentOutput, 0);
			rightMotor2.set(ControlMode.PercentOutput, 0);
			leftEncoder.reset();
			rightEncoder.reset();
			Counter ++;
		}
	
	  }
public void leftTurn(double degrees, double speed) { //degrees should be in degrees, basically out of 360
	long leftCount = leftEncoder.get();
	long rightCount = rightEncoder.get();
	double distanceR = 15*degrees;
	double distanceL = -15*degrees;
	if(rightCount <= distanceR || leftCount>= distanceL) {
		leftMotor1.set(ControlMode.PercentOutput, speed);
		leftMotor2.set(ControlMode.PercentOutput, speed);
		rightMotor1.set(ControlMode.PercentOutput, speed);
		rightMotor2.set(ControlMode.PercentOutput, speed);
	}
	else {
	leftEncoder.reset();
	rightEncoder.reset();
	leftMotor1.set(ControlMode.PercentOutput, 0);
	leftMotor2.set(ControlMode.PercentOutput, 0);
	rightMotor1.set(ControlMode.PercentOutput, 0);
	rightMotor2.set(ControlMode.PercentOutput, 0);
	Counter ++;

		
	}
}
public void rightTurn(double degrees, double speed) { //degrees should be in degrees, basically out of 360, speed is percent power of the motors
	long leftCount = leftEncoder.get();
	long rightCount = rightEncoder.get();
	double distanceL = 15*degrees;
	double distanceR = -15*degrees;
	if(leftCount <= distanceL || rightCount>= distanceR) {
		leftMotor1.set(ControlMode.PercentOutput, -speed);
		leftMotor2.set(ControlMode.PercentOutput, -speed);
		rightMotor1.set(ControlMode.PercentOutput, -speed);
		rightMotor2.set(ControlMode.PercentOutput, -speed);
	}
	else {
	leftEncoder.reset();
	rightEncoder.reset();
	leftMotor1.set(ControlMode.PercentOutput, 0);
	leftMotor2.set(ControlMode.PercentOutput, 0);
	rightMotor1.set(ControlMode.PercentOutput, 0);
	rightMotor2.set(ControlMode.PercentOutput, 0);
	Counter ++;

		
	}
}
public void eject() {
	Solenoid3.set(true);
	Timer.delay(.3);
	intakeBags.set(ControlMode.PercentOutput, .7);
	Timer.delay(.3);
	Solenoid0.set(true);
	Counter ++;
}
public void retract() {
	Solenoid0.set(false);
	Timer.delay(.3);
	intakeBags.set(ControlMode.PercentOutput, 0);
	Counter ++;
}
public void squareOff() {
	leftMotor1.set(ControlMode.PercentOutput, -.3);
	leftMotor2.set(ControlMode.PercentOutput, -.3);
	rightMotor1.set(ControlMode.PercentOutput, .3);
	rightMotor2.set(ControlMode.PercentOutput, .3);
	Timer.delay(1);
	leftMotor1.set(ControlMode.PercentOutput, 0);
	leftMotor2.set(ControlMode.PercentOutput, 0);
	rightMotor1.set(ControlMode.PercentOutput, 0);
	rightMotor2.set(ControlMode.PercentOutput, 0);
	Counter ++;
}
public void reverse() {
	Solenoid3.set(false);
	leftMotor1.set(ControlMode.PercentOutput, .3);
	leftMotor2.set(ControlMode.PercentOutput, .3);
	rightMotor1.set(ControlMode.PercentOutput, -.3);
	rightMotor2.set(ControlMode.PercentOutput, -.3);
	Timer.delay(1);
	leftMotor1.set(ControlMode.PercentOutput, 0);
	leftMotor2.set(ControlMode.PercentOutput, 0);
	rightMotor1.set(ControlMode.PercentOutput, 0);
	rightMotor2.set(ControlMode.PercentOutput, 0);
	Counter++;
}
public void getCube() { //If code goes wrong check here first. Could be intake.
	leftElevatorMotor.set(ControlMode.PercentOutput, .45);
	rightElevatorMotor.set(ControlMode.PercentOutput, -.45);
	Timer.delay(1.8);
	leftElevatorMotor.set(ControlMode.PercentOutput, 0);
	rightElevatorMotor.set(ControlMode.PercentOutput, 0);
	intakeBags.set(ControlMode.PercentOutput, -.7);
	Solenoid0.set(true);
	Timer.delay(.3);
	Solenoid3.set(true);
	leftMotor1.set(ControlMode.PercentOutput, -.5);
	leftMotor2.set(ControlMode.PercentOutput, -.5);
	rightMotor1.set(ControlMode.PercentOutput, .5);
	rightMotor2.set(ControlMode.PercentOutput, .5);
	Timer.delay(1.5);
	leftMotor1.set(ControlMode.PercentOutput, 0);
	leftMotor2.set(ControlMode.PercentOutput, 0);
	rightMotor1.set(ControlMode.PercentOutput, 0);
	rightMotor2.set(ControlMode.PercentOutput, 0);
	Solenoid0.set(false);
	Timer.delay(.5);
	Solenoid3.set(false);
	intakeBags.set(ControlMode.PercentOutput, 0);
	leftElevatorMotor.set(ControlMode.PercentOutput, -75);
	rightElevatorMotor.set(ControlMode.PercentOutput, .75);		
	leftMotor1.set(ControlMode.PercentOutput, .5);
	leftMotor2.set(ControlMode.PercentOutput, .5);
	rightMotor1.set(ControlMode.PercentOutput, -.55);
	rightMotor2.set(ControlMode.PercentOutput, -.55);
	Timer.delay(1.60);
	leftMotor1.set(ControlMode.PercentOutput, 0);
	leftMotor2.set(ControlMode.PercentOutput, 0);
	rightMotor1.set(ControlMode.PercentOutput, 0);
	rightMotor2.set(ControlMode.PercentOutput, 0);
	leftEncoder.reset();
	rightEncoder.reset();
	Counter ++;
	
}
public void rightAuto() {
	 if(Counter == 0) {
   		 driveStraight(12, 60);
   	 }
   	 else if(Counter == 1) { 
   		Timer.delay(.5);
   		leftTurn(90, .35);
   	 }
   	 else if(Counter == 2) {
   		 eject();
   	 }
   	 else { 
   		 System.out.println("done");
   	 }
}
public void leftAuto() {
	 if(Counter == 0) {
  		 driveStraight(12, 60);
  	 }
  	 else if(Counter == 1) { 
  		Timer.delay(.5);
  		rightTurn(90, .35);
  	 }
  	 else if(Counter == 2) {
  		 eject();
  	 }
  	 else {
  		 System.out.println("done");
  	 }
}
public void middleAutoR() {
	if(Counter == 0) {
		driveStraight(7.4, 40);
	}
	else if (Counter == 1) {
		eject();
	}
	else {
		System.out.println("done");
	}
}
public void middleAutoR2() {
	if(Counter == 0) {
		driveStraight(7.0, 120);
	}
	else if (Counter == 1) {
		eject();
	}
	else if (Counter == 2) {
		leftMotor1.set(ControlMode.PercentOutput, .3);
		leftMotor2.set(ControlMode.PercentOutput, .3);
		rightMotor1.set(ControlMode.PercentOutput, -.3);
		rightMotor2.set(ControlMode.PercentOutput, -.3);
		Timer.delay(.5);
		leftMotor1.set(ControlMode.PercentOutput, 0);
		leftMotor2.set(ControlMode.PercentOutput, 0);
		rightMotor1.set(ControlMode.PercentOutput, 0);
		rightMotor2.set(ControlMode.PercentOutput, 0);
		Counter ++;
	}
	else if (Counter == 3) {
		leftTurn(50.0, .4);
	}
	else if (Counter == 4) {
		getCube();
		
	}
	else if (Counter == 5) {
		rightTurn(55,.35);
	}
	else if (Counter == 6) {
		driveStraight(1.0, 40);
	}

	else if (Counter == 7) {
		eject();
	}
	else {
		System.out.println("done");
	}
}
public void middleAutoL() {
	if(Counter == 0) {
		driveStraight(3, 80);
	}
	else if(Counter == 1) {
		leftTurn(80, .35);
	}
	else if(Counter == 2) {
		driveStraight(7.2, 80);	
	}
	else if(Counter == 3) {
		rightTurn(80, .35);
	}
	else if(Counter == 4) {
		driveStraight(3.5, 80);
	}
	else if(Counter == 5) {
		eject();
	}
	else {
		System.out.println("done");
	}
}
public void unluckyAuto() { //basically drivestraight for when the switch is not on our side
	if(Counter == 0) {
		driveStraight(12, 60);
	}
	else {
		System.out.println("done");
	}
}

}
