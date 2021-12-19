package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.SwitchableCamera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.internal.camera.delegating.SwitchableCameraName;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvPipeline;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;

@Disabled
@TeleOp
public class EOCVSwitchableCameras extends LinearOpMode {
    String VUFORIA_LICENCE_KEY = "AWPSm1P/////AAABmfp26UJ0EUAui/y06avE/y84xKk68LTTAP3wBE75aIweAnuSt" +
            "/zSSyaSoqeWdTFVB5eDsZZOP/N/ISBYhlSM4zrkb4q1YLVLce0aYvIrso" +
            "GnQ4Iw/KT12StcpQsraoLewErwZwf3IZENT6aWUwODR7vnE4JhHU4+2Iy" +
            "ftSR0meDfUO6DAb4VDVmXCYbxT//lPixaJK/rXiI4o8NQt59EIN/W0RqT" +
            "ReAehAZ6FwBRGtZFyIkWNIWZiuAPXKvGI+YqqNdL7ufeGxITzc/iAuhJz" +
            "NZOxGXfnW4sHGn6Tp+meZWHFwCYbkslYHvV5/Sii2hR5HGApDW0oDml6g" +
            "OlDmy1Wmw6TwJTwzACYLKl43dLL35G";

    OpenCvCamera openCvCamera;
    SwitchableCamera switchableCamera;

    WebcamName cameraName1 = hardwareMap.get(WebcamName.class, "Webcam 1");
    WebcamName cameraName2 = hardwareMap.get(WebcamName.class, "Webcam 2");

    SwitchableCameraName switchableCameraName = ClassFactory.getInstance()
            .getCameraManager()
            .nameForSwitchableCamera(cameraName1, cameraName2);

    @Override
    public void runOpMode() {
        switchableCamera.setActiveCamera(cameraName1);

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_LICENCE_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        parameters.cameraName = switchableCameraName;
        VuforiaLocalizer vuforia = ClassFactory.getInstance().createVuforia(parameters);
        switchableCamera = (SwitchableCamera) vuforia.getCamera();

        openCvCamera = OpenCvCameraFactory.getInstance().createVuforiaPassthrough(vuforia, parameters);

        // we're passing in a SwitchableCamera(not a Camera), which causes Eocv to block us when we start running
        // To bypass this, we use reflections
        try {
            Class<?> aClass = Class.forName("org.openftc.easyopencv.OpenCvVuforiaPassthroughImpl");
            Field isWebcamField = aClass.getDeclaredField("isWebcam");
            isWebcamField.setAccessible(true);
            isWebcamField.set(openCvCamera, true);
        } catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException e) {
            Log.e("Switchable Cameras: ", "cannot set isWebcam ", e);
        }

        waitForStart();

        while (opModeIsActive()) {
            // yeet
            openCvCamera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
                @Override
                public void onOpened() {
                    openCvCamera.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
                    openCvCamera.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);

                    openCvCamera.setPipeline(new CameraConsumerProcessor());
                    openCvCamera.startStreaming(1920, 1080);
                }

                @Override
                public void onError(int errorCode) {
                    Log.d("Camera", "Error: " + errorCode);
                }
            });
        }
    }

    private final class CameraConsumerProcessor extends OpenCvPipeline {
        @Override
        public Mat processFrame(Mat input) {
            File file = new File(AppUtil.ROBOT_DATA_DIR + "/" + "webcam-frame-" + new Date().getTime() + ".jpg");
            MatOfByte mob = new MatOfByte();
            Imgcodecs.imencode(".jpg", input, mob);
            try (FileOutputStream stream = new FileOutputStream(file)) {
                stream.write(mob.toArray());
            } catch (IOException e) {
                Log.w("OpenCV demonstration", e);
            }

            return input;
        }
    }
}
