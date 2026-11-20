package org.firstinspires.ftc.teamcode.Auto;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.pedropathing.follower.Follower;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import static com.pedropathing.ivy.commands.Commands.*;

import static com.pedropathing.api.Paths.*;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import org.firstinspires.ftc.teamcode.Util.OpModeStorage;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous
public class ShootAuto extends OpMode {

        private Follower follower;
        private final PoseFactory p = PoseFactory.degrees();
        private final Pose startPose = p.of(59.59397417503587, 8.203012912482071, 90);
        private final Pose park = p.of(12.961262553802007, 94.46771879483498, 0);
        private final Pose controlPoint = p.of(22.893113342898154, 53.46484935437594, 45);
        private DcMotor shooter = null;
        private CRServo Feeder = null;
        private DcMotor Feed2 = null;

        private Path Park() {
            return curve(startPose, controlPoint, park).linear(startPose, controlPoint);
        }
        Command Shoot = instant(() -> shooter.setPower(1));
        Command FeedSa = instant(() -> Feeder.setPower(0.7));
        Command FeedSo = instant(() -> Feeder.setPower(0));
        Command Feed2Sa = instant(()-> Feed2.setPower(1));
        Command Feed2So = instant(()-> Feed2.setPower(0));
        Command ShootSo = instant(() -> shooter.setPower(0));
        Command Pause = instant(() -> waitMs(2000));

        private Command shootAndPark(){
            return sequential(
                    Shoot,
                    FeedSa,
                    Feed2Sa,
                    Pause,
                    follow(follower, Park()),
                    FeedSo,
                    FeedSo,
                    ShootSo
            );
        }


        @Override
        public void init() {
            shooter = hardwareMap.get(DcMotor.class,"Shooter");
            Feeder = hardwareMap.get(CRServo.class,"Feeder");
            Feed2 = hardwareMap.get(DcMotor.class,"Feed2");
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

