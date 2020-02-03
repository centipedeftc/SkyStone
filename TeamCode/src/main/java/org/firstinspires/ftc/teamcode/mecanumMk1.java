package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name = "Carrot")
public class mecanumMk1 extends OpMode {


    /**
     * Created by duecadj on 9/7/19.
     */


        DcMotor frontLeft;
        DcMotor frontRight;
        DcMotor backRight;
        DcMotor backLeft;

        @Override
        public void init() {
            frontLeft = hardwareMap.dcMotor.get("FL");
            frontRight = hardwareMap.dcMotor.get("FR");
            backLeft = hardwareMap.dcMotor.get("BL");
            backRight = hardwareMap.dcMotor.get("BR");
            frontRight.setDirection(DcMotor.Direction.REVERSE);
            backRight.setDirection(DcMotor.Direction.REVERSE);


        }


        @Override
        public void loop() {
//Drive train --
            double x = gamepad1.left_stick_x;
            double y = -gamepad1.left_stick_y;
            double z = gamepad1.right_trigger-gamepad1.left_trigger;

            double fL = y+x+z;
            double fR = y-x-z;
            double bL = y-x+z;
            double bR = y+x-z;

            frontRight.setPower(fR);
            frontLeft.setPower(fL);
            backRight.setPower(bR);
            backLeft.setPower(bL);
//--
        }
    }



