package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;


//november 14 2019
@TeleOp(name = "Melee")
    public class Neutral extends OpMode {


    BNO055IMU imu;
    Orientation angles;

  //Drivetrain
    double[] driveValues;
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backRight;
    DcMotor backLeft;
    cartesianConverter CC;
  //Collector
    DcMotor leftCollector;
    DcMotor rightCollector;
    CRServo leftServo;
    CRServo rightServo;

  //Deposit
    DcMotor liftMotorY;
    DcMotor liftMotorX;
    Servo swivel;
    Servo grab;
  //other
    Servo plateLeft;
    Servo plateRight;
    int plate = 1;

    @Override
    public void init() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";

        imu = hardwareMap.get(BNO055IMU.class,"imu");
        imu.initialize(parameters);

    //DriveTrain
        frontLeft = hardwareMap.dcMotor.get("FL");
        frontRight = hardwareMap.dcMotor.get("FR");
        backLeft = hardwareMap.dcMotor.get("BL");
        backRight = hardwareMap.dcMotor.get("BR");
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

    //Collector
        CC = new cartesianConverter();
        //motors
        leftCollector = hardwareMap.dcMotor.get("LC");
        rightCollector = hardwareMap.dcMotor.get("RC");
        rightCollector.setDirection(DcMotor.Direction.REVERSE);
        //servos
        leftServo = hardwareMap.crservo.get("LS");
        rightServo = hardwareMap.crservo.get("RS");
        rightServo.setDirection(CRServo.Direction.REVERSE);

    //Deposit
        //Lift
        liftMotorY = hardwareMap.dcMotor.get("LMY");
        liftMotorX = hardwareMap.dcMotor.get("LMX");
        swivel = hardwareMap.servo.get("S");
        grab = hardwareMap.servo.get("G");
    //Other
        plateLeft = hardwareMap.servo.get("PL");
        plateRight = hardwareMap.servo.get("PR");

    }

    @Override
    public void loop() {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
//Drive train --
        driveValues = CC.convert(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_trigger- gamepad1.left_trigger, angles.firstAngle);
        frontLeft.setPower(driveValues[0]);
        frontRight.setPower(driveValues[1]);
        backLeft.setPower(driveValues[2]);
        backRight.setPower(driveValues[3]);
//Collector
        if(gamepad1.a){
             leftCollector.setPower(1);
             leftServo.setPower(1);
             rightCollector.setPower(1);
             rightServo.setPower(1);
        }
         else if(gamepad1.b){
             leftCollector.setPower(-1);
             leftServo.setPower(-1);
             rightCollector.setPower(-1);
             rightServo.setPower(-1);
        }
         else{
             leftCollector.setPower(0);
             leftServo.setPower(0);
             rightCollector.setPower(0);
             rightServo.setPower(0);
        }
//Deposit
            liftMotorY.setPower(gamepad2.left_stick_y);
            if(gamepad2.left_bumper)
                liftMotorX.setPower(1);
            else if(gamepad2.right_bumper)
                liftMotorX.setPower(-1);
            else
                liftMotorX.setPower(0);


            if(gamepad2.b)
            swivel.setPosition(.3);
            else
            swivel.setPosition(.7);

            if(gamepad2.a)
            grab.setPosition(.45);
            else
            grab.setPosition(0);


//Other
            if(gamepad1.x)
               plate = 1;
            else
                plate = 0;

            plateLeft.setPosition(plate);
            plateRight.setPosition(plate);

        //angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("heading ", -angles.firstAngle+180);
        telemetry.addData("roll ", -angles.secondAngle+180);
        telemetry.addData("pitch ", -angles.thirdAngle+180);
        telemetry.addData("leftJoyBX", gamepad2.right_stick_x);
        telemetry.update();






    }
}
