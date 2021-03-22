package frc.robot;

import frc.robot.RobotMap;

//import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
// import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
// import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import com.ctre.phoenix.motorcontrol.*;


public class DriveTrain {
    WPI_TalonFX leftMaster = new WPI_TalonFX(RobotMap.DriveTrain_leftMaster);
    WPI_TalonFX leftSlave = new WPI_TalonFX(RobotMap.DriveTrain_leftSlave);
    WPI_TalonFX rightMaster = new WPI_TalonFX(RobotMap.DriveTrain_rightMaster);
    WPI_TalonFX rightSlave = new WPI_TalonFX(RobotMap.DriveTrain_rightSlave);

    SpeedControllerGroup leftGroup = new SpeedControllerGroup(leftMaster, leftSlave);
    SpeedControllerGroup rightGroup = new SpeedControllerGroup(rightMaster, rightSlave);

    DifferentialDrive drive = new DifferentialDrive(leftGroup, rightGroup);


    public void robotInitDriveTrain(){

      rightMaster.configFactoryDefault();
      rightSlave.configFactoryDefault();
      leftMaster.configFactoryDefault();
      leftSlave.configFactoryDefault();

      leftMaster.setNeutralMode(NeutralMode.Coast);
      rightMaster.setNeutralMode(NeutralMode.Coast);
      leftSlave.setNeutralMode(NeutralMode.Coast);
      rightSlave.setNeutralMode(NeutralMode.Coast);

      leftMaster.getSensorCollection().setIntegratedSensorPosition(0, 0);
      rightMaster.getSensorCollection().setIntegratedSensorPosition(0, 0);
      leftSlave.getSensorCollection().setIntegratedSensorPosition(0, 0);
      rightSlave.getSensorCollection().setIntegratedSensorPosition(0, 0);

      leftMaster.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 0);
      leftMaster.setSensorPhase(true);
      leftMaster.setInverted(false);
      rightMaster.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 0);
      rightMaster.setSensorPhase(true);
      rightMaster.setInverted(false);
      leftSlave.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 0);
      leftSlave.setSensorPhase(true);
      leftSlave.setInverted(false);
      rightSlave.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 0);
      rightSlave.setSensorPhase(true);
      rightSlave.setInverted(false);

      leftMaster.config_kF(0,0,0);
      leftMaster.config_kP(0,0.1,0);
      leftMaster.config_kI(0,0,0);
      leftMaster.config_kD(0,0.2,0);
      rightMaster.config_kF(0,0,0);
      rightMaster.config_kP(0,0.1,0);
      rightMaster.config_kI(0,0,0);
      rightMaster.config_kD(0,0.2,0);
      leftSlave.config_kF(0,0,0);
      leftSlave.config_kP(0,0.1,0);
      leftSlave.config_kI(0,0,0);
      leftSlave.config_kD(0,0.2,0);
      rightSlave.config_kF(0,0,0);
      rightSlave.config_kP(0,0.1,0);
      rightSlave.config_kI(0,0,0);
      rightSlave.config_kD(0,0.2,0);

      leftMaster.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 30, 35, 1));
      leftMaster.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true, 30, 35, 0.5));
      leftSlave.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 30, 35, 1));
      leftSlave.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true, 30, 35, 0.5));
      rightMaster.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 30, 35, 1));
      rightMaster.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true, 30, 35, 0.5));
      rightSlave.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 30, 35, 1));
      rightSlave.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true, 30, 35, 0.5));
    }

    public void driveTrainByLimelight(double[] control) {
      drive.arcadeDrive(control[0], control[1]);
    }

    public void seek() {
      drive.curvatureDrive(0.4, 0.4, false);
    }
    

    public void driveTrainByOperatorControl() {
      if (RobotMap.joystick.getThrottle() > 0) {
        drive.curvatureDrive(Math.pow(RobotMap.joystick.getY(),3)*RobotMap.DriveTrain_limitSpeed,RobotMap.joystick.getX()*RobotMap.DriveTrain_limitSpeed, RobotMap.joystick.getRawButton(2));
      }
      else {
        drive.curvatureDrive(Math.pow(-RobotMap.joystick.getY(),3)*RobotMap.DriveTrain_limitSpeed,RobotMap.joystick.getX()*RobotMap.DriveTrain_limitSpeed, RobotMap.joystick.getRawButton(2));
      }
    }

    public void driveTrainAutoInit() {
      configureOutput(.2);
      zeroDriveTrainEncoders();

      
    }

    public boolean driveTrainByInches(double position) {
      double[] data = new double[4];
      data[0] = position;
      data[1] = RobotMap.DriveTrain_wheelSize;
      data[2] = RobotMap.DriveTrain_encodersPerRev;
      data[3] = RobotMap.DriveTrain_gearRatio;
      int encoders = Conversions.inchesToEncoders(data);
      leftMaster.set(ControlMode.Position, encoders);
      leftSlave.set(ControlMode.Position, encoders);
      rightMaster.set(ControlMode.Position, -encoders);
      rightSlave.set(ControlMode.Position, -encoders);
      if (leftMaster.getSensorCollection().getIntegratedSensorPosition() > encoders * 0.97 && leftMaster.getSensorCollection().getIntegratedSensorPosition() < encoders * 1.03) {
        return true;
      }
      return false;
    }

    public void driveTrainByInchesGoFromBase(double position){ // might work if only run once in autoInit();
      double currentEncoderCount = leftMaster.getSensorCollection().getIntegratedSensorPosition();
      double[] data = new double[4];
      data[0] = position;
      data[1] = RobotMap.DriveTrain_wheelSize;
      data[2] = RobotMap.DriveTrain_encodersPerRev;
      data[3] = RobotMap.DriveTrain_gearRatio;
      int encoders = Conversions.inchesToEncoders(data);
      leftMaster.set(ControlMode.Position, encoders + currentEncoderCount);
      leftSlave.set(ControlMode.Position, encoders + currentEncoderCount);
      rightMaster.set(ControlMode.Position, -encoders - currentEncoderCount);
      rightSlave.set(ControlMode.Position, -encoders - currentEncoderCount);
    }

    public void driveTrainByInchesTELEOP(double position){
      leftMaster.getSensorCollection().setIntegratedSensorPosition(0, 0);
      rightMaster.getSensorCollection().setIntegratedSensorPosition(0, 0);
      leftSlave.getSensorCollection().setIntegratedSensorPosition(0, 0);
      rightSlave.getSensorCollection().setIntegratedSensorPosition(0, 0);
      double[] data = new double[4];
      data[0] = position;
      data[1] = RobotMap.DriveTrain_wheelSize;
      data[2] = RobotMap.DriveTrain_encodersPerRev;
      data[3] = RobotMap.DriveTrain_gearRatio;
      int encoders = Conversions.inchesToEncoders(data);
      leftMaster.set(ControlMode.Position, encoders);
      leftSlave.set(ControlMode.Position, encoders);
      rightMaster.set(ControlMode.Position, -encoders);
      rightSlave.set(ControlMode.Position, -encoders);
    }

    public void driveTrainByInchesTeleopForRotation(double position){
      leftMaster.getSensorCollection().setIntegratedSensorPosition(0, 0);
      rightMaster.getSensorCollection().setIntegratedSensorPosition(0, 0);
      leftSlave.getSensorCollection().setIntegratedSensorPosition(0, 0);
      rightSlave.getSensorCollection().setIntegratedSensorPosition(0, 0);
      double[] data = new double[4];
      data[0] = position;
      data[1] = RobotMap.DriveTrain_wheelSize;
      data[2] = RobotMap.DriveTrain_encodersPerRev;
      data[3] = RobotMap.DriveTrain_gearRatio;
      int encoders = Conversions.inchesToEncoders(data);
      leftMaster.set(ControlMode.Position, encoders);
      leftSlave.set(ControlMode.Position, encoders);
      rightMaster.set(ControlMode.Position, encoders);
      rightSlave.set(ControlMode.Position, encoders);
    }

    public void driveTrainByInchesTeleopForRotationLEFT(double position){
      leftMaster.getSensorCollection().setIntegratedSensorPosition(0, 0);
      rightMaster.getSensorCollection().setIntegratedSensorPosition(0, 0);
      leftSlave.getSensorCollection().setIntegratedSensorPosition(0, 0);
      rightSlave.getSensorCollection().setIntegratedSensorPosition(0, 0);
      double[] data = new double[4];
      data[0] = position;
      data[1] = RobotMap.DriveTrain_wheelSize;
      data[2] = RobotMap.DriveTrain_encodersPerRev;
      data[3] = RobotMap.DriveTrain_gearRatio;
      int encoders = Conversions.inchesToEncoders(data);
      leftMaster.set(ControlMode.Position, encoders);
      leftSlave.set(ControlMode.Position, encoders);
      rightMaster.set(ControlMode.Position, encoders);
      rightSlave.set(ControlMode.Position, encoders);
    }

    public void driveTrainByInchesTeleopForRotationRIGHT(double position){
      leftMaster.getSensorCollection().setIntegratedSensorPosition(0, 0);
      rightMaster.getSensorCollection().setIntegratedSensorPosition(0, 0);
      leftSlave.getSensorCollection().setIntegratedSensorPosition(0, 0);
      rightSlave.getSensorCollection().setIntegratedSensorPosition(0, 0);
      double[] data = new double[4];
      data[0] = position;
      data[1] = RobotMap.DriveTrain_wheelSize;
      data[2] = RobotMap.DriveTrain_encodersPerRev;
      data[3] = RobotMap.DriveTrain_gearRatio;
      int encoders = Conversions.inchesToEncoders(data);
      leftMaster.set(ControlMode.Position, encoders);
      leftSlave.set(ControlMode.Position, encoders);
      rightMaster.set(ControlMode.Position, encoders);
      rightSlave.set(ControlMode.Position, encoders);
    }

    public void teleopInitDrive(){
      configureOutput(1);
      leftMaster.getSensorCollection().setIntegratedSensorPosition(0, 0);
      rightMaster.getSensorCollection().setIntegratedSensorPosition(0, 0);
      leftSlave.getSensorCollection().setIntegratedSensorPosition(0, 0);
      rightSlave.getSensorCollection().setIntegratedSensorPosition(0, 0);
    }

 
    public void disabledInitDrive(){
      leftMaster.getSensorCollection().setIntegratedSensorPosition(0, 0);
      rightMaster.getSensorCollection().setIntegratedSensorPosition(0, 0);
      leftSlave.getSensorCollection().setIntegratedSensorPosition(0, 0);
      rightSlave.getSensorCollection().setIntegratedSensorPosition(0, 0);
    }

    // public void rotate(){
    //   // leftMaster.set(ControlMode.PercentOutput, RobotMap.rightJoystick.getZ());
    //   // leftSlave.set(ControlMode.PercentOutput, RobotMap.rightJoystick.getZ());
    //   // rightMaster.set(ControlMode.PercentOutput, RobotMap.rightJoystick.getZ());
    //   // rightSlave.set(ControlMode.PercentOutput, RobotMap.rightJoystick.getZ());
    //   double heading = Robot.gyro.ahrs.getCompassHeading();
    //   double temp;
    //   double temp1;
    //   if(RobotMap.joystick.getRawButton(7)){
    //     if((heading + 45.0) > 360.0){
    //       temp = (heading + 45.0);
    //       temp1 = temp - 360.0;
    //       while ((Robot.gyro.ahrs.getCompassHeading() < temp1) && (Robot.gyro.ahrs.getCompassHeading() > heading)){
    //         // leftMaster.set(ControlMode.PercentOutput, 0.25);
    //         // leftSlave.set(ControlMode.PercentOutput, 0.25);
    //         // rightMaster.set(ControlMode.PercentOutput, 0.25);
    //         // rightSlave.set(ControlMode.PercentOutput, 0.25);
    //         drive.arcadeDrive(0, 0.25);
    //       }
    //     } else {
    //       while ((Robot.gyro.ahrs.getCompassHeading() < (heading + 45.0))){
    //         drive.arcadeDrive(0, 0.25);
    //       }
    //     }
    //     drive.arcadeDrive(0, 0.25);
    //   }
    // }

    public void moveALittleBit(){
      if(RobotMap.joystick.getPOV() == 0){
        driveTrainByInchesTELEOP(1.5);
      }

      if(RobotMap.joystick.getPOV() == 180){
        driveTrainByInchesTELEOP(-1.5);
      }
    }

    public void rotateSlowly(){
        if(RobotMap.joystick.getPOV() == 90){
            driveTrainByInchesTeleopForRotation(2);
          }
    
          if(RobotMap.joystick.getPOV() == 270){
            driveTrainByInchesTeleopForRotation(-2);
          }

          if(RobotMap.joystick.getPOV() == 45){

          }
    }

    public void configureOutput(double output){
      leftMaster.configNominalOutputForward(0, 0);
      leftMaster.configNominalOutputReverse(0, 0);
      leftMaster.configPeakOutputForward(output, 0);
      leftMaster.configPeakOutputReverse(-output, 0);
      rightMaster.configNominalOutputForward(0, 0);
      rightMaster.configNominalOutputReverse(0, 0);
      rightMaster.configPeakOutputForward(output, 0);
      rightMaster.configPeakOutputReverse(-output, 0);
      leftSlave.configNominalOutputForward(0, 0);
      leftSlave.configNominalOutputReverse(0, 0);
      leftSlave.configPeakOutputForward(output, 0);
      leftSlave.configPeakOutputReverse(-output, 0);
      rightSlave.configNominalOutputForward(0, 0);
      rightSlave.configNominalOutputReverse(0, 0);
      rightSlave.configPeakOutputForward(output, 0);
      rightSlave.configPeakOutputReverse(-output, 0);
    }


    public void zeroDriveTrainEncoders(){
      leftMaster.getSensorCollection().setIntegratedSensorPosition(0, 0);
      rightMaster.getSensorCollection().setIntegratedSensorPosition(0, 0);
      leftSlave.getSensorCollection().setIntegratedSensorPosition(0, 0);
      rightSlave.getSensorCollection().setIntegratedSensorPosition(0, 0);
    }

  }