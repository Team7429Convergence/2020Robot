package frc.robot;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.time.StopWatch;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;
import com.revrobotics.ControlType;

/**
 * Add your docs here.
 */
public class Climber {
    public CANSparkMax spark4 = new CANSparkMax(4, MotorType.kBrushless);
    public TalonFX talonSpool4 = new TalonFX(4);
    //StopWatch stopwatch = new StopWatch();
    public CANEncoder spark4Encoder = new CANEncoder(spark4);
    public CANPIDController spark4PID = new CANPIDController(spark4);
    double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput;

    public void climberInit(){
        spark4PID.setFeedbackDevice(spark4Encoder);
        spark4Encoder.setPosition(0);
        kP = 0.05;
        kI = 0;
        kD = 0;
        kIz = 0;
        kFF = 0;
        kMaxOutput = .2;
        kMinOutput = -.2;
        spark4PID.setP(kP);
        spark4PID.setI(kI);
        spark4PID.setD(kD);
        spark4PID.setIZone(kIz);
        spark4PID.setFF(kFF);
        spark4PID.setOutputRange(kMinOutput, kMaxOutput);



    }
    public void deployHook(){
        //stopwatch.start();
        boolean set = false;
        
        if(RobotMap.controller.getRawButton(4)){
            // do{
            //     motor7.set(0.3);
            // } while (stopwatch.getDurationMs() < 2000);
            // motor7.set(0.0);
            System.out.println("Button 4 pressed");
            spark4PID.setReference(180, ControlType.kPosition);

        }

        if(RobotMap.controller.getRawButton(5)){
            spark4Encoder.setPosition(0);
            spark4PID.setReference(-5, ControlType.kPosition);
        }

        if(RobotMap.controller.getRawButton(6)){
            spark4Encoder.setPosition(0);
            spark4PID.setReference(5, ControlType.kPosition);
        }


        
    }

    public void deploySpool(){
        if(RobotMap.joystick.getRawButton(6)){
            talonSpool4.set(ControlMode.PercentOutput, .07);
        }
        if(RobotMap.joystick.getRawButton(7)){
            talonSpool4.set(ControlMode.PercentOutput, 0.0);
        }
        if(RobotMap.joystick.getRawButton(8)){
            talonSpool4.set(ControlMode.PercentOutput, -.07);
        }
    }
}
