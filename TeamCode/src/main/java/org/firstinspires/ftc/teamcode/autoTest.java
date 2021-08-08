package org.firstinspires.ftc.teamcode;

import android.os.SystemClock;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous
public class autoTest extends LinearOpMode {
    DcMotor fLeft;
    DcMotor bLeft;
    DcMotor fRight;
    DcMotor bRight;

    double sum = 0;
    double targetPosition = 24;

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

        fLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        bLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        fRight.setDirection(DcMotorSimple.Direction.REVERSE);
        bRight.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while(opModeIsActive()) {

            double encoderLeftPosition = fLeft.getCurrentPosition();
            double encoderBackPosition = bLeft.getCurrentPosition();
            double encoderRightPosition = fRight.getCurrentPosition();

            sum -= (targetPosition * -1384.375 - getCurrentPos(encoderLeftPosition, encoderRightPosition)) / 100;

//            if (targetPosition > getCurrentPos(encoderLeftPosition, encoderRightPosition)) {
//                setFullPower();
//            }
//            else {
//                setZeroPower();
//            }
            setFullPower();

            telemetry.addData("Time: ", SystemClock.elapsedRealtime());
            telemetry.addData("Gamepad left stick y", gamepad1.left_stick_y);
            telemetry.addData("P: ", getProportional(targetPosition));
            telemetry.addData("I: ", getIntergral(sum));
            telemetry.addData("Encoder left Position: ", encoderLeftPosition);
            telemetry.addData("Encoder right Position: ", encoderRightPosition);
            telemetry.update();
        }
    }

    public void setFullPower() {
        double proportional = getProportional(targetPosition);
        double integral = getIntergral(sum);

//        fLeft.setPower(proportional + integral);
//        bLeft.setPower(proportional + integral);
//        fRight.setPower(proportional + integral);
//        bRight.setPower(proportional + integral);
        fLeft.setPower(proportional);
        bLeft.setPower(proportional);
        fRight.setPower(proportional);
        bRight.setPower(proportional);
    }

    public void setZeroPower() {
        fLeft.setPower(0.0);
        bLeft.setPower(0.0);
        fRight.setPower(0.0);
        bRight.setPower(0.0);
    }

    public double getProportional(double inches) {
        double targetTickCount = inches * -1384.375;
        return -(targetTickCount - ((fLeft.getCurrentPosition() + fRight.getCurrentPosition()) / 2.0)) / 40000;
    }

    public double getIntergral(double sum) {
        return sum * 0.000000001;
    }

    public double getCurrentPos(double encoderLeftPosition, double encoderRightPosition) {
         return (encoderLeftPosition + encoderRightPosition) / 2;
    }
}
