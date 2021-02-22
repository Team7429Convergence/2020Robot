package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;

public class ProximitySensor {
    DigitalInput proxSensor = new DigitalInput(RobotMap.ProxSensor_Primary);
    public boolean ballsLoaded() {
        if (proxSensor.get() == true) {
            return false;
        }
        else {
            return true;
        }
    }
}