package cc.kenai.call;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;

import com.kenai.function.message.XLog;
import com.kenai.function.setting.XSetting;
import com.kenai.function.state.XState;

import cc.kenai.callflash.R;
import cc.kenai.common.ad.KenaiTuiguang;
import cc.kenai.common.ad.LoadDialog;
import cc.kenai.common.program.Question;
import cc.kenai.common.stores.StoreUtil;
import cc.kenai.function.base.BacePreferenceActivity;

public class MainActivity extends BacePreferenceActivity implements OnSharedPreferenceChangeListener {

    private final static String MAINSERVICE_MODE = "mainservice_mode";

    public MainActivity() {
        super(TYPE_RELEASE_AFTER_PAUSE, false);
        // TODO 自动生成的构造函数存根
    }

    @SuppressWarnings("deprecation")
    @Override
    public void xCreate(Bundle arg0) {
        /**
         * 版本更新
         */
        if (XState.get_isfirst(getBaseContext())) {
            LoadDialog.showDialog(MainActivity.this);
            try {
                Camera open = Camera.open();
                open.release();
            }catch (Exception e){
            }
        }

        addPreferencesFromResource(R.xml.mainsettings_call);
        XSetting.getSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        XLog.xLog("activity_oncreat");
        if (XSetting.xget_boolean(this, MAINSERVICE_MODE)) {
            XLog.xLog("activity_open service");
            startService(new Intent(this, MainService.class));
        }
        bindbt(this);


    }

    @Override
    public void xCreatePrepare() {

    }

    @Override
    public void xDestroy() {
        XSetting.getSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void xPause() {
        // TODO 自动生成的方法存根

    }

    @Override
    public void xResume() {
        // TODO 自动生成的方法存根

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if (XSetting.xget_boolean(this, MAINSERVICE_MODE)) {
            startService(new Intent(this, MainService.class));
        } else {
            stopService(new Intent(this, MainService.class));
        }
    }


    @SuppressWarnings("deprecation")
    void bindbt(final Context context) {
        PreferenceScreen main_melody = (PreferenceScreen) findPreference("main_melody");
        main_melody
                .setOnPreferenceClickListener(new OnPreferenceClickListener() {
                    public boolean onPreferenceClick(Preference preference) {
                        startActivity(new Intent(
                                "android.intent.action.VIEW",
                                Uri.parse("mstore:http://app.meizu.com/phone/apps/a1156e07ad7e4f1bba05014c88b3b98c")));
                        return true;
                    }
                });
        PreferenceScreen call_mainhelp = (PreferenceScreen) findPreference("call_mainhelp");
        call_mainhelp
                .setOnPreferenceClickListener(new OnPreferenceClickListener() {
                    public boolean onPreferenceClick(Preference preference) {
                        new AlertDialog.Builder(context)
                                .setIcon(R.drawable.ic_launcher)
                                .setTitle(R.string.call_mainhelp)
                                .setMessage(R.string.call_mainhelp_value)
                                .setPositiveButton("I know",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int whichButton) {

                                            }
                                        }
                                ).create().show();
                        return true;
                    }
                });
        //载入kenai软件推广
        findPreference("tuiguang").setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                KenaiTuiguang.show(getBaseContext());
                return true;
            }
        });
        PreferenceScreen main_info = (PreferenceScreen) findPreference("main_info");
        main_info
                .setOnPreferenceClickListener(new OnPreferenceClickListener() {
                    public boolean onPreferenceClick(Preference preference) {
                        StoreUtil.showInMeizuStore(getBaseContext(), "");
                        return true;
                    }
                });
        PreferenceScreen main_mail = (PreferenceScreen) findPreference("main_mail");
        main_mail
                .setOnPreferenceClickListener(new OnPreferenceClickListener() {
                    public boolean onPreferenceClick(Preference preference) {
                        Question.NotificationAndDialog(MainActivity.this);
                        return true;
                    }
                });
    }
}
