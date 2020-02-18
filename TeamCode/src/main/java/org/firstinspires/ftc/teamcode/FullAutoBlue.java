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
@Autonomous(name = "FullAutoBlue", group = "DogeCV")

public class FullAutoBlue extends LinearOpMode {
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
            telemetry.addData("LMY", Motors[LMY].getCurrentPosition());
            telemetry.addData("FL", Motors[FL].getCurrentPosition());

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
                    //reset claw and encoders
                case 1:
                    //raise lift and move forward to shake out collector
                case 2:
                    //move backwards to shake out collector
                case 3:
                    //move forwards towards skystone and lower lift
                case 4:
                    //move sideways until skystone is detected
                case 5:
                    //move onto one of 3 autonomous programs base on time
                case 6:
                    //OUT 0
                case 7:
                    //MIDDLE 0
                case 8:
                    //IN 0
                case 9:
                    //OUT 1
                case 10:
                    //MIDDLE 1
                case 11:
                    //IN 1
                case 12:
                    //OUT 2
                case 13:
                    //MIDDLE 2
                case 14:
                    //IN 2
                case 15:
                    //OUT 3
                case 16:
                    //MIDDLE 3
                case 17:
                    //IN 3
                case 18:
                    //OUT 4
                case 19:
                    //MIDDLE 4
                case 20:
                    //IN 4
                case 21:
                    //OUT 5
                case 22:
                    //MIDDLE 5
                case 23:
                    //IN 5
                case 24:
                    //OUT 6
                case 25:
                    //MIDDLE 6
                case 26:
                    //IN 6
                case 27:
                    //OUT 7
                case 28:
                    //MIDDLE 7
                case 29:
                    //IN 7
                case 30:
                    //OUT 8
                case 31:
                    //MIDDLE 8
                case 32:
                    //IN 8
                case 33:
                    //OUT 9
                case 34:
                    //MIDDLE 9
                case 35:
                    //IN 9
                case 36:
                    //OUT 10
                case 37:
                    //MIDDLE 10
                case 38:
                    //IN 10
                case 39:
                    //OUT 11
                case 40:
                    //MIDDLE 11
                case 41:
                    //IN 11
                case 42:
                    //OUT 12
                case 43:
                    //MIDDLE 12
                case 44:
                    //IN 12
                case 45:
                    //OUT 13
                case 46:
                    //MIDDLE 13
                case 47:
                    //IN 13
                case 48:
                    //OUT 14
                case 49:
                    //MIDDLE 14
                case 50:
                    //IN 14
                case 51:
                    //OUT 15
                case 52:
                    //MIDDLE 15
                case 53:
                    //IN 15
                case 54:
                    //OUT 16
                case 55:
                    //MIDDLE 16
                case 56:
                    //IN 16
                case 57:
                    //OUT 17
                case 58:
                    //MIDDLE 17
                case 59:
                    //IN 17
                case 60:
                    //OUT 18
                case 61:
                    //MIDDLE 18
                case 62:
                    //IN 18
                case 63:
                    //OUT 19
                case 64:
                    //MIDDLE 19
                case 65:
                    //IN 19
                case 66:
                    //OUT 20
                case 67:
                    //MIDDLE 20
                case 68:
                    //IN 20
                case 69:
                    //OUT 21
                case 70:
                    //MIDDLE 21
                case 71:
                    //IN 21
            }
        }
    }

    private void resetEncoders() {
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

    private void resetLift() {
        Motors[LMY].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Motors[LMY].setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Motors[LMX].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Motors[LMX].setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    private void MotorEnd() {
        Motors[FL].setPower(0);
        Motors[BL].setPower(0);
        Motors[FR].setPower(0);
        Motors[BR].setPower(0);
    }

    private void CollectEnd() {
        Motors[LC].setPower(0);
        Motors[RC].setPower(0);
    }
}
