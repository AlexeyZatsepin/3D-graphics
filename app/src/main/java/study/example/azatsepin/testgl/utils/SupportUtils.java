package study.example.azatsepin.testgl.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;

public class SupportUtils {

    public static boolean supportES2(Context context) {
    ActivityManager activityManager =
            (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
    return (configurationInfo.reqGlEsVersion >= 0x20000);
}
}
