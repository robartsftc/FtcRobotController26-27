package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.List;


public class AprilTagCameraVision {
    private AprilTagProcessor aprilTagProcessor;
    // Manages the camera and vision processors
    private VisionPortal visionPortal;
    // Stores the list of currently detected April Tags
    private List<AprilTagDetection> detectedTags = new ArrayList<>();
    // Used to send messages to the Driver Hub screen
    private Telemetry telemetry;

    public void init(HardwareMap hwMap, Telemetry telemetry){
        this.telemetry = telemetry;

        aprilTagProcessor = new AprilTagProcessor.Builder()
                .setDrawTagID(true)        // Show tag ID on camera stream
                .setDrawTagOutline(true)   // Draw outline around detected tag
                .setDrawAxes(true)         // Draw axes on tag
                .setDrawCubeProjection(true) // Draw 3D cube on tag
                .setOutputUnits(DistanceUnit.CM, AngleUnit.DEGREES) // Use CM and degrees
                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(hwMap.get(WebcamName.class, "Webcam 1"));
        // Set camera resolution to 640x480
        builder.setCameraResolution(new Size(640, 480)); // fixed: was "640 x 480"
    }
}