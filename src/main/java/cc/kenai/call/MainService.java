package cc.kenai.call;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.IBinder;

import com.kenai.function.message.XLog;
import com.kenai.function.setting.XSetting;

public class MainService extends Service implements OnSharedPreferenceChangeListener {

    cc.kenai.call.call.MainXService mSilenceService = new cc.kenai.call.call.MainXService(this);

    @Override
    public void onCreate() {
        XLog.xLog("service_oncreat");
        super.onCreate();
        XSetting.getSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        mSilenceService.xCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        XSetting.getSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);

        mSilenceService.xDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        mSilenceService.xstart(intent);
        return START_STICKY;
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO 自动生成的方法存根
        return null;
    }

}
