/* Hello, all the code in this file was coded by Tejas Heejebu from RoBarts (Team 27941)
This code is meant for the Rev Robotics mecanum wheels drivertrain.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous
public class Autonomous extends OpMode {
    // This declares the four motors needed
    private DcMotor frontLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backLeftMotor;
    private DcMotor backRightMotor;

    private IMU imu;
    private AprilTagCameraVision aprilTagVision;

    private boolean isBlueTeam = true;  // true = Blue (Tag 20), false = Red (Tag 24)

    @Override
    public void init() {

        //Mapping motors
        frontLeftMotor = hardwareMap.get(DcMotor.class, "front_left_motor");
        frontRightMotor = hardwareMap.get(DcMotor.class, "front_right_motor");
        backLeftMotor = hardwareMap.get(DcMotor.class, "back_left_motor");
        backRightMotor = hardwareMap.get(DcMotor.class, "back_right_motor");

        //Set motor directions
        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotor.Direction.FORWARD);
        backRightMotor.setDirection(DcMotor.Direction.FORWARD);

        //Set IMU
        imu = hardwareMap.get(IMU.class, "imu");

        //Set orientation
        RevHubOrientationOnRobot RevOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD);

        //Initialise imu
        imu.initialize(new IMU.Parameters(RevOrientation));

        //Call the April tag detector file
        aprilTagVision = new AprilTagCameraVision();
        aprilTagVision.init(hardwareMap, telemetry);
    }

    public void drive(double forward, double strafe, double rotate){

        //Declare the power equations
        double frontLeftPower = forward + strafe + rotate;
        double backLeftPower = forward - strafe + rotate;
        double frontRightPower = forward - strafe - rotate;
        double backRightPower = forward + strafe - rotate;

        //Cap power and speed
        double maxPower = 1.0;
        double maxSpeed = 1.0;
        maxPower = Math.max(maxPower, Math.abs(frontLeftPower));
        maxPower = Math.max(maxPower, Math.abs(backLeftPower));
        maxPower = Math.max(maxPower, Math.abs(frontRightPower));
        maxPower = Math.max(maxPower, Math.abs(backRightPower));

        //Set motor power
        frontLeftMotor.setPower(maxSpeed * (frontLeftPower / maxPower));
        backLeftMotor.setPower(maxSpeed * (backLeftPower / maxPower));
        frontRightMotor.setPower(maxSpeed * (frontRightPower / maxPower));
        backRightMotor.setPower(maxSpeed * (backRightPower / maxPower));


    }


    @Override
    public void loop() {

        aprilTagVision.update();

        int targetTagId = isBlueTeam ? 20 : 24;

        AprilTagDetection targetTag = aprilTagVision.getTagBySpecificId(targetTagId);

        if (targetTag != null) {

            telemetry.addLine("Target Tag Found: " + targetTagId);
            aprilTagVision.displayDetectionTelemetry(targetTag);

            double range = targetTag.ftcPose.range;
            double bearing = targetTag.ftcPose.bearing;
            double yaw = targetTag.ftcPose.yaw;

            double forward = 0;
            double strafe = 0;
            double rotate = 0;

            // Desired distance from tag (cm)
            double targetDistance = 30;

            // Forward/backward control
            forward = (range - targetDistance) * 0.02;

            // Strafe to center the tag
            strafe = bearing * 0.01;

            // Rotate to face tag
            rotate = yaw * 0.01;

            drive(forward, strafe, rotate);

        } else {

            telemetry.addLine("Target Tag NOT Found");

            // Stop robot if tag not visible
            drive(0,0,0.2);
        }

        telemetry.update();
    }
}