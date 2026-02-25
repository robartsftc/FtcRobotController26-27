// Import The Required Packages in order for Robot control
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

// Define OP Mode
@TeleOP(name="DriveTrain TeleOP", group="Linear OpMode")
public class BasicOpMode_Linear extends LinearOpMode {
    // Declare OpMode Members
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private Dcmotor rightDrive = null;

    @Override
    public void runOpMode() {
        // Report Ready
        telemetry.addData("Status:", "Initialized");
        telemetry.update();

        // Init Motors
        leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");

        // Set Motor Direction 
        leftDrive.setDirection(DcMotor.direction.REVERSE);
        rightDrive.setDirection(DcMotor.direction.FORWARD)

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            // Define Motor Power Variables
            double leftPower;
            double rightPower;

            // Some Math To Calculate drive
            double drive = -gamepad1.left_stick_y;
            double turn = gamepad1.left_stick_x;


            leftPower    = Range.clip(drive + turn, -1.0, 1.0);
            rightPower   = Range.clip(drive - turn, -1.0, 1.0);

            leftDrive.setPower(leftPower);
            rightDrive.setPower(rightDrive);


            // Update Telementary
            telemetry.addData("Status: ", "Running");
            telemetry.addData("Runtime: ", runtime.toString());
            telemetry.addData("Motors - ", "Left: (%.2f), Right: (%.2f)", leftPower, rightPower);
            telemetry.addData("Input  - ", "Drive: (%.2f), Turn: (%.2f)", drive, turn);
            telemetry.update();
        }
    }
}