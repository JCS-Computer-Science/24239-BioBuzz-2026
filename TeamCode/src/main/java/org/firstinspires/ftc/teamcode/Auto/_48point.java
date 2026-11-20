package org.firstinspires.ftc.teamcode.Auto;

import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.api.Paths.*;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.pedropathing.paths.Path;
import org.firstinspires.ftc.teamcode.Util.OpModeStorage;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous
public class _48point extends OpMode {
    private DcMotor shooter = null;
    private DcMotor Intake = null;
    private CRServo Feeder = null;

    private Follower follower;
    private PoseFactory p = PoseFactory.degrees();
    private final Pose Start = p.of(54.74778761061947,8,90);
    private final Pose ConerSet = p.of(8.95,14.89,270);
    private final Pose controle1 = p.of(28.04,67.58,180);
    private final Pose Coner = p.of(8.91,7.94,270);
    private final Pose FarShoot = p.of(58.33,129.42,270);
    private final Pose controle2 = p.of(23.86,111.48,270);
    private final Pose Flower1Set = p.of(46.67,122.4,90);
    private final Pose Flower1 = p.of(46.56,133.91,90);
    private final Pose flower2Set = p.of(16.62,47.13,180);
    private final Pose controle3 = p.of(22.05,107.78,225);
    private final Pose flower2 = p.of(8.08,47.17,180);
    private final Pose Park = p.of(10.65,92.53,0);
    private final Pose controle4 = p.of(13.59,42.56,45);

    private Path ToCorner(){
        return curve(Start,controle1,ConerSet).linear(Start,controle1);
    }
    private Path InCorner(){
        return line(ConerSet,Coner).linear(ConerSet,Coner);
    }
    private Path ToFarShoot(){
        return curve(Coner,controle2,FarShoot).linear(Coner,controle2);
    }
    private Path Flowerset1(){
        return line(FarShoot, Flower1Set).linear(FarShoot, Flower1Set);
    }
    private Path Flower1In(){
        return line(Flower1Set, Flower1).linear(Flower1Set, Flower1);
    }
    private Path Flower1Out(){
        return line(Flower1,Flower1Set).linear(Flower1,Flower1Set);
    }
    private Path FarShoot2(){
        return line(Flower1Set,FarShoot).linear(Flower1Set,FarShoot);
    }
    private Path ToFlower2(){
        return curve(FarShoot,controle3,flower2Set).linear(FarShoot,controle3);
    }
    private Path Flower2In(){
        return line(flower2Set,flower2).linear(flower2Set,flower2);
    }
    private Path Flower2Out(){
        return line(flower2,flower2Set).linear(flower2,flower2Set);
    }
    private Path ToCloseShoot(){
        return line(flower2Set,Start).linear(flower2Set,Start);
    }
    private Path ToPark(){
        return curve(Start,controle4,Park).linear(Start,controle4);
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
          follow(follower, ToCorner()),
          IntakeSa,
          follow(follower, InCorner()),
          IntakeSo,
          follow(follower, ToFarShoot()),
          FeedSa,
          Pause,
          FeedSo,
          follow(follower, Flowerset1()),
          IntakeSa,
          follow(follower, Flower1In()),
          Pause,
          follow(follower, Flower1Out()),
          IntakeSo,
          follow(follower, FarShoot2()),
          FeedSa,
          Pause,
          FeedSo,
          follow(follower, ToFlower2()),
          IntakeSa,
          follow(follower, Flower2In()),
          Pause,
          follow(follower, Flower2Out()),
          IntakeSo,
          follow(follower, ToCloseShoot()),
          FeedSa,
          Pause,
          FeedSo,
          ShootSo,
          follow(follower, ToPark())
        );
    }

    public void init(){
        shooter = hardwareMap.get(DcMotor.class,"Shooter");
        Intake = hardwareMap.get(DcMotor.class,"Intake");
        Feeder = hardwareMap.get(CRServo.class,"Feeder");
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(Start);
        follower.update();
    }
    public void start(){
        schedule(Everything());
    }
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
    public void stop(){
        OpModeStorage.autonomousEndPose = follower.pose();
    }
}
