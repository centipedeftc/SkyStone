package org.firstinspires.ftc.teamcode;



import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "encoderz")
public class encoderTest extends OpMode {

        DcMotor testMotor;

    @Override
    public void init() {

        testMotor = hardwareMap.dcMotor.get("TM");
    }

    @Override
    public void loop() {

        telemetry.addData("testMotor",  testMotor.getCurrentPosition());

        testMotor.getCurrentPosition();

        if (gamepad1.a){
            testMotor.setPower(.3);}
        else if (gamepad1.b){
            testMotor.setPower(-.3);}
        else
            testMotor.setPower(0);}




    }



