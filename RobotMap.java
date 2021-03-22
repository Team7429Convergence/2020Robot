package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
public class RobotMap {
	// Button 10: autonomous go 200k encoder counts.
    // Button 11: Zero Yaw on Gyro
    public static int ledPort = 9;
	public static int fieldStartingPosition; // can be 1, 2, or 3. Counting from the left of the field.

	public static double Limelight_steerSensitivity = 0.8;
	public static double Limelight_driveSensitivity = 0.5;
	public static double Limelight_minCommand = 0.2;

    public static int DriveTrain_leftMaster = 1;
    public static int DriveTrain_leftSlave = 0;
    public static int DriveTrain_rightMaster = 3;
	public static int DriveTrain_rightSlave = 2;
	public static int DriveTrain_configPeakCurrentLimit = 30;
	public static int DriveTrain_configPeakCurrentDuration = 100;
	public static int DriveTrain_configContinuousCurrentLimit = 20;
	
	public static int ProxSensor_Primary = 6;
	public static int ProxSensor_Secondary = 3;
    
    public static int DriveTrain_wheelSize = 8;
    public static int DriveTrain_encodersPerRev = 2048;
    public static double DriveTrain_gearRatio = 7.56;

    public static int Control_Joystick = 0;
	public static int Control_Controller = 4;
	
	public static double Launcher_targetHeight = 102;
	public static double Launcher_launchHeight = 27;
	public static double Launcher_zeroDistance = 122;
	public static double Launcher_maxSetSpeed = 0.6;
	public static double Launcher_minSetSpeed = 0.4;
	public static double Launcher_maxHoodAngle = -35;
	public static double Launcher_maxDistance = 216;
	public static double Launcher_minDistance = 86;
	
	public static int intakeSolenoidIn = 4;
	public static int intakeSolenoidOut = 3;
	public static int spinnerSolenoidIn = 5;
	public static int spinnerSolenoidOut = 2;
	public static int openSolenoidIn = 6;
	public static int openSolenoidOut = 1;

    public static double DriveTrain_limitSpeed = 0.5;

    public static Joystick joystick = new Joystick(RobotMap.Control_Joystick);
    public static Joystick controller = new Joystick(RobotMap.Control_Controller);
}