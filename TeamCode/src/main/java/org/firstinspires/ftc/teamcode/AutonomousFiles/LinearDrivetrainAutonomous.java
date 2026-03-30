package org.firstinspires.ftc.teamcode.AutonomousFiles;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous
public class LinearDrivetrainAutonomous extends OpMode {

    // This declares the 2 motors needed
    private DcMotor leftMotor;
    private DcMotor rightMotor;


    private AprilTagCameraVision aprilTagVision;

    private boolean isBlueTeam = true;  // true = Blue (Tag 20), false = Red (Tag 24)

    @Override
    public void init() {

        //Mapping motors
        leftMotor = hardwareMap.get(DcMotor.class, "left_motor");
        rightMotor = hardwareMap.get(DcMotor.class, "right_motor");

        //Set motor directions
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        rightMotor.setDirection(DcMotor.Direction.FORWARD);

        //Call the April tag detector file
        aprilTagVision = new AprilTagCameraVision();
        aprilTagVision.init(hardwareMap, telemetry);

    }

    public void drive(double forward, double rotate) {
        //Declare the power equations
        double leftPower = forward + rotate;
        double rightPower = forward - rotate;

        //Cap power and speed
        double maxPower = 1.0;
        double maxSpeed = 1.0;
        maxPower = Math.max(maxPower, Math.abs(leftPower));
        maxPower = Math.max(maxPower, Math.abs(rightPower));

        //Set motor power
        leftMotor.setPower(maxSpeed * (leftPower / maxPower));
        rightMotor.setPower(maxSpeed * (rightPower / maxPower));


    }

    @Override
    public void loop() {

        aprilTagVision.update();

        int targetTagId = isBlueTeam ? 20 : 24;

        // Show all tags except the target tag
        for (AprilTagDetection tag : aprilTagVision.getDetectedTags()) {
            if (tag.id != targetTagId) {
                telemetry.addLine("Seen Tag ID: " + tag.id);
            }
        }

        AprilTagDetection targetTag = aprilTagVision.getTagBySpecificId(targetTagId);

        if (targetTag != null) {

            telemetry.addLine("Target Tag Found: " + targetTagId);
            aprilTagVision.displayDetectionTelemetry(targetTag);

            double range = targetTag.ftcPose.range;
            double bearing = targetTag.ftcPose.bearing;
            double yaw = targetTag.ftcPose.yaw;

            double forward = 0;
            double rotate = 0;

            // Desired distance from tag (cm)
            double targetDistance = 30;

            // Forward/backward control
            forward = (range - targetDistance) * 0.02;

            // Rotate to face tag
            rotate = (bearing + yaw) * 0.01;

            drive(forward, rotate);

        } else {

            telemetry.addLine("Target Tag NOT Found");

            // Stop robot if tag not visible
            drive(0, 0.2);
        }

        telemetry.update();
    }

}