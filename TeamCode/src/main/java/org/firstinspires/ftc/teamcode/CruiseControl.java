package org.firstinspires.ftc.teamcode;

import android.os.SystemClock;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class CruiseControl extends LinearOpMode {
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

        fLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        bLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        fRight.setDirection(DcMotorSimple.Direction.REVERSE);
        bRight.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {
            double linearPower = gamepad1.left_stick_y;
            double strafePower = gamepad1.left_stick_x;
            double rotationalPower = gamepad1.right_stick_x;
            double scalar = 0.5;

            if (gamepad1.dpad_up) {
                updateScalarIncreasing(scalar);
                sleep(50);
            }
            else if (gamepad1.dpad_down) {
                updateScalarDecreasing(scalar);
                sleep(50);
            }

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
    public void updateScalarIncreasing(double scalar) {
        scalar = increaseSpeed(scalar);
    }
    public void updateScalarDecreasing(double scalar) {
        scalar = decreaseSpeed(scalar);
    }

    public double increaseSpeed(double scalar) {
        scalar = Math.min(1.0, scalar += 0.1);

        return scalar;
    }

    public double decreaseSpeed(double scalar) {
        scalar = Math.max(0.0, scalar -= 0.1);

        return scalar;
    }
}
