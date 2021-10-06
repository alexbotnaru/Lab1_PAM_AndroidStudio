package com.example.lab1pam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    Button notificationBtn, searchBtn, goBackBtn, magicButton;
    RadioGroup radioGroup;
    RadioButton frontCameraBtn, rearCameraBtn;
    EditText searchInput;
    ImageView imageView;
    TextView showDateTV, dateTextView;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationBtn = findViewById(R.id.notificationBtn);
        searchInput = findViewById(R.id.searchInput);
        searchBtn = findViewById(R.id.searchButton);
        frontCameraBtn = findViewById(R.id.frontCameraBtn);
        rearCameraBtn = findViewById(R.id.rearCameraBtn);
        radioGroup = findViewById(R.id.radioGroup);

        showDateTV = findViewById(R.id.showDateTV);
        dateTextView = findViewById(R.id.dateTextView);
        magicButton = findViewById(R.id.magicButton);

        //Notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Lab1 Notification", "Lab1 Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "Lab1 Notification")
                        .setContentTitle("PAM PAM PAM")
                        .setContentText("Wash your hands!")
                        .setSmallIcon(R.drawable.ic_baseline_clean_hands_24)
                        .setAutoCancel(true);

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
                        managerCompat.notify(1, builder.build());
                    }
                }, 10000);
            }
        });

        /////////Google Search\\\\\\\\\\\\

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchWords = searchInput.getText().toString();
                if (!searchWords.equals("")) {
                    googleSearchBrowser(searchWords);
                }

            }
        });


        //Permission for using camera
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    },
                    100);
        }

    }

    //search with the browser
    private void googleSearchBrowser(String words) {
        try {
            Uri uri = Uri.parse("http://www.google.com/search?q=" + words);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }
    }

    ///////// Taking photos \\\\\\\\\\\\\

    public void openFrontCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("com.google.assistant.extra.USE_FRONT_CAMERA", true);
        intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
        intent.putExtra("android.intent.extras.LENS_FACING_FRONT", REQUEST_IMAGE_CAPTURE);
        intent.putExtra("android.intent.extras.CAMERA_FACING", REQUEST_IMAGE_CAPTURE);

        //for Samsung
        intent.putExtra("camerafacing", "front");
        intent.putExtra("previous_mode", "front");

        startActivityForResult(intent, 100);
        setContentView(R.layout.activity_photo);

    }


    public void openRearCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("com.google.assistant.extra.USE_BACK_CAMERA", true);
        intent.putExtra("android.intent.extra.USE_BACK_CAMERA", true);
        intent.putExtra("android.intent.extras.LENS_FACING_BACK", REQUEST_IMAGE_CAPTURE);
        intent.putExtra("android.intent.extras.CAMERA_FACING", REQUEST_IMAGE_CAPTURE);

        //for Samsung
        intent.putExtra("camerafacing", "back");
        intent.putExtra("previous_mode", "back");

        startActivityForResult(intent, 100);
        setContentView(R.layout.activity_photo);

    }

    //show photo in other activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageView = findViewById(R.id.imageView);
        goBackBtn = findViewById(R.id.goBackButton);
        if (requestCode == 100) {
            //get capture image
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            //set capture image
            imageView.setImageBitmap(captureImage);
        }
        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_main);
            }
        });
    }
    //brainstorming

    public void showDateAndTime(View view) {
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        currentDate = simpleDateFormat.format(calendar.getTime());

        showDateTV.setText(currentDate);

        showDateTV.setVisibility(View.VISIBLE);
        dateTextView.setVisibility(View.VISIBLE);

    }
}


//******************** SCRATCH *****************************//

//search with the default search app
//    private void googleSearch(String words) {
//        try {
//            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
//            intent.putExtra(SearchManager.QUERY, words);
//            startActivity(intent);
//        } catch (ActivityNotFoundException e) {
//            e.printStackTrace();
//            googleSearchBrowser(words);
//        }
//
//    }

////////OTHER METHOD\\\\\\\\\
//    public void checkButton(View view){
//        int radioId = radioGroup.getCheckedRadioButtonId();
//
//        radioButton = findViewById(radioId);
//        Toast.makeText(this, "You selected: " + radioButton.getText() + radioButton.getId(), Toast.LENGTH_SHORT).show();
//
//
//        if(((int) radioButton.getId()) == 2131231191){
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////            intent.putExtra(MediaStore.EXTRA_OUTPUT,"");
////            intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
////            intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
////            intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
////            startActivityForResult(intent, 100);
//            intent.putExtra("com.google.assistant.extra.USE_FRONT_CAMERA", true);
//            intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
//            intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
//            intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
//
//            intent.putExtra("camerafacing", "front");
//            intent.putExtra("previous_mode", "front");
//            startActivityForResult(intent, 100);
//        } else {
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////            intent.putExtra(MediaStore.EXTRA_OUTPUT,"");
////            intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
////            intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
////            intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
////            startActivityForResult(intent, 100);
//            intent.putExtra("com.google.assistant.extra.USE_BACK_CAMERA", true);
//            intent.putExtra("android.intent.extra.USE_BACK_CAMERA", true);
//            intent.putExtra("android.intent.extras.LENS_FACING_BACK", 1);
//            intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
//
//            intent.putExtra("camerafacing", "back");
//            intent.putExtra("previous_mode", "back");
//            startActivityForResult(intent, 100);
////            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////            intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_BACK);
////            intent.putExtra("android.intent.extras.LENS_FACING_BACK", 1);
////            intent.putExtra("android.intent.extra.USE_BACK_CAMERA", true);
//
//        }
//
//
//        setContentView(R.layout.activity_photo);
//    }

