package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import java.util.Locale;

@Autonomous(name = "AutonomousBlueStraight")
public class AutonomousBlueStraight extends OpMode {
    final int FR = 0;
    final int FL = 1;
    final int BL = 2;
    final int BR = 3;
    final int LMY = 4;
    int stage = -1;
    DcMotor Motors[]= new DcMotor[8];
    Servo Servos[] = new Servo[2];
    final int R = 0;
    final int L = 1;

    private ElapsedTime runtime = new ElapsedTime();
    double scale = .75;
    boolean cartesian = false;
    double pivotPower = 0;
    double extPower = 0;
    double pivotScale = .7;
    boolean stayExt = false;




    @Override
    public void init() {
        Motors[FR] = hardwareMap.get(DcMotor.class,"FR");
        Motors[FL] = hardwareMap.get(DcMotor.class,"FL");
        Motors[BR] = hardwareMap.get(DcMotor.class, "BR");
        Motors[BL] = hardwareMap.get(DcMotor.class, "BL");
        Motors[LMY] = hardwareMap.get(DcMotor.class, "LMY");
        Servos[R] = hardwareMap.get(Servo.class, "R");
        Servos[L] = hardwareMap.get(Servo.class, "L");
        Servos[R].setPosition(0);
        Servos[L].setPosition(1);
        Motors[BL].setDirection(DcMotorSimple.Direction.REVERSE);
        Motors[FR].setDirection(DcMotorSimple.Direction.REVERSE);
        Motors[BR].setDirection(DcMotorSimple.Direction.REVERSE);
        Motors[FR].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motors[FL].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motors[BR].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motors[BL].setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Motors[lift].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


    }
    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.startTime();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        switch(stage) {
            case -1:
                resetEncoders();
                resetLift();
                stage++;
                break;
            case 0:
                if (Motors[FL].getCurrentPosition() <= -400) {
                    telemetry.addData("FL", Motors[FL].getCurrentPosition());
                    resetEncoders();
                    stage++;
                }
                else {
                    Motors[FR].setPower(0.25);
                    Motors[FL].setPower(-0.25);
                    Motors[BR].setPower(-0.25);
                    Motors[BL].setPower(0.25);
                }
                break;
            case 1:
                if (Motors[FL].getCurrentPosition() <= -900) {
                    telemetry.addData("FL", Motors[FL].getCurrentPosition());
                    stage++;
                }
                else {
                    Motors[FR].setPower(-0.25);
                    Motors[FL].setPower(-0.25);
                    Motors[BR].setPower(-0.25);
                    Motors[BL].setPower(-0.25);
                }
                break;
            case 2:
                runtime.reset();
                stage++;
                break;
            case 3:
                if (runtime.milliseconds()>1000) {
                    stage++;
                    runtime.reset();
                }
                else {
                    stop();
                    Servos[R].setPosition(1);
                    Servos[L].setPosition(0);
                }
                break;
            case 4:
                if (runtime.milliseconds()>1850) {
                    runtime.reset();
                    resetEncoders();
                    stage++;
                }
                else {
                    moveBackwards(0.25);
                }
                break;
            case 5:
                if (Motors[FL].getCurrentPosition() <= -30) {
                    telemetry.addData("FL", Motors[FL].getCurrentPosition());
                    stop();
                    stage++;
                }
                else {
                    Motors[FR].setPower(-0.15);
                    Motors[FL].setPower(-0.15);
                    Motors[BR].setPower(-0.15);
                    Motors[BL].setPower(-0.15);
                }
                break;
            case 6:
                runtime.reset();
                stage++;
                break;
            case 7:
                if (runtime.milliseconds()>1000) {
                    resetEncoders();
                    stage++;
                }
                Servos[R].setPosition(0);
                Servos[L].setPosition(1);
                break;
            case 8:
                if (Motors[FL].getCurrentPosition() <= -1200) {
                    telemetry.addData("FL", Motors[FL].getCurrentPosition());
                    stage++;
                    stop();
                    resetEncoders();
                }
                else {
                    Motors[FR].setPower(0.20);
                    Motors[FL].setPower(-0.25);
                    Motors[BR].setPower(-0.25);
                    Motors[BL].setPower(0.20);
                }
                break;
            case 9:
                if (Motors[FL].getCurrentPosition() <= -300) {
                    telemetry.addData("FL", Motors[FL].getCurrentPosition());
                    stop();
                    resetEncoders();
                    stage++;
                    runtime.reset();
                }
                else {
                    Motors[FR].setPower(-0.25);
                    Motors[FL].setPower(-0.25);
                    Motors[BR].setPower(-0.25);
                    Motors[BL].setPower(-0.25);
                }
                break;
            case 10:

                if (Motors[FL].getCurrentPosition() <= -550) {
                    telemetry.addData("FL", Motors[FL].getCurrentPosition());
                    stop();
                    stage++;
                }
                else {
                    Motors[FR].setPower(0.25);
                    Motors[FL].setPower(-0.25);
                    Motors[BR].setPower(0.25);
                    Motors[BL].setPower(-0.25);
                }
                break;
            case 11:
                if (Motors[LMY].getCurrentPosition() <= -1000) {
                    Motors[LMY].setPower(0);
                    resetEncoders();
                    runtime.reset();
                    stage++;
                }
                else {
                    Motors[LMY].setPower(-0.5);
                }
                break;
            case 12:
                if (runtime.milliseconds()>1000) {
                    stop();
                    resetEncoders();
                    stage++;
                }
                else {
                    stop();
                }
                break;
            case 13:
                if (Motors[FL].getCurrentPosition() >= 100) {
                    stop();
                    telemetry.addData("FL", Motors[FL].getCurrentPosition());
                    stage++;
                }
                else {
                    Motors[FR].setPower(0.5);
                    Motors[FL].setPower(0.5);
                    Motors[BR].setPower(0.5);
                    Motors[BL].setPower(0.5);
                }
                break;
            case 14:
                if (Motors[FL].getCurrentPosition() <= -100) {
                    stop();
                    telemetry.addData("FL", Motors[FL].getCurrentPosition());
                    stage++;
                }
                else {
                    Motors[FR].setPower(-0.5);
                    Motors[FL].setPower(-0.5);
                    Motors[BR].setPower(-0.5);
                    Motors[BL].setPower(-0.5);
                }
                break;
            case 15:
                if (Motors[LMY].getCurrentPosition() >= -200) {
                    telemetry.addData("LMY", Motors[LMY].getCurrentPosition());
                    Motors[LMY].setPower(0);
                    runtime.reset();
                    resetEncoders();
                    stage++;
                }
                else {
                    Motors[LMY].setPower(0.25);
                }
                break;
            case 16:
                if (runtime.milliseconds()>=1500) {
                    stop();
                    runtime.reset();
                    resetEncoders();
                    stage++;
                }
                else {
                    moveSideways(-0.25);
                }
                break;
            case 17:
                if (runtime.milliseconds()>= 50) {
                    stop();
                    runtime.reset();
                    resetEncoders();
                    stage++;
                }
                else {
                    moveSideways(0.25);
                }
                break;
            case 18:
                if (Motors[FL].getCurrentPosition() >= 650) {
                    stop();
                    telemetry.addData("FL", Motors[FL].getCurrentPosition());
                    stage++;
                }
                else {
                    Motors[FR].setPower(0.25);
                    Motors[FL].setPower(0.25);
                    Motors[BR].setPower(0.25);
                    Motors[BL].setPower(0.25);
                }
                break;



        }
        telemetry.addData("LMY", Motors[LMY].getCurrentPosition());
        telemetry.addData("FL", Motors[FL].getCurrentPosition());
        telemetry.addData("stage", stage);

    }
    public void moveForward(double power){
        Motors[FL].setPower(power);
        Motors[FR].setPower(-0.15*power);
        Motors[BR].setPower(power);
        Motors[BL].setPower(-0.15*power);

    }
    public void latchServos(double position){
        Servos[R].setPosition(position);
        Servos[L].setPosition(position);
    }


    public void moveBackwards(double power){
        Motors[FR].setPower(power);
        Motors[FL].setPower(power);
        Motors[BR].setPower(power);
        Motors[BL].setPower(power);
    }
    public void resetEncoders () {
        Motors[FL].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Motors[FR].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Motors[BR].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Motors[BL].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // Motors[FL].setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Motors[FL].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motors[FR].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motors[BR].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motors[BL].setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }
    public void resetLift () {
        Motors[LMY].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Motors[LMY].setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void stop() {
        Motors [FL].setPower(0);
        Motors [BL].setPower(0);
        Motors [FR].setPower(0);
        Motors [BR].setPower(0);
    }
    public void moveSideways(double power){
        Motors[FR].setPower(power);
        Motors[FL].setPower(-power);
        Motors[BR].setPower(-power);
        Motors[BL].setPower(power);
    }
}

