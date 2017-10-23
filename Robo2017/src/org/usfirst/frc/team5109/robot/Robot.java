package org.usfirst.frc.team5109.robot;


import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;

import org.json.JSONObject;
import com.ctre.CANTalon;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;




import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogOutput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Compressor;


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
	 * Robot setup:
	 *
	 * Controls:
	 * 	Automatic gears approach: left trigger
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

public class Robot extends SampleRobot {
	
	//declaring variables
	
	/*Notes:
	 * 
	 * To declare a motor run by a talon if the talon is controlled by CAN you must:
	 * import com.ctre.CANTalon
	 * declare each motor by doing CANTalon motorName;
	 */
    
	//drive motors
    CANTalon leftDrive1;
    CANTalon leftDrive2;
    CANTalon rightDrive1;
    CANTalon rightDrive2;
    //these constants should be used every time drive motors called - they change motor direction if wiring backwards
    //use by doing constant*value within the argument (the ()) for the set method (.set) for each motor
    final int rightDriveConstant = -1;
    final int leftDriveConstant = 1;
    
    //intake motor
    CANTalon intake;
    final int intakeConstant = -1;
    final double intakeSpeed = .84;
    
    //shooter motors
    CANTalon shooter1;
    CANTalon shooter2;
    final int shooter1Constant = 1;
    final int shooter2Constant = -1;
    double shooterSpeed = 1;
    
    //elevator motor
    CANTalon elevator;
    final int elevatorConstant = -1;
    //was .41
    final double elevatorSpeed = 1;
    
    //turret
    CANTalon turret;
    
    //scaling motors
    CANTalon scale1;
    CANTalon scale2;
    final int scale1Constant = 1;
    final int scale2Constant = -1;
    final double scalingSpeed = .3;
    
    //declaring joysticks
    Joystick leftStick;
    Joystick rightStick;
    Joystick operatorStick;
    
    //declaring encoders
    Encoder rightDrivetrain;
    Encoder leftDrivetrain;
    int leftEncoderConstant = 1;
    int rightEncoderConstant = -1;
    Encoder elevatorEncoder;
    Encoder shooter; 
    
    //Declaring Solenoids
    Solenoid leftShift;
    Solenoid rightShift;
    Solenoid flap;
    Solenoid gear;
    
    Compressor compressor;
    
    AnalogInput ultrasonic;
    
    /*
    AnalogOutput cameraAim;
    Victor cameraAim2;
    */
    
    //tracks what gear we are in - for automated shifting
    boolean lowGear;
    
    final String server_IP = "10.51.9.30";
    final int gearPort = 5805;
    final int goalPort = 5806;
    final int laserPort = 5807;
    final int dataPort = 5808;
    double tmpSpeed;
    double speed;
    boolean red;
    int startingPosition;
    boolean killScaling = false;
  
    boolean testing = true;
    
    //Declare Camera
    UsbCamera usbCamera;
    
    CameraServer cameraServer;
    boolean recentlyShot = false;
    double recentShotTime;
    
    //Declare UDP connection
    DatagramSocket fromPi;
    
   public Robot() {//constructor

	   //established can motors
        rightDrive1 = new CANTalon(3);
        rightDrive2 = new CANTalon(4);
        elevator = new CANTalon(5);
        intake = new CANTalon(6);
        leftDrive1 = new CANTalon(7);
        leftDrive2 = new CANTalon(8);
        scale1 = new CANTalon(1);
        scale2 = new CANTalon(9);
        shooter1 = new CANTalon(10);
        shooter2 = new CANTalon(2);
        
        shooter1.enableBrakeMode(false);
        shooter2.enableBrakeMode(false);
        intake.enableBrakeMode(false);
       
        
        
        
        //values for can tbd
        turret = new CANTalon(11);
        
        //Solenoids
        gear = new Solenoid(0);
        gear.set(false);
        flap = new Solenoid(1);
        leftShift = new Solenoid(2);
        rightShift = new Solenoid(3);
        
        //Compressor
        compressor = new Compressor(0);
        
        //Encoders
        rightDrivetrain = new Encoder(0, 1);
        //256 pulses per revolution of encoder, .975214 inches per rotation
        rightDrivetrain.setDistancePerPulse(.00380943 * 50/29);
        leftDrivetrain = new Encoder(2, 3);
        leftDrivetrain.setDistancePerPulse(.00380943 * 50/29);
        //elevatorEncoder = new Encoder(4,5);
        shooter = new Encoder(4,5);
        
        
        //Joystick Stuff
        leftStick = new Joystick(0);
        rightStick = new Joystick(1);
        operatorStick = new Joystick(2);
        
        
        
        lowGear = true;
        ultrasonic = new AnalogInput(3);
        tmpSpeed = .4;
        speed = 0;
        
        //auto variables
        red = false;
        startingPosition = 2;
        
        //Camera
        //usbCamera = new UsbCamera("USB Camera 0", 0);
        //CameraServer.getInstance().startAutomaticCapture();
        recentlyShot = false;
		try{
			fromPi = new DatagramSocket(5109);
		}
		catch(SocketException e){
			DriverStation.reportError("Not connecting to Pi", false);
		}
        
    }
//driving
   public void turnDegrees(double degrees) {
   	//diameter of wheels 29.5 inches
   	//circumference of wheels is 92.676983
   	double circumference = 92.676983; 
   	double fraction = degrees/360;
   	double distance = fraction * circumference;
   	double rightTurnSpeed = .98;
   	double leftTurnSpeed = 1;
   	rightDrivetrain.reset();
       leftDrivetrain.reset();
       DriverStation.reportError("Left Drivetrain: " + leftDrivetrain.getDistance(), false);
		DriverStation.reportError("Right Drivetrain: " + rightDrivetrain.getDistance(), false);
       if (degrees > 0) {
       	while (rightDrivetrain.getDistance() * rightEncoderConstant < distance && leftDrivetrain.getDistance() * leftEncoderConstant > -distance  && !operatorStick.getRawButton(11)) {
       		DriverStation.reportError("Left Drivetrain: " + leftDrivetrain.getDistance(), false);
				DriverStation.reportError("Right Drivetrain: " + rightDrivetrain.getDistance(), false);
       		rightDrive1.set(-rightTurnSpeed * rightDriveConstant);
       		rightDrive2.set(-rightTurnSpeed * rightDriveConstant);
       		leftDrive1.set(leftTurnSpeed * leftDriveConstant);
       		leftDrive2.set(leftTurnSpeed * leftDriveConstant);
       		Timer.delay(.005);
       	}
       	while (rightDrivetrain.getDistance() * rightEncoderConstant < distance  && !operatorStick.getRawButton(11)) {
       		DriverStation.reportError("Left Drivetrain: " + leftDrivetrain.getDistance(), false);
				DriverStation.reportError("Right Drivetrain: " + rightDrivetrain.getDistance(), false);
       		rightDrive1.set(-rightTurnSpeed * rightDriveConstant);
       		rightDrive2.set(-rightTurnSpeed * rightDriveConstant);
       		Timer.delay(.005);
       	}
       	while (leftDrivetrain.getDistance() * leftEncoderConstant > -distance  && !operatorStick.getRawButton(11)) {
       		DriverStation.reportError("Left Drivetrain: " + leftDrivetrain.getDistance(), false);
				DriverStation.reportError("Right Drivetrain: " + rightDrivetrain.getDistance(), false);
       		leftDrive1.set(leftTurnSpeed * leftDriveConstant);
       		leftDrive2.set(leftTurnSpeed * leftDriveConstant);
       		Timer.delay(.005);
       	}
       }
       else 
       {
       	distance *= -1;
       	while (rightDrivetrain.getDistance() * rightEncoderConstant > -distance && leftDrivetrain.getDistance() * leftEncoderConstant < distance  && !operatorStick.getRawButton(11)) {
       		DriverStation.reportError("Left Drivetrain: " + leftDrivetrain.getDistance(), false);
				DriverStation.reportError("Right Drivetrain: " + rightDrivetrain.getDistance(), false);
       		rightDrive1.set(rightTurnSpeed * rightDriveConstant);
       		rightDrive2.set(rightTurnSpeed * rightDriveConstant);
       		leftDrive1.set(-leftTurnSpeed * leftDriveConstant);
       		leftDrive2.set(-leftTurnSpeed * leftDriveConstant);
       		Timer.delay(10);
       	}
       	while (rightDrivetrain.getDistance() * rightEncoderConstant > -distance  && !operatorStick.getRawButton(11)) {
       		DriverStation.reportError("Left Drivetrain: " + leftDrivetrain.getDistance(), false);
				DriverStation.reportError("Right Drivetrain: " + rightDrivetrain.getDistance(), false);
       		rightDrive1.set(rightTurnSpeed * rightDriveConstant);
       		rightDrive2.set(rightTurnSpeed * rightDriveConstant);
       		Timer.delay(.005);
       	}
       	while (leftDrivetrain.getDistance() * leftEncoderConstant < distance  && !operatorStick.getRawButton(11)) {
       		DriverStation.reportError("Left Drivetrain: " + leftDrivetrain.getDistance(), false);
				DriverStation.reportError("Right Drivetrain: " + rightDrivetrain.getDistance(), false);
       		leftDrive1.set(-leftTurnSpeed * leftDriveConstant);
       		leftDrive2.set(-leftTurnSpeed * leftDriveConstant);
       		Timer.delay(.005);
       	}
       }
       rightDrive1.set(0);
       rightDrive2.set(0);
		leftDrive1.set(0);
		leftDrive2.set(0);
   }
   
   //distance is in inches
   public void moveForward(double distance) {
		rightDrivetrain.reset();
       leftDrivetrain.reset();
       double rightMoveSpeed = -.98;
       double leftMoveSpeed = -1;
       while (rightDrivetrain.getDistance() * rightEncoderConstant < distance && leftDrivetrain.getDistance() * leftEncoderConstant < distance  && !operatorStick.getRawButton(11)) {
       	rightDrive1.set(rightMoveSpeed * rightDriveConstant);
           rightDrive2.set(rightMoveSpeed * rightDriveConstant);
           leftDrive1.set(leftMoveSpeed * leftDriveConstant);
           leftDrive2.set(leftMoveSpeed * leftDriveConstant);
           DriverStation.reportError("Right encoder" + rightDrivetrain.getDistance(), false);
			DriverStation.reportError("Left Encoder" + leftDrivetrain.getDistance(), false);
       	Timer.delay(.001);
       }
       while (rightDrivetrain.getDistance() * rightEncoderConstant < distance  && !operatorStick.getRawButton(11)) {
       	rightDrive1.set(rightMoveSpeed * rightDriveConstant);
           rightDrive2.set(rightMoveSpeed * rightDriveConstant);
       	Timer.delay(.005);
       }
       while (leftDrivetrain.getDistance() * leftEncoderConstant < distance  && !operatorStick.getRawButton(11)) {
           leftDrive1.set(leftMoveSpeed * leftDriveConstant);
           leftDrive2.set(leftMoveSpeed * leftDriveConstant);
       	Timer.delay(.005);
       }
       rightDrive1.set(0);
		rightDrive2.set(0);
		leftDrive1.set(0);
		leftDrive2.set(0);
   }
   
 //distance is in inches
   public void straightMoveForward(double distance) {
		rightDrivetrain.reset();
       leftDrivetrain.reset();
       double rightMoveSpeed = -.7125;
       double leftMoveSpeed = -.75;
       while (rightDrivetrain.getDistance() * rightEncoderConstant < distance && leftDrivetrain.getDistance() * leftEncoderConstant < distance  && !operatorStick.getRawButton(11) && isAutonomous()) {
       	rightDrive1.set(rightMoveSpeed * rightDriveConstant);
           rightDrive2.set(rightMoveSpeed * rightDriveConstant);
           leftDrive1.set(leftMoveSpeed * leftDriveConstant);
           leftDrive2.set(leftMoveSpeed * leftDriveConstant);
           DriverStation.reportError("Right encoder" + rightDrivetrain.getDistance(), false);
			DriverStation.reportError("Left Encoder" + leftDrivetrain.getDistance(), false);
       	Timer.delay(.001);
       }
       while (rightDrivetrain.getDistance() * rightEncoderConstant < distance  && !operatorStick.getRawButton(11) && isAutonomous()) {
       	rightDrive1.set(rightMoveSpeed * rightDriveConstant);
           rightDrive2.set(rightMoveSpeed * rightDriveConstant);
       	Timer.delay(.005);
       }
       while (leftDrivetrain.getDistance() * leftEncoderConstant < distance  && !operatorStick.getRawButton(11) && isAutonomous()) {
           leftDrive1.set(leftMoveSpeed * leftDriveConstant);
           leftDrive2.set(leftMoveSpeed * leftDriveConstant);
       	Timer.delay(.005);
       }
       rightDrive1.set(0);
		rightDrive2.set(0);
		leftDrive1.set(0);
		leftDrive2.set(0);
   }
   
   public void moveBackward(double distance) {
		rightDrivetrain.reset();
       leftDrivetrain.reset();
       distance *= -1;
       double rightMoveSpeed = .98;
       double leftMoveSpeed = 1;
       rightDrivetrain.reset();
       leftDrivetrain.reset();
       //since input is a positive distance, it is made negative then encoders count down to it
       while (rightDrivetrain.getDistance() * rightEncoderConstant > distance || leftDrivetrain.getDistance() * leftEncoderConstant > distance   && !operatorStick.getRawButton(11)) {
       	rightDrive1.set(rightMoveSpeed * rightDriveConstant);
           rightDrive2.set(rightMoveSpeed * rightDriveConstant);
           leftDrive1.set(leftMoveSpeed * leftDriveConstant);
           leftDrive2.set(leftMoveSpeed * leftDriveConstant);
       	Timer.delay(.005);
       }
       while (rightDrivetrain.getDistance() * rightEncoderConstant > distance  && !operatorStick.getRawButton(11)) {
       	rightDrive1.set(rightMoveSpeed * rightDriveConstant);
           rightDrive2.set(rightMoveSpeed * rightDriveConstant);
       	Timer.delay(.005);
       }
       while (leftDrivetrain.getDistance() * leftEncoderConstant > distance  && !operatorStick.getRawButton(11)) {
           leftDrive1.set(leftMoveSpeed * leftDriveConstant);
           leftDrive2.set(leftMoveSpeed * leftDriveConstant);
       	Timer.delay(.005);
       }
       rightDrive1.set(0);
		rightDrive2.set(0);
		leftDrive1.set(0);
		leftDrive2.set(0);
   }
   
   
   
   
   
//gear
   
   public void pushGear() {
   	rightDrive1.set(0);
   	rightDrive2.set(0);
   	leftDrive1.set(0);
   	leftDrive2.set(0);
   	gear.set(true);
		Timer.delay(.3);
		gear.set(false);
		Timer.delay(.1);
		gear.set(true);
		Timer.delay(.3);
		gear.set(false);	
   }
   
   
    public void autoGearNew() { // 2017GRITS auto gear with vision tracking
    	double angle=0;
    	boolean keepDriving=true;
    	String data = getVisionData();
    	DriverStation.reportError(data, false);
    	
		JSONObject j = new JSONObject(data);
		keepDriving = j.getBoolean("Keep Driving");
		while(keepDriving && !rightStick.getRawButton(12)){
			
			data = getVisionData();
			
			j = new JSONObject(data);
			angle = j.getDouble("Angle");
			keepDriving = j.getBoolean("Keep Driving");
   		
			turnDegrees(angle);//(xPosition / (cameraPxWidth/2)) * (cameraViewingAngle/2));
			Timer.delay(.5);
		}
		//pushGear();
		DriverStation.reportError("done turning", false);
    }
   
   public String getVisionData(){ // 2017GRITS new command, reads file with vision data
	   	DriverStation.reportError("Getting vision tracking data", false);
		String line = "";
		
		byte[] b = new byte[100];
		DatagramPacket p = new DatagramPacket(b, b.length);
		try{
			fromPi.receive(p);
		}
		catch(IOException ex){
			DriverStation.reportError("Not recieving vision tracking data", false);
	    }
		line = new String(b);
		return line;
	}
   
   public void autoDoGear() {
   	String[] tmp = getData();
		DriverStation.reportError("Connected to Processing Pi", false);
		System.err.println("Connected to Processing Pi");
		int direction = Integer.parseInt(tmp[0]);
		double changeSpeed = 0;
		if (direction == -1) {
			//if it is to the left we turn right (this value is added to the right motor)
			changeSpeed = .1;
		}
		else if (direction == 1) {
			changeSpeed = -.1;
		}
		else {
			if (direction == -2) {
				DriverStation.reportError("No gear visible", false);
			}
			changeSpeed = 0;
		}
		if (direction != -3) {
			rightDrive1.set(rightDriveConstant * (.4 + changeSpeed));
			rightDrive2.set(rightDriveConstant * (.4 + changeSpeed));
			leftDrive1.set(leftDriveConstant * (.4 - changeSpeed));
			leftDrive1.set(leftDriveConstant * (.4 - changeSpeed));
		}
		else {
			rightDrive1.set(0);
			rightDrive2.set(0);
			leftDrive1.set(0);
			leftDrive1.set(0);
			DriverStation.reportError("No vision data from pi", false);
		}
		
   }
   
   //works
   public void doGear() {
			String[] tmp = getData();
			DriverStation.reportError("Connected to Processing Pi", false);
			System.err.println("Connected to Processing Pi");
			int direction = Integer.parseInt(tmp[0]);
			double changeSpeed = 0;
			if (direction == -1) {
				//if it is to the left we turn right (this value is added to the right motor)
				changeSpeed = .1;
			}
			else if (direction == 1) {
				changeSpeed = -.1;
			}
			else {
				if (direction == -2) {
					DriverStation.reportError("No gear visible", false);
				}
				changeSpeed = 0;
			}
			if (direction != -3) {
				rightDrive1.set((rightStick.getY()/2 + .4 + changeSpeed));
				rightDrive2.set((rightStick.getY()/2 + .4 + changeSpeed));
				leftDrive1.set((-leftStick.getY()/2 - .4 + changeSpeed));
				leftDrive1.set((-leftStick.getY()/2 - .4 + changeSpeed));
			}
			else {
				leftDrive1.set(leftDriveConstant * leftStick.getY());
   			leftDrive2.set(leftDriveConstant * leftStick.getY());
   			rightDrive1.set(rightDriveConstant * rightStick.getY());
   			rightDrive2.set(rightDriveConstant * rightStick.getY());
   			DriverStation.reportError("No vision data, stop using gear button", false);
			}
   }
   
   
//autonomous 
   
  	//IMPORTANT COMMENT DO NOT DELETE

   //Drive left & right motors for 2 seconds then stop

    public void autonomous() {
  	DriverStation.reportError("Left Drive 1" + leftDrive1.getOutputCurrent(), false);
  	DriverStation.reportError("Left Drive 2" + leftDrive2.getOutputCurrent(), false);
  	DriverStation.reportError("Right Drive 1" + rightDrive1.getOutputCurrent(), false);
  	DriverStation.reportError("Right Drive 2" + rightDrive2.getOutputCurrent(), false);
  	DriverStation.reportError("Intake" + intake.getOutputCurrent(), false);
  	DriverStation.reportError("Elevator" + elevator.getOutputCurrent(), false);
  	DriverStation.reportError("Scale 1" + scale1.getOutputCurrent(), false);
  	DriverStation.reportError("Scale 2" + scale2.getOutputCurrent(), false);
  	DriverStation.reportError("Shooter 1" + shooter1.getOutputCurrent(), false);
  	DriverStation.reportError("Shooter 2" + shooter2.getOutputCurrent(), false);
  	DriverStation.reportError("Doing autonomous", false);
  	DriverStation.reportWarning("auto", false);

      int autoMode = 0; 
      double waitTime = .01;
      /*
       * 1 - Red Starting Position Left
       * 2 - Red Starting Position Middle
       * 3 - Red Starting Position Right
       * 4 - Blue Starting Position Left
       * 5 - Blue Starting Position Middle
       * 6 - Blue Starting Position Right
       */
     switch (autoMode) {
      case 1:
      	//moves forward to cross baseline/get close to peg
      	moveForward(76);
      	Timer.delay(waitTime);
      	//needs to turn to face peg
      	turnDegrees(-55);
      	Timer.delay(waitTime);
      	//auto gear goes into the peg then backs up
      	moveForward(2);
      	Timer.delay(waitTime);
      	pushGear();
      	moveBackward(25);
      	Timer.delay(waitTime);
      	//turn to face hopper
      	turnDegrees(-15);
      	Timer.delay(waitTime);
      	//move close to hopper
      	moveBackward(45);
      	Timer.delay(waitTime);
      	
      	break;
      case 2:
      	//68
      	straightMoveForward(75);
      	if (isAutonomous()) {
      		pushGear();
      		Timer.delay(.5);
      		moveBackward(20);
      	}
      	/*
      	turnDegrees(-60);
      	leftDrive1.set(1 * leftDriveConstant);
      	leftDrive2.set(1 * leftDriveConstant);
      	rightDrive1.set(.95 * rightDriveConstant);
      	rightDrive2.set(.95 * rightDriveConstant);
      	Timer.delay(2.5);
      	shooter1.set(shooter1Constant * shooterSpeed);
      	shooter2.set(shooter2Constant * shooterSpeed);
      	Timer.delay(.5);
      	leftDrive1.set(0);
      	leftDrive2.set(0);
      	rightDrive1.set(-.95 * rightDriveConstant);
      	rightDrive2.set(-.95 * rightDriveConstant);
      	Timer.delay(.2);
      	rightDrive1.set(0);
      	rightDrive2.set(0);
      	intake.set(intakeConstant * intakeSpeed); 
      	
      	while(isAutonomous()) {
      		shoot();
      	}
      	intake.set(0);
      	*/
     	break;
      case 3:
      	//moves forward to cross baseline/get close to peg
      	moveForward(77);
      	Timer.delay(waitTime);
      	//needs to turn to face peg
      	//55 in round 34 was too far
      	turnDegrees(30);
      	Timer.delay(waitTime);
      	//auto gear goes into the peg then backs up
      	moveForward(23);
      	Timer.delay(waitTime);
      	pushGear();
      	moveBackward(25);
      	Timer.delay(waitTime);
      	//turn to face hopper
      	turnDegrees(15);
      	Timer.delay(waitTime);
      	//move close to hopper
      	moveBackward(45);
      	Timer.delay(waitTime);
      	hitHopperThenShootRed();
      	break;
      case 4:
      	//moves forward to cross baseline/get close to peg
      	moveForward(76);
      	Timer.delay(waitTime);
      	//needs to turn to face peg
      	turnDegrees(-55);
      	Timer.delay(waitTime);
      	//auto gear goes into the peg then backs up
      	moveForward(23);
      	Timer.delay(waitTime);
      	//pushGear();
      	moveBackward(25);
      	Timer.delay(waitTime);
      	//turn to face hopper
      	turnDegrees(-15);
      	Timer.delay(waitTime);
      	//move close to hopper
      	moveBackward(45);
      	Timer.delay(waitTime);
      	hitHopperThenShootBlue();
      	break;
      case 5:
      	straightMoveForward(68);
      	moveBackward(1);
      	if (isAutonomous()) {
      		pushGear();
      		Timer.delay(.5);
      		moveBackward(20);
      	}
      	break;
      case 6:
      	//moves forward to cross baseline/get close to peg
      	moveForward(83);
      	Timer.delay(waitTime);
      	//needs to turn to face peg
      	turnDegrees(55);
      	Timer.delay(waitTime);
      	//auto gear goes into the peg then backs up
      	moveForward(23);
      	Timer.delay(waitTime);
      	pushGear();
      	moveBackward(25);
      	Timer.delay(waitTime);
      	//turn to face hopper
      	turnDegrees(15);
      	Timer.delay(waitTime);
      	//move close to hopper
      	moveBackward(45);
      	Timer.delay(waitTime);
      	
      
      	break;
      default:
      	straightMoveForward(75);
      	//pushGear();
      	//Timer.delay(.2);
      	//moveBackward(20);
      }
  }
  
  public void hitHopperThenShootRed() {
  	//hit hopper (adjust distance using the Timer.delay - make sure robot squares itself up against the hopper so push longer than needed)
  	rightDrive1.set(1 * rightDriveConstant);
		rightDrive2.set(1 *rightDriveConstant);
		leftDrive1.set(.9 * leftDriveConstant);
		leftDrive2.set(.9 * leftDriveConstant);
		intake.set(intakeConstant * intakeSpeed);
		Timer.delay(.05);
		//stops the motors to wait for balls to fall in, use Timer.delay to adjust the wait time
		rightDrive1.set(0);
		rightDrive2.set(0);
		leftDrive1.set(0);
		leftDrive2.set(0);
		Timer.delay(1.5);
		//backs away to make turning easier 
		moveForward(5);
		//turn to face center of boiler
		turnDegrees(-75);
		//move closer to boiler (leave a yard)
		moveBackward(40);
		//facde boiler
		turnDegrees(20);
		//hit boiler with this
		rightDrive1.set(1 * rightDriveConstant);
		rightDrive2.set(1 *rightDriveConstant);
		leftDrive1.set(1 * leftDriveConstant);
		leftDrive2.set(1 * leftDriveConstant);
		Timer.delay(.5);
		rightDrive1.set(-1 * rightDriveConstant);
		rightDrive2.set(-1 * rightDriveConstant);
		shooter1.set(shooter1Constant * shooterSpeed);
		shooter2.set(shooter2Constant * shooterSpeed);
		Timer.delay(.2);
		//stops drivetrain
		rightDrive1.set(0);
		rightDrive2.set(0);
		leftDrive1.set(0);
		leftDrive2.set(0);
		//starts shooter, elevator and intake to jostle balls
		elevator.set(elevatorConstant * elevatorSpeed);
		while(isAutonomous() && isEnabled()) {
			shake();
		}
		
  }
  
  public void hitHopperThenShootBlue() {
  	//hit hopper (adjust distance using the Timer.delay - make sure robot squares itself up against the hopper so push longer than needed)
  	rightDrive1.set(1 * rightDriveConstant);
		rightDrive2.set(1 *rightDriveConstant);
		leftDrive1.set(.9 * leftDriveConstant);
		leftDrive2.set(.9 * leftDriveConstant);
		intake.set(intakeConstant * intakeSpeed);
		Timer.delay(.5);
		//stops the motors to wait for balls to fall in, use Timer.delay to adjust the wait time
		rightDrive1.set(0);
		rightDrive2.set(0);
		leftDrive1.set(0);
		leftDrive2.set(0);
		Timer.delay(1);
		//backs away to make turning easier 
		moveForward(5);
		//turn to face center of boiler
		turnDegrees(85);
		//move closer to boiler (leave a yard)
		moveBackward(40);
		//facde boiler
		turnDegrees(-20);
		//hit boiler with this
		shooter1.set(shooter1Constant * shooterSpeed);
		shooter2.set(shooter2Constant * shooterSpeed);
		rightDrive1.set(1 * rightDriveConstant);
		rightDrive2.set(1 *rightDriveConstant);
		leftDrive1.set(1 * leftDriveConstant);
		leftDrive2.set(1 * leftDriveConstant);
		Timer.delay(.6);
		rightDrive1.set(-1 * rightDriveConstant);
		rightDrive2.set(-1 * rightDriveConstant);
		Timer.delay(.2);
		//stops drivetrain
		rightDrive1.set(0);
		rightDrive2.set(0);
		leftDrive1.set(0);
		leftDrive2.set(0);
		//starts shooter, elevator and intake to jostle balls
		elevator.set(elevatorConstant * elevatorSpeed);
		while(isAutonomous() && isEnabled()) {
			shoot();
			shake();
		}
		
		//this will last till end of auto - during teleop once enabled shooter should reset to 0 on its own
  }
   
  public void autoShootOnly() {
  	double elevatorSpeed = 1;
  	shooter1.set(shooterSpeed * shooter1Constant);
  	shooter2.set(shooterSpeed * shooter2Constant);
  	Timer.delay(1);
  	elevator.set(elevatorSpeed * elevatorConstant);
  }
   
   
   
  
  
//shooting
  
  public void shoot() {
  	DriverStation.reportError("Shooter Encoder: " + shooter.getRate(), false);
  	if (shooter.getRate() < 428) {
			shooter1.set(shooter1Constant * 1); 
			shooter2.set(shooter2Constant * 1);
		}
		else if (shooter.getRate() > 429) {
			shooter1.set(shooter1Constant * (shooterSpeed - .1)); 
			shooter2.set(shooter2Constant * (shooterSpeed - 1));
		}
		else {
			shooter1.set(shooter1Constant * shooterSpeed); 
			shooter2.set(shooter2Constant * shooterSpeed);
		}
  }
  //finished - need to test
	public void doHighShot() {
  	String[] tmp = getHighShot();
  	double distance = Double.parseDouble(tmp[0]);
  	int direction = 0;
  	if (distance == -3 || direction == -3 || direction == -2) {
  		//do nothing - error getting information
  	}
  	else {
  		//do shooting stuff
  		if (direction == 1) {
  			leftDrive1.set(leftDriveConstant * .3);
  			leftDrive2.set(leftDriveConstant * .3);
  		}
  		else if (direction == -1) {
  			rightDrive1.set(rightDriveConstant * .3);
  			rightDrive2.set(rightDriveConstant * .3);
  		}
  		else {
  			double speed = findSpeed(distance);
  			if (shooter1.get() < (speed - .09)) {
  				shooter1.set((shooter1.get() + .1) * shooter1Constant);
  				shooter2.set((shooter2.get() + .1) * shooter2Constant);
  			}
  			else if (shooter1.get() < speed) {
  				shooter1.set((shooter1.get() + .01) * shooter1Constant);
  				shooter2.set((shooter2.get() + .01) * shooter2Constant);
  			}
  			else {
  				elevator.set(1);
  			}
  		}	
  	}
  }
   
	
	
	
 //scaling
	
	//unfinished - need to test
  public void doScaling() {
  	//double check to ensure proper direction
  	scale1.set(1);
  	//scale2.set(1);
  }
  
  
  
  
  
  
//get data
  
  /*
   * Expects: String with distance, int
   * 
   * Ouputs:
   * String array with [double, int] - distance in meters, direction (left/right/center)
   * for int:
   * -3 - no connection
   * -2 - no goal seen
   * -1 - left 
   * 0 - straight
   * 1 - right
   */
  public String[] getHighShot() {
  	try {

			Socket socket = new Socket(server_IP, goalPort);
			InputStreamReader isr = new InputStreamReader(socket.getInputStream());

			BufferedReader br = new BufferedReader(isr);

			String str = br.readLine();
			DriverStation.reportError("This is damn near impressive. Successfully connected to socket and retrieved output.", true);

			// str should be in order: direction, sideways angle, boolean value
		
			String[] returnArray = {str};
			DriverStation.reportError("String: " + str, false);
			DriverStation.reportError("The array made from the string: " + returnArray.toString(), false);
			DriverStation.reportError("The first value: " + returnArray[0], false);
			socket.close();

			return returnArray;
		}
		catch (IOException e) {
			e.printStackTrace();
			DriverStation.reportError("Failed to connect to processing pi", false);
			System.err.println("Failed to connect to processing pi");
			//insert all default values into the string below
			//current defaults (in order): gears (default 0), 
			String[] failed = {"-3"};
			return failed;
		}

	}
  
  public String[] getLaser() {
  	try {

			Socket socket = new Socket(server_IP, laserPort);
			InputStreamReader isr = new InputStreamReader(socket.getInputStream());

			BufferedReader br = new BufferedReader(isr);

			String str = br.readLine();
			DriverStation.reportError("This is damn near impressive. Successfully connected to socket and retrieved output.", false);

			// str should be in order: direction, sideways angle, boolean value
		
			String[] returnArray = {str};
			
			DriverStation.reportError("The array made from the string: " + returnArray.toString(), false);
			DriverStation.reportError("The first value: " + returnArray[0], false);
			socket.close();

			return returnArray;
		}
		catch (IOException e) {
			e.printStackTrace();
			DriverStation.reportError("Failed to connect to processing pi", false);
			System.err.println("Failed to connect to processing pi");
			//insert all default values into the string below
			//current defaults (in order): gears (default 0), 
			String[] failed = {"-3"};
			return failed;
		}

	}
  
  /*
   * Gear, Laser, Shooter
   */
  public String[] getGear() {



		try {

			Socket socket = new Socket(server_IP, gearPort);
			InputStreamReader isr = new InputStreamReader(socket.getInputStream());

			BufferedReader br = new BufferedReader(isr);

			String str = br.readLine();
			DriverStation.reportError("This is damn near impressive. Successfully connected to socket and retrieved output.", false);

			// str should be in order: direction, sideways angle, boolean value
		
			String[] returnArray = {str};
			
			DriverStation.reportError("The array made from the string: " + returnArray.toString(), false);
			DriverStation.reportError("The first value: " + returnArray[0], false);
			socket.close();

			return returnArray;
		}
		catch (IOException e) {
			e.printStackTrace();
			DriverStation.reportError("Failed to connect to processing pi", false);
			System.err.println("Failed to connect to processing pi");
			//insert all default values into the string below
			//current defaults (in order): gears (default 0), 
			String[] failed = {"-3"};
			return failed;
		}

	}
  
  public String[] getData() {



		try {

			Socket socket = new Socket(server_IP, dataPort);
			InputStreamReader isr = new InputStreamReader(socket.getInputStream());

			BufferedReader br = new BufferedReader(isr);

			String str = br.readLine();
			DriverStation.reportError("This is damn near impressive. Successfully connected to socket and retrieved output.", false);

			// str should be in order: direction, sideways angle, boolean value
			String[] returnArray = {"-3", "-3", "-3"};
			DriverStation.reportError("Data :" + str, false);
			returnArray = str.split(",");
			if (str.split(",") != null) {
				 returnArray = str.split(",");
			}
			DriverStation.reportError("The first value: " + returnArray[0], false);
			DriverStation.reportError("The second value: " + returnArray[1], false);
			DriverStation.reportError("The third value: " + returnArray[2], false);
			socket.close();

			return returnArray;
		}
		catch (IOException e) {
			e.printStackTrace();
			DriverStation.reportError("Failed to connect to processing pi", false);
			System.err.println("Failed to connect to processing pi");
			//insert all default values into the string below
			//current defaults (in order): gears (default 0), 
			String[] failed = {"-3", "-3", "-3"};
			return failed;
		}

	}
  
  
  
//operator control
  
  
  

  
  /*
   * 
   * Methods that have been completed:
   * doGear
   * doHighShot
   * 
   * Methods that have not been completed:
   * Movement tracking
   * doHopper
   * doLoadingStation
   */
  public void operatorControl() {
  	boolean intakeBool = false;
  	boolean shooterBool = false;
  	while (isOperatorControl() && isEnabled()) {
  		
  		/*DriverStation.reportError("Left Drive 1 " + leftDrive1.getOutputCurrent(), false);
      	DriverStation.reportError("Left Drive 2 " + leftDrive2.getOutputCurrent(), false);
      	DriverStation.reportError("Right Drive 1 " + rightDrive1.getOutputCurrent(), false);
      	DriverStation.reportError("Right Drive 2 " + rightDrive2.getOutputCurrent(), false);
      	DriverStation.reportError("Intake " + intake.getOutputCurrent(), false);
      	DriverStation.reportError("Elevator " + elevator.getOutputCurrent(), false);
      	DriverStation.reportError("Scale 1 " + scale1.getOutputCurrent(), false);
      	DriverStation.reportError("Scale 2 " + scale2.getOutputCurrent(), false);
      	DriverStation.reportError("Shooter 1 " + shooter1.getOutputCurrent(), false);
      	DriverStation.reportError("Shooter 2 " + shooter2.getOutputCurrent(), false);*/
  			//Shot speed controlled by Operator Throttle
  			//Intake toggled by Operator Button 5
  		  		
  		
  			if (operatorStick.getRawButton(5)) {
	    		if (intakeBool) {
	    			intakeBool = false;
	    		}
	    		else {
	    			intakeBool = true;
	    		}
	    		Timer.delay(.2);
	    	}
	    	if (intakeBool ) {
	    		intake.set(intakeSpeed * intakeConstant);
	    	}
	    	else {
	    		intake.set(0);
	    	}
	    		
	    	//Shooting controlled by Operator Trigger
	    	if (operatorStick.getTrigger()) {
	    		elevator.set(elevatorConstant * elevatorSpeed);
	    	}
	    	else {
	    		if (operatorStick.getRawButton(2)) {
		    		elevator.set(elevatorConstant * elevatorSpeed * -1);
		    	}
	    		else {
	    			elevator.set(0);
	    		}
	    	}
	    		
	    	if (operatorStick.getRawButton(12)) {
	    		if (shooterBool) {
	    			shooterBool = false;
	    		}
	    		else {
	    			shooterBool = true;
	    		}
	    		Timer.delay(.2);
	    	}
	    		
	    	if (shooterBool) {
	    		shoot();
	    	}
	    	else {
	    		shooter1.set(0);
	    		shooter2.set(0);
	    	}
	    	
	    	//Scaling controlled by Operator Button 3
	    	if (operatorStick.getRawButton(3)) {
					
				scale1.set(.7 * scale1Constant);
				scale2.set(.7 * scale2Constant);

		        DriverStation.reportError("Scale 1 " + scale1.getOutputCurrent(), false);
		        DriverStation.reportError("Scale 2 " + scale2.getOutputCurrent(), false);
			}
			else {
				scale1.set(0);
				scale2.set(0);
			}
	    		
	    	
  			//Flap controlled by Operator Button 4
  			if (operatorStick.getRawButton(4)) {
	    		flap.set(false);
	    	}
	    	else {
	    		flap.set(true);
	    	}
  			
  			
  			//Driver controls
  			rightDrive1.set(rightDriveConstant * rightStick.getY());
  			rightDrive2.set(rightDriveConstant * rightStick.getY());
  			leftDrive1.set(leftDriveConstant * leftStick.getY());
  			leftDrive2.set(leftDriveConstant * leftStick.getY());
  			
  			
  			if (rightStick.getTrigger()) {
  				pushGear();
  				//autoGearNew();
  			}
  			if (rightStick.getRawButton(2)) {
	    		if (lowGear) {
	    			leftShift.set(true);
	    			rightShift.set(true);
	    			lowGear = false;
	    		}
	    		else {
	    			leftShift.set(false);
	    			rightShift.set(false);
	    			lowGear = true;
	    		}
	    		Timer.delay(.2);
	    	}
  			if (rightStick.getRawButton(11)) {
  				if (killScaling) {
  					killScaling = false;
  				}
  				else {
  					killScaling = true;
  				}
  			}
  		
  		Timer.delay(.005);
  		}
  	}
  
   
   
//misc
   
    public void shake()
    {    
    	rightDrive1.set(.2 * rightDriveConstant);
		rightDrive2.set(.2 *rightDriveConstant);
		leftDrive1.set(.2 * leftDriveConstant);
		leftDrive2.set(.2 * leftDriveConstant);
		Timer.delay(.1);
		rightDrive1.set(-.2 * rightDriveConstant);
		rightDrive2.set(-.2 *rightDriveConstant);
		leftDrive1.set(-.2 * leftDriveConstant);
		leftDrive2.set(-.2 * leftDriveConstant);
		Timer.delay(.1);
    }
    
    
 //unused methods
    public void autoGear() {
    	/*
    	while(interpretUltrasonicSensor() > .25 && Timer.getMatchTime() < 15.0) {
    		autoDoGear();
    	}
    	rightDrive1.set(0);
		rightDrive2.set(0);
		leftDrive1.set(0);
		leftDrive1.set(0);
    	if (Timer.getMatchTime() >= 15.0) {
    		return;
    	}
    	else {
    		pushGear();
    	}
    	moveBackward(30);*/
    	
    }
    
	public void oldAuto() {
		 
        		rightDrive1.set(-.75 * rightDriveConstant);
        		rightDrive2.set(-.75 *rightDriveConstant);
        		leftDrive1.set(-.77 * leftDriveConstant);
        		leftDrive2.set(-.77 * leftDriveConstant);
        		Timer.delay(1.9);
        		//if we are blue, left needs to stop, if we are red, right needs to stop
        		//rightDrive1.set(0);
        		//rightDrive2.set(0);
        		leftDrive1.set(0);
        		leftDrive2.set(0);
        		Timer.delay(.5);
        		leftDrive1.set(-.77 * leftDriveConstant);
        		leftDrive2.set(-.77 * leftDriveConstant);
        		Timer.delay(1);
        		rightDrive1.set(0);
        		rightDrive2.set(0);
        		leftDrive1.set(0);
        		leftDrive2.set(0);
        		/* Baseline then shooter
        		rightDrive1.set(-.75 * rightDriveConstant);
        		rightDrive2.set(-.75 *rightDriveConstant);
        		leftDrive1.set(-.77 * leftDriveConstant);
        		leftDrive2.set(-.77 * leftDriveConstant);
        		//2.1 for middle
        		Timer.delay(2.3);
        		rightDrive1.set(0);
        		rightDrive2.set(0);
        		leftDrive1.set(0);
        		leftDrive2.set(0);
        		Timer.delay(.2);
        		rightDrive1.set(.75 * rightDriveConstant);
        		rightDrive2.set(.75 *rightDriveConstant);
        		leftDrive1.set(.77 * leftDriveConstant);
        		leftDrive2.set(.77 * leftDriveConstant);
        		Timer.delay(1.3);
        		rightDrive1.set(.77 * rightDriveConstant);
        		rightDrive2.set(.77 * rightDriveConstant);
        		leftDrive1.set(-.77 * leftDriveConstant*0);
        		leftDrive2.set(-.77 * leftDriveConstant*0);
        		Timer.delay(.9);
        		rightDrive1.set(0);
        		rightDrive2.set(0);
        		leftDrive1.set(0);
        		leftDrive2.set(0);
        		Timer.delay(.2);
        		rightDrive1.set(.75 * rightDriveConstant);
        		rightDrive2.set(.75 *rightDriveConstant);
        		leftDrive1.set(.77 * leftDriveConstant);
        		leftDrive2.set(.77 * leftDriveConstant);
        		Timer.delay(1.5);
        		rightDrive1.set(0);
        		rightDrive2.set(0);
        		leftDrive1.set(0);
        		leftDrive2.set(0);
        		shooter1.set(shooter1Constant * shooterSpeed);
        		shooter2.set(shooter2Constant * shooterSpeed);
        		elevator.set(elevatorConstant * elevatorSpeed);
        		intake.set(intakeSpeed * intakeConstant);
        		Timer.delay(6);*/
        		
        		/*Center Gear then shooter
        		rightDrive1.set(-.75 * rightDriveConstant);
        		rightDrive2.set(-.75 *rightDriveConstant);
        		leftDrive1.set(-.77 * leftDriveConstant);
        		leftDrive2.set(-.77 * leftDriveConstant);
        		//2.1 for middle
        		Timer.delay(1.95);
        		rightDrive1.set(0);
        		rightDrive2.set(0);
        		leftDrive1.set(0);
        		leftDrive2.set(0);
        		pushGear();
        		Timer.delay(.3);
        		rightDrive1.set(.75 * rightDriveConstant);
        		rightDrive2.set(.75 *rightDriveConstant);
        		leftDrive1.set(.77 * leftDriveConstant);
        		leftDrive2.set(.77 * leftDriveConstant);
        		Timer.delay(1.8);
        		//red
        		//leftDrive1.set(-.77 * leftDriveConstant);
        		//leftDrive2.set(-.77 * leftDriveConstant);
        		//blue
        		rightDrive1.set(-.77 * rightDriveConstant);
        		rightDrive2.set(-.77 * rightDriveConstant);
        		Timer.delay(.35);
        		rightDrive1.set(-.75 * rightDriveConstant);
        		rightDrive2.set(-.75 *rightDriveConstant);
        		leftDrive1.set(-.77 * leftDriveConstant);
        		leftDrive2.set(-.77 * leftDriveConstant);
        		Timer.delay(3.0);
        		rightDrive1.set(0);
        		rightDrive2.set(0);
        		leftDrive1.set(0);
        		leftDrive2.set(0);
        		shooter1.set(shooter1Constant * shooterSpeed);
        		shooter2.set(shooter2Constant * shooterSpeed);
        		elevator.set(elevatorConstant * elevatorSpeed);
        		intake.set(intakeSpeed * intakeConstant);
        		Timer.delay(6);
        		*/
	}

	/*
	  Old Teleop Controls (for just 1 driver)
			leftDrive1.set(leftDriveConstant * leftStick.getY());
		leftDrive2.set(leftDriveConstant * leftStick.getY());
		rightDrive1.set(rightDriveConstant * rightStick.getY());
		rightDrive2.set(rightDriveConstant * rightStick.getY());
		
		if (rightStick.getRawButton(2)) {
			if (lowGear) {
				leftShift.set(true);
				rightShift.set(true);
				lowGear = false;
			}
			else {
				leftShift.set(false);
				rightShift.set(false);
				lowGear = true;
			}
		}
		
		if (rightStick.getRawButton(5)) {
			shooterSpeed += .01;
			Timer.delay(.1);
		}
		if (rightStick.getRawButton(6)) {
			shooterSpeed -= .01;
			Timer.delay(.1);
		}
		
		//right trigger shoots the ball
		if (rightStick.getTrigger()) {
			shooter1.set(shooter1Constant * shooterSpeed);
			shooter2.set(shooter2Constant * shooterSpeed);
			elevator.set(elevatorConstant * -1 * elevatorSpeed);
			intake.set(intakeConstant * intakeSpeed);
		}
		else {
			shooter1.set(0);
			shooter2.set(0);
			elevator.set(0);
		}
		
		
		//left trigger punches out the gear
		if (leftStick.getTrigger()) {
			pushGear();
		}
		
		//left stick button 3 does gear flap
		if (leftStick.getRawButton(3)) {
			flap.set(false);
		}
		else {
			flap.set(true);
		}
		
		//right stick button 3 toggles intake
		if (rightStick.getRawButton(3)) {
			if (intakeBool) {
				intakeBool = false;
			}
			else {
				intakeBool = true;
			}
			Timer.delay(.2);
		}
		
		//intake
		if (intakeBool ) {+
			if (!rightStick.getTrigger()) {
				intake.set(intakeSpeed * intakeConstant);
			}
		}
		else {
			if (!rightStick.getTrigger()) {
				intake.set(0);
			}
		}
		
		//right stick button 4 starts scaling
		if (rightStick.getRawButton(4)) {
			
			//scale1.set(.5 * scale1Constant);
			scale2.set(.5 *scale2Constant);
		}
		else {
			scale1.set(0);
			scale2.set(0);
	}
	*/
    
    //need location for following two
    public void doHopper() {
    //	String[] location = getLocation();
    }
    
    public void doLoadingStation() {
    	//[] location = getLocation();
    }
    
    public void setLocationWithGear() {
    	
    }
    
    public double findSpeed(double distance) {
    	return 0;
    }
    
    public double interpretUltrasonicSensor() {
    	/*
For users of this output that desire to work in voltage, a 5V power supply yields~4.88mV per 5 mm. Output voltage range
when powered with 5V is 293mV for 300-mm, and 4.885V for 5000-mm.
*/
    	 
    	double voltage = ultrasonic.getVoltage();
    	//conversion is 4.88V/M
    	double distance = (voltage / 4.88) * 5;
    	return distance;

    }

    public void test() {
    	boolean temp = true;
    	while(temp){
    	Timer.delay(1.1);
    	turnDegrees(1);
    	
    	}
    }
    	/*int scaleTest = 0;
    	boolean startup = false;
    	while (isTest() && isEnabled()) {
    		if (leftStick.getRawButton(11)) {
    			doGear();
    		}
	    	if (leftStick.getRawButton(2)) {
	    		String[] output = getData();
	    		
				DriverStation.reportError("Ultrasonic Sensor: " + interpretUltrasonicSensor(), false);
				
				DriverStation.reportError("Gear Vision Output: " + output[0], false);
				
				DriverStation.reportError("Shooter Vision " + output[2], false);
				
				DriverStation.reportError("Laser Distance: " + output[1], false);
				DriverStation.reportError("Scaler 1 Current: " + scale1.getOutputCurrent(), false);
				DriverStation.reportError("Scaler 2 Current: " + scale2.getOutputCurrent(), false);
			}
			if (rightStick.getRawButton(4)) {
				DriverStation.reportError("Right encoder" + rightDrivetrain.getDistance(), false);
				DriverStation.reportError("Left Encoder" + leftDrivetrain.getDistance(), false);
			}
			if (leftStick.getRawButton(7)) {
				moveBackward(20);
			}
			if (leftStick.getRawButton(8)) {
				scale2.set(scale2Constant * .7);
				scale1.set(scale1Constant * .7);
			}
			else {
				scale2.set(0);
				scale1.set(0);
			}
			if (leftStick.getRawButton(9)) {
				DriverStation.reportError("Left Drivetrain: " + leftDrivetrain.getDistance(), false);
				DriverStation.reportError("Right Drivetrain: " + rightDrivetrain.getDistance(), false);
				turnDegrees(360);
				DriverStation.reportError("Left Drivetrain: " + leftDrivetrain.getDistance(), false);
				DriverStation.reportError("Right Drivetrain: " + rightDrivetrain.getDistance(), false);
			}
			if (leftStick.getRawButton(10)) {
				DriverStation.reportError("Left Drivetrain: " + leftDrivetrain.getDistance(), false);
				DriverStation.reportError("Right Drivetrain: " + rightDrivetrain.getDistance(), false);
				moveForward(30);
				DriverStation.reportError("Left Drivetrain: " + leftDrivetrain.getDistance(), false);
				DriverStation.reportError("Right Drivetrain: " + rightDrivetrain.getDistance(), false);
			}/*
			/*
			if (leftStick.getTrigger()) {
				shooter1.set(shooter1Constant * (shooterSpeed + .075));
				shooter2.set(shooter2Constant * (shooterSpeed + .075));
				DriverStation.reportError("Shooter 1 : " + shooter1.getOutputCurrent(), false);
				DriverStation.reportError("Shooter 2 : " + shooter2.getOutputCurrent(), false);
				DriverStation.reportError("Shooter 1 Value: " + shooter1.get(), false);
				DriverStation.reportError("Shooter 2 Value: " + shooter2.get(), false);
				DriverStation.reportError("Shooter Speed: " + shooter.getRate(), false);

				if (!startup) {
					shooter1.set(shooter1Constant * shooterSpeed);
					shooter2.set(shooter2Constant * shooterSpeed);
					Timer.delay(2);
					startup = true;
				}
				elevator.set(elevatorConstant * elevatorSpeed);
			}
			else {
				shooter1.set(0);
				shooter2.set(0);
				elevator.set(0);
			}*/
			
			/*if (rightStick.getRawButton(3)){
				
				elevator.set(elevatorConstant * elevatorSpeed); //might be wrong look  here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! (try : elevatorSpeed * elevatorConstant)
				Timer.delay(.2);
			}
			if (rightStick.getRawButton(5)) {
				if (scaleTest < 3) {
					scaleTest++;
				}
				else {
					scaleTest = 0;
				}
				Timer.delay(.2);
			}
			if (rightStick.getRawButton(12) && !leftStick.getTrigger()) {
				shoot();
			}
			else {
				shooter1.set(0);
				shooter2.set(0);
				elevator.set(0);
			}*/
			
			/*if (scaleTest == 1) {
				scale1.set(.3 * scale1Constant);
				scale2.set(.3 * scale2Constant);
			}
			else if (scaleTest == 2) {
				scale1.set(0);
				scale2.set(0);
			}
			else if (scaleTest == 3) {
				scale1.set(.7 * scale1Constant);
				scale2.set(.7 * scale2Constant);
			}
			else {
				scale1.set(0);
				scale2.set(0);
			}*/
			
			/*if (rightStick.getTrigger()) {
				pushGear();
			}
			if (rightStick.getRawButton(6)) {
				
				shooterSpeed += .01;
				DriverStation.reportError("Shooter Speed:" + shooterSpeed, false);
				Timer.delay(.2);
			}
			if (rightStick.getRawButton(5)) {
				
				shooterSpeed -= .01;
				DriverStation.reportError("Shooter Speed:" + shooterSpeed, false);
				Timer.delay(.2);
			}
			if (leftStick.getRawButton(3)) {
				flap.set(false);
			}
			else {
				flap.set(true);
			}
			if (rightStick.getRawButton(2)) {
				if (lowGear) {
					leftShift.set(true);
					rightShift.set(true);
					lowGear = false;
				}
				else {
					leftShift.set(false);
					rightShift.set(false);
					lowGear = true;
				}
				Timer.delay(.2);
			}
			if (leftStick.getRawButton(5)) {
				intake.set(intakeConstant * intakeSpeed);
			}
			else {
				intake.set(0);
			}
			
			rightDrive1.set(rightDriveConstant * rightStick.getY());
			rightDrive2.set(rightDriveConstant * rightStick.getY());
			leftDrive1.set(leftDriveConstant * leftStick.getY());
			leftDrive2.set(leftDriveConstant * leftStick.getY());
			Timer.delay(.005);
			
    	}
    }*/
}





