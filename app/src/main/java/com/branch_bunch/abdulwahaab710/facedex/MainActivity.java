package com.branch_bunch.abdulwahaab710.facedex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.sandrios.sandriosCamera.internal.SandriosCamera;
import com.sandrios.sandriosCamera.internal.configuration.CameraConfiguration;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    private static final int CAPTURE_MEDIA = 368;

    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        new SandriosCamera(activity, CAPTURE_MEDIA)
                .setShowPicker(false)
                .setMediaAction(CameraConfiguration.MEDIA_ACTION_PHOTO)
                .enableImageCropping(false)
                .launchCamera();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAPTURE_MEDIA && resultCode == RESULT_OK) {
            Log.d("File", "" + data.getStringExtra(CameraConfiguration.Arguments.FILE_PATH));
            Toast.makeText(this, "Media captured.", Toast.LENGTH_SHORT).show();
            new SendRequests().execute(data.getStringExtra(CameraConfiguration.Arguments.FILE_PATH));
            
        }
    }
}
