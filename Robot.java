package org.usfirst.frc.team5109.robot;


import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.DriverStation;

import com.ctre.CANTalon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

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
    final int rightConstant = -1;
    final int leftConstant = 1;
    
    //intake motor
    CANTalon intake;
    final int intakeConstant = -1;
    final double intakeSpeed = .84;
    
    //shooter motors
    CANTalon shooter1;
    CANTalon shooter2;
    final int shooter1Constant = 1;
    final int shooter2Constant = 1;
    
    //elevator motor
    CANTalon elevator;
    final int elevatorConstant = 1;
    //was .41
    final double elevatorSpeed = .41;
    
    //turret
    CANTalon turret;
    
    //scaling motors
    CANTalon scale1;
    CANTalon scale2;
    final int scale1Constant = 1;
    final int scale2Constant = -1;
    
    //declaring joysticks
    Joystick leftStick;
    Joystick rightStick;
    
    //declaring encoders
    Encoder rightDrivetrain;
    Encoder leftDrivetrain;
    Encoder elevatorEncoder;
    Encoder shooter; //maybe
    
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
    final int goalPort = 5810;
    final int locationPort = 5815;
    final int aimPort = 5820;
    double tmpSpeed;
    double speed;
    
  
    
    
    
  

   public Robot() {

	   //established can motors
        leftDrive1 = new CANTalon(3);
        leftDrive2 = new CANTalon(4);
        elevator = new CANTalon(5);
        intake = new CANTalon(6);
        rightDrive1 = new CANTalon(7);
        rightDrive2 = new CANTalon(8);
        scale1 = new CANTalon(1);
        scale2 = new CANTalon(9);
        shooter1 = new CANTalon(10);
        shooter2 = new CANTalon(2);
        
        
        
        //values for can tbd
        turret = new CANTalon(11);
        
        
        gear = new Solenoid(0);
        flap = new Solenoid(1);
        leftShift = new Solenoid(2);
        rightShift = new Solenoid(3);
        
        compressor = new Compressor(0);
        
        leftStick = new Joystick(0);
        rightStick = new Joystick(1);
        rightDrivetrain = new Encoder(4, 5);
        lowGear = true;
        ultrasonic = new AnalogInput(3);
        tmpSpeed = .4;
        speed = 0;
        
    }


     //Drive left & right motors for 2 seconds then stop
 
    public void autonomous() {
        //starts positioning system
        (new Movement()).start();
        
        //print statement to check threading
        for (int i = 0; i < 100; i++) {
            System.out.println("*********************");
        }
        /*
        //auto testing of encoder
        rightDrivetrain.reset();
        while (rightDrivetrain.get() < 1000) {
            rightDrive1.set(-.5);
            rightDrive2.set(-.5);
        }
        rightDrive1.set(0);
        rightDrive2.set(0);
        leftDrive1.set(.5);
        leftDrive2.set(.5);
        */
        
    }
    
    
    
    

    
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
    	double shooterSpeed = .75;
    	boolean intakeBool = false;
    	while (isOperatorControl() && isEnabled()) {
    		
    		leftDrive1.set(leftConstant * leftStick.getY());
    		leftDrive2.set(leftConstant * leftStick.getY());
    		rightDrive1.set(rightConstant * rightStick.getY());
    		rightDrive2.set(rightConstant * rightStick.getY());
    		//right trigger shoots the ball
    		if (rightStick.getRawButton(5)) {
    			shooterSpeed += .01;
    			Timer.delay(.1);
    		}
    		if (rightStick.getRawButton(6)) {
    			shooterSpeed -= .01;
    			Timer.delay(.1);
    		}
    		DriverStation.reportError("Shooter speed: " + shooterSpeed, false);
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
    			gear.set(true);
    			Timer.delay(.15);
    			gear.set(false);
    		}
    		//left stick button 3 does gear flap
    		if (leftStick.getRawButton(3)) {
    			flap.set(false);
    		}
    		else {
    			flap.set(true);
    		}
    		//right stick button three toggles intake
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
    		if (intakeBool) {
    			intake.set(intakeSpeed * intakeConstant);
    		}
    		else {
    			intake.set(0);
    		}
    		//right stick button four starts scaling
    		if (rightStick.getRawButton(4)) {
    			
    			//scale1.set(.5 * scale1Constant);
    			scale2.set(.5 *scale2Constant);
    		}
    		else {
    			scale1.set(0);
    			scale2.set(0);
    		}
    		
    		
        	
    		//all methods that mess with the drivetrain need to be in this chain of if statements
    		/*if (leftStick.getTrigger()) {
    			doGear();
    		}
    		else if (leftStick.getRawButton(8)) {
    			doHopper();
    		}
    		else if (leftStick.getRawButton(10)) {
    			doLoadingStation();
    		}
    		else if (rightStick.getTrigger()) {
    			//doHighShot();
    			shooter1.set(shooter1Constant * 1);
    			shooter2.set(shooter2Constant * 1);
    		}
    		else {
    			shooter1.set(0);
        		shooter2.set(0);
        		
        		leftDrive1.set(leftConstant * leftStick.getY());
        		leftDrive2.set(leftConstant * leftStick.getY());
        		rightDrive1.set(rightConstant * rightStick.getY());
        		rightDrive2.set(rightConstant * rightStick.getY());
    		}
    		//leftDrive1.set(leftConstant * leftStick.getY());
    		//leftDrive2.set(leftConstant * leftStick.getY());
    		//rightDrive1.set(rightConstant * rightStick.getY());
    		//rightDrive2.set(rightConstant * rightStick.getY());
    		
    		
    		
    		
    		//all methods that do not mess with the drivetrain can go below
    		if (rightStick.getRawButton(4)) {
    			doScaling();
    		}
    		if (rightStick.getRawButton(12)) {
    			elevator.set(elevatorConstant * elevatorSpeed);
    		}
    		else {
    			elevator.set(0);
    		}
    		if (leftStick.getRawButton(12)) {
    			intake.set(intakeConstant * intakeSpeed);
    		}
    		else {
    			intake.set(0);
    		}
    		compressor.start();
    		if (rightStick.getRawButton(3)) {
    			gear.set(true);
    		}
    		if (rightStick.getRawButton(5)) {
    			gear.set(false);
    		}
    		if (rightStick.getRawButton(6)) {
    			flap.set(true);
    		}*/
    		
    		
    		/*
    		 * shooter ~ .6 to shoot from bottom of goal
    		 
    		if (leftStick.getRawButton(3)){
    			leftDrive1.set(-tmpSpeed);
    		}
    		else {
    			leftDrive1.set(0);
    		]
    		if (leftStick.getRawButton(8)) {
    			tmpSpeed += .01;
    			DriverStation.reportError("Speed: " + tmpSpeed, false);
    			Timer.delay(.2);
    		}
    		if (leftStick.getRawButton(12)) {
    			tmpSpeed -= .01;
    			DriverStation.reportError("Speed: " + tmpSpeed, false);
    			Timer.delay(.2);
    		}
    		
    		*/
    		
    		// rightDrive1.set(interpretUltrasonicSensor());
    		//rightDrive2.set(interpretUltrasonicSensor());
    		//System.err.println(interpretUltrasonicSensor());

    		//rightDrive1.set(ultrasonic.getVoltage());
    		//rightDrive2.set(ultrasonic.getVoltage());
    		//System.err.println(ultrasonic.getVoltage());
    		
    		Timer.delay(.005);
    	}
    }
    

    
    /*
     * Expects two doubles with x, y offset in meters
     * 
     * Ouputs string array of length one:
     * -3 - no connection
     * -2 - no gear seen
     * -1 - left 
     * 0 - straight
     * 1 - right
     */
    public String[] getLocation() {

		try {

			Socket socket = new Socket(server_IP, locationPort);
			InputStreamReader isr = new InputStreamReader(socket.getInputStream());

			BufferedReader br = new BufferedReader(isr);

			String str = br.readLine();
			DriverStation.reportError("This is damn near impressive. Successfully connected to socket and retrieved output.", true);

			// str should be in order: direction, sideways angle, boolean value
		
			String[] returnArray = new String[2];
			returnArray = str.split(",");
			
			DriverStation.reportError("The array made from the string: " + returnArray.toString(), true);
			DriverStation.reportError("The first value: " + returnArray[0], false);
			DriverStation.reportError("The second value: " + returnArray[1], false);

			socket.close();

			return returnArray;
		}
		catch (IOException e) {
			e.printStackTrace();
			DriverStation.reportError("Failed to connect to processing pi", false);
			System.err.println("Failed to connect to processing pi");
			//insert all default values into the string below
			//current defaults (in order): gears (default 0), 
			String[] failed = {"-1000", "-1000"};
			return failed;
		}

	}
    
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
		
			String[] returnArray = new String[2];
			returnArray = str.split(",");
			
			DriverStation.reportError("The array made from the string: " + returnArray.toString(), true);
			DriverStation.reportError("The first value: " + returnArray[0], false);
			DriverStation.reportError("The second value: " + returnArray[1], false);

			socket.close();

			return returnArray;
		}
		catch (IOException e) {
			e.printStackTrace();
			DriverStation.reportError("Failed to connect to processing pi", false);
			System.err.println("Failed to connect to processing pi");
			//insert all default values into the string below
			//current defaults (in order): gears (default 0), 
			String[] failed = {"-3", "-3"};
			return failed;
		}

	}
    
    /*
     * Expects int with -2, -1, 0, 1
     * 
     * Ouputs string array of length one:
     * -3 - no connection
     * -2 - no gear seen
     * -1 - left 
     * 0 - straight
     * 1 - right
     */
    public String[] getGear() {



		try {

			Socket socket = new Socket(server_IP, gearPort);
			InputStreamReader isr = new InputStreamReader(socket.getInputStream());

			BufferedReader br = new BufferedReader(isr);

			String str = br.readLine();
			DriverStation.reportError("This is damn near impressive. Successfully connected to socket and retrieved output.", true);

			// str should be in order: direction, sideways angle, boolean value
		
			String[] returnArray = new String[1];
			returnArray = str.split(",");
			
			DriverStation.reportError("The array made from the string: " + returnArray.toString(), true);
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
     * Expects int with -2, -1, 0, 1
     * 
     * Ouputs string array of length one:
     * -3 - no connection
     * -2 - no gear seen
     * -1 - left 
     * 0 - straight
     * 1 - right
     */
    public String[] getAim() {



		try {

			Socket socket = new Socket(server_IP, aimPort);
			InputStreamReader isr = new InputStreamReader(socket.getInputStream());

			BufferedReader br = new BufferedReader(isr);

			String str = br.readLine();
			DriverStation.reportError("This is damn near impressive. Successfully connected to socket and retrieved output.", true);

			// str should be in order: direction, sideways angle, boolean value
		
			String[] returnArray = new String[1];
			returnArray = str.split(",");
			
			DriverStation.reportError("The array made from the string: " + returnArray.toString(), true);
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
    
    //works
    public void doGear() {
			String[] tmp = getGear();
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
				leftDrive1.set(leftConstant * leftStick.getY());
    			leftDrive2.set(leftConstant * leftStick.getY());
    			rightDrive1.set(rightConstant * rightStick.getY());
    			rightDrive2.set(rightConstant * rightStick.getY());
    			DriverStation.reportError("No vision data, stop using gear button", false);
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
    			leftDrive1.set(leftConstant * .3);
    			leftDrive2.set(leftConstant * .3);
    		}
    		else if (direction == -1) {
    			rightDrive1.set(rightConstant * .3);
    			rightDrive2.set(rightConstant * .3);
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
    
	//unfinished - need to test
    public void doScaling() {
    	//double check to ensure proper direction
    	scale1.set(1);
    	//scale2.set(1);
    }
    
    //need location for following two
    public void doHopper() {
    	String[] location = getLocation();
    }
    
    public void doLoadingStation() {
    	String[] location = getLocation();
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
    	
    }
}