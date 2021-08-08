package org.firstinspires.ftc.teamcode;

import android.os.SystemClock;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class PlebTest extends LinearOpMode {
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

        fLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        fLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        double encoderA = fLeft.getCurrentPosition();
        double encoderB = bLeft.getCurrentPosition();
        double encoderC = fRight.getCurrentPosition();

        fLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        bLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        fRight.setDirection(DcMotorSimple.Direction.FORWARD);
        bRight.setDirection(DcMotorSimple.Direction.FORWARD);

        waitForStart();

        while(opModeIsActive()) {
            setDrivetrainPower();

            telemetry.addData("Time: ", SystemClock.elapsedRealtime());
            telemetry.addData("Gamepad left stick y", gamepad1.left_stick_y);
            telemetry.addData("Encoder A Position: ", encoderA);
            telemetry.addData("Encoder B Position: ", encoderB);
            telemetry.addData("Encoder C Position: ", encoderC);
            telemetry.update();
        }
    }

    public void setDrivetrainPower() {
        double linearPower = gamepad1.left_stick_y;
        double strafePower = gamepad1.left_stick_x;
        double rotationalPower = gamepad1.right_stick_x;

        double fLeftPower = linearPower - rotationalPower + strafePower;
        double bLeftPower = linearPower - rotationalPower - strafePower;
        double fRightPower = linearPower + rotationalPower - strafePower;
        double bRightPower = linearPower + rotationalPower + strafePower;

        double largestPower1;
        double largestPower2;
        double largestPower;

        largestPower1 = Math.max(Math.abs(fLeftPower), Math.abs(bLeftPower));
        largestPower2 = Math.max(Math.abs(fRightPower), Math.abs(bRightPower));
        largestPower = Math.max(largestPower1, largestPower2);

        double scaleFactor = 1 / largestPower;

        fLeftPower *= scaleFactor;
        bLeftPower *= scaleFactor;
        fRightPower *= scaleFactor;
        bRightPower *= scaleFactor;

        fLeft.setPower(fLeftPower);
        bLeft.setPower(bLeftPower);
        fRight.setPower(fRightPower);
        bRight.setPower(bRightPower);
    }
}
