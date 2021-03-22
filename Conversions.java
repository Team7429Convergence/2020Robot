package frc.robot;

public class Conversions {
    public static final int inchesToEncoders(double[] data) {
        double doubleEncoders = (data[0]/(data[1] * Math.PI)) * data[2] * data[3];
        // data 0: inches, data 1: wheel size, data 2: encoders per rev, data 3: gear ratio
        int encoders = (int)doubleEncoders;
        return encoders;
    }

    public static double[] launcherTrajectory(double distance) {
        double[] speedAndAngle = new double[2];
        if (distance > RobotMap.Launcher_maxDistance) {
            distance = RobotMap.Launcher_maxDistance;
        }
        if (distance < RobotMap.Launcher_minDistance) {
            distance = RobotMap.Launcher_minDistance;
        }
        double motorSetSpeed = (RobotMap.Launcher_maxSetSpeed - RobotMap.Launcher_minSetSpeed)*(distance/RobotMap.Launcher_maxDistance-RobotMap.Launcher_minDistance);
        double angle = (RobotMap.Launcher_maxHoodAngle)*(distance/RobotMap.Launcher_maxDistance-RobotMap.Launcher_minDistance);
        speedAndAngle[0] = motorSetSpeed;
        speedAndAngle[1] = (int)angle;
        return speedAndAngle;
    }
}