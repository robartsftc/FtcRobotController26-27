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
        telemetry.addData("Status", "Initialized")
        telemetry.update();

        // Init Motors
        leftDrive = hardwareMap.get(DcMotor.class, "left_drive")
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive")

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            
        }
    }
}