package org.firstinspires.ftc.teamcode.Auto;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.hardware.HardwareMap;

import static com.pedropathing.api.Paths.*;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import org.firstinspires.ftc.teamcode.Util.OpModeStorage;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous
public class FlowerAuto extends OpMode {
    private Follower follower;
    private final PoseFactory p = PoseFactory.degrees();
    private final Pose Start = p.of(54.9,8.0,90);
    private final Pose Controle1 = p.of(48.64,39.01,145);
    private final Pose Flower1 = p.of(7.88,46.91,180);
    private final Pose OutFlower1 = p.of(23.09,46.69,180);
    private final Pose Shoot = p.of(59.26,130.25,270);
    private final Pose Controle2 = p.of(20.52,94.2,225);
    private final Pose FlowerSetUP = p.of(46.46,121.7,180);
    private final Pose Flower2 = p.of(46.56,133.06,90);
    private final Pose Park = p.of(12.13,120.76,180);
    private final Pose Controle3 = p.of(44.93,103.36,255);

    private DcMotor shooter = null;
    private CRServo Feeder = null;
    private DcMotor Intake = null;

    private Path ToFlower1(){
        return curve(Start,Controle1,Flower1).linear(Start,Controle1);
    }
    private Path FlowerOut1(){
        return line(Flower1,OutFlower1).linear(Flower1,OutFlower1);
    }
    private Path ToFarShoot(){
        return curve(OutFlower1,Controle2,Shoot).linear(Start,Controle2);
    }
    private Path FlowerSetup(){
        return line(Shoot,FlowerSetUP).linear(Shoot,FlowerSetUP);
    }
    private Path ToFlower2(){
        return line(FlowerSetUP,Flower2).linear(FlowerSetUP,Flower2);
    }
    private Path FlowerOut2(){
        return line(Flower2,FlowerSetUP).linear(Flower2,FlowerSetUP);
    }
    private Path FarShoot2(){
        return line(FlowerSetUP,Shoot).linear(FlowerSetUP,Shoot);
    }
    private Path ToPark(){
        return curve(Shoot,Controle3,Park).linear(Shoot,Controle3);
    }

    Command ShootSa = instant(() -> shooter.setPower(1)); //Sa=Start
    Command FeedSa = instant(() -> Feeder.setPower(0.7)); //So=Stop
    Command FeedSo = instant(() -> Feeder.setPower(0));
    Command ShootSo = instant(() -> shooter.setPower(0));
    Command Pause = instant(() -> waitMs(2000));
    Command IntakeSa = instant(() -> Intake.setPower(1));
    Command IntakeSo = instant(() -> Intake.setPower(0));

    private Command Everything(){
        return sequential(
                ShootSa,
                FeedSa,
                Pause,
                FeedSo,
                IntakeSa,
                follow(follower, ToFlower1()),
                Pause,
                IntakeSo,
                follow(follower, FlowerOut1()),
                follow(follower, ToFarShoot()),
                FeedSa,
                Pause,
                FeedSo,
                follow(follower, FlowerSetup()),
                IntakeSa,
                follow(follower, ToFlower2()),
                Pause,
                follow(follower, FlowerOut2()),
                IntakeSo,
                follow(follower, FarShoot2()),
                FeedSa,
                Pause,
                FeedSo,
                ShootSo,
                follow(follower, ToPark())
        );
    }
    @Override
    public void init(){
        Feeder = hardwareMap.get(CRServo.class,"Feeder");
        shooter = hardwareMap.get(DcMotor.class,"Shooter");
        Intake = hardwareMap.get(DcMotor.class,"Intake");
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(Start);
        follower.update();
    }
    @Override
    public void start(){
        schedule(Everything());
    }
    @Override
    public void loop(){
        follower.update();
        Scheduler.execute();

        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));
        telemetry.addData("Path Completion", follower.completion());
        telemetry.addData("Path Mode", follower.mode());
        telemetry.addData("Distance remaining in path", follower.remainingDistance());
    }
    @Override
    public void stop(){
        OpModeStorage.autonomousEndPose = follower.pose();
    }
}
