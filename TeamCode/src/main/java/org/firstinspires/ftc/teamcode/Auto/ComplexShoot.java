package org.firstinspires.ftc.teamcode.Auto;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.pedropathing.follower.Follower;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.Util.OpModeStorage;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import static com.pedropathing.api.Paths.*;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

@Autonomous
public class ComplexShoot extends OpMode{

    private Follower follower;

    private PoseFactory p = PoseFactory.degrees();

    private final Pose Start = p.of(56.1,7.7,90);
    private final Pose pickup = p.of(9.01,7.76,270);
    private final Pose controlePoint1 = p.of(36.848637015781925,61.30129124820658,180);
    private final Pose farShoot = p.of(60.41248206599714,128.46700143472026,270);
    private final Pose controlePoint2 = p.of(30.599713055954087,119.07675753228122,270);
    private final Pose park = p.of(10.63,122.17,0);
    private final Pose controlePoint3 = p.of(29.77403156384505,110.91822094691535,135);

    private DcMotor shooter = null;
    private CRServo Feeder = null;

    private DcMotor Intake = null;


    private Path pickUp(){
            return curve(Start, controlePoint1, pickup).linear(Start, controlePoint1);
    }
    private Path SecondShoot(){
        return curve(pickup, controlePoint2, farShoot).linear(pickup,controlePoint2);
    }
    private Path Park(){
        return curve(farShoot, controlePoint3, park).linear(farShoot,controlePoint3);
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
                ShootSo,
                IntakeSa,
                follow(follower, pickUp()),
                Pause,
                IntakeSo,
                follow(follower, SecondShoot()),
                ShootSa,
                FeedSa,
                Pause,
                FeedSo,
                ShootSo,
                follow(follower, Park())
        );
    }

    @Override
    public void init(){
        shooter = hardwareMap.get(DcMotor.class,"shooter");
        Feeder = hardwareMap.get(CRServo.class,"Feeder");
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
    public void stop() {
        OpModeStorage.autonomousEndPose = follower.pose();
    }
}
