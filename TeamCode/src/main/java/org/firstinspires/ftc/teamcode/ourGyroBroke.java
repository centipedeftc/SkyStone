package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;


import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;



//november 14 2019
@TeleOp(name = "apple")
public class ourGyroBroke extends OpMode {

   // BNO055IMU imu;
   // Orientation angles;


    //Drivetrain
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backRight;
    DcMotor backLeft;
    cartesianConverter CC;
    double[] drivePowers = new double[4];
    double maxNum;
    double offSetAngle = 0;

    //Collector
    DcMotor leftCollector;
    DcMotor rightCollector;
    Servo leftServo;
    Servo rightServo;

    //Deposit
    DcMotor liftMotorY;
    DcMotor liftMotorX;
    Servo swivel;
    Servo grab;
    DigitalChannel liftLimitCloud;
    double closeDisFinder;
    double[] stage = {0,-1033,-2254,-3502,-4799,-5880,-7000,-8259,-9578,-10583};
    int targetIndex;
    //other
    Servo plateLeft;
    Servo plateRight;
    Servo capStone;
    double plate = 1;
    double plate2 = 1;
    double x;
    double y;
    double z;
    double scale = 1;
    double fL;
    double fR;
    double bL;
    double bR;
    boolean xWas = false;
    boolean prevA = false;
    boolean prevB = false;
    boolean clawDown = false;
    boolean swivelActive = false;
    //boolean deathToggle = false;


    BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
    @Override
    public void init() {

    //*
       // parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
      //  parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        //parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
      //  parameters.loggingEnabled = true;
      //  parameters.loggingTag = "IMU";

        //parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        //imu = hardwareMap.get(BNO055IMU.class, "imu");
       // imu.initialize(parameters);

    //*/
        //DriveTrain
        frontLeft = hardwareMap.dcMotor.get("FL");
        frontRight = hardwareMap.dcMotor.get("FR");
        backLeft = hardwareMap.dcMotor.get("BL");
        backRight = hardwareMap.dcMotor.get("BR");
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        CC = new cartesianConverter();


        //Collector
        //motors
        leftCollector = hardwareMap.dcMotor.get("LC");
        rightCollector = hardwareMap.dcMotor.get("RC");
        rightCollector.setDirection(DcMotor.Direction.REVERSE);
        //servos
        leftServo = hardwareMap.servo.get("LS");
        rightServo = hardwareMap.servo.get("RS");
        rightServo.setDirection(Servo.Direction.REVERSE);

        //Deposit
        //Lift
        liftMotorY = hardwareMap.dcMotor.get("LMY");
        liftMotorY.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotorX = hardwareMap.dcMotor.get("LMX");
        swivel = hardwareMap.servo.get("S");
        grab = hardwareMap.servo.get("G");
        liftLimitCloud = hardwareMap.get(DigitalChannel.class, "LL");
        liftLimitCloud.setMode(DigitalChannel.Mode.INPUT);
        //Other
        plateLeft = hardwareMap.servo.get("R");
        plateRight = hardwareMap.servo.get("L");
        capStone = hardwareMap.servo.get("CS");

    }




    @Override
    public void start(){


    }

    @Override
    public void loop() {
       //*
           // angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

//Drive train --
            x = gamepad1.left_stick_x;
            y = -gamepad1.left_stick_y;
            z = gamepad1.right_trigger - gamepad1.left_trigger;
           // drivePowers = CC.convert(x, y, z * .65, (Math.toRadians(-angles.firstAngle - 90 + offSetAngle)));
            if (gamepad1.dpad_up) {
                //offSetAngle = angles.firstAngle;
            }

            x = gamepad1.left_stick_x;
            y = -gamepad1.left_stick_y;
            z = (gamepad1.right_trigger - gamepad1.left_trigger)/2;
            drivePowers[0] = y+x+z;
            drivePowers[1] = y-x-z;
            drivePowers[2] = y-x+z;
            drivePowers[3] = y+x-z;
            telemetry.addData("BIIIIIIIGGGGGGG YIKES" ,"L");
        // */
        /*x = gamepad1.left_stick_x;
        y = -gamepad1.left_stick_y;
        z = gamepad1.right_trigger - gamepad1.left_trigger;
        drivePowers[0] = y+x+z;
        drivePowers[1] = y-x-z;
        drivePowers[2] = y-x+z;
        drivePowers[3] = y+x-z;
*/
        //scale = x==0 && y==0?1:Math.pow((x*x)+(y*y), .5);
        //x/=scale*2;
        //y/=scale*2;

        if(gamepad1.x&&!xWas){
            scale = scale == 1?.5:1;}
        xWas=gamepad1.x;

        if(gamepad1.dpad_left) {
           // offSetAngle = 90;
        }
        if(gamepad1.dpad_right) {
           // offSetAngle = -90;
        }



        //fL = y+x+z;
      //  fR = y-x-z;
       // bL = y-x+z;
      //  bR = y+x-z;
        /*
        drivePowers[0] = y+x+z;
        drivePowers[1] = y-x-z;
        drivePowers[2] = y-x+z;
        drivePowers[3] = y+x-z;
*/
        //double[] drivePowers = {fL,fR,bL,bR};
        maxNum = drivePowers[0];
        for (int i = 0; i<drivePowers.length; i++){
            if (Math.abs(maxNum) < Math.abs(drivePowers[i])){
                maxNum = drivePowers[i];
            }
        }
        for (int i = 0; i<drivePowers.length; i++){
            if(Math.abs(maxNum)>1){
                drivePowers[i]/= Math.abs(maxNum);
            }
        }


        frontLeft.setPower(drivePowers[0]*scale);
        frontRight.setPower(drivePowers[1]*scale);
        backLeft.setPower(drivePowers[2]*scale);
        backRight.setPower(drivePowers[3]*scale);
//Collector
        if(gamepad1.a){
            leftCollector.setPower(1);
            leftServo.setPosition(.9);
            rightCollector.setPower(1);
            rightServo.setPosition(.9);
        }
        else if(gamepad1.b){
            leftCollector.setPower(-1);
            leftServo.setPosition(.1);
            rightCollector.setPower(-1);
            rightServo.setPosition(.1);
        }
        else{
            leftCollector.setPower(0);
            leftServo.setPosition(0.5);
            rightCollector.setPower(0);
            rightServo.setPosition(0.5);
        }
//Deposit

        if(gamepad2.left_bumper )//&& liftMotorX.getCurrentPosition()<0)
            liftMotorX.setPower(1);
        else if(gamepad2.right_bumper )//&& liftMotorX.getCurrentPosition()>-564)
            liftMotorX.setPower(-1);
        else
            liftMotorX.setPower(0);


        if(gamepad2.x && !prevB)
            swivelActive = !swivelActive;
        prevB = gamepad2.x;

        if(swivelActive)
            swivel.setPosition(.735);
        else
            swivel.setPosition(.36);


        if(gamepad2.a && !prevA)
            clawDown =!clawDown;

        prevA = gamepad2.a;

        if(clawDown)
            grab.setPosition(1);
        else
            grab.setPosition(0);







        //double[] stage = {0,-1033,-2254,-3502,-4799,-5880,-7000,-8259,-9578,-10583};

        closeDisFinder = Math.abs(liftMotorY.getCurrentPosition()-stage[0]);
        targetIndex = 0;
        for (int i = 0; i<stage.length; i++) {
            if (closeDisFinder > Math.abs(liftMotorY.getCurrentPosition() - stage[i])) {
                closeDisFinder = Math.abs(liftMotorY.getCurrentPosition() - stage[i]);
                targetIndex = i;
            }
        }






                if(liftMotorY.getCurrentPosition() >= 0 && gamepad2.left_stick_y > 0) {
                    liftMotorY.setPower(0);
                }
                else if(liftMotorY.getCurrentPosition() <= -10750 && gamepad2.left_stick_y < 0){
                    liftMotorY.setPower(0);
                }
                else if (liftMotorY.getCurrentPosition() >=-300 && gamepad2.left_stick_y > 0) {
                    liftMotorY.setPower(0.5*gamepad2.left_stick_y);
                }
                else if(gamepad2.left_stick_y==0&&gamepad2.b) {
                    liftMotorY.setPower(runToPosition(liftMotorY.getCurrentPosition(), stage[targetIndex], .5));
        }
                else
                    liftMotorY.setPower(gamepad2.left_stick_y);



                if(liftMotorX.getCurrentPosition() >= 0 && ((gamepad2.left_bumper?1:0) - (gamepad2.right_bumper?1:0) > 0)){
                liftMotorX.setPower(0);
                }
                else if(liftMotorX.getCurrentPosition() <= -577 && ((gamepad2.left_bumper?1:0) - (gamepad2.right_bumper?1:0) > 0)){
                liftMotorX.setPower(0);
                }
                else
                liftMotorX.setPower((gamepad2.left_bumper?1:0) - (gamepad2.right_bumper?1:0));
                //}

//Other
        if(gamepad1.y){
            plate = 1;
            plate2 = 0;}
        else{
            plate = 0;
            plate2 =1;}
        plateLeft.setPosition(plate);
        plateRight.setPosition(plate2);

        if (gamepad2.y){
            capStone.setPosition(.3);
        }
        else{
            capStone.setPosition(.47 );
        }


        //liftMotorX.setPower(runToPosition(liftMotorY.getCurrentPosition(), 1234,1));
        //try {
          //  telemetry.addData("Heading", angles.firstAngle);
        //}catch(Exception e){

        //}
        telemetry.addData("liftMotorY", liftMotorY.getCurrentPosition());
        telemetry.addData("liftMotorX", liftMotorX.getCurrentPosition());
        telemetry.addData("FL", frontLeft.getCurrentPosition());
        telemetry.addData("FR", frontRight.getCurrentPosition());
        telemetry.addData("BL", backLeft.getCurrentPosition());
        telemetry.addData("BR", backRight.getCurrentPosition());
        //telemetry.addData("x", x);
        //telemetry.addData("y", y);
        //telemetry.addData("z", z);
        telemetry.addData("fL", fL);
        telemetry.addData("fR", fR);
        telemetry.addData("bL", bL);
        telemetry.addData("bL", bL);
        //telemetry.addData("closedis",closeDis);
        telemetry.addData("plate",plate);
        telemetry.addData("capstone",capStone);
        if (liftLimitCloud.getState()) {
            telemetry.addData("Digital Touch", "Is Not Pressed");
        } else {
            telemetry.addData("Digital Touch", "Is Pressed");
        }
        telemetry.update();



    }
    public double runToPosition (double encoder,double encoderTarget, double power){
        double encoderDistance = encoderTarget - encoder;
        if(Math.abs(encoderDistance)>=50){
            return (encoderDistance/Math.abs(encoderDistance))*power;
        }
        return 0;

    }
    public double distance(double encoderVal,double stage){
        double stageDis = Math.abs(encoderVal-stage);
        return stageDis;



    }

}
