package com.example.alrtfront;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'alrtfront' library on application startup.
    static {
        System.loadLibrary("alrtfront");
    }
    private static View myView = null;

    private static int flash = 0;
    private final static int SIREN_DURATION = 10;
    final static int SIREN_INTERVAL = 1000;
    boolean whichColor = true;

    private final static int HONK_DURATION = 5000;
    private final static int HONK_INCREASE = 1000;
    private static int honkTime = 0;
    private static boolean honkSleep = false;

    int[] COLORS = {0xff0000, 0x0000ff};
    int TIME = 500;
    int color = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            honk();
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            siren();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myView = (View) findViewById(R.id.mylayout);
        myView.setBackgroundColor(Color.BLACK);// set initial colour

        WindowManager.LayoutParams layout = getWindow().getAttributes();
        layout.screenBrightness = 1F;
        getWindow().setAttributes(layout);


        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

//
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

        // Example of a call to a native method
//        TextView tv = binding.sampleText;
//        tv.setText(stringFromJNI("hello world"));
        //toggle(-1);
    }

    private void updateColor() {
        runOnUiThread(() -> {
            if (whichColor)
                myView.setBackgroundColor(Color.RED);
            else
                myView.setBackgroundColor(Color.BLUE);
        });
    }

    public void siren() {
        if (flash == 0) {
            new Thread(() -> {
                flash = SIREN_DURATION;
                while (flash > 0) {
                    try {
                        Thread.sleep(SIREN_INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    updateColor();
                    whichColor = !whichColor;
                    flash--;
                }
                runOnUiThread(() -> {
                    myView.setBackgroundColor(Color.BLACK);// set initial colour
                });
            }).start();
        } else {
            flash = SIREN_DURATION;
        }

    }

    public void honk() {
        if (honkSleep == false) {
            new Thread(() -> {
                honkSleep = true;
                honkTime = HONK_DURATION;
                runOnUiThread(() -> {
                    myView.setBackgroundColor(Color.WHITE);// set initial colour
                });
                while (honkTime > 0) {
                    try {
                        int time = honkTime;
                        honkTime = 0;
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(() -> {
                    myView.setBackgroundColor(Color.BLACK);// set initial colour
                });
                honkSleep = false;
            }).start();
        } else {
            if(honkTime < HONK_DURATION) {
                honkTime += HONK_INCREASE;
            }
        }
    }

    public void toggle(int type) {
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText("Type: " + type);
    }

    /**
     * A native method that is implemented by the 'alrtfront' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI(String test);
}