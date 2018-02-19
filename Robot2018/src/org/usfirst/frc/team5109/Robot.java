
package org.usfirst.frc.team5109.robot; //import the framework to make everything work


import com.ctre.phoenix.motorcontrol.*; //all of our imports, if you see an issue press the + 
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
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
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
	TalonSRX rightMotor1 =  new TalonSRX(6); //make the talon of rightmotor1 associate with the talon with ID 6
	TalonSRX rightMotor2 =  new TalonSRX(10); //make the talon of rightmotor2 associate with the talon with ID 10
	TalonSRX leftMotor1 =  new TalonSRX(5);//make the talon of leftmotor1 associate with the talon with ID 5
	TalonSRX leftMotor2 =  new TalonSRX(4);//make the talon of leftmotor2 associate with the talon with ID 4
	TalonSRX rightElevatorMotor = new TalonSRX(2); //make the talon of rightElevatorMotor associate with the talon with ID 2
	TalonSRX leftElevatorMotor = new TalonSRX(1);//make the talon of leftElevatorMotor associate with the talon with ID 1
	TalonSRX intakeBags = new TalonSRX(8);//make the talon of intakeBags associate with the talon with ID 8
	TalonSRX scalar = new TalonSRX(0); //make the talon of scalar associate with the talon with ID 0
	Joystick leftJoy = new Joystick(0); //make the leftJoy Joystick be the first joystick plugged in (order is from the laptop edge to the screen)
	Joystick rightJoy = new Joystick(1); //make the rightJoy Joystick be the second joystick plugged in
	Joystick operator = new Joystick(2); //make the operator Joystick be the third joystick plugged in
	// Solenoids for gear shifting
	Solenoid Solenoid2 = new Solenoid(2);//make the solenoid2 (Pneumatic) associate with the third solenoid on the PCM
	Solenoid Solenoid1 = new Solenoid(1);//make the solenoid1 (Pneumatic)associate with the second solenoid on the PCM
	//0 is used for clamping, 3 is used for extending
	Solenoid Solenoid0 = new Solenoid(0); //make the solenoid0 (Pneumatic) associate with the first soleniod on the PCM
	boolean clamped = false; //set the boolean of clamped that tells if the intake is clamped
	Solenoid Solenoid3 = new Solenoid(3); //make the solenoid3 (Pneumatic) associate with the fourth soleniod on the PCM
	boolean extended = false; //set the boolean of extended that tells if the intake is extended
	boolean spinningBags = false; //set the boolean of spinningBags that tells if the intake motors are spinning
	
	Solenoid Solenoid5 = new Solenoid(5); //make the solenoid5 (Pneumatic) associate with the sixth solenoid on the PCM
	//Solenoids for gear shifting
	Solenoid Solenoid4 = new Solenoid(4);//make the solenoid4 (Pneumatic) associate with the fifth solenoid on the PCM
	Compressor compressor; //instatiate the compressor
	boolean lowgear = false; //make a boolean of lowgear to see if we are in low gear and if not its in high gear
	Encoder rightEncoder = new Encoder(0, 1, true); //create an encoder that maps to the port plugins of 0 and 1 and set it to true to make it turn right to get positive values
	Encoder leftEncoder = new Encoder(8, 9, false); //create an encoder that maps to the port plugins of 8 and 9 and set it to true to make it turn left to get positive values
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
	   compressor = new Compressor(0); //set the compressor to associate with the first and only compressor
		CameraServer.getInstance().startAutomaticCapture(); //get camera into driver station
		leftEncoder.setDistancePerPulse(1); //make the left encoder pulses per distance one to get accurate values
		rightEncoder.setDistancePerPulse(1); //make the right encoder pulses per distance one to get accurate values
	}
	/**
	 * This function is run once each time the robot enters autonomous mode.
	 */
	@Override
	public void autonomousInit() {
		
		leftEncoder.reset(); //reset the values of the encoder
		rightEncoder.reset();//reset the values of the encoder
		
		leftMotor1.set(ControlMode.PercentOutput, 0.05); //progressive speed increase to not rock the bot 
		leftMotor2.set(ControlMode.PercentOutput, 0.05);
		rightMotor1.set(ControlMode.PercentOutput, - 0.05);
		rightMotor2.set(ControlMode.PercentOutput, - 0.05);
		leftMotor1.set(ControlMode.PercentOutput, 0.1);
		leftMotor2.set(ControlMode.PercentOutput, 0.1);
		rightMotor1.set(ControlMode.PercentOutput, - 0.1);
		rightMotor2.set(ControlMode.PercentOutput, - 0.1);
		leftMotor1.set(ControlMode.PercentOutput, 0.2);
		leftMotor2.set(ControlMode.PercentOutput, 0.2);
		rightMotor1.set(ControlMode.PercentOutput, - 0.2);
		rightMotor2.set(ControlMode.PercentOutput, - 0.2);
		leftMotor1.set(ControlMode.PercentOutput, 0.3);
		leftMotor2.set(ControlMode.PercentOutput, 0.3);
		rightMotor1.set(ControlMode.PercentOutput, - 0.3);
		rightMotor2.set(ControlMode.PercentOutput, - 0.3); 
		leftMotor1.set(ControlMode.PercentOutput, 0.4);
		leftMotor2.set(ControlMode.PercentOutput, 0.4);
		rightMotor1.set(ControlMode.PercentOutput, - 0.4);
		rightMotor2.set(ControlMode.PercentOutput, - 0.4); //end of progressive increase
		Solenoid0.set(true); //clamp the box
		clamped = true;

	}
	/**
	 * This function is called periodically during autonomous.
	 */
	public void autonomousPeriodic() {
		int automode = 1; //1 = LEFT, 2 = MIDDLE 3 = RIGHT
		String gameData; //this get the "LLR" string from the FMS
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		Timer.delay(.2);
		switch(automode) {
		case 1: //left side
			   if(gameData.charAt(0) == 'L'){ //if the first character in the 3 string which is our switch is L then 
				   driveStraightForward(); //drive forward into the switch
				   Timer.delay(1);
				   Solenoid3.set(true); //extent the service module
				   extended = true; //make the checker true as it is extended
				   Timer.delay(.4); //wait until the intake is fully ejected
				   Solenoid0.set(false); //declamp the box
				   clamped = false; //set the checker to false as we are no longer clamped
			   }
			   else if(gameData.charAt(1) == 'L') { //does not work but if we go for scale then move elevator up and release
				   driveStraightForward();
				   driveStraightForward(); //drive to switch
				   //raise elevator
				   Solenoid0.set(false); //extent the service module
				   extended = true; //make the checker true as it is extended
				   Timer.delay(2); //wait until the intake is fully ejected
				   Solenoid3.set(true); //declamp the box
				   clamped = false; //set the checker to false as we are no longer clamped
			   }
			   else { //goes to other switch
				   driveStraightForward(); //drive to other switch
				   driveStraightForward();
				   driveStraightForward();
			   }
		case 2: //middle (requires that you line up with the switch
			   if(gameData.charAt(0) == 'R') { //if switch is on the right side
				   driveStraightForward(); //go to switch on our side and drop
			   } else {
				   driveStraightForward(); //got to the other switch and drop
			   }
		case 3://right side
			   if(gameData.charAt(0) == 'R'){ //if the switch is on our side
				   driveStraightForward(); //go to switch
				   Solenoid0.set(true); //extent the service module
				   extended = true; //make the checker true as it is extended
				   Timer.delay(2); //wait until the intake is fully ejected
				   Solenoid3.set(true); //declamp the box
				   clamped = false; //set the checker to false as we are no longer clamped
			   } 
		}

		
	}
		
	@Override
	public void teleopInit() { //
		leftEncoder.reset();
		rightEncoder.reset();
	}

	/**
	 * This function is called periodically during teleoperated mode.
	 */
	@Override
	public void teleopPeriodic() {
	compressor = new Compressor(0);
		leftMotor1.set(ControlMode.PercentOutput, -1 * leftJoy.getY());
		leftMotor2.set(ControlMode.PercentOutput, -1 * leftJoy.getY());
		rightMotor1.set(ControlMode.PercentOutput,  rightJoy.getY());
		rightMotor2.set(ControlMode.PercentOutput,  rightJoy.getY());
		leftElevatorMotor.set(ControlMode.PercentOutput, operator.getY());
		rightElevatorMotor.set(ControlMode.PercentOutput, (-1 *operator.getY()));
		int leftCount = leftEncoder.get();
		int rightCount = rightEncoder.get();
		System.out.println("right: " + rightCount);
		System.out.println("left: " + leftCount);
		//rightElevatorMotor.set(ControlMode.PercentOutput, -1*operator.getY());
		compressor.setClosedLoopControl(true);
		compressor.start();
		double x = 0;
		double y = 0;

		if(operator.getRawButton(11)) { //checks if the button is pressed
			scalar.set(ControlMode.PercentOutput, .25); //set the motor to power at 1/4 power
		}else {//if the button is not being pressed AT THAT MOMENT
			scalar.set(ControlMode.PercentOutput, .0); //kill power to the motor
		}
		
		if(operator.getRawButton(7)) {
			if(spinningBags == false) {
				intakeBags.set(ControlMode.PercentOutput, -1);
				spinningBags = true;
				Timer.delay(.0001);
			}
			else {
				intakeBags.set(ControlMode.PercentOutput, 0);
				spinningBags = false;
				Timer.delay(.0001);
			}
		}
		
		if (rightJoy.getRawButton(2)) {
			if (lowgear == true) {
				Solenoid1.set(true);
				lowgear = false;
				Timer.delay(.001);
			} else {
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
		if (leftJoy.getRawButton(1)) {
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
		}
		
		//while(isOperatorControl() && isEnabled()) {
			
			
		//}
	

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
		/*leftMotor1.set(ControlMode.PercentOutput, .5);
		double encoderDistanceRaw = testEncoder.getRaw();
		boolean encoderDirection = testEncoder.getDirection();
		int count = testEncoder.get();
		//System.out.println(encoderDirection);
		//System.out.println(encoderDistanceRaw);
		//System.out.println(count);
		
		//inches per pulse = .0736310in/pulse
		double rate = testEncoder.getRate();
		//System.out.println(rate * -1);
		*/
		
		
	}
	
	public void driveStraightForward() {//goes 11.67 ft
		double  leftspeed = .4;
		double rightspeed = .4;
		
		int leftCount = leftEncoder.get();
		int rightCount = rightEncoder.get();
		System.out.println("left: " + leftCount);
		System.out.println("right: " + rightCount);
		
		double dstraight = leftCount - rightCount;
		
		if (rightCount <= 7000) {
			if ( dstraight ==  0) {
			}
			
			else {
			if (dstraight >= 1) {
				rightspeed = rightspeed - 0.03;
				
			}
			else if (dstraight <= -1) {
				leftspeed = leftspeed - 0.03;
				
			}
			leftMotor1.set(ControlMode.PercentOutput, leftspeed);
			leftMotor2.set(ControlMode.PercentOutput, leftspeed);
			rightMotor1.set(ControlMode.PercentOutput, - rightspeed);
			rightMotor2.set(ControlMode.PercentOutput, - rightspeed);
			
			}

			System.out.println("dstraight: " + dstraight);
			Timer.delay(0.01);
		}
		
		else if((rightCount <= 7820) && (rightCount >= 7000)) {
			rightspeed = rightspeed - .3;
			leftspeed = leftspeed - .3;
			if ( dstraight ==  0) {
			}
			
			else {
			if (dstraight >= 1) {
				rightspeed = rightspeed - 0.03;
				
			}
			else if (dstraight <= -1) {
				leftspeed = leftspeed - 0.03;
				
			}
			leftMotor1.set(ControlMode.PercentOutput, leftspeed);
			leftMotor2.set(ControlMode.PercentOutput, leftspeed);
			rightMotor1.set(ControlMode.PercentOutput, - rightspeed);
			rightMotor2.set(ControlMode.PercentOutput, - rightspeed);
			
			}

			System.out.println("dstraight: " + dstraight);
			Timer.delay(0.01);
		}
		else {
			leftMotor1.set(ControlMode.PercentOutput, 0);
			leftMotor2.set(ControlMode.PercentOutput, 0);
			rightMotor1.set(ControlMode.PercentOutput, 0);
			rightMotor2.set(ControlMode.PercentOutput, 0);
		}
	}
}

