package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class ShootTest extends OpMode {
    private DcMotor shooter = null;
    private CRServo Feeder = null;
    private DcMotor turret = null;
    private DcMotor intake = null;
    double shootSpeed = 0;
    boolean Feed = false;

    @Override
    public void init() {
        shooter = hardwareMap.get(DcMotor.class,"shooter");
        Feeder = hardwareMap.get(CRServo.class,"Feeder");
        turret = hardwareMap.get(DcMotor.class,"turret");
        intake = hardwareMap.get(DcMotor.class,"intake");
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        shooter.setPower(-shootSpeed);
        if (gamepad2.right_trigger_pressed){
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

        if(gamepad2.yWasPressed()){
            shootSpeed += 0.1;
        }

        if(gamepad2.aWasPressed()){
           shootSpeed -= 0.1;
        }

        if(gamepad2.bWasPressed()){
            shootSpeed -= 0.01;
        }

        if(gamepad2.xWasPressed()){
            shootSpeed += 0.01;
        }

        if (shootSpeed>1){
            shootSpeed = 1;
        } else if (shootSpeed<-1) {
            shootSpeed = -1;
        }

        if (gamepad2.left_trigger_pressed){
            intake.setPower(-0.7);
        }else if (gamepad2.right_trigger_pressed){
            intake.setPower(0.7);
        }else {
            intake.setPower(0);
        }

        double turretSpin = gamepad2.left_stick_x;
        double turretHeading = turret.getCurrentPosition();
        telemetry.addData("Turret Heading", turretHeading);
        turret.setPower(turretSpin);

        telemetry.addData("Speed", shootSpeed);
        telemetry.addData("Feeding", Feed);
        telemetry.update();
    }
}
