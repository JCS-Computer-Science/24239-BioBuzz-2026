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
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Util.OpModeStorage;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp
public class FieldDrive extends OpMode{
    private Follower follower;
    //private DcMotor shooter = null;
    //private DcMotor turret = null;
    //private CRServo Feeder = null;
    Boolean Shooting = false;
    Boolean Shoot = false;
    boolean Feed = false;
    @Override
    public void init(){
        follower = Constants.create(hardwareMap);
        //shooter = hardwareMap.get(DcMotor.class,"Shooter");
        //turret = hardwareMap.get(DcMotor.class,"turret");
        //Feeder = hardwareMap.get(CRServo.class,"Feeder");
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
            if(Shooting){
                Shoot = false;
            } else if (!Shooting) {
                Shooting = true;
            }
        }
        double turretSpin = gamepad2.left_stick_x;
        //turret.setPower(turretSpin);
        if(Shoot){
            //Shooter.setPower(1);
        }else if (!Shoot){
            //Shooter.setPower(0);
        }
        if (gamepad2.right_trigger_pressed){
            if(!Feed){
                Feed = true;
            } else if (Feed) {
                Feed = false;
            }
        }
        //if (Feed){
            //Feeder.setPower(0.7);
        //} else if (!Feed){
            //Feeder.setPower(0);
        //}
    }
}

//pad1 left stick up and down: forword and back
//pad1 left stick side to side: strafe
//pad1 right stick side to side: turn

//pad2 b button: flywheel
//pad2 left stick side to side: turret
//pad2 right trigger: feed balls to the shooter
