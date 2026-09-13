package org.firstinspires.ftc.teamcode.pedro;

public class Tuning {
    // Tuners go here
    @Tuner
    public static Procedure mecanumTuner() {
        return new MecanumTuner();
    }

    @Tuner
    public static Procedure pinpointTuner(){
        return new PinpointTuner();
    }
}
