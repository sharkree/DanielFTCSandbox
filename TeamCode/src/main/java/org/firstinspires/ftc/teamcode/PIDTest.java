package org.firstinspires.ftc.teamcode;

import android.os.SystemClock;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous
public class PIDTest extends LinearOpMode {
    public static final double TICKS_PER_INCH = -1384.375;
    public static final double Ki = 0.000000001;
    public static final double Kp = 0.000025;
    private DcMotor fLeft;
    private DcMotor bLeft;
    private DcMotor fRight;
    private DcMotor bRight;

    private double errorSum = 0;
    private double distance = 24;

    @Override
    public void runOpMode() throws InterruptedException {
        fLeft = hardwareMap.dcMotor.get("fLeft");
        bLeft = hardwareMap.dcMotor.get("bLeft");
        fRight = hardwareMap.dcMotor.get("fRight");
        bRight = hardwareMap.dcMotor.get("bRight");

        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        fLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        bLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        fRight.setDirection(DcMotorSimple.Direction.REVERSE);
        bRight.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while(opModeIsActive()) {

            double encoderLeftPosition = fLeft.getCurrentPosition();
            double encoderBackPosition = bLeft.getCurrentPosition();
            double encoderRightPosition = fRight.getCurrentPosition();

            errorSum -= (distance * TICKS_PER_INCH - getCurrentPos(fLeft, fRight)) / 100;

            setFullPower();

            telemetry.addData("Time: ", SystemClock.elapsedRealtime());
            telemetry.addData("P: ", getProportional(distance));
            telemetry.addData("I: ", getIntergral(errorSum));
            telemetry.addData("Encoder left Position: ", encoderLeftPosition);
            telemetry.addData("Encoder right Position: ", encoderRightPosition);
            telemetry.update();
        }
    }

    private void setRunMode(DcMotor.RunMode runMode) {
        fLeft.setMode(runMode);
        bLeft.setMode(runMode);
        fRight.setMode(runMode);
        bRight.setMode(runMode);
    }

    public void setFullPower() {
        double proportional = getProportional(distance);
        double integral = getIntergral(errorSum);

//        setPower(proportional + integral);
        setPower(proportional);
    }

    public void setPower(double power) {
        fLeft.setPower(power);
        bLeft.setPower(power);
        fRight.setPower(power);
        bRight.setPower(power);
    }

    public double getProportional(double inches) {
        double targetTickCount = inches * TICKS_PER_INCH;
        return -(targetTickCount - getCurrentPos(fLeft, fRight)) * Kp;
    }

    public double getIntergral(double errorSum) {
        return errorSum * Ki;
    }

    public double getCurrentPos(DcMotor left, DcMotor right) {
        return (left.getCurrentPosition() + right.getCurrentPosition()) / 2;
    }
}
