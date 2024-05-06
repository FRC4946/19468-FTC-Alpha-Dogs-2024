package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Utilities;

public class Claw {

    Servo leftClaw0;
    Servo rightClaw2;
    int leftC = 0;
    int rightC = 0;

    public Claw(HardwareMap hardwareMap) {
        leftClaw0 = hardwareMap.get(Servo.class, Constants.ClawConstants.leftClaw0);
        rightClaw2 = hardwareMap.get(Servo.class, Constants.ClawConstants.rightClaw2);

        leftClaw0.setDirection(Constants.ClawConstants.invertL);
        rightClaw2.setDirection(Constants.ClawConstants.invertR);

        closeRClaw();
        closeLClaw();
    }

    public double getLeftAngle() {
        return (leftClaw0.getPosition() * 300);
    }

    public double getRightAngle() {
        return (rightClaw2.getPosition() * 300);
    }

    public void setLeftAngle(double angle) {
        leftClaw0.setPosition(angle / 300);
    }

    public void setRightAngle(double angle) {
        rightClaw2.setPosition(angle / 300);
    }

    public double getLPos() {
        return leftClaw0.getPosition();
    }

    public double getRPos() {
        return rightClaw2.getPosition();
    }

    public void setPos(double position) {
        rightClaw2.setPosition(position);
    }

    public void teleop(Gamepad gamepad1, Gamepad gamepad2) {

        if (leftC == 0) {
            if (gamepad2.dpad_left && Utilities.withinBounds(getLeftAngle(), Constants.ClawConstants.openAngle, 1)) {
                closeLClaw();
                leftC++;
            } else if (gamepad2.dpad_left) {
                openLClaw();
                leftC++;
            }
        } else if (!gamepad2.dpad_left) {
            leftC = 0;
        }

        if (rightC == 0) {
            if (gamepad2.dpad_right && Utilities.withinBounds(getRightAngle(), Constants.ClawConstants.openAngle, 1)) {
                closeRClaw();
                rightC++;
            } else if (gamepad2.dpad_right) {
                openRClaw();
                rightC++;
            }

        } else if (!gamepad2.dpad_right) {
            rightC = 0;
        }

        if (gamepad1.left_bumper) {
            closeRClaw();
            closeLClaw();
        } else if (gamepad1.right_bumper) {
            openRClaw();
            openLClaw();
        }


    }

    public void openLClaw() {
        setLeftAngle(Constants.ClawConstants.openAngle);
    }

    public void closeLClaw() {
        setLeftAngle(Constants.ClawConstants.closedAngle);
    }

    public void openRClaw() {
        setRightAngle(Constants.ClawConstants.openAngle);
    }

    public void closeRClaw() {
        setRightAngle(Constants.ClawConstants.closedAngle);
    }

    public void periodic(Telemetry telemetry) {
        telemetry.addLine("Claw:");
        telemetry.addLine("L Claw Angle: " + getLeftAngle());
        telemetry.addLine("R Claw Angle: " + getRightAngle());
        telemetry.addLine("L Claw Pos: " + getLPos());
        telemetry.addLine("R Claw Pos: " + getRPos());
    }
}