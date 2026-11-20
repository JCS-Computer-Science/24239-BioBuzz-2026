package org.firstinspires.ftc.teamcode.TeleOp;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Util.OpModeStorage;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp
public class FieldDrive extends OpMode{
    private Follower follower;
    private DcMotor shooter = null;
    private DcMotor turret = null;
    private CRServo Feeder = null;
    private DcMotor intake = null;
    private DcMotor Feed2 = null;
    Boolean Shooting = false;
    Boolean Shoot = false;
    boolean Feed = false;
    boolean Feeder2 = false;
    @Override
    public void init(){
        follower = Constants.create(hardwareMap);
        shooter = hardwareMap.get(DcMotor.class,"Shooter");
        turret = hardwareMap.get(DcMotor.class,"turret");
        Feeder = hardwareMap.get(CRServo.class,"Feeder");
        intake = hardwareMap.get(DcMotor.class,"intake");
        Feed2 = hardwareMap.get(DcMotor.class,"Feed2");
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    @Override
    public void start(){
        follower.setPose(OpModeStorage.autonomousEndPose);
        follower.update();

    }
    @Override
    public void loop(){
        DrivePowers powers = ManualDrive.fieldCentric(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x,
                follower.pose().heading()
        );
        follower.manual(powers);
        follower.update();
        if(gamepad2.bWasPressed()){
            if(Shooting == false){
                Shooting = true;
                shooter.setPower(-0.7);
            } else if (Shooting == true) {
                Shooting = false;
                shooter.setPower(0);
            }
        }
        double turretHeading = turret.getCurrentPosition();
        telemetry.addData("Turret Heading", turretHeading);
        //double turretSpin = gamepad2.left_stick_x;
        //Add turret spin limits
        //if(turretHeading>=100){
            //turretSpin = 0;
        //}
        //turret.setPower(turretSpin);

        if (gamepad2.xWasPressed()){
            if(!Feed){
                Feed = true;
            } else if (Feed) {
                Feed = false;
            }
        }
        if (Feed){
            Feeder.setPower(1);
        } else if (!Feed){
            Feeder.setPower(0);
        }

        if (gamepad2.left_trigger_pressed){
            intake.setPower(0.7);
        }else if (gamepad2.right_trigger_pressed){
            intake.setPower(-0.7);
        }else {
            intake.setPower(0);
        }

        if(gamepad2.yWasPressed()){
            if(Feeder2 == false) {
                Feeder2 = true;
                Feed2.setPower(-1);
            }else if(Feeder2 == true) {
                Feeder2 = false;
                Feed2.setPower(0);
            }
        }
        telemetry.addData("Feed2",Feeder2);
        telemetry.addData("Shooting",Shoot);
        telemetry.update();
    }
}

//pad1 left stick up and down: forword and back
//pad1 left stick side to side: strafe
//pad1 right stick side to side: turn

//pad2 b button: flywheel
//pad2 left stick side to side: turret
//pad2 right trigger: feed balls to the shooter
//pad2 left trigger: intake / move ball threw robot

//Expancion hub
//motor
//Port 0 Shooter
//Port 1 intake
//Port 2 Feed2
//Port 3 turret
//servo
//Port 5 Feeder
//I2C Bus 1 Pin PinPoint
