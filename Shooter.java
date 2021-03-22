package frc.robot;
import com.ctre.phoenix.motorcontrol.can.*;
// import com.ctre.phoenix.motorcontrol.*;
import com.revrobotics.*;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
/**
 * Add your docs here.
 */

import edu.wpi.first.wpilibj.SpeedControllerGroup;

public class Shooter {
    public WPI_TalonFX talon6 = new WPI_TalonFX(6); // shooter back
    public WPI_TalonFX talon5 = new WPI_TalonFX(5); // shooter front
    public CANSparkMax turretSpark1 = new CANSparkMax(1, MotorType.kBrushless);
    public CANSparkMax hoodSpark2 = new CANSparkMax(2, MotorType.kBrushless);

    public CANEncoder spark1Encoder = new CANEncoder(turretSpark1);
    public CANPIDController spark1PID = new CANPIDController(turretSpark1);

    public CANEncoder spark2Encoder = new CANEncoder(hoodSpark2);
    public CANPIDController spark2PID = new CANPIDController(hoodSpark2);

    SpeedControllerGroup talonShooterGroup = new SpeedControllerGroup(talon5, talon6);

    private final Limelight limelight = new Limelight();

    public void initShooter(){
        spark1PID.setFeedbackDevice(spark1Encoder);
        spark1Encoder.setPosition(0);
        spark1PID.setP(0.05);
        spark1PID.setI(0);
        spark1PID.setD(0);
        spark1PID.setIZone(0);
        spark1PID.setFF(0);
        spark1PID.setOutputRange(-0.35, 0.35);
        //spark1PID.setReference(0.0, ControlType.kPosition); // // see if this is ok. We need to establish what 0 
        //is for these things then stop zeroing them with setPosition(0);
        spark2Encoder.setPosition(0);
        spark2PID.setP(0.05);
        spark2PID.setI(0);
        spark2PID.setD(0);
        spark2PID.setIZone(0);
        spark2PID.setFF(0);
        spark2PID.setOutputRange(-0.35, 0.35);

        spark2PID.setReference(0, ControlType.kPosition); // see if this is ok. We need to establish what 0 
        //is for these things then stop zeroing them.
    }



    public boolean launchTracking(){ // this method will probably need some parameters/inputs Austin
        double[] launch = limelight.launcherTracking();
        if ((spark1Encoder.getPosition() > -30) && (spark1Encoder.getPosition() < 30)) {
            turretSpark1.set(launch[0]);
        }
        else {
            if ((spark1Encoder.getPosition() < 0) && (launch[0] > 0)) {
                turretSpark1.set(launch[0]);
            }
            else if ((spark1Encoder.getPosition() > 0) && (launch[0] < 0)) {
                turretSpark1.set(launch[0]);
            }
        }
        talonShooterGroup.set(launch[1]);
        spark2PID.setReference(launch[2], ControlType.kPosition); 
        if ((talon6.getSensorCollection().getIntegratedSensorVelocity()/18691 > talonShooterGroup.get()*0.97) && (launch[0] < 0.1) && (launch[2] > spark1Encoder.getPosition() * 0.97) && (launch[2] < spark1Encoder.getPosition() * 1.03)){
            return true;
        }
        return false;
    }
}