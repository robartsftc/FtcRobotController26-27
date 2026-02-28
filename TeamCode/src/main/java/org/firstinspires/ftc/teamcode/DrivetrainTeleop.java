// Import The Required Packages in order for Robot control
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.hardware.lynx.LynxModule;
import org.firstinspires.ftc.robotcore.external.navigation.TempUnit;
import java.util.List;

// Define OP Mode
@TeleOp(name="DriveTrain TeleOP", group="TeleOp")
public class DrivetrainTeleop extends LinearOpMode {
    // Declare OpMode Members
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;

    @Override
    public void runOpMode() {
        // Report Ready Over Telemetry
        telemetry.addData("Status:", "Initialized");
        telemetry.update();

        // Map Motors to Robot Configuration
        leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");

        // Set Drive Motor Direction.
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);

        // Set Motors to Break on Zero Power
        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        // Get Main Hub from All Hubs;
        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);
        LynxModule controlHub = allHubs.get(0);

        // Wait for Driver To Start
        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            // Define Motor Power Variables
            double leftPower;
            double rightPower;

            // Get Joystick Input Values 
            double drive = -gamepad1.left_stick_y;
            double turn = gamepad1.left_stick_x;

            // Get Button State
            boolean PrecisionMode = gamepad1.left_bumper;

            // Santitize the Input Values and calculate Drive Motor Power. 
            leftPower    = Range.clip(drive + turn, -1.0, 1.0);
            rightPower   = Range.clip(drive - turn, -1.0, 1.0);


            // Precision Control Mode (Limits Drive Motors to 40% Power)
            if (PrecisionMode) {
                leftPower = leftPower * 0.4;
                rightPower = rightPower * 0.4;
            }
            
            // Apply Power to the DriveTrain based on previously calculated values
            leftDrive.setPower(leftPower);
            rightDrive.setPower(rightPower);

            // Get Temperature for Telementary
            double temp = controlHub.getTemperature(TempUnit.CELSIUS);
            String Overheat = "False";

            // Check if Overheat
            if (temp >= 60) {
                Overheat = "True";
            }

            // Update Telementary Data with Runtime, Motor Power, and Joystic Values
            telemetry.addData("Status: ", "Running");
            telemetry.addData("Runtime: ", runtime.toString());
            telemetry.addData("Temprature - ", "Temp: (%.2f), Overheat: (%.2f)", temp, Overheat);
            telemetry.addData("Input  - ", "Drive: (%.2f), Turn: (%.2f)", drive, turn);
            telemetry.addData("Motors - ", "Left: (%.2f), Right: (%.2f)", leftPower, rightPower);
            telemetry.update();
        }

        // Park Motors
        leftDrive.setPower(0);
        rightDrive.setPower(0);

    }
}