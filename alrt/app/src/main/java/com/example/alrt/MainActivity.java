package com.example.alrt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.alrt.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'alrt' library on application startup.
    static {
        System.loadLibrary("alrt");
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
//        TextView tv = binding.sampleText;
//        tv.setText(stringFromJNI("hello world"));
        toggle(-1);
    }

    public void toggle(int type) {
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText("Type: " + type);
    }

    /**
     * A native method that is implemented by the 'alrt' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI(String test);
}