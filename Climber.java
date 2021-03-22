package frc.robot;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// import com.ctre.phoenix.time.StopWatch;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;
import com.revrobotics.ControlType;

/**
 * Add your docs here.
 */
public class Climber {
    public CANSparkMax hookSpark4 = new CANSparkMax(4, MotorType.kBrushless);
    public TalonFX talonSpool4 = new TalonFX(4);
    //StopWatch stopwatch = new StopWatch();
    public CANEncoder spark4Encoder = new CANEncoder(hookSpark4);
    public CANPIDController spark4PID = new CANPIDController(hookSpark4);
    boolean canDoSmallAdjust;

    public void climberInit(){
        canDoSmallAdjust = false;
        spark4PID.setFeedbackDevice(spark4Encoder);
        spark4Encoder.setPosition(0);
        spark4PID.setP(0.05);
        spark4PID.setI(0);
        spark4PID.setD(0);
        spark4PID.setIZone(0);
        spark4PID.setFF(0);
        spark4PID.setOutputRange(-1, 1);
        
        spark4PID.setReference(0, ControlType.kPosition); //////////////////////////////// just wondering if this is ok
        talonSpool4.setNeutralMode(NeutralMode.Brake);
    }


    public void deployHook(){
        //stopwatch.start();
        SmartDashboard.putNumber("spark", spark4Encoder.getPosition());
        if(RobotMap.controller.getRawButton(2)){ // B
            spark4PID.setReference(-181, ControlType.kPosition);
            canDoSmallAdjust = true;
        }
        else if (RobotMap.controller.getRawButton(3)) {
            spark4PID.setReference(0, ControlType.kPosition);
            canDoSmallAdjust = false;
        }
        if (canDoSmallAdjust && (RobotMap.controller.getRawAxis(5) > 0.1 || RobotMap.controller.getRawAxis(5) < -0.1)) {
            if (RobotMap.controller.getRawAxis(5) > 0.1) {
                spark4PID.setReference(spark4Encoder.getPosition() + 3, ControlType.kPosition);
            }
            else {
                spark4PID.setReference(spark4Encoder.getPosition() - 3, ControlType.kPosition);
            }
        }
    }

    public void deploySpool(){
        if(RobotMap.controller.getRawButton(4)){ // Y
            talonSpool4.set(ControlMode.PercentOutput, -0.7);
        }
        // else if(RobotMap.controller.getRawButton(6)){ // RB
        //     talonSpool4.set(ControlMode.PercentOutput, 0.5);
        // }
        else {
            talonSpool4.set(ControlMode.PercentOutput, 0);
        }
    }
}