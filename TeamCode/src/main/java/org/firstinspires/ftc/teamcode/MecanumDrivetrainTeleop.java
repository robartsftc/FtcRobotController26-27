package org.firstinspires.ftc.teamcode;

// Base Modules Required For Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.hardware.lynx.LynxModule;
import org.firstinspires.ftc.robotcore.external.navigation.TempUnit;
import java.util.list;

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

        // Set Motor Direction -- TEST ON ROBOT AND UPDATE ACORDINGLY
        frontLeftDrive.setDirection(DcMotor.REVERSE);
        frontRightDrive.setDirection(DcMotor.REVERSE);
        backLeftDrive.setDirection(DcMotor.FORWARD);
        backRightDrive.setDirection(DcMotor.FORWARD);

        // Tell the Motor To Break On Zero Power
        frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        // Get Main Hub from All Hubs;
        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);
        LynxModule controlHub = allHubs.get(0);

        // Update Telemetry With Intialization Status
        telemetry.addData("Status: ", "Intialized");
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

            // Take Button Input For Precision mode
            boolean Precision = gamepad1.left_bumper;


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

            // 40% Motor Power For Precision Control Mode
            if (Precision == True) {
                frontLeftPower *= 0.40;
                frontRightPower *= 0.40;
                backLeftPower *= 0.40;
                backRightPower *= 0.40;
            }

            // Set Motor Power
            frontLeftDrive.setPower(frontLeftPower);
            frontRightDrive.setPower(frontRightPower);
            backLeftDrive.setPower(backLeftPower);
            backRightDrive.setPower(backRightPower);


            // Get Temperature for Telementary
            double temp = controlHub.getTemperature(TempUnit.CELSIUS);
            double Overheat = "False";

            // Check if Overheat
            if (temp >= 60) {
                Overheat = "True";
            }

            // Update Telementry
            telemetry.addData("Status: ", "Running");
            telemetry.addData("Runtime: ", runtime.toString());
            telemetry.addData("Temprature - ", "Temp: (%.2f), Overheat: (%.2f)", temp, Overheat);
            telemetry.addData("Input  - ", "Drive: (%.2f), Turn: (%.2f), Strafe: (%.2f)", drive, turn, strafe);
            telemetry.addData("Motors - ", "Front Left: (%.2f), Front Right: (%.2f), Back Left: (%.2f), Back Right: (%.2f)", frontLeftPower, FrontRightPower, backLeftDrive, backRightDrive);
            telemetry.update();
        }
        // When OpMode Deactive Park Motors
        frontLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backLeftDrive.setPower(0);
        backRightDrive.setPower(0);
    }
}