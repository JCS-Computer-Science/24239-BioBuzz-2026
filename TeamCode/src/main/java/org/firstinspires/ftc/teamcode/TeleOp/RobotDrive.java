package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="RobotDrive", group="Linear OpMode")

public class RobotDrive extends LinearOpMode{
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor FrontLeft = null;
    private DcMotor FrontRight = null;
    private DcMotor BackLeft = null;
    private DcMotor BackRight = null;
    private CRServo Feeder = null;
    //private DcMotor Turret = null;
    //private DcMotor shooter = null;

    @Override
    public void runOpMode() {
        FrontLeft = hardwareMap.get(DcMotor.class, "FrontLeft"); //Front Left Wheel
        FrontRight = hardwareMap.get(DcMotor.class, "FrontRight"); //Front Right Wheel
        BackLeft = hardwareMap.get(DcMotor.class, "BackLeft"); //Back Left Wheel
        BackRight = hardwareMap.get(DcMotor.class, "BackRight"); //Back Right Wheel
        //Feeder = hardwareMap.get(CRServo.class,"Feeder"); //Feeds the shooter
        //Turret = hardwareMap.get(DcMotor.class,"TurretGear"); //Motor to spin the turret
        //shooter = hardwareMap.get(DcMotor.class, "Shooter"); //Shooter wheel

        FrontLeft.setDirection(DcMotor.Direction.REVERSE);
        BackLeft.setDirection(DcMotor.Direction.FORWARD);   //Flip of add if controls are inverted
        FrontRight.setDirection(DcMotor.Direction.FORWARD);
        BackRight.setDirection(DcMotor.Direction.REVERSE);

        boolean Shoot = false;
        boolean Feed = false;


        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            telemetry.addData("Shooting", Shoot);
            telemetry.addData("Feeding", Feed);
            telemetry.update();

            double max;
            double froward = -gamepad1.left_stick_y; //froward and back
            double strafe = gamepad1.right_stick_x; //Side to side
            double turn = gamepad1.left_stick_x; //Tuning left and right
            //double turretSpin = gamepad2.left_stick_x; // Spin the turret
            if (gamepad1.bWasPressed()) {
                if (!Shoot) {
                    Shoot = true; //Turns on fly wheel
                } else if (Shoot) {
                    Shoot = false; //Turns off fly wheel
                }
            }
            if (gamepad2.right_trigger_pressed){
                if(!Feed){
                    Feed = true;
                } else if (Feed) {
                    Feed = false;
                }
            }

            double FrontLeftPower = froward + strafe + turn;
            double FrontRightPower = froward - strafe - turn;
            double BackLeftPower = froward - strafe + turn;
            double BackRightPower = froward + strafe - turn;

            max = Math.max(Math.abs(FrontLeftPower), Math.abs(FrontRightPower));
            max = Math.max(max, Math.abs(BackLeftPower));
            max = Math.max(max, Math.abs(BackRightPower));

            if (max > 1.0) {
                FrontLeftPower /= max;
                FrontRightPower /= max;
                BackLeftPower /= max;
                BackRightPower /= max;
            }

            FrontLeft.setPower(FrontLeftPower);
            FrontRight.setPower(FrontRightPower);
            BackLeft.setPower(BackLeftPower);
            BackRight.setPower(BackRightPower);
            //Turret.setPower(turretSpin);
            //if (Shoot) {
                //Shooter.setPower(1);
            //}
            if(Shoot){
                Feeder.setPower(0.7);
            }
        }
    }
}

