package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.Constants.MecanumConstants;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Utilities;

public class Mecanum {

    private DcMotor frontLeft0, frontRight1, backLeft2, backRight3;
    private IMU imu;


    double deadband = 0.3;
    double deadbandSpeed = 0.05;
    public Mecanum(HardwareMap hardwareMap) {

        frontLeft0 = hardwareMap.get(DcMotor.class, MecanumConstants.frontLeftMotor);
        frontRight1 = hardwareMap.get(DcMotor.class, MecanumConstants.frontRightMotor);
        backLeft2 = hardwareMap.get(DcMotor.class, MecanumConstants.backLeftMotor);
        backRight3 = hardwareMap.get(DcMotor.class, MecanumConstants.backRightMotor);

        frontLeft0 = motorConfig(frontLeft0);
        frontRight1 = motorConfig(frontRight1);
        backLeft2 = motorConfig(backLeft2);
        backRight3 = motorConfig(backRight3);

        frontLeft0.setDirection(MecanumConstants.invertLeft);
        frontRight1.setDirection(MecanumConstants.invertRight);
        backLeft2.setDirection(MecanumConstants.invertLeft);
        backRight3.setDirection(MecanumConstants.invertRight);

        imu = hardwareMap.get(IMU.class, "imu");

        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);

    }

    public void teleop(Gamepad gamepad1, boolean mode) {
        double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
        double x = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x / 2;

        if (gamepad1.options) {
            imu.resetYaw();
        }

        if (mode) {
            x /= 10;
            y /= 10;
            rx /= 10;
        }

        drive(y, x * 1.1, rx, true);
    }

    public void resetGyro() {
        imu.resetYaw();
    }

    public void drive(double ySpeed, double xSpeed, double rot, boolean fieldOriented) {

        double rotY = ySpeed;
        double rotX = xSpeed;

        if (fieldOriented) {
            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            // Rotate the movement direction counter to the bot's rotation
            rotX = xSpeed * Math.cos(-botHeading) - ySpeed * Math.sin(-botHeading);
            rotY = xSpeed * Math.sin(-botHeading) + ySpeed * Math.cos(-botHeading);
        }

        double denominator = Math.max(Math.abs(ySpeed) + Math.abs(xSpeed) + Math.abs(rot), 1);
        double frontLeftPower = (rotY + rotX + rot) / denominator;
        double backLeftPower = (rotY - rotX + rot) / denominator;
        double frontRightPower = (rotY - rotX - rot) / denominator;
        double backRightPower = (rotY + rotX - rot) / denominator;

        setPower(frontLeftPower, backLeftPower, frontRightPower, backRightPower);
    }

    public void setPower(double frontLeft, double backLeft, double frontRight, double backRight) {
        frontLeft0.setPower(frontLeft);
        backLeft2.setPower(backLeft);
        frontRight1.setPower(frontRight);
        backRight3.setPower(backRight);
    }

    public double[] getPower() {
        double[] power = new double[4];

        power[0] = frontLeft0.getPower();
        power[1] = backLeft2.getPower();
        power[2] = frontRight1.getPower();
        power[3] = backRight3.getPower();

        return power;
    }


    public double getLeftDist() {
        return (frontLeft0.getCurrentPosition()) * MecanumConstants.ticksToInch;
    }

    public double getRightDist() {
        return (frontRight1.getCurrentPosition()) * MecanumConstants.ticksToInch;
    }

    public double getAvgDist() {
        return (getLeftDist() + getRightDist()) / 2;
    }

    public double getYaw() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }

    public void resetEncoders() {
        motorConfig(frontLeft0);
        motorConfig(frontRight1);
        motorConfig(backLeft2);
        motorConfig(backRight3);
    }

    public void driveToPos(int dist) {
        frontLeft0.setTargetPosition(dist);
        frontRight1.setTargetPosition(dist);
        backLeft2.setTargetPosition(dist);
        backRight3.setTargetPosition(dist);
        positionConfig();

    }

    public boolean atTarget() {
        return Utilities.withinBounds(frontLeft0.getCurrentPosition(), frontLeft0.getTargetPosition(), 5) &&
                Utilities.withinBounds(frontRight1.getCurrentPosition(), frontRight1.getTargetPosition(), 5);
    }


    public void periodic(Telemetry telemetry) {
        telemetry.addLine("Drive:");
        telemetry.addData("Front Left:", getPower()[0]);
        telemetry.addData("Back Left:", getPower()[1]);
        telemetry.addData("Front Right:", getPower()[2]);
        telemetry.addData("Back Right:", getPower()[3]);
        telemetry.addLine("Position:");
        telemetry.addData("Left:", frontLeft0.getCurrentPosition());
        telemetry.addData("Right:", frontRight1.getCurrentPosition());
        telemetry.addData("Distance:", getAvgDist());
        telemetry.addData("Yaw:", getYaw());
    }

    /**
     * Standard motor config for all drivetrain motors
     *
     * @param motor DcMotor to  configure
     * @return configured DcMotor
     */
    public DcMotor motorConfig(DcMotor motor) {
        motor.setZeroPowerBehavior(MecanumConstants.neutralMode);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        return motor;
    }

    public void positionConfig() {
        frontLeft0.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight3.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

}