package cc.kenai.call.call;

import android.content.Context;
import android.database.ContentObserver;
import android.hardware.Camera;
import android.net.Uri;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

import com.kenai.function.setting.XSetting;

import java.util.Timer;
import java.util.TimerTask;

import cc.kenai.function.base.BacePlugInUnit;

/**
 * 已经自带静音停止功能
 *
 * @author kenai
 */
public abstract class CallFlashPlugUnit extends BacePlugInUnit {
    private Camera myCamera;
    private Camera.Parameters param;
    private Context context;
    private int limite = 30;

    public CallFlashPlugUnit(Context context) {
        this.context = context;
    }

    private final ContentObserver silenceObserver = new ContentObserver(null) {
        @Override
        public void onChange(boolean selfChange) {
            xstop();
        }
    };
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
            } else if (n > limite) {
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
        try {
            Uri silenceUri = android.provider.Settings.System
                    .getUriFor(Settings.System.MODE_RINGER);
            if (android.provider.Settings.System.getInt(
                    context.getContentResolver(), Settings.System.MODE_RINGER) != 0)
                try {
                    myCamera = Camera.open();
                    param = myCamera.getParameters();
                    context.getContentResolver().registerContentObserver(
                            silenceUri, true, silenceObserver);
                    switch (XSetting.xget_int(context, MySettings.CALLFLASH_SPEED)) {
                        case 0:
                        default:
                            myTimer.schedule(task, 0, 500);
                            limite = 30;
                            break;
                        case 1:
                            myTimer.schedule(task, 0, 800);
                            limite = 23;
                            break;
                        case 2:
                            myTimer.schedule(task, 0, 230);
                            limite = 50;
                            break;
                    }


                    return true;
                } catch (Exception e) {
                    return false;
                }
        } catch (SettingNotFoundException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean myStop() {
        if (myCamera != null) {
            try {
                myTimer.cancel();
                myCamera.release();
                myCamera = null;
                context.getContentResolver().unregisterContentObserver(
                        silenceObserver);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return true;
        }
    }
}
