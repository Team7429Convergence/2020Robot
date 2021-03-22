package frc.robot;

// import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// import com.ctre.phoenix.motorcontrol.can.*;
import com.revrobotics.ControlType;
// import com.ctre.phoenix.motorcontrol.*;

import edu.wpi.cscore.CvSink;
// import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.*;
//import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.cscore.VideoMode;
//import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.cameraserver.CameraServer;


public class Robot extends TimedRobot {
  public static Gyro gyro = new Gyro();
  private final Climber climber = new Climber();
  Intake intake = new Intake();
  private Shooter shooter = new Shooter();
  public Indexer indexer = new Indexer();
  public ProximitySensor proximitySensor = new ProximitySensor();

  private final DriveTrain driveTrain = new DriveTrain();
  private final Limelight limeLight = new Limelight();
  private int autonomousStage;
  
  CvSink cvSink;
  CvSource outputStream;

  //public UsbCamera usbCamera = new UsbCamera("Rear", "UsbCamera");
  // PixelFormat pixelFormat = new Pixe
  // VideoMode videomode = new VIdeoMode()
  

  @Override
  public void robotInit() {
    // usbCamera.setResolution(400, 400);
    // usbCamera.setVideoMode()
    CameraServer.getInstance().startAutomaticCapture();
    cvSink = CameraServer.getInstance().getVideo();
    outputStream = CameraServer.getInstance().putVideo("Blur", 640, 480);
    climber.climberInit();
    shooter.initShooter();
    intake.initIntake();
    shooter.spark2Encoder.setPosition(0);
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
    driveTrain.driveTrainAutoInit();
    autonomousStage = 1;
  }

  @Override
  public void autonomousPeriodic() {
    if (autonomousStage == 1) {
      shooter.talonShooterGroup.set(0.45);
      if (shooter.talon6.getSensorCollection().getIntegratedSensorVelocity() > 8000) {
        autonomousStage = 2;
      }
      try{
        Thread.sleep(3000);
      }
      catch (Exception e){}
      autonomousStage = 2;
    }
    if (autonomousStage == 2) {
      indexer.beltSpark7.set(-0.5);
      indexer.indexerSpark5.set(-0.5);
      try{
        Thread.sleep(5000);
      }
      catch (Exception e){}
      autonomousStage = 3;
    }
    if (autonomousStage == 3) {
      shooter.talonShooterGroup.set(0.0);
      indexer.beltSpark7.set(0.0);
      indexer.indexerSpark5.set(0.0);
      if (driveTrain.driveTrainByInches(36)) {
        autonomousStage = 4;
      }
    }
    if(autonomousStage == 4){
      
    }

    // if(autonomousStage == 1){
    //   driveTrain.zeroDriveTrainEncoders();
    //   autonomousStage = 2;
    // }

    // if (autonomousStage == 0) {
    //   if (shooter.launchTracking()) {
    //     indexer.indexerSpark5.set(.5);
    //     indexer.beltSpark7.set(.5);
    //     if (!proximitySensor.ballsLoaded() && !proximitySensor.ballsReady()){
    //       indexer.indexerSpark5.set(0.0);
    //       indexer.beltSpark7.set(0.0);
    //       autonomousStage = 1;
    //     }
    //   }
    // }
    // driveTrain.autoDrivePeriodic();
  }

  @Override
  public void teleopInit() {
    //driveTrain.robotInitDriveTrain();
    //driveTrain.autoInitDrive();
    //driveTrain.rotate();
    driveTrain.teleopInitDrive();
  }


  @Override
  public void teleopPeriodic() {
    if (RobotMap.joystick.getRawButton(1) == true) {
      if (RobotMap.joystick.getThrottle() > 0) {
        // double[] steer = limeLight.ballTracking();
        // SmartDashboard.putNumber("steer", steer[0]);
        // SmartDashboard.putNumber("drive", steer[1]);
        // driveTrain.driveTrainByLimelight(steer);
        driveTrain.driveTrainByOperatorControl();
        intake.intakeSolenoidReverse();
        shooter.talonShooterGroup.set(0.0);
        intake.intakeSpark6.set(0.5);
        if (proximitySensor.proxSensor3.get() == false) {
          indexer.beltSpark7.set(-0.5);
        }
      }
      else if (RobotMap.joystick.getThrottle() < 0) {
        System.out.println("talon5: " +shooter.talon5.getSensorCollection().getIntegratedSensorVelocity());
        System.out.println("talon6: " +shooter.talon6.getSensorCollection().getIntegratedSensorVelocity());
        if (shooter.launchTracking()){
          indexer.indexerSpark5.set(-.3);
          indexer.beltSpark7.set(-.3);
        } 
        else{
          indexer.indexerSpark5.set(0);
          indexer.beltSpark7.set(0);
        }
      }
      else {
        intake.intakeSpark6.set(0);
        indexer.indexerSpark5.set(0);
        indexer.beltSpark7.set(0);
      }
    }
    else {
      System.out.println("proxSensor" +proximitySensor.proxSensor3.get());
      if (RobotMap.controller.getPOV() == 0) {
        indexer.beltSpark7.set(-0.5);
        intake.intakeSpark6.set(0.5);
      }
      else if (RobotMap.controller.getPOV() == 180) {
        indexer.beltSpark7.set(0.5);
        intake.intakeSpark6.set(-0.5);
        indexer.indexerSpark5.set(0.5);
      }
      else {
        indexer.beltSpark7.set(0.0);
        intake.intakeSpark6.set(0.0);
        indexer.indexerSpark5.set(0.0);
      }
      if (RobotMap.controller.getPOV() == 90) {
        shooter.spark2PID.setReference(-290, ControlType.kPosition);
      }
      else if (RobotMap.controller.getPOV() == 270) {
        shooter.spark2PID.setReference(0, ControlType.kPosition);
      }
      if (RobotMap.joystick.getRawButton(4)) {
        intake.intakeSolenoidForward();
      }
      if (RobotMap.joystick.getRawButton(7)) {
        limeLight.zeroLimelightCam();
      }
      SmartDashboard.putNumber("pov", RobotMap.controller.getPOV());
      //SmartDashboard.putBoolean("proxsensor", proximitySensor.proxSensor1.get());
      //SmartDashboard.putBoolean("proxsensor2", proximitySensor.proxSensor6.get());
      shooter.spark2PID.setReference(0.0, ControlType.kPosition);
      shooter.talonShooterGroup.set(0.0);
      driveTrain.driveTrainByOperatorControl();
      driveTrain.moveALittleBit();
      driveTrain.rotateSlowly();
      shooter.spark1PID.setReference(0.0, ControlType.kPosition);
      SmartDashboard.putNumber("distance", limeLight.distance);
    }
    climber.deployHook();
    climber.deploySpool();
}

@Override
public void testPeriodic() {
  // // climber.deploySpool();
  // // climber.deployHook();
  // // shooter.testShooter();
  // // intake.runIntake();
  // // indexer.runIndexer();
  // if (RobotMap.joystick.getRawButton(1)) {
  //   driveTrain.drive.arcadeDrive(0, 1);
  // }
  // else {
  //   driveTrain.drive.arcadeDrive(0, 0);
  // }
}

  @Override
  public void disabledInit() {
    driveTrain.disabledInitDrive();
    autonomousStage = 1;
    shooter.spark2PID.setReference(0, ControlType.kPosition);
    shooter.talonShooterGroup.set(0.0);
    intake.intakeSolenoidForward();
    driveTrain.zeroDriveTrainEncoders();
    climber.spark4PID.setReference(0, ControlType.kPosition);
  }

  public int getAutoStage(){
    return this.autonomousStage;
  }
}