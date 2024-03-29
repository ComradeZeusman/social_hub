package com.example.class_project;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import androidx.core.app.ActivityCompat;

import com.example.class_project.MainActivity;

public class Loading_screen extends AppCompatActivity {

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        //check for permissions if not granted request for permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }



        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_background));
            actionBar.setDisplayShowTitleEnabled(false);
            //hide action bar
            actionBar.hide();
        }


        progressBar = findViewById(R.id.progressBar);

        // Set the progress bar color to red
        progressBar.getProgressDrawable().setColorFilter(
                getResources().getColor(R.color.red),
                android.graphics.PorterDuff.Mode.SRC_IN
        );

        // Create a Runnable to update the progress bar
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;

                    // Update the progress bar on the main thread
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressStatus);
                        }
                    });

                    // Sleep for 50 milliseconds to slow down the progress update
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // After the progress bar is filled, launch the intent
                launchNextActivity();
            }
        };

        // Create a new thread to run the progress update
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void launchNextActivity() {
        // Replace NextActivity.class with the class you want to launch
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
