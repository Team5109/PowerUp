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
	TalonSRX leftMotor1 =  new TalonSRX(6);
	TalonSRX leftMotor2 =  new TalonSRX(10);
	TalonSRX rightMotor1 =  new TalonSRX(5);//10
	TalonSRX rightMotor2 =  new TalonSRX(4);//10
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
	
	Solenoid Solenoid5 = new Solenoid(5);
	//Solenoids for gear shifting
	Solenoid Solenoid4 = new Solenoid(4);//1
	Compressor compressor;
	boolean lowgear = false;
	Encoder rightEncoder = new Encoder(0, 1, true); 
	Encoder leftEncoder = new Encoder(8, 9, false); 
	double  leftspeed = 0;
	double rightspeed = 0;
	long idealright = 0;
	long idealleft = 0;
	int Counter = 0;
	/*boolean spinningBags = false;
	boolean spinningBags1 = false;*/
	
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
	  String gameData;
		leftEncoder.reset();
		rightEncoder.reset();
		idealright = rightEncoder.get();
		idealleft = leftEncoder.get();
		Counter = 0;

		
		
		

	}
	/**
	 * This function is called periodically during autonomous.
	 */
	public void autonomousPeriodic() {
		long leftCount = leftEncoder.get();
		long rightCount = rightEncoder.get();
		String gameData = DriverStation.getInstance().getGameSpecificMessage ();
		if(gameData.length() > 0)
	    {
	    if(gameData.charAt(0) == 'R')
			  {
	    	if (Counter == 0) {
	    		driveStraight();
	    	}    	
	
	    else if(Counter == 1) {
	    		leftTurn();
				}		  
	System.out.println("left: " + leftCount);
        System.out.println("right: " + rightCount);
        System.out.println("leftspeed: " + leftspeed);
        System.out.println("rightspeed: " + rightspeed);
        else {
        if(Counter == 0) {
			driveStraight();        
        }
        }
	}
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
	//compressor = new Compressor(0);
		leftMotor1.set(ControlMode.PercentOutput, leftJoy.getY());
		leftMotor2.set(ControlMode.PercentOutput, leftJoy.getY());
		rightMotor1.set(ControlMode.PercentOutput, -1 * rightJoy.getY());
		rightMotor2.set(ControlMode.PercentOutput, -1 * rightJoy.getY());
		leftElevatorMotor.set(ControlMode.PercentOutput, operator.getY());
		int leftCount = leftEncoder.get();
		int rightCount = rightEncoder.get();
		System.out.println("right: " + rightCount);
		System.out.println("left: " + leftCount);
		//rightElevatorMotor.set(ControlMode.PercentOutput, -1*operator.getY());
		//compressor.setClosedLoopControl(true);
		//compressor.start();
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
		if(operator.getRawButton(3) == true) {
			scalar.set(ControlMode.PercentOutput, .5);
		}
		else {
			scalar.set(ControlMode.PercentOutput, 0);
		}
		if(operator.getRawButton(7) == true) {
			intakeBags.set(ControlMode.PercentOutput, -.7);
		}
		else {
			if(operator.getRawButton(6) == true) {
				intakeBags.set(ControlMode.PercentOutput, .7);
			}
			else {
				intakeBags.set(ControlMode.PercentOutput, 0);
			}
		}
		
		
		if (rightJoy.getRawButton(2)) {
			if (lowgear == true) {
				Solenoid2.set(true);
				Solenoid1.set(true);
				lowgear = false;
				Timer.delay(1);
			} else {
				Solenoid2.set(false);
				Solenoid1.set(false);
				lowgear = true;
				Timer.delay(1);
			}
		}
		if (rightJoy.getRawButton(1)) {
			if(clamped == false) {
				Solenoid0.set(true);
				clamped = true;
				Timer.delay(1);
			}
			else {
				Solenoid0.set(false);
				clamped = false;
				Timer.delay(1);
			}
		} 
		if (leftJoy.getRawButton(1)) {
			if(extended == false) {
				Solenoid3.set(true);
				extended = true;
				Timer.delay(1);
			}
			else {
				Solenoid3.set(false);
				extended = false;
				Timer.delay(1);
			}
		}
		
		
		//while(isOperatorControl() && isEnabled()) {
			
			
		//}
	}
	public void leftTurn() {
		long leftCount = leftEncoder.get();
		long rightCount = rightEncoder.get();
		if(rightCount <= 5500) {
			leftMotor1.set(ControlMode.PercentOutput, 0);
			leftMotor2.set(ControlMode.PercentOutput, 0);
			rightMotor1.set(ControlMode.PercentOutput, 0.35);
			rightMotor2.set(ControlMode.PercentOutput, 0.35);
		}
		else {
			leftMotor1.set(ControlMode.PercentOutput, -0.2);
			leftMotor2.set(ControlMode.PercentOutput, -0.2);
			rightMotor1.set(ControlMode.PercentOutput, 0.2);
			rightMotor2.set(ControlMode.PercentOutput, 0.2);
		Timer.delay(.75);
		leftMotor1.set(ControlMode.PercentOutput, 0);
		leftMotor2.set(ControlMode.PercentOutput, 0);
		rightMotor1.set(ControlMode.PercentOutput, 0);
		rightMotor2.set(ControlMode.PercentOutput, 0);
Solenoid3.set(true);
Timer.delay(1);
Solenoid0.set(true);
			Counter = 2;
		}

	}
	public void driveStraight() {

		double Acceleration = 0.01;
		long leftCount = leftEncoder.get();
		long rightCount = rightEncoder.get();
		long leftChange = leftCount - idealleft;
		long rightChange = rightCount - idealright;
		idealleft = leftEncoder.get();
		idealright = rightEncoder.get();
		leftElevatorMotor.set(ControlMode.PercentOutput, operator.getY());
			if(leftCount <= 17526 && rightCount < 17526) {	
				if (leftChange == 40) {
				}
				else if (leftChange >= 40) {
					leftspeed = leftspeed - Acceleration;
				}
				else if (leftChange <= 40) {
					leftspeed = leftspeed + Acceleration;	
				}
				if (rightChange == 40) {
				}
				else if (rightChange >= 40) {
					rightspeed = rightspeed - Acceleration;
				}
				else if (rightChange <= 40) {
					rightspeed = rightspeed + Acceleration;	
				}
				if (leftspeed >= 0.5) {
					leftspeed = 0.5;
				}
				if (rightspeed >= 0.5) {
					rightspeed = 0.5;
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

				Counter = 1;
				leftEncoder.reset();
				rightEncoder.reset();
			}
		
		  }
    
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
	/**
	 * This function is called periodically during test mode.
	 */
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
}
