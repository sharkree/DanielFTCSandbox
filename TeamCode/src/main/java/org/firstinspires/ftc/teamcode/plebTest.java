package org.firstinspires.ftc.teamcode;

import android.os.SystemClock;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class plebTest extends LinearOpMode {
    DcMotor fLeft;
    DcMotor bLeft;
    DcMotor fRight;
    DcMotor bRight;
    @Override
    public void runOpMode() throws InterruptedException {
        fLeft = hardwareMap.dcMotor.get("fLeft");
        bLeft = hardwareMap.dcMotor.get("bLeft");
        fRight = hardwareMap.dcMotor.get("fRight");
        bRight = hardwareMap.dcMotor.get("bRight");

        fLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        bLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        fRight.setDirection(DcMotorSimple.Direction.FORWARD);
        bRight.setDirection(DcMotorSimple.Direction.FORWARD);

        waitForStart();

        while(opModeIsActive()) {
            double power = -gamepad1.left_stick_y;

            fLeft.setPower(power);
            bLeft.setPower(power);
            fRight.setPower(power);
            bRight.setPower(power);

            telemetry.addData("Time", SystemClock.elapsedRealtime());
            telemetry.addData("Gamepad left stick y", gamepad1.left_stick_y);
            telemetry.update();
        }
    }
}
