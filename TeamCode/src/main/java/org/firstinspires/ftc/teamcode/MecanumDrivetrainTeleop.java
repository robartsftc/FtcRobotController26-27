package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Mecanum Drivetrain Teleop", group="TeleOP")
public class MecanumDrivetrainTeleOP extends LinearOpMode {
    //Define Motors 
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor backRightDrive = null;

    public void runOpMode() {
        // Map the Motors to there respective Hardware 
        frontLeftDrive = hardwareMap.get(DcMotor.class, "front_left_drive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "front_right_drive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "back_left_drive");
        backRightDrive = hardwareMap.get(DcMotor.class, "back_right_drive");

        // Update Telemetry With Intialization Status
        telemetry.addData("Status: ". "Intialized");
        telemetry.update();

        // Reset RunTime and Wait for Game Start
        waitForStart();
        rumtime.reset();


        // Run Op Mode
        while (opModeIsActive()) {
            // Setup for Later
            double max;

            // Take Joystick Input and Map to a Variable
            double drive = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;
            double strafe = gamepad1.left_stick_x;

            // Calculate Motor Power Based Of Joystick input 
            frontLeftPower = drive + strafe + turn;
            frontRightPower = drive - strafe - turn;
            backLeftPower = drive - strafe + turn;
            backRightPower = drive + strafe - turn;

            // Normalise Motor Power across the Drivetrain to maintain Intended Direction
            max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
            max = Math.max(max, Math.abs(backLeftPower));
            max = Math.max(max, Math.abs(backRightPower));

            // Clip Motor Power to Bellow 1
            if (max > 1.0) {
                frontLeftPower /= max;
                frontRightPower /= max;
                backLeftDrive /= max;
                backrightDrive /= max;
            }


            // Set Motor Power
            frontLeftDrive.setPower(frontLeftPower);
            frontRightDrive.setPower(frontRightPower);
            backLeftDrive.setPower(backLeftPower);
            backRightDrive.setPower(backRightPower);


            // Update Telementry
            telemetry.addData("Status: ", "Running");
            telemetry.addData("Runtime: ", runtime.toString();
            telemetry.addData("Input  - ", "Drive: (%.2f), Turn: (%.2f), Strafe: (%.2f)", drive, turn, strafe);
            telemetry.addData("Motors - ", "Front Left: (%.2f), Front Right: (%.2f), Back Left: (%.2f), Back Right: (%.2f)", frontLeftPower, FrontRightPower, backLeftDrive, backRightDrive);
            telemetry.update();
        }
    }
}