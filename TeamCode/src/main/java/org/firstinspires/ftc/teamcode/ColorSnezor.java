package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

@TeleOp
public class ColorSnezor extends LinearOpMode {
    ColorSensor colorSnezor;

    @Override
    public void runOpMode() {
        colorSnezor = hardwareMap.get(ColorSensor.class, "color");
        long update = 0;

        waitForStart();

        while(opModeIsActive()) {
            long millis = System.currentTimeMillis();
            update = millis - update;

            telemetry.addData("Update Time: ", update);
            telemetry.addData("Red: ", colorSnezor.red());
            telemetry.addData("Green: ", colorSnezor.green());
            telemetry.addData("Blue: ", colorSnezor.blue());
            telemetry.addData("Alpha: ", colorSnezor.alpha());
            telemetry.update();
        }
    }
}
