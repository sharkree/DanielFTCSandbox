package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import android.os.SystemClock;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

@TeleOp
public class ColorSnezor extends LinearOpMode {
    ColorSensor couleursnezer;

    @Override
    public void runOpMode() {
        couleursnezer = hardwareMap.get(ColorSensor.class, "colorSensor");

        waitForStart();

        while(opModeIsActive()) {
            telemetry.addData("Time: ", SystemClock.elapsedRealtime());
            telemetry.addData("Red: ", couleursnezer.red());
            telemetry.addData("Green: ", couleursnezer.green());
            telemetry.addData("Blue: ", couleursnezer.blue());
            telemetry.addData("Alpha: ", couleursnezer.alpha());
            telemetry.update();
        }
    }
}
