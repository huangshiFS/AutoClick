package com.hs.autoclick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.hs.autoclick.databinding.ActivityMainBinding;

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
        startService();
    }

    public void test(){
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