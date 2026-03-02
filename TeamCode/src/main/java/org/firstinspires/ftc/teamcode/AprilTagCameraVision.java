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
    // Processes camera frames and detects April Tags
    private AprilTagProcessor aprilTagProcessor;
    // Manages the camera and vision processors
    private VisionPortal visionPortal;
    // Stores the list of currently detected April Tags
    private List<AprilTagDetection> detectedTags = new ArrayList<>();
    // Used to send messages to the Driver Hub screen
    private Telemetry telemetry;

    // Called once to set up the camera and april tag processor
    public void init(HardwareMap hwMap, Telemetry telemetry){
        // Save telemetry so we can use it in other methods
        this.telemetry = telemetry;

        // Build the april tag processor with display options
        aprilTagProcessor = new AprilTagProcessor.Builder()
                .setDrawTagID(true)        // Show tag ID on camera stream
                .setDrawTagOutline(true)   // Draw outline around detected tag
                .setDrawAxes(true)         // Draw axes on tag
                .setDrawCubeProjection(true) // Draw 3D cube on tag
                .setOutputUnits(DistanceUnit.CM, AngleUnit.DEGREES) // Use CM and degrees
                .build();

        // Build the vision portal which connects the camera to the processor
        VisionPortal.Builder builder = new VisionPortal.Builder();
        // Set the camera to use by looking it up in the hardware config
        builder.setCamera(hwMap.get(WebcamName.class, "Webcam 1"));
        // Set camera resolution to 640x480
        builder.setCameraResolution(new Size(640, 480));
        // Attach the april tag processor to the vision portal
        builder.addProcessor(aprilTagProcessor);

        // Finalize and create the vision portal
        visionPortal = builder.build();
    }

    // Call this every loop to refresh the list of detected tags
    public void update(){
        detectedTags = aprilTagProcessor.getDetections();
    }

    // Returns the full list of currently detected April Tags
    public List<AprilTagDetection> getDetectedTags(){
        return detectedTags;
    }

    public void displayDetectionTelemetry(AprilTagDetection detectedId){
        if (detectedId == null){return;}
        if (detectedId.metadata != null) {
            telemetry.addLine(String.format("\n==== (ID %d) %s", detectedId.id, detectedId.metadata.name));
            telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detectedId.ftcPose.x, detectedId.ftcPose.y, detectedId.ftcPose.z));
            telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detectedId.ftcPose.pitch, detectedId.ftcPose.roll, detectedId.ftcPose.yaw));
            telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detectedId.ftcPose.range, detectedId.ftcPose.bearing, detectedId.ftcPose.elevation));
        } else {
            telemetry.addLine(String.format("\n==== (ID %d) Unknown", detectedId.id));
            telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detectedId.center.x, detectedId.center.y));
        }
    }

    // Searches the detected tags for one with a specific ID
    // Returns the tag if found, or null if not found
    public AprilTagDetection getTagBySpecificId(int id){
        // Loop through every detected tag
        for (AprilTagDetection detection : detectedTags){
            // If this tag's ID matches what we're looking for, return it
            if (detection.id == id){
                return detection;
            }
        }
        // No tag with that ID was found
        return null;
    }

    // Shuts down the vision portal when autonomous is done
    public void stop() {
        // Only close if the vision portal was actually created
        if (visionPortal != null){
            visionPortal.close();
        }
    }
}