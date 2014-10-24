package cc.kenai.call;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kenai.function.message.XLog;
import com.kenai.function.setting.XSetting;

public class AutoStart extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            if (XSetting.xget_boolean(context, "mainservice_mode")){
                context.startService(new Intent(context, MainService.class));
            }
		}

	}
}
