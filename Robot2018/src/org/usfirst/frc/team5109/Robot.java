
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
	TalonSRX rightMotor1 =  new TalonSRX(6);
	TalonSRX rightMotor2 =  new TalonSRX(10);
	TalonSRX leftMotor1 =  new TalonSRX(5);//10
	TalonSRX leftMotor2 =  new TalonSRX(4);//10
	TalonSRX rightElevatorMotor = new TalonSRX(2);
	TalonSRX leftElevatorMotor = new TalonSRX(1);
	TalonSRX intakeBags = new TalonSRX(8);
	TalonSRX scalar = new TalonSRX(0);
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
	boolean spinningBags = false;
	
	Solenoid Solenoid5 = new Solenoid(5);
	//Solenoids for gear shifting
	Solenoid Solenoid4 = new Solenoid(4);//1
	Compressor compressor;
	boolean lowgear = false;
	Encoder rightEncoder = new Encoder(0, 1, true); 
	Encoder leftEncoder = new Encoder(8, 9, false); 	
	
	/*double length = testEncoder.getDistance();
	//double period = testEncoder.getPeriod();
	
	boolean direction = testEncoder.getDirection();
	boolean stopped = testEncoder.getStopped();
	//For the encoder do not move 
	int count = 0;
	int i = 0;
	boolean testing = true;
	*/


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
		
		leftMotor1.set(ControlMode.PercentOutput, 0.05);
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
		rightMotor2.set(ControlMode.PercentOutput, - 0.4); 
		

	}
	/**
	 * This function is called periodically during autonomous.
	 */
	public void autonomousPeriodic() {
		/*
		leftMotor1.set(ControlMode.PercentOutput, -normalspeed);
		leftMotor2.set(ControlMode.PercentOutput, -normalspeed);
		rightMotor1.set(ControlMode.PercentOutput, (normalspeed/2));
		rightMotor2.set(ControlMode.PercentOutput, (normalspeed/2));
		*/
		int automode = 1; //1 = LEFT, 2 = MIDDLE 3 = RIGHT
		String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		switch(automode) {
		case 1: //left side
			   if(gameData.charAt(0) == 'L'){
				   driveStraightForward();
				   Solenoid3.set(true);
				   clamped = true;
				   Solenoid0.set(true);
				   extended = true;
			   }
			   /*else if(gameData.charAt(1) == 'L') { //does not work but if we go for scale then move elevator up and release
				   driveStraightForward();
				   driveStraightForward();
				   //raise elevator
				   Solenoid3.set(true);
				   clamped = true;
				   Solenoid0.set(true);
				   extended = true;
			   }
			   else { //goes to other switch
				   driveStraightForward();
				   driveStraightForward();
				   driveStraightForward();
			   }*/
		case 2: //middle (requires that you line up with the switch
			   if(gameData.charAt(0) == 'R') {
				   driveStraightForward(); //go to switch on our side and drop
			   } else {
				   driveStraightForward(); //got to the other switch and drop
			   }
		case 3://right side
			   if(gameData.charAt(0) == 'R'){
				   driveStraightForward();
				   driveStraightForward();
				   Solenoid3.set(true);
				   clamped = true;
				   Solenoid0.set(true);
				   extended = true;
			   } 
			   /*else if(gameData.charAt(1) == 'R') { //go to switch and drop (need to raise elevator)
				   driveStraightForward();
				   driveStraightForward();
				   //raise elevator
				   Solenoid3.set(true);
				   clamped = true;
				   Solenoid0.set(true);
				   extended = true;
			   }
			   else { //right side
			   	   driveStraightForward();
				   driveStraightForward();
				   driveStraightForward();
			   }*/
		}
		/*
		double masterPower = 0.1;
		double slavePower = 0.1;
		int error = 0;
		int kp = 25;
	
		leftMotor1.set(ControlMode.PercentOutput, -masterPower);
		leftMotor2.set(ControlMode.PercentOutput, -masterPower);
		rightMotor1.set(ControlMode.PercentOutput, slavePower);
		rightMotor2.set(ControlMode.PercentOutput, slavePower);
		leftEncoder.reset();
		rightEncoder.reset();
		error = -leftCount + rightCount;
		slavePower  = slavePower + (error/kp);
		Timer.delay(0.1);
		System.out.println("error:" + error + "slavePower" + slavePower);
		*/
		
		
			/*
	
			if (rightCount < (144513.262))
			{
				System.out.println(leftCount);
			}
			else {
				leftMotor1.set(ControlMode.PercentOutput, 0);
				leftMotor2.set(ControlMode.PercentOutput, 0);
				rightMotor1.set(ControlMode.PercentOutput, 0);
				rightMotor2.set(ControlMode.PercentOutput, 0);
			}
		*/

		
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
		/*if (degree2 == 45) {
			leftMotor1.set(ControlMode.PercentOutput, 4);
			leftMotor2.set(ControlMode.PercentOutput, 4);
			rightMotor1.set(ControlMode.PercentOutput, -4);
			rightMotor2.set(ControlMode.PercentOutput, -4);
		} else if(degree2 == -45) {
			leftMotor1.set(ControlMode.PercentOutput, -4);
			leftMotor2.set(ControlMode.PercentOutput, -4);
			rightMotor1.set(ControlMode.PercentOutput, 4);
			rightMotor2.set(ControlMode.PercentOutput, 4);
		}*/
	}
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

