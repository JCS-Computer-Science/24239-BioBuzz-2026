package org.firstinspires.ftc.teamcode.Auto;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import static com.pedropathing.api.Paths.*;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.pedropathing.ivy.Command;
import org.firstinspires.ftc.teamcode.Util.OpModeStorage;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import com.pedropathing.paths.Path;

@Autonomous
public class Nonpark extends OpMode {
    private Follower follower;
    private PoseFactory p = PoseFactory.degrees();

    private final Pose Start = p.of(55.533715925394546,8.406025824964141,90);

    private final Pose End = p.of(55.533715925394546,26.073888091822088,90);

    private Path move(){
        return line(Start, End).linear(Start, End);
    }

    private Command Move(){
        return sequential(
                follow(follower, move())
        );
    }

    @Override
    public void init(){
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(Start);
        follower.update();
    }

    @Override
    public void start(){
        schedule(Move());
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
