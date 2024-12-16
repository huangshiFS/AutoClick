package com.hs.autoclick;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class CheckService extends AccessibilityService {
    private final String TAG = CheckService.class.getSimpleName();
    private MyBroadCast myBroadCast;
    public static final String ACTION_UPDATE_SWITCH = "action_update_switch";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        myBroadCast = new MyBroadCast();
        myBroadCast.init(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == intent) {
            return super.onStartCommand(intent, flags, startId);
        }

        String action = intent.getAction();
        Log.d(TAG, "onStartCommand Aciton: " + action);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo serviceInfo = new AccessibilityServiceInfo();
        serviceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        serviceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        serviceInfo.packageNames = new String[]{"com.gotokeep.keep", "com.eg.android.AlipayGphone", "com.sinovatech.unicom.ui", "com.tencent.mm"};// 监控的app
        serviceInfo.notificationTimeout = 100;
        serviceInfo.flags = serviceInfo.flags | AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY;
        setServiceInfo(serviceInfo);
        Log.d(TAG, "onServiceConnected: ");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        String packageName = event.getPackageName().toString();
        String className = event.getClassName().toString();
        Log.d(TAG, "packageName = " + packageName + ", className = " + className);
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "onInterrupt: ");

    }

    class MyBroadCast extends BroadcastReceiver {

        public void init(Context mContext) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_USER_PRESENT);
            mContext.registerReceiver(this, intentFilter);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == intent) {
                return;
            }

            String action = intent.getAction();
            Log.d(TAG, "action = " + action);

            if (Intent.ACTION_USER_PRESENT.equals(action)) {
//                startUI();
            }
        }
    }
}
