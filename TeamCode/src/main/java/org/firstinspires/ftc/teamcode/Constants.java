package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.ArmConfig;

public class Constants {

    public static final class OpModes {
        public static final String linearOp = "Linear Opmode";
        public static final String teleop = "Teleop";
        public static final String pidOp = "PID Tune";
        public static final String spikeAuto = "Just Spike";
        public static final String parkAuto = "Park";
        public static final String parkAutoF = "Park FAR";
    }

    public static final class MecanumConstants {

        public static final String frontLeftMotor = "frontLeft0";
        public static final String frontRightMotor = "frontRight1";
        public static final String backLeftMotor = "backLeft2";
        public static final String backRightMotor = "backRight3";

        public static final PIDCoefficients yPID = new PIDCoefficients(0.8, 0, 0);
        public static final PIDCoefficients xPID = new PIDCoefficients(0.8, 0, 0);
        public static final PIDCoefficients yawPID = new PIDCoefficients(0.4, 0, 0);

        public static final DcMotor.Direction invertLeft = DcMotor.Direction.FORWARD;
        public static final DcMotor.Direction invertRight = DcMotor.Direction.REVERSE;
        public static final DcMotor.ZeroPowerBehavior neutralMode = DcMotor.ZeroPowerBehavior.BRAKE;

        public static final double ticksPerRev = 8192;
        public static final double wheelD = 38 / 25.4; //38mm in inches
        public static final double gearRatio = 1;
        public static final double ticksToInch = 1;//(wheelD * Math.PI) / ticksPerRev;


        public static double LATERAL_DISTANCE = 0; // in - distance between left and right
        public static double FORWARD_OFFSET = 0; // in - distance between the forward center

    }

    public static final class ClawConstants {

        public static final String leftClaw0 = "leftClaw0";
        public static final String rightClaw2 = "rightClaw2";

        public static final Servo.Direction invertL = Servo.Direction.FORWARD;
        public static final Servo.Direction invertR = Servo.Direction.REVERSE;

        public static final double openAngle = 0;
        public static final double closedAngle = 42.5;
    }

    public static final class WinchConstants {
        public static final String leftArm0 = "leftArm0";
        public static final String rightArm1 = "rightArm1";
        public static final double encoderToDeg = 965/180;
        public static final double scoreSafety = 190;
        public static final double intakeSafety = -15;
        public static final double pickupAngle = 0; //0
        public static final double scoreAngle = 175.5; //0
        public static final double maxSpeed = 0.6;
        public static final PIDCoefficients winchPID = new PIDCoefficients(0.013, 0, 0.001);
        public static final double tolerance = 4;
        public static final double wristTolerance = 12;
        public static final DcMotor.Direction rightInvert = DcMotor.Direction.FORWARD;
        public static final DcMotor.Direction leftInvert = DcMotor.Direction.REVERSE;
        public static final DcMotor.ZeroPowerBehavior neutralMode = DcMotor.ZeroPowerBehavior.BRAKE;
    }

    public static final class ClimberConstants {
        public static final String climber4 = "climberServo4";
        public static final double homeAngle = 300-200;
        public static final double climb = 300;
        public static final String winchMotor3 = "winchMotor3";
        public static final Servo.Direction invert = Servo.Direction.REVERSE;
    }

    public static final class WristConstants {

        public static final String leftWristServo = "leftWrist0";
        public static final String rightWristServo = "rightWrist2";

        public static final Servo.Direction leftInvert = Servo.Direction.REVERSE;
        public static final Servo.Direction rightInvert = Servo.Direction.FORWARD;

        public static final double homeAngle = 200; //0
        public static final double pickupAngle = 5; //0
        public static final double scoreAngle = 125.83; //0
    }

    public static final class PlaneConstants {
        public static final String launcher0 = "launcher0";

        public static final double hold = 75;
        public static final double release = 0;

        public static final Servo.Direction inverted = Servo.Direction.FORWARD;
    }

    public static final class VisionConstants {

        public static final String huskyLens = "huskylens";

        public static final int readPeriod = 1;

        public static final int width = 240;
    }

    public static final class ScoringConstants {

        public static final double armLevel1 = 4000;
        public static final double armLevel2 = 5100;
        public static final double scoreAngle = 118.91; //0.2937

        public static final double tagOffset = 0;

        public static final ArmConfig scoreLow = new ArmConfig(armLevel1, scoreAngle);
        public static final ArmConfig scoreMid = new ArmConfig(armLevel2, scoreAngle);
    }

    /**
     * The pose is based on red, x should be flipped for blue
     */
    public static final class AutoPoses {
        public static final Pose2d parkClose = new Pose2d(0,12, 0);

        public static final double spikeY = 0.25;
        public static final double midSpike = 0;
        public static final double leftSpike = 60;
        public static final double rightSpike = -leftSpike;

        public static final Pose2d BparkFar = new Pose2d(-30,10, 0);
        public static final Pose2d RparkFar = new Pose2d(30,10, 0);
    }

}