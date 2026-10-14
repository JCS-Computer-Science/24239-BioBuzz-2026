package org.firstinspires.ftc.teamcode.Auto;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.pedropathing.follower.Follower;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import static com.pedropathing.api.Paths.*;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import static com.pedropathing.ivy.groups.Groups.sequential;

import org.firstinspires.ftc.teamcode.Util.OpModeStorage;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import org.firstinspires.ftc.teamcode.TeleOp.FieldDrive;

@Autonomous
public class SimpleAuto extends OpMode{
    private Follower follower;
    private final PoseFactory p = PoseFactory.degrees();
    private final Pose startPose = p.of(31.578192252510757, 8.203012912482071, 90);
    private final Pose park = p.of(12.75824964131994, 119.23529411764704, 0);

    private Path Park() {
        return line(startPose, park).linear(startPose, park);
    }

    private Command shootAndPark(){
        return sequential(
                follow(follower, Park())
        );
    }


    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();

    }

    @Override
    public void start() {
        schedule(shootAndPark());
    }

    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();

        telemetry.addData("X", follower.pose().x());
        telemetry.addData("y", follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));
        telemetry.addData("Follower Mode", follower.mode());
        telemetry.addData("Path completion", follower.completion());
        telemetry.addData("Distance Remaining In Path", follower.remainingDistance());
        telemetry.update();
    }
    @Override
    public void stop(){
        OpModeStorage.autonomousEndPose = follower.pose();
    }

}
