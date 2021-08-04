package org.firstinspires.ftc.teamcode;

import android.os.SystemClock;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class cruiseKontrol extends LinearOpMode {
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

            if (cruiseControl && gamepad1.right_trigger > 0) {
                final double scalarCruise = gamepad1.right_trigger;
                while (true) {
                    if (gamepad1.x) {
                        cruiseControl = !cruiseControl;
                        break;
                    }
                    fLeft.setPower(scalarCruise * (linearPower + rotationalPower + strafePower));
                    bLeft.setPower(scalarCruise * (linearPower + rotationalPower - strafePower));
                    fRight.setPower(scalarCruise * (linearPower - rotationalPower - strafePower));
                    bRight.setPower(scalarCruise * (linearPower - rotationalPower + strafePower));
                }
            }
            else if (!cruiseControl) {
                scalar = gamepad1.right_trigger;
            }
            else {
                scalar = 0.5;
            }

            if(gamepad1.x){
                if (cruiseControl){
                    cruiseControl = !cruiseControl;
                    sleep(250);
                }
                else {
                    cruiseControl = !cruiseControl;
                    sleep(250);
                }
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
}
