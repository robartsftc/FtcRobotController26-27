/* Hello, all the code in this file was coded by Tejas Heejebu from RoBarts (Team 27941)
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous
public class AprilTagAutonomousTest extends OpMode {

    private AprilTagCameraVision aprilTagVision;
    private boolean isBlueTeam = true;  // true = Blue (Tag 20), false = Red (Tag 24)

    @Override
    public void init() {
        //Call the April tag detector file
        aprilTagVision = new AprilTagCameraVision();
        aprilTagVision.init(hardwareMap, telemetry);
    }


    @Override
    public void loop() {

        aprilTagVision.update();

        int targetTagId = isBlueTeam ? 20 : 24;

        AprilTagDetection targetTag = aprilTagVision.getTagBySpecificId(targetTagId);

        if (targetTag != null) {

            telemetry.addLine("Target Tag Found: " + targetTagId);
            aprilTagVision.displayDetectionTelemetry(targetTag);

        } else {

            telemetry.addLine("Target Tag NOT Found");


        }

        telemetry.update();
    }
}