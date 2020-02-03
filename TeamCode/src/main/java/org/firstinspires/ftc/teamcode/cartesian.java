package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@TeleOp(name = "optimalMovement")
public class cartesian extends OpMode {

    BNO055IMU imu;
    Orientation angles;

    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backRight;
    DcMotor backLeft;
    @Override
    public void init() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        frontLeft = hardwareMap.dcMotor.get("FL");
        frontRight = hardwareMap.dcMotor.get("FR");
        backLeft = hardwareMap.dcMotor.get("BL");
        backRight = hardwareMap.dcMotor.get("BR");
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
    }
        @Override
        public void loop() {

            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            telemetry.addData("heading ", -angles.firstAngle+0);
            telemetry.addData("roll ", -angles.secondAngle+0);
            telemetry.addData("pitch ", -angles.thirdAngle+0);
            telemetry.update();

            double x = gamepad1.left_stick_x;
            double y = gamepad1.left_stick_y;
            double turn = gamepad1.right_trigger-gamepad1.left_trigger;
            double sinDeg = Math.cos(-angles.firstAngle);
            double coSineDeg = Math.sin(-angles.firstAngle);
            double newY = (y * coSineDeg  +  x * sinDeg);
            double newX = (x * coSineDeg  -  y * sinDeg);
            double fL = newY + newX + turn;
            double fR = newY - newX - turn;
            double bL = newY - newX + turn;
            double bR = newY + newX - turn;

            frontRight.setPower(fR);
            frontLeft.setPower(fL);
            backRight.setPower(bR);
            backLeft.setPower(bL);
        }

}
