package frc.robot;

import edu.wpi.first.wpilibj.Solenoid;
import com.revrobotics.*;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
public class Intake {
    public CANSparkMax intakeMotor7 = new CANSparkMax(7, MotorType.kBrushless);
    // public CANSparkMax intakeMotor2 = new CANSparkMax(4, MotorType.kBrushless);

    public Solenoid intakeSolenoid = new Solenoid(1);

    public void activateSolenoid(){
        intakeSolenoid.clearAllPCMStickyFaults();
        intakeSolenoid.set(true); // ?
    }

    public void runIntake(){
        if(RobotMap.joystick.getRawButton(9)){
            intakeMotor7.set(0.35);
        }

        if(RobotMap.joystick.getRawButton(11)){
            intakeMotor7.set(0);
        }

        if(RobotMap.joystick.getRawButton(12)){
            intakeMotor7.set(-0.35);
        }

    }


    
}
