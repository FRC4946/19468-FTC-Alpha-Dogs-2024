package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.Constants.MecanumConstants;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Mecanum {

    private DcMotor frontLeft0, frontRight1, backLeft2, backRight3;
    private IMU imu;

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

    public void teleop(Gamepad gamepad1) {
        double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
        double x = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;

        if (gamepad1.options) {
            imu.resetYaw();
        }

        drive(y, x * 1.1, rx, false);
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
        frontLeft0.setPower(frontLeft / 2);
        backLeft2.setPower(backLeft / 2);
        frontRight1.setPower(frontRight / 2);
        backRight3.setPower(backRight / 2);
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
        return (frontLeft0.getCurrentPosition() / 20.0) * MecanumConstants.ticksToInch;
    }

    public double getRightDist() {
        return -(frontRight1.getCurrentPosition() / 20.0) * MecanumConstants.ticksToInch;
    }

    public double getAvgDist() {
        return (getLeftDist() + getRightDist()) / 2;
    }

    public double getYaw() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }


    public void periodic(Telemetry telemetry) {
        telemetry.addLine("Drive:");
        telemetry.addData("Front Left:", getPower()[0]);
        telemetry.addData("Back Left:", getPower()[1]);
        telemetry.addData("Front Right:", getPower()[2]);
        telemetry.addData("Back Right:", getPower()[3]);
        telemetry.addLine("Position:");
        telemetry.addData("Left:", getLeftDist());
        telemetry.addData("Right:", getRightDist());
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

}