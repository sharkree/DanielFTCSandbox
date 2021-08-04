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

    boolean cruiseControl = false;

    @Override
    public void runOpMode() throws InterruptedException {
        fLeft = hardwareMap.dcMotor.get("fLeft");
        bLeft = hardwareMap.dcMotor.get("bLeft");
        fRight = hardwareMap.dcMotor.get("fRight");
        bRight = hardwareMap.dcMotor.get("bRight");

        fLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        bLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        fRight.setDirection(DcMotorSimple.Direction.REVERSE);
        bRight.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while(opModeIsActive()) {
            double linearPower = gamepad1.left_stick_y;
            double strafePower = gamepad1.left_stick_x;
            double rotationalPower = gamepad1.right_stick_x;
            double scalar = 0.5;

            fLeft.setPower(scalar * (linearPower + rotationalPower + strafePower));
            bLeft.setPower(scalar * (linearPower + rotationalPower - strafePower));
            fRight.setPower(scalar * (linearPower - rotationalPower - strafePower));
            bRight.setPower(scalar * (linearPower - rotationalPower + strafePower));

            telemetry.addData("Time: ", SystemClock.elapsedRealtime());
            telemetry.addData("Robot Speed: ", scalar);
            telemetry.addData("Gamepad left stick y", gamepad1.left_stick_y);
            telemetry.update();
        }
    }
}
