package cc.kenai.call.call;

import android.content.Context;
import android.hardware.SensorEvent;

import com.kenai.function.message.XLog;
import com.kenai.function.sensor.XSensorListener;

public class ToSilence extends XSensorListener {
    public ToSilence(int id_sensor, Context context) {
        super(id_sensor, context);
    }

    @Override
    public void doInformation(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        float total = (x * x + y * y + z * z);
        if (total < 80) {
            XLog.xLog("total : " + total);
        }
//		XLog.xLog("x:" + (int) (x * 100f) / 100f + " y:" + (int) (y * 100f)
//				/ 100f + " z:" + (int) (z * 100f) / 100f);
    }

    @Override
    public final void clear() {
        // //Log.v("执行方法", "acc-clear()");
        // min_z = 0;
        // max_z = 0;

    }

    public void start() {

    }

    public void stop() {

    }

}
