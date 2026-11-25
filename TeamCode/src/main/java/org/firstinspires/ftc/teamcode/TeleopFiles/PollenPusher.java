package org.firstinspires.ftc.teamcode.TeleopFiles;


import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class PollenPusher {
    private Servo pusherServo;


    // Map to hardware
    public void init(HardwareMap hwMap){
        pusherServo = hwMap.get(Servo.class, "pusherServo");
        pusherServo.setPosition(0.0);   // Start at Normal Position

    }

// Move servo to the correct angle
    public void setPusherServo(double angle){
        pusherServo.setPosition(angle);
    }
}
