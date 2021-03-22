package frc.robot;

import com.revrobotics.*;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Indexer {
    public CANSparkMax indexerSpark5 = new CANSparkMax(5, MotorType.kBrushless);
    public CANSparkMax beltSpark7 = new CANSparkMax(7, MotorType.kBrushless);

    public CANEncoder spark5Encoder = new CANEncoder(indexerSpark5);
    public CANPIDController spark5PID = new CANPIDController(indexerSpark5);

    public CANEncoder spark7Encoder = new CANEncoder(beltSpark7);
    public CANPIDController spark7PID = new CANPIDController(beltSpark7);

    public void indexerInit(){
        spark5PID.setFeedbackDevice(spark5Encoder);
        spark5Encoder.setPosition(0);
        spark5PID.setP(0.05);
        spark5PID.setI(0);
        spark5PID.setD(0);
        spark5PID.setIZone(0);
        spark5PID.setFF(0);
        spark5PID.setOutputRange(-1, 1);

        spark7PID.setFeedbackDevice(spark7Encoder);
        spark7Encoder.setPosition(0);
        spark7PID.setP(0.05);
        spark7PID.setI(0);
        spark7PID.setD(0);
        spark7PID.setIZone(0);
        spark7PID.setFF(0);
        spark7PID.setOutputRange(-1, 1);
    }

    public void runIndexer(){

        if(RobotMap.joystick.getRawButton(10)){
            System.out.println("button 8 pressed");
            beltSpark7.set(-0.45);
        } 
        else {
            beltSpark7.set(0.0);   
        }

        if(RobotMap.joystick.getRawButton(12)){
            System.out.println("button 12 pressed");
            indexerSpark5.set(-0.45);
        } else {
            indexerSpark5.set(0.0);
        }
    }
}
