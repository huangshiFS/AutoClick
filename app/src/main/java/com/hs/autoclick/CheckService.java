package com.hs.autoclick;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class CheckService extends AccessibilityService {
    private final String TAG = CheckService.class.getSimpleName();
    private MyBroadCast myBroadCast;
    private Context mContext;

    public static final String ACTION_UPDATE_SWITCH = "action_update_switch";
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        Log.d(TAG, "onCreate: ");
//        myBroadCast = new MyBroadCast();
//        myBroadCast.init(this);
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == intent) {
            return super.onStartCommand(intent, flags, startId);
        }

        String action = intent.getAction();
        mContext = getApplicationContext();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

    }

    public AccessibilityNodeInfo findViewByText(String text, boolean clickable) {
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return null;
        }
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null && (nodeInfo.isClickable() == clickable)) {
                    return nodeInfo;
                }
            }
        }
        return null;
    }

    public void performViewClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        while (nodeInfo != null) {
            if (nodeInfo.isClickable()) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
            }
            nodeInfo = nodeInfo.getParent();
        }
    }

    public void clickTextViewByID(String id) {
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return;
        }
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(id);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null) {
                    performViewClick(nodeInfo);
                    break;
                }
            }
        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, "onAccessibilityEvent: adadada");
        String packageName = event.getPackageName().toString();
        String className = event.getClassName().toString();
        Log.d(TAG, "packageName = " + packageName + ", className = " + className);
        //  packageName = com.android.systemui, className = android.app.Dialog
        if (packageName.equals("com.android.systemui") && className.equals("android.app.Dialog")) {
            AccessibilityNodeInfo nodeInfo = findViewByText("允许", true);
            if (nodeInfo != null) {
                Log.d(TAG, "onAccessibilityEvent: " + nodeInfo);
                performViewClick(nodeInfo);
                clickTextViewByID("android:id/button1");
            }

        }
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
