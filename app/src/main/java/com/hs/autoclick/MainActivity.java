package com.hs.autoclick;

import androidx.appcompat.app.AppCompatActivity;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;
import android.widget.TextView;

import com.hs.autoclick.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'autoclick' library on application startup.
    static {
        System.loadLibrary("autoclick");
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
        TextView tv = binding.sampleText;
        tv.setText(stringFromJNI());
        AccessibilityManager mAccessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> accessibilityServices =
                mAccessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        boolean flag = true;
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().equals("com.hs.autoclick/.CheckService")) {
                flag = false;
            }
        }
        if (flag) {
            Log.d("CheckService", "onCreate: 增加权限 ");
            Intent i1 = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(i1);
        }
        startService();

    }

    public void test() {
        Intent intent = new Intent(this, CheckService.class);
        intent.setAction(CheckService.ACTION_UPDATE_SWITCH);
        MainActivity.this.startService(intent);
    }

    private void startService() {
        Intent mIntent = new Intent(this, CheckService.class);
        startService(mIntent);
    }

    /**
     * A native method that is implemented by the 'autoclick' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}