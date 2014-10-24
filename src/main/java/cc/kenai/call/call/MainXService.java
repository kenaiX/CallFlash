package cc.kenai.call.call;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.kenai.function.message.XLog;
import com.kenai.function.setting.XSetting;

import cc.kenai.function.base.BaceInterfacePlugInUnit;
import cc.kenai.function.base.XService;

public class MainXService extends XService {


    public MainXService(Context context) {
        super(context);
    }

    private static final String TAG = "SilenceService";

    private BaceInterfacePlugInUnit flash;
    private final PhoneStateListener phoneListenner = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, final String incomingNumber) {
            //Ringing. A new call arrived and is ringing or waiting. In the latter case, another call is already active.
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                if (flash != null) {
                    flash.xstop();
                    flash = null;
                }
                if (XSetting.xget_boolean(context, MySettings.CALLFLASH_MODE)) {
                    flash = new CallFlashPlugUnit(context) {
                        public void onStop() {
                            flash = null;
                        }
                    };
                    flash.xstart();
                }
            }
            //Off-hook. At least one call exists that is dialing, active, or on hold, and no calls are ringing or waiting.
            else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                if (flash != null) {
                    flash.xstop();
                    flash = null;
                }
            }
            //No activity.
            else if (state == TelephonyManager.CALL_STATE_IDLE) {
                if (flash != null) {
                    flash.xstop();
                    flash = null;
                }
            }
        }

    };

    @Override
    public void xCreate() {
        regis_listenPhoneState();
//		if(XLog.model){
//			XSetting.xset_boolean(context, MySettings.CALLFLASH_MODE, true);
//		}
    }

    @Override
    public void xDestroy() {
        unregis_listenPhoneState();
    }

    @Override
    public void xstart(Intent intent) {
        // TODO Auto-generated method stub

    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {

    }

    /**
     * 用于注册并相应电话状态
     *
     * @author lx_yjq
     */
    private final void regis_listenPhoneState() {
        XLog.xLog(TAG, "regis_listenPhoneState()");
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListenner,
                PhoneStateListener.LISTEN_CALL_STATE);
    }

    private final void unregis_listenPhoneState() {
        XLog.xLog(TAG, "regis_listenPhoneState()");
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListenner, PhoneStateListener.LISTEN_NONE);
    }
}
