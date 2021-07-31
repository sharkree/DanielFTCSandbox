package org.firstinspires.ftc.teamcode;

import android.os.SystemClock;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class randomTelemetryProgram extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();

        while(opModeIsActive()) {
            telemetry.addData("Time", SystemClock.elapsedRealtime());
            telemetry.update();
        }
    }
}
