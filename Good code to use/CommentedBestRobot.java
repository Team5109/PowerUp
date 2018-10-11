package org.usfirst.frc.team5109.robot;

//Commented code on 10/10/18

//Motor Controller Imports
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

//Camera Import
import edu.wpi.first.wpilibj.CameraServer;

//Pneumatics Imports
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;

//Actual Robot Imports
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.DriverStation;

//IMU Imports
import edu.wpi.first.networktables.*;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    //Initizlizing left and right motors, change Talon IDs on different bots. 
	//Check for them at 10.51.9.2 in IE
	TalonSRX leftMotor1 =  new TalonSRX(6); //6, 10, 5, 4
    TalonSRX leftMotor2 =  new TalonSRX(10);
    TalonSRX rightMotor1 =  new TalonSRX(5);
    TalonSRX rightMotor2 =  new TalonSRX(4);
    
    //Initializing elevator motors. Check Talon IDs like other motors
    TalonSRX rightElevatorMotor = new TalonSRX(2);
	TalonSRX leftElevatorMotor = new TalonSRX(1);
    TalonSRX scalar = new TalonSRX(0);
    TalonSRX intakeBags = new TalonSRX(8);
    
    //Initializing joysticks. Check Driver Station to check which joystick is which.
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
    
    //Initializing Encoders, check pins to make sure encoders are plugged in correctly.
    //True/False denotes whether encoder is mounted backwards or forwards
    Encoder rightEncoder = new Encoder(0, 1, true); //true
    Encoder leftEncoder = new Encoder(8, 9, false); //false
    double  leftspeed = 0;
	double rightspeed = 0;
	long idealright = 0;
	long idealleft = 0;
	int Counter = 0;
    
    @Override
    //This runs just once when robot is first enabled
    public void robotInit() {
    	//Starts the camera and displays it on the laptop
    	CameraServer.getInstance().startAutomaticCapture();
    	
    	//Starts the compressor
    	compressor = new Compressor(0);
    	
    	//Just sets distance per pulse to one because we didn't do the math to actually get a distance.
   	 	leftEncoder.setDistancePerPulse(1);
   	 	rightEncoder.setDistancePerPulse(1);   	 
    }

    
    @Override
    //This runs when autonomous first begins.
    public void autonomousInit() {
    	
    	//Resets encoder counts for auto
    	leftEncoder.reset();
    	rightEncoder.reset();
    }  
    
    //Runs this code 20 times per second
    public void autonomousPeriodic() {
    	//startpos 1 is far right, startpos 2 is middle right, startpos 3 is far left
    	int startpos = 2; 
    	
    	//Gets game data on which side of the switch belongs to us
    	String gameData = DriverStation.getInstance().getGameSpecificMessage();
    	
    	//Ensures we actually got game data
		if(gameData.length() > 0) {
			
			//We are in the far right pos
			if (startpos == 1) {
				
				//Our side of the switch is on the right side
				if(gameData.charAt(0) == 'R') {
					
					//Run right auto
					rightAuto();
				}
				
				//Our side of the switch is on the opposite side
				else {
					
					//Just cross the auto line
					unluckyAuto();
				}
			}
			
			//We are in the middle right position
			else if(startpos == 2) {
				
				//Our side of the switch is in front of us
				if(gameData.charAt(0) == 'R') {
					
					//Run two cube middle auto. 
					//If for some reason two cube isn't working use middleAutoR();
					middleAutoR2();
				}
				//Our side of the switch is on the opposite side from us
				else {
					
					//Run middleAutoL
					middleAutoL();
				}
			}
			
			//We are in far left pos
			else if(startpos == 3) {
				//Our side of the switch is on our sidd
				if(gameData.charAt(0) == 'L') {
					
					//Run left auto
					leftAuto();
				}
				
				//Our side of the switch is on the opposite side
				else {
					
					//Just cross the auto line
					unluckyAuto();
				}
			}
		}
		
		//Keeps elevator from falling down mid match
		leftElevatorMotor.set(ControlMode.PercentOutput, -.4);
		rightElevatorMotor.set(ControlMode.PercentOutput, .4);
		
		//Prints left and right encoder counts for debug purposes
	   	System.out.println("right " + rightEncoder.get());
	   	System.out.println("left " + leftEncoder.get());
    }
  	 
    
    @Override
    //Runs once when teleop begins
    public void teleopInit() {
    	
    	//Gets encoder counts
    	rightEncoder.reset();
    	leftEncoder.reset();
   	}    

    public void teleopPeriodic() {		
    	//Robot is controlled via tank controls. Speed is related to joystick y pos.
    	leftMotor1.set(ControlMode.PercentOutput, leftJoy.getY());
    	leftMotor2.set(ControlMode.PercentOutput, leftJoy.getY());
   		rightMotor1.set(ControlMode.PercentOutput, -1 * rightJoy.getY());
  		rightMotor2.set(ControlMode.PercentOutput, -1 * rightJoy.getY());
  		
  		//Elevator controlled via a third joystick. Speed is related to joystick y pos/
  		leftElevatorMotor.set(ControlMode.PercentOutput, -operator.getY());
   		rightElevatorMotor.set(ControlMode.PercentOutput, operator.getY());
   		
   		//Gets encoder counts
   		int leftCount = leftEncoder.get();
   		int rightCount = rightEncoder.get();
   		
   		//Robot ejection set to false
   		boolean ejc = false;
   		
   		//Prints encoder counts for debugging
    	System.out.println("right: " + rightCount);
    	System.out.println("left: " + leftCount);
    	
    	//No idea what this button does, someone please update
    	if(operator.getRawButton(3) == true) {
    		scalar.set(ControlMode.PercentOutput, .5);
    	}
    	
    	else if(operator.getRawButton(2) == true) {
   			scalar.set(ControlMode.PercentOutput, -.5);
  		}
    	
    	else {
    		scalar.set(ControlMode.PercentOutput, 0);
   		}
    		
    	//Begins spinning the intake inwards when button 7 is pressed
    	if(operator.getRawButton(7) == true) {
   			intakeBags.set(ControlMode.PercentOutput, -1);
  		}
    	
    	//Begins spinning the intake outwards when button 8 is pressed
    	else {
    		if(operator.getRawButton(8) == true) {
   				intakeBags.set(ControlMode.PercentOutput, 1);
  			}
    		
    		//Stops spinning intake if no buttons are pressed
    		else {
    			intakeBags.set(ControlMode.PercentOutput, 0);
    		}
   		}
    	
    	//Opens or closes arm when button 9 is pressed
   		if (operator.getRawButton(9)) {
   			//If it hasn't ejected, eject the cube
    		if (ejc = false) {
    			Solenoid2.set(false);
   				ejc = true;
  			} 
    		
    		//If it has ejected then close arms
    		else {
 				Solenoid2.set(true);
    			ejc = false;
    		}
    	}
    	
   		//Seems like this also ejects? Not sure if this and the scalar code are redundant
   		if (operator.getRawButton(8)) {
    		if (ejc = true) {
    			Solenoid2.set(false);
    			ejc = false;
    		} 
    		
    		else {
    			Solenoid2.set(false);
    			ejc = false;
    		}
   		}
    	
   		//Switch from low to high gear or vice versa
   		if (rightJoy.getRawButton(2)) {
   			
   			//If in low gear switch to high gear. 
   			//Pause for a bit to prevent the chain from breaking or something
    		if (lowgear == true) {		
    			Solenoid1.set(true);
    			lowgear = false;
    			Timer.delay(.08);
    		}
    		
    		//If in high gear switch to low gear and pause a bit.
    		else {
    			Solenoid1.set(false);
    			lowgear = true;
    			Timer.delay(.08);
    		}
    	}
    	
   		//Clamps
   		if (rightJoy.getRawButton(1) == true) {
    		Solenoid0.set(true);			
    		Timer.delay(.08);
    	}
    	
   		else {
    		Solenoid0.set(false);			
    		Timer.delay(.08);
    	}
    	
   		//Extension/Retraction
    	if (leftJoy.getRawButton(1)) {
    		
    		//If not extended, extend and wait a bit.
    		if(extended == false) {
    			Solenoid3.set(true);
    			extended = true;
    			Timer.delay(.08);
    		}
    		
    		//If extended, retract and wait a bit
    		else {
    			Solenoid3.set(false);
    			extended = false;
    			Timer.delay(.08);
    		}
   		}
  	}

    @Override
    //Only use testInit and testPeriodic for test code because it won't run in comp.
    public void testInit() {    
    	leftEncoder.reset();
    	rightEncoder.reset();

    }


    public void testPeriodic() {
    	middleAutoR2();
    	leftElevatorMotor.set(ControlMode.PercentOutput, -.4);
		rightElevatorMotor.set(ControlMode.PercentOutput, .4);		 
    	System.out.println("left: " + leftEncoder.get());
		System.out.println("right: " + rightEncoder.get());

    }

//failed drivestraight i think. Delete later
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

//Drives straight. 
//Takes a distance in feet, and encoder pulses/run of loop (pulses/.05 seconds).
public void driveStraight(double distance, int speed) {   //distance should be in feet, speed should be in encoder counts per cycle (40 is our base)
	
	//Converts distance to the correct num of encoder counts
	double encoder_counts = distance * 1564.556753;
	
	//Acceleration value if encoders get off. Equivalent to 3% motor power 
	double Acceleration = 0.03;
	
	//Gets encoder counts
	long leftCount = leftEncoder.get();
	long rightCount = rightEncoder.get();
	
	//Gets the change in encoder counts from the last measured value
	long leftChange = leftCount - idealleft;
	long rightChange = rightCount - idealright;
	
	//Sets the ideal value to the current value for the next run through
	idealleft = leftEncoder.get();
	idealright = rightEncoder.get();
	
		//Checks that both encoder counts have not reached their goal
	if(leftCount <= encoder_counts && rightCount < encoder_counts) {	
		
		//If the change is equal to the set counts/pulse change nothing
		if (leftChange == speed) {
		}
		
		//If left encoder is going faster than we want slow it down by 3%
		else if (leftChange >= speed) {
			leftspeed = leftspeed - Acceleration;
		}
		
		//If left encoder is going slower than we want, speed it up by 3%
		else if (leftChange <= speed) {
			leftspeed = leftspeed + Acceleration;	
		}
		
		//If right encoder is going at the speed we want, do nothing
		if (rightChange == speed) {
		}
		
		//If the right encoder is going faster than we want, slow it down by 3%
		else if (rightChange >= speed) {
			rightspeed = rightspeed - Acceleration;
		}
		
		//If the right encoder is going slower than we want, speed it up by 3%
		else if (rightChange <= speed) {
			rightspeed = rightspeed + Acceleration;	
		}
		
		//If either motor exceeds 70% power, set them to 70% power
		if (leftspeed >= 0.7) {
			leftspeed = 0.7;
		}
		
		if (rightspeed >= 0.7) {
			rightspeed = 0.7;
		}
		
		//Sets motor to whatever the speed is for this cycle
		leftMotor1.set(ControlMode.PercentOutput, -leftspeed);
		leftMotor2.set(ControlMode.PercentOutput, -leftspeed);
		rightMotor1.set(ControlMode.PercentOutput, rightspeed);
		rightMotor2.set(ControlMode.PercentOutput, rightspeed);
		 
	}
		
	//If encoders have reached the goal, stop all motors. Remember the bot will drift a bit!
	else {
		leftMotor1.set(ControlMode.PercentOutput, 0);
		leftMotor2.set(ControlMode.PercentOutput, 0);
		rightMotor1.set(ControlMode.PercentOutput, 0);
		rightMotor2.set(ControlMode.PercentOutput, 0);
		
		//Resets encoders
		leftEncoder.reset();
		rightEncoder.reset();
		
		//Progresses the auto to the next step
		Counter ++;
	}
}

//Left turn method. Takes degrees in degrees out of 360, and a speed in a decimal from 0-1.
//1 degree in encoder counts is roughly 15.
public void leftTurn(double degrees, double speed) {
	
	//Gets encoder counts
	long leftCount = leftEncoder.get();
	long rightCount = rightEncoder.get();
	
	//Sets distance in encoder counts to 15 encoder counts * degrees
	double distanceR = 15*degrees;
	double distanceL = -15*degrees;
	
	//If the encoders have not reached destination, keep them moving
	//Note: does not need to worry about difference because both motors are moving in same direction
	if(rightCount <= distanceR || leftCount>= distanceL) {
		leftMotor1.set(ControlMode.PercentOutput, speed);
		leftMotor2.set(ControlMode.PercentOutput, speed);
		rightMotor1.set(ControlMode.PercentOutput, speed);
		rightMotor2.set(ControlMode.PercentOutput, speed);
	}
	
	//If encoders have reached the goal, stop them
	else {
		
		//Resets encoders
		leftEncoder.reset();
		rightEncoder.reset();
		
		leftMotor1.set(ControlMode.PercentOutput, 0);
		leftMotor2.set(ControlMode.PercentOutput, 0);
		rightMotor1.set(ControlMode.PercentOutput, 0);
		rightMotor2.set(ControlMode.PercentOutput, 0);
		
		//Progresses auto to next step
		Counter ++;		
	}
}

//Right turn method. Takes degrees in degrees, and a speed in form of a decimal from 0-1
public void rightTurn(double degrees, double speed) { //degrees should be in degrees, basically out of 360, speed is percent power of the motors
	//Gets encoder counts
	long leftCount = leftEncoder.get();
	long rightCount = rightEncoder.get();
	
	//Sets distance to encoder counts	
	double distanceL = 15*degrees;
	double distanceR = -15*degrees;
	
	//If encoders have not reached the goal, keep motors going
	//Note: Difference in encoder counts doesn't matter because 
	if(leftCount <= distanceL || rightCount>= distanceR) {
		leftMotor1.set(ControlMode.PercentOutput, -speed);
		leftMotor2.set(ControlMode.PercentOutput, -speed);
		rightMotor1.set(ControlMode.PercentOutput, -speed);
		rightMotor2.set(ControlMode.PercentOutput, -speed);
	}
	
	//If it reached its goal, stop all motors
	else {
		
		//Reset encoder counts
		leftEncoder.reset();
		rightEncoder.reset();
		
		
		leftMotor1.set(ControlMode.PercentOutput, 0);
		leftMotor2.set(ControlMode.PercentOutput, 0);
		rightMotor1.set(ControlMode.PercentOutput, 0);
		rightMotor2.set(ControlMode.PercentOutput, 0);
		
		//Progresses counter for next auto step
		Counter ++;
	}
}

//Method to eject a cube
public void eject() {
	
	//Extends the arms and waits a bit
	Solenoid3.set(true);
	Timer.delay(.3);
	
	//Spins the intake out and waits a bit
	intakeBags.set(ControlMode.PercentOutput, .7);
	Timer.delay(.3);
	
	//Unclamps
	Solenoid0.set(true);
	
	//Progresses counter for next auto step
	Counter ++;
}

//Retracts the arm. Unused, delete?
public void retract() {
	
	//Clamps, waits, and stops intake
	Solenoid0.set(false);
	Timer.delay(.3);
	intakeBags.set(ControlMode.PercentOutput, 0);
	
	//Progresses auto step
	Counter ++;
}

//Square off method. Not exactly sure why we have it but okay.
public void squareOff() {
	//Moves everything forward a bit, then stops
	leftMotor1.set(ControlMode.PercentOutput, -.3);
	leftMotor2.set(ControlMode.PercentOutput, -.3);
	rightMotor1.set(ControlMode.PercentOutput, .3);
	rightMotor2.set(ControlMode.PercentOutput, .3);
	Timer.delay(1);
	leftMotor1.set(ControlMode.PercentOutput, 0);
	leftMotor2.set(ControlMode.PercentOutput, 0);
	rightMotor1.set(ControlMode.PercentOutput, 0);
	rightMotor2.set(ControlMode.PercentOutput, 0);
	
	//Progresses auto step
	Counter++;
}

//square off method but backwards
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
	Counter ++;
}

//Get cube method for the two cube. Hardcoded could be optimized.
public void getCube() {
	
	//Moves elevator down for some time
	leftElevatorMotor.set(ControlMode.PercentOutput, .4);
	rightElevatorMotor.set(ControlMode.PercentOutput, -.4);
	Timer.delay(1.8);
	
	//Stops elevator motors
	leftElevatorMotor.set(ControlMode.PercentOutput, 0);
	rightElevatorMotor.set(ControlMode.PercentOutput, 0);
	
	//Starts spinning intake in
	intakeBags.set(ControlMode.PercentOutput, -.7);
	
	//Unclamps and waits a bit
	Solenoid0.set(true);
	Timer.delay(.3);
	
	//Extends the arm
	Solenoid3.set(true);
	
	//Drives forward for a bit
	leftMotor1.set(ControlMode.PercentOutput, -.5);
	leftMotor2.set(ControlMode.PercentOutput, -.5);
	rightMotor1.set(ControlMode.PercentOutput, .5);
	rightMotor2.set(ControlMode.PercentOutput, .5);
	Timer.delay(1.5);
	
	//Stops movement
	leftMotor1.set(ControlMode.PercentOutput, 0);
	leftMotor2.set(ControlMode.PercentOutput, 0);
	rightMotor1.set(ControlMode.PercentOutput, 0);
	rightMotor2.set(ControlMode.PercentOutput, 0);
	
	//Clamps and waits a bit
	Solenoid0.set(false);
	Timer.delay(.5);
	
	//Retracts
	Solenoid3.set(false);
	
	//Stops intake
	intakeBags.set(ControlMode.PercentOutput, 0);
	
	//Moves elevator up and robot backwards for 1 second
	leftElevatorMotor.set(ControlMode.PercentOutput, -.7);
	rightElevatorMotor.set(ControlMode.PercentOutput, .7);		
	leftMotor1.set(ControlMode.PercentOutput, .5);
	leftMotor2.set(ControlMode.PercentOutput, .5);
	rightMotor1.set(ControlMode.PercentOutput, -.55);
	rightMotor2.set(ControlMode.PercentOutput, -.55);
	Timer.delay(1);
	
	//Stop all motors
	leftMotor1.set(ControlMode.PercentOutput, 0);
	leftMotor2.set(ControlMode.PercentOutput, 0);
	rightMotor1.set(ControlMode.PercentOutput, 0);
	rightMotor2.set(ControlMode.PercentOutput, 0);
	
	//Progress the auto step
	Counter ++;
}

//Auto for right startpos, right switch
public void rightAuto() {
	//Step 1, drive straight 12 feet 
	if(Counter == 0) {
   		driveStraight(12, 60);
   	}
	
	//Step 2, wait a bit then turn left 90 degrees
   	else if(Counter == 1) { 
   		Timer.delay(.5);
   		leftTurn(90, .35);
   	}
	
	//Step 3, eject the cube
   	else if(Counter == 2) {
   		eject();
    }
	
	//Print done when finished
 	else { 
   		System.out.println("done");
    }
}

//Auto for left startpos, left switch pos
public void leftAuto() {
	
	//Step 1, drive straight 12 feet
	if(Counter == 0) {
		driveStraight(12, 60);
  	}
	
	//Step 2, wait a bit then turn right 90 degrees
  	else if(Counter == 1) { 
  		Timer.delay(.5);
  		rightTurn(90, .35);
  	}
	
	//Step 3, eject the cube
  	else if(Counter == 2) {
  		eject();
  	}
	
	//Print done when finished
  	else { 
  		System.out.println("done");
  	}
}

//1 cube middle start pos, right switch pos
public void middleAutoR() {
	//Step 1, drive straight 7.4 feet
	if(Counter == 0) {
		driveStraight(7.4, 40);
	}
	
	//Step 2, eject cube
	else if (Counter == 1) {
		eject();
	}
	
	//Print done when finished
	else {
		System.out.println("done");
	}
}

//Two cube middle right startpos, right switch pos
public void middleAutoR2() {
	//Step 1, drive straight 7 feet
	if(Counter == 0) {
		driveStraight(7.0, 120);
	}
	
	//Step 2, eject first cube
	else if (Counter == 1) {
		eject();
	}
	
	//Step 3, go back a bit to prepare for second cube
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
	
	//Step 4, turn left 80 degrees (the last 10 degrees come from drift).
	else if (Counter == 3) {
		leftTurn(80, .4);
	}
	
	//Step 5, get second cube
	else if (Counter == 4) {
		getCube();
	}
	
	//Step 6, turn right 40 degrees
	else if (Counter == 5) {
		rightTurn(40, .35);
	}
	
	//Eject second cube. UNCOMMENT THIS IF ITS RELIABLE.
	/*else if (Counter == 6) {
		eject();
	}
	*/
	
	//Print finished when done
	else {
		System.out.println("done");
	}
}

//Auto for middle right startpos, left switch pos/
public void middleAutoL() {
	
	//Step 1, drive straight 3 feet
	if(Counter == 0) {
		driveStraight(3, 80);
	}
	
	//Step 2, turn left 80 degrees (last 10 degrees come from drift)
	else if(Counter == 1) {
		leftTurn(80, .35);
	}
	
	//Step 3, drive straight 7.2 feet
	else if(Counter == 2) {
		driveStraight(7.2, 80);	
	}
	
	//Step 4, turn right 80 degrees (last 10 come from drift)
	else if(Counter == 3) {
		rightTurn(80, .35);
	}
	
	//Step 5, drive straight 3.5 feet
	else if(Counter == 4) {
		driveStraight(3.5, 80);
	}
	
	//Step 6, eject cube
	else if(Counter == 5) {
		eject();
	}
	
	//Print done when finished
	else {
		System.out.println("done");
	}
}

//Unlucky auto is when bot is in a far startpos, but switch is on opposite side
public void unluckyAuto() {
	//Drive 12 feet past auto line then print done
	driveStraight(12, 60);
	System.out.println("done");
}
}
//Congrats you made it to the end
