package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.Arm;
import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.Climber;
import org.firstinspires.ftc.teamcode.subsystems.Mecanum;
import org.firstinspires.ftc.teamcode.subsystems.Vision;
import org.firstinspires.ftc.teamcode.subsystems.Wrist;

@TeleOp(name = Constants.OpModes.teleop, group = Constants.OpModes.linearOp)
public class RobotTeleOp extends LinearOpMode {

    Mecanum s_Drivetrain;

        Arm s_Arm;
        Climber s_Climber;
        Wrist s_Wrist;
        Claw s_Claw;
        //Vision s_Vision;

    private ElapsedTime runtime = new ElapsedTime();

    boolean slomode = false;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        s_Drivetrain = new Mecanum(hardwareMap);

        try {
            s_Arm = new Arm(hardwareMap);
            s_Climber = new Climber(hardwareMap);
            s_Claw = new Claw(hardwareMap);
            s_Wrist = new Wrist(hardwareMap);
            //s_Vision = new Vision(hardwareMap);
        } catch (Exception e) {

        }
        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            if (gamepad2.left_bumper) {
                slomode = true;
            } else if (gamepad2.right_bumper) {
                slomode = false;
            }


            s_Drivetrain.teleop(gamepad1, slomode);
            try {
                s_Arm.teleop(gamepad1);
                s_Claw.teleop(gamepad1, gamepad2);
                s_Wrist.teleop(gamepad1, gamepad2, s_Arm.atWSetpoint());
                s_Climber.teleop(gamepad1);
            } catch (Exception e) {

            }


            s_Drivetrain.periodic(telemetry);
            try {
                s_Arm.periodic(telemetry);
                s_Climber.periodic(telemetry);
                s_Wrist.periodic(telemetry);
                s_Claw.periodic(telemetry);
                //s_Vision.periodic(telemetry);
                telemetry.addData("Slo mode", slomode);
            } catch (Exception e) {

            }
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }

    }

}