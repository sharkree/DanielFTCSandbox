// simple teleop program that drives bot using controller joysticks in tank mode.
// this code monitors the period and stops when the period is ended.

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;

@Disabled
@TeleOp
public class MultiThreadTankDrive extends LinearOpMode
{
    DcMotor fLeft, fRight, bLeft, bRight;
    float   leftY, rightY;

    public MultiThreadTankDrive() throws Exception
    {
        RobotLog.i("Starting DriveTankMT");
    }

    // called when init button is  pressed.
    @Override
    public void runOpMode() throws InterruptedException
    {
        fLeft = hardwareMap.dcMotor.get("fLeft");
        fRight = hardwareMap.dcMotor.get("fRight");
        bLeft = hardwareMap.dcMotor.get("bLeft");
        bRight = hardwareMap.dcMotor.get("bRight");

        fLeft.setDirection(DcMotor.Direction.REVERSE);
        bLeft.setDirection(DcMotor.Direction.REVERSE);
        fRight.setDirection(DcMotor.Direction.FORWARD);
        bRight.setDirection(DcMotor.Direction.FORWARD);

        // create an instance of the DriveThread.

        Thread driveThread = new DriveThread();

        telemetry.addData("Mode", "waiting");
        telemetry.update();

        // wait for start button.

        RobotLog.i("wait for start");

        waitForStart();

        RobotLog.i("started");

        // start the driving thread.

        driveThread.start();

        // continue with main thread.

        try
        {
            while (opModeIsActive())
            {
                telemetry.addData("Mode", "running");
                telemetry.addData("Run Time", this.getRuntime());
                telemetry.addData("Buttons", "x1=" + gamepad1.x);
                telemetry.addData("sticks", "  left=" + leftY + "  right=" + rightY);
                telemetry.update();

                idle();
            }
        }
        catch(Exception e) {RobotLog.logStackTrace(e);}

        RobotLog.i("out of while loop");

        // stop the driving thread.

        driveThread.interrupt();

        RobotLog.i("end");
    }

    private class DriveThread extends Thread
    {
        public DriveThread()
        {
            this.setName("DriveThread");

            RobotLog.i("%s", this.getName());
        }

        // called when tread.start is called. thread stays in loop to do what it does until exit is
        // signaled by main code calling thread.interrupt.
        @Override
        public void run() {
            RobotLog.i("Starting thread %s",this.getName());

            try
            {
                while (!isInterrupted())
                {
                    // we record the Y values in the main class to make showing them in telemetry
                    // easier.

                    leftY = gamepad1.left_stick_y;
                    rightY = gamepad1.right_stick_y;

                    fLeft.setPower(Range.clip(leftY, -1.0, 1.0));
                    fRight.setPower(Range.clip(rightY, -1.0, 1.0));
                    bLeft.setPower(Range.clip(leftY, -1.0, 1.0));
                    bRight.setPower(Range.clip(rightY, -1.0, 1.0));

                    idle();
                }
            }
            // interrupted means time to shutdown. note we can stop by detecting isInterrupted = true
//            // or by the interrupted exception thrown from the sleep function.
//            catch (InterruptedException e) {RobotLog.i(e.getMessage());}
            // an error occurred in the run loop.
            catch (Exception e) {RobotLog.i(e.toString());}

            RobotLog.i("end of thread %s", this.getName());
        }
    }
}