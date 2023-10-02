package org.firstinspires.ftc.teamcode.subsystems;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import static org.firstinspires.ftc.teamcode.Constants.VisionConstants;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;

public class Vision {

    WebcamName camera;

    AprilTagProcessor tagProcessor;
    VisionPortal visionPortal;

    public Vision(HardwareMap hardwareMap) {
        camera = hardwareMap.get(WebcamName.class, VisionConstants.webcam);

        tagProcessor = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                .build();

        visionPortal = new VisionPortal.Builder()
                .addProcessor(tagProcessor)
                .setCamera(camera)
                .setCameraResolution(new Size(640, 480))
                .build();
    }

    public boolean hasTag() {
        return tagProcessor.getDetections().size() > 0;
    }

    public ArrayList<AprilTagDetection> getTags() {
        return hasTag() ? tagProcessor.getDetections() : new ArrayList<>();
    }

    public boolean hasTagID(int tagID) {

        for (AprilTagDetection tag : getTags()) {
            if (tag.id == tagID) {
                return true;
            }
        }

        return false;
    }

    public AprilTagPoseFtc getPose() {
        return getTags().get(0).ftcPose;
    }

    public void periodic(Telemetry telemetry) {
        if (hasTag()) {
            telemetry.addLine("Vision:");
            telemetry.addLine("Has Tag: " + hasTag());
        }
    }
}
