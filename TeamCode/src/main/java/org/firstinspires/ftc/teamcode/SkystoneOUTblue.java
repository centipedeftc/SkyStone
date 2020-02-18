package org.firstinspires.ftc.teamcode;


import com.disnodeteam.dogecv.detectors.skystone.SkystoneDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

import java.util.Locale;

/*
 * Thanks to EasyOpenCV for the great API (and most of the example)
 *
 * Original Work Copright(c) 2019 OpenFTC Team
 * Derived Work Copyright(c) 2019 DogeDevs
 */
@Autonomous(name = "SkystoneOUTblue", group = "DogeCV")

public class SkystoneOUTblue extends LinearOpMode {
    private OpenCvCamera phoneCam;
    private SkystoneDetector skyStoneDetector;
    final int FR = 0;
    final int FL = 1;
    final int BL = 2;
    final int BR = 3;
    final int LMY = 4;
    final int RC = 5;
    final int LC = 6;
    final int LMX = 7;
    int stage = 0;
    DcMotor Motors[] = new DcMotor[8];
    Servo Servos[] = new Servo[16];
    final int R = 0;
    final int L = 1;
    final int G = 2;
    final int LS = 3;
    final int RS = 4;

    private ElapsedTime runtime = new ElapsedTime();
    double scale = .75;
    boolean cartesian = false;
    double pivotPower = 0;
    double extPower = 0;
    double pivotScale = .7;
    boolean stayExt = false;

    @Override
    public void runOpMode() {

        /*
         * Instantiate an OpenCvCamera object for the camera we'll be using.
         * In this sample, we're using the phone's internal camera. We pass it a
         * CameraDirection enum indicating whether to use the front or back facing
         * camera, as well as the view that we wish to use for camera monitor (on
         * the RC phone). If no camera monitor is desired, use the alternate
         * single-parameter constructor instead (commented out below)
         */
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);

        // OR...  Do Not Activate the Camera Monitor View
        //phoneCam = new OpenCvInternalCamera(OpenCvInternalCamera.CameraDirection.BACK);

        /*
         * Open the connection to the camera device
         */
        phoneCam.openCameraDevice();

        /*
         * Specify the image processing pipeline we wish to invoke upon receipt
         * of a frame from the camera. Note that switching pipelines on-the-fly
         * (while a streaming session is in flight) *IS* supported.
         */
        skyStoneDetector = new SkystoneDetector();
        phoneCam.setPipeline(skyStoneDetector);

        /*
         * Tell the camera to start streaming images to us! Note that you must make sure
         * the resolution you specify is supported by the camera. If it is not, an exception
         * will be thrown.
         *
         * Also, we specify the rotation that the camera is used in. This is so that the image
         * from the camera sensor can be rotated such that it is always displayed with the image upright.
         * For a front facing camera, rotation is defined assuming the user is looking at the screen.
         * For a rear facing camera or a webcam, rotation is defined assuming the camera is facing
         * away from the user.
         */
        phoneCam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);

        /*
         * Wait for the user to press start on the Driver Station
         */
        /*
        Code from here to waitForStart() is the equivalent to int() in OpMode
         */
        runtime.startTime();

        Motors[FR] = hardwareMap.get(DcMotor.class, "FR");
        Motors[FL] = hardwareMap.get(DcMotor.class, "FL");
        Motors[BR] = hardwareMap.get(DcMotor.class, "BR");
        Motors[BL] = hardwareMap.get(DcMotor.class, "BL");
        Motors[LMY] = hardwareMap.get(DcMotor.class, "LMY");
        Motors[RC] = hardwareMap.get(DcMotor.class, "RC");
        Motors[LC] = hardwareMap.get(DcMotor.class, "LC");
        Motors[LMX] = hardwareMap.get(DcMotor.class, "LMX");
        Servos[R] = hardwareMap.get(Servo.class, "R");
        Servos[L] = hardwareMap.get(Servo.class, "L");
        Servos[G] = hardwareMap.get(Servo.class, "G");
        Servos[LS] = hardwareMap.get(Servo.class, "LS");
        Servos[RS] = hardwareMap.get(Servo.class, "RS");
        Servos[R].setPosition(0);
        Servos[L].setPosition(1);
        Motors[BL].setDirection(DcMotorSimple.Direction.REVERSE);
        Motors[FR].setDirection(DcMotorSimple.Direction.REVERSE);
        Motors[BR].setDirection(DcMotorSimple.Direction.REVERSE);
        Motors[FR].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motors[FL].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motors[BR].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motors[BL].setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        while ((opModeIsActive())) {
            /*
             * Send some stats to the telemetry
             */
            telemetry.addData("Stone Position X", skyStoneDetector.getScreenPosition().x);
            telemetry.addData("Stone Position Y", skyStoneDetector.getScreenPosition().y);
            telemetry.addData("Frame Count", phoneCam.getFrameCount());
            telemetry.addData("FPS", String.format(Locale.US, "%.2f", phoneCam.getFps()));
            telemetry.addData("Total frame time ms", phoneCam.getTotalFrameTimeMs());
            telemetry.addData("Pipeline time ms", phoneCam.getPipelineTimeMs());
            telemetry.addData("Overhead time ms", phoneCam.getOverheadTimeMs());
            telemetry.addData("Theoretical max FPS", phoneCam.getCurrentPipelineMaxFps());
            telemetry.update();

            /*
             * NOTE: stopping the stream from the camera early (before the end of the OpMode
             * when it will be automatically stopped for you) *IS* supported. The "if" statement
             * below will stop streaming from the camera when the "A" button on gamepad 1 is pressed.
             */
            if (gamepad1.a) {
                /*
                 * IMPORTANT NOTE: calling stopStreaming() will indeed stop the stream of images
                 * from the camera (and, by extension, stop calling your vision pipeline). HOWEVER,
                 * if the reason you wish to stop the stream early is to switch use of the camera
                 * over to, say, Vuforia or TFOD, you will also need to call closeCameraDevice()
                 * (commented out below), because according to the Android Camera API documentation:
                 *         "Your application should only have one Camera object active at a time for
                 *          a particular hardware camera."
                 *
                 * NB: calling closeCameraDevice() will internally call stopStreaming() if applicable,
                 * but it doesn't hurt to call it anyway, if for no other reason than clarity.
                 *
                 * NB2: if you are stopping the camera stream to simply save some processing power
                 * (or battery power) for a short while when you do not need your vision pipeline,
                 * it is recommended to NOT call closeCameraDevice() as you will then need to re-open
                 * it the next time you wish to activate your vision pipeline, which can take a bit of
                 * time. Of course, this comment is irrelevant in light of the use case described in
                 * the above "important note".
                 */
                phoneCam.stopStreaming();
                //webcam.closeCameraDevice();
            }

            /*
             * The viewport (if one was specified in the constructor) can also be dynamically "paused"
             * and "resumed". The primary use case of this is to reduce CPU, memory, and power load
             * when you need your vision pipeline running, but do not require a live preview on the
             * robot controller screen. For instance, this could be useful if you wish to see the live
             * camera preview as you are initializing your robot, but you no longer require the live
             * preview after you have finished your initialization process; pausing the viewport does
             * not stop running your pipeline.
             *
             * The "if" statements below will pause the viewport if the "X" button on gamepad1 is pressed,
             * and resume the viewport if the "Y" button on gamepad1 is pressed.
             */
            else if (gamepad1.x) {
                phoneCam.pauseViewport();
            } else if (gamepad1.y) {
                phoneCam.resumeViewport();
            }
            switch (stage) {

                case 0:
                    resetEncoders();
                    resetLift();
                    Servos[G].setPosition(0);
                    stage++;
                    break;
                //reset all encoders and the claw
                case 1:
                    if (Motors[FL].getCurrentPosition() >= 300) {
                        MotorEnd();
                        resetEncoders();
                        stage++;
                    } else {
                        Motors[FR].setPower(0.15);
                        Motors[FL].setPower(0.15);
                        Motors[BR].setPower(0.15);
                        Motors[BL].setPower(0.15);
                    }
                    break;
                //move forward to position the block into the collector
                case 2:
                    if (Motors[FL].getCurrentPosition() >= 200) {
                        MotorEnd();
                        resetEncoders();
                        runtime.reset();
                        stage++;
                    } else {
                        Motors[FR].setPower(0.05);
                        Motors[FL].setPower(0.05);
                        Motors[BR].setPower(0.05);
                        Motors[BL].setPower(0.05);
                        Motors[LC].setPower(1);
                        Motors[RC].setPower(-1);
                        Servos[LS].setPosition(.9);
                        Servos[RS].setPosition(.1);
                    }
                    break;
                //move forward while collecting to pick up the block
                case 3:
                    if (runtime.milliseconds() > 200) {
                        CollectEnd();
                        Servos[LS].setPosition(0);
                        Servos[RS].setPosition(0);
                        resetEncoders();
                        stage++;
                    } else {
                        Motors[LC].setPower(1);
                        Motors[RC].setPower(-1);
                        Servos[LS].setPosition(.9);
                        Servos[RS].setPosition(.1);
                    }
                    break;
                //continue collecting for a bit
                case 4:
                    if (Motors[FL].getCurrentPosition() <= -600) {
                        MotorEnd();
                        resetEncoders();
                        stage++;
                    } else {
                        Motors[FR].setPower(-0.25);
                        Motors[FL].setPower(-0.25);
                        Motors[BR].setPower(-0.25);
                        Motors[BL].setPower(-0.25);
                        Motors[LC].setPower(1);
                        Motors[RC].setPower(-1);
                        Servos[LS].setPosition(.9);
                        Servos[RS].setPosition(.1);
                    }
                    break;
                //move backwards to line up with the bridge while collecting
                case 5:
                    if (Motors[FL].getCurrentPosition() <= -460) {
                        telemetry.addData("FL", Motors[FL].getCurrentPosition());
                        MotorEnd();
                        Servos[G].setPosition(1);
                        resetEncoders();
                        stage++;
                    } else {
                        Motors[FR].setPower(0.25);
                        Motors[FL].setPower(-0.25);
                        Motors[BR].setPower(0.25);
                        Motors[BL].setPower(-0.25);
                    }
                    break;
                //turn to align with the bridge
                case 6:
                    if (Motors[FL].getCurrentPosition() >= 1850) {
                        telemetry.addData("FL", Motors[FL].getCurrentPosition());
                        MotorEnd();
                        Motors[LC].setPower(0);
                        Motors[RC].setPower(0);
                        Servos[LS].setPosition(0);
                        Servos[RS].setPosition(0);
                        resetEncoders();
                        stage++;
                    } else {
                        Motors[FR].setPower(0.75);
                        Motors[FL].setPower(0.75);
                        Motors[BR].setPower(0.75);
                        Motors[BL].setPower(0.75);
                    }
                    break;
                //move under the bridge to the foundation
                case 7:
                    if (Motors[FL].getCurrentPosition() <= -100) {
                        telemetry.addData("FL", Motors[FL].getCurrentPosition());
                        MotorEnd();
                        resetEncoders();
                        runtime.reset();
                        stage++;
                    } else {
                        Motors[FR].setPower(0.25);
                        Motors[FL].setPower(-0.25);
                        Motors[BR].setPower(0.25);
                        Motors[BL].setPower(-0.25);
                    }
                    break;
                //turn to align claws with the foundation
                case 8:
                    if (Motors[FL].getCurrentPosition() <= -450) {
                        telemetry.addData("FL", Motors[FL].getCurrentPosition());
                        resetEncoders();
                        Servos[G].setPosition(0);
                        MotorEnd();
                        stage++;
                    } else {
                        Motors[FR].setPower(-0.35);
                        Motors[FL].setPower(-0.35);
                        Motors[BR].setPower(-0.35);
                        Motors[BL].setPower(-0.35);
                    }
                    //move forwards to align with the front of the pad
                    break;
                case 9:
                    telemetry.addData("Position", Motors[LMX].getCurrentPosition());
                    if (Motors[LMX].getCurrentPosition() <= -450) {
                        Motors[LMX].setPower(0);
                        runtime.reset();
                        resetEncoders();
                        Servos[R].setPosition(1);
                        Servos[L].setPosition(0);
                        Servos[G].setPosition(1);
                        stage++;
                    } else {
                        Motors[LMX].setPower(-1);
                        Servos[G].setPosition(1);
                        Servos[R].setPosition(1);
                        Servos[L].setPosition(0);
                    }
                    break;
                //extend horizontal lift to place block and latch servos
                case 10:
                    telemetry.addData("Position: ", Motors[FL].getCurrentPosition());
                    if (Motors[FL].getCurrentPosition() >= 800) {
                        telemetry.addData("FL", Motors[FL].getCurrentPosition());
                        MotorEnd();
                        Servos[G].setPosition(0);
                        resetEncoders();
                        runtime.reset();
                        stage++;
                    } else {
                        Motors[FR].setPower(0.25);
                        Motors[FL].setPower(0.1);
                        Motors[BR].setPower(0.25);
                        Motors[BL].setPower(0.1);
                        Servos[R].setPosition(1);
                        Servos[L].setPosition(0);
                        Servos[G].setPosition(0);
                    }
                    if (Motors[LMY].getCurrentPosition() <= -1200) {
                        Motors[LMY].setPower(0);
                        Servos[G].setPosition(0);
                        runtime.reset();
                    } else {
                        Motors[LMY].setPower(-1);
                    }
                    break;
                //turn to put the pad horizontally against the wall and raise the lift so it can retract over the block
                case 11:
                    if (Motors[LMX].getCurrentPosition() >= 0) {
                        telemetry.addData("LMX", Motors[LMX].getCurrentPosition());
                        Motors[LMX].setPower(0);
                        runtime.reset();
                    } else {
                        Motors[LMX].setPower(1);
                        Servos[R].setPosition(0);
                        Servos[L].setPosition(1);
                    }
                    if (Motors[FL].getCurrentPosition() <= -700) {
                        telemetry.addData("FL", Motors[FL].getCurrentPosition());
                        MotorEnd();
                        resetEncoders();
                        runtime.reset();
                        stage++;
                    } else {
                        Motors[FR].setPower(-0.25);
                        Motors[FL].setPower(-0.25);
                        Motors[BR].setPower(-0.25);
                        Motors[BL].setPower(-0.25);
                    }
                    break;
                //release the pad, push it against the wall, and retract the horizontal lift
                case 12:
                    if (Motors[LMY].getCurrentPosition() >= -50) {
                        telemetry.addData("LMY", Motors[LMY].getCurrentPosition());
                        Motors[LMY].setPower(0);
                        runtime.reset();
                    } else {
                        Motors[LMY].setPower(1);
                    }
                    if (Motors[FL].getCurrentPosition() >= 2300) {
                        MotorEnd();
                        resetEncoders();
                        stage++;
                    } else {
                        Motors[FR].setPower(0.5);
                        Motors[FL].setPower(0.5);
                        Motors[BR].setPower(0.5);
                        Motors[BL].setPower(0.5);
                    }
                    break;
                //lower the lift to go under the bridge and move back to the quarry
                case 13:
                    if (Motors[FL].getCurrentPosition() <= -20) {
                        telemetry.addData("FL", Motors[FL].getCurrentPosition());
                        MotorEnd();
                        resetEncoders();
                        runtime.reset();
                        stage++;
                    } else {
                        Motors[FR].setPower(0.15);
                        Motors[FL].setPower(-0.15);
                        Motors[BR].setPower(0.15);
                        Motors[BL].setPower(-0.15);
                    }
                    break;
                //turn to align with the block furthest from the wall
                case 14:
                    if (Motors[FL].getCurrentPosition() >= 200) {
                        MotorEnd();
                        resetEncoders();
                        runtime.reset();
                        stage++;
                    } else {
                        Motors[FR].setPower(0.15);
                        Motors[FL].setPower(0.15);
                        Motors[BR].setPower(0.15);
                        Motors[BL].setPower(0.15);
                    }
                    break;
                //move forward to position the block into the collector
                case 15:
                    if (Motors[FL].getCurrentPosition() >= 200) {
                        MotorEnd();
                        resetEncoders();
                        runtime.reset();
                        stage++;
                    } else {
                        Motors[FR].setPower(0.05);
                        Motors[FL].setPower(0.05);
                        Motors[BR].setPower(0.05);
                        Motors[BL].setPower(0.05);
                        Motors[LC].setPower(1);
                        Motors[RC].setPower(-1);
                        Servos[LS].setPosition(.9);
                        Servos[RS].setPosition(.1);
                    }
                    break;
                //move forward while collecting to pick up the block
                case 16:
                    if (Motors[FL].getCurrentPosition() <= -700) {
                        MotorEnd();
                        resetEncoders();
                        stage++;
                    } else {
                        Motors[FR].setPower(-0.25);
                        Motors[FL].setPower(-0.25);
                        Motors[BR].setPower(-0.25);
                        Motors[BL].setPower(-0.25);
                        Motors[LC].setPower(1);
                        Motors[RC].setPower(-1);
                        Servos[LS].setPosition(.9);
                        Servos[RS].setPosition(.1);
                    }
                    break;
                //move backwards while collecting to be able to move under the bridge
                case 17:
                    if (Motors[FL].getCurrentPosition() >= 50) {
                        telemetry.addData("FL", Motors[FL].getCurrentPosition());
                        MotorEnd();
                        resetEncoders();
                        runtime.reset();
                        stage++;
                    } else {
                        Motors[FR].setPower(-0.25);
                        Motors[FL].setPower(0.25);
                        Motors[BR].setPower(-0.25);
                        Motors[BL].setPower(0.25);
                    }
                    break;
                //turn to align with the bridge
                case 18:
                    if (Motors[FL].getCurrentPosition() <= -1500) {
                        MotorEnd();
                        resetEncoders();
                        Servos[G].setPosition(1);
                        stage++;
                    } else {
                        Motors[FR].setPower(-0.5);
                        Motors[FL].setPower(-0.5);
                        Motors[BR].setPower(-0.5);
                        Motors[BL].setPower(-0.5);
                    }
                    break;
                //move under the bridge and grasp the block
                case 19:
                    if (Motors[LMY].getCurrentPosition() <= -1200) {
                        Motors[LMY].setPower(0);
                        resetEncoders();
                        runtime.reset();
                        stage++;
                    } else {
                        Motors[LMY].setPower(-1);
                    }
                    if (Motors[LMX].getCurrentPosition() <= -450) {
                        Motors[LMX].setPower(0);
                        runtime.reset();
                        resetEncoders();
                        Servos[G].setPosition(0);
                    } else {
                        Motors[LMX].setPower(-1);
                        Servos[G].setPosition(1);
                    }
                    break;
                //raise the lift and extend the horizontal lift while grasping the block and release it
                case 20:
                    if (Motors[FL].getCurrentPosition() >= 700) {
                        MotorEnd();
                        resetEncoders();
                        stage++;
                    } else {
                        Motors[FR].setPower(0.25);
                        Motors[FL].setPower(0.25);
                        Motors[BR].setPower(0.25);
                        Motors[BL].setPower(0.25);
                    }
                    if (Motors[LMY].getCurrentPosition() >= -50) {
                        Motors[LMY].setPower(0);
                        Servos[G].setPosition(0);
                        runtime.reset();
                    } else {
                        Motors[LMY].setPower(1);
                        Servos[G].setPosition(0);
                    }
                    break;
                //move under the bridge to park and lower the lift
                case 21:
                    if (Motors[LMX].getCurrentPosition() >= -50) {
                        Motors[LMX].setPower(0);
                        runtime.reset();
                        resetEncoders();
                        Servos[G].setPosition(0);
                    } else {
                        Motors[LMX].setPower(1);
                        Servos[G].setPosition(0);
                    }
                    break;
                //retract the horizontal lift
            }
        }
    }


    public void resetEncoders() {
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

    public void resetLift() {
        Motors[LMY].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Motors[LMY].setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Motors[LMX].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Motors[LMX].setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void MotorEnd() {
        Motors[FL].setPower(0);
        Motors[BL].setPower(0);
        Motors[FR].setPower(0);
        Motors[BR].setPower(0);
    }

    public void CollectEnd() {
        Motors[LC].setPower(0);
        Motors[RC].setPower(0);
    }
}