package cc.kenai.call.call;

import android.content.Context;
import android.hardware.Camera;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

import com.kenai.function.message.XLog;

import java.util.Timer;
import java.util.TimerTask;

import cc.kenai.function.base.BacePlugInUnit;

public abstract class SMSFlashPlugUnit extends BacePlugInUnit {
    private final static String TAG = "SMSFlash";
    private Camera myCamera;
    private Camera.Parameters param;
    private Context context;

    public SMSFlashPlugUnit(Context context) {
        this.context = context;
    }

    private final Timer myTimer = new Timer();
    private final TimerTask task = new TimerTask() {
        int n = 0;

        @Override
        public void run() {
            if (n % 3 == 0) {
                if (myCamera != null) {
                    param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    myCamera.setParameters(param);
                }
            } else if (n > 4) {
                xstop();
            } else {
                if (myCamera != null) {
                    param.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    myCamera.setParameters(param);
                }
            }
            n++;
        }

    };

    @SuppressWarnings("deprecation")
    @Override
    public boolean myStart() {
        XLog.xLog(TAG, "myStart()");
        try {
            if (android.provider.Settings.System.getInt(
                    context.getContentResolver(), Settings.System.MODE_RINGER) != 0) {
                try {
                    XLog.xLog(TAG, "not silence");
                    myCamera = Camera.open();
                    param = myCamera.getParameters();
                    myTimer.schedule(task, 0, 500);
                    return true;
                } catch (Exception e) {
                    XLog.xLog(TAG, "Camera.open()--error");
                    return false;
                }
            }
        } catch (SettingNotFoundException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean myStop() {
        XLog.xLog(TAG, "myStop()");
        if (myCamera != null) {
            try {
                XLog.xLog(TAG, "myStop()--successeed");
                myTimer.cancel();
                myCamera.release();
                myCamera = null;
                return true;
            } catch (Exception e) {
                XLog.xLog(TAG, "myStop()--error");
                return false;
            }
        } else {
            return true;
        }
    }

}
