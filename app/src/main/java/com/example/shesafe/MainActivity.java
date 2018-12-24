package com.example.shesafe;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int CAMERA_ACTION_CODE=1;
    public static  final int REQUEST_CALL=1;

    FusedLocationProviderClient fusedLocationProviderClient;
    String latitude;
    String longitude;
    String Message;
    Bitmap finalPhoto;

    public int CAMERA_PERMISSION_CODE =1;
    public int CAMERA =2;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button aboutBtn = (Button)findViewById(R.id.aboutBtn);
        Button tipsBtn= (Button)findViewById(R.id.tipsBtn);
        Button addContactBtn = (Button)findViewById(R.id.addcontactBtn);
        Button highAlertBtn = (Button)findViewById(R.id.redBtn);
        Button yellowBtn= (Button)findViewById(R.id.yellowBtn);
        Button btn_personal= (Button)findViewById(R.id.btn_personal);
        //imageView = findViewById(R.id.imageView);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient( //for geo loc
                MainActivity.this
        );

        btn_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent= new Intent(getApplicationContext(), personalActivity.class);
                startActivity(startIntent);
            }
        });


        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent= new Intent(getApplicationContext(), About.class);
                startActivity(startIntent);
            }

        });


        tipsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tipsString= "https://www.bluesecurity.co.za/11-safety-tips-for-women/";
                Uri webaddress = Uri.parse(tipsString);

                Intent gotoTips = new Intent(Intent.ACTION_VIEW, webaddress);
                if (gotoTips.resolveActivity(getPackageManager()) != null){
                    startActivity(gotoTips);
                }

            }
        });


        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent= new Intent(getApplicationContext(), addContacts.class);
                startActivity(startIntent);
            }

        });

        //---yellowbtn
        yellowBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                //fetches phone contacts from db
                final String TAG = "MyActivity";
                String location;

                int permissionCheck= ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS);

                DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
                ArrayList success = dataBaseHelper.getContacts();

                // for camera
                Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(intent, CAMERA_ACTION_CODE);

                }
                else{
                    Toast.makeText(MainActivity.this, "No app available", Toast.LENGTH_SHORT).show();
                }

                //for sending mms

                //Uri imgg= getImageUri(MainActivity.this, finalPhoto);
                //Log.d(TAG, "URI "+ imgg);

                //Log.d(TAG, "URI "+ finalPhoto);



                //condition for geoLoc
                if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                    //when both permissions are granted
                    location= getGeoLocation();

                    Message= "Hi, just letting you know my current location!: " +location;
                    //Toast.makeText(MainActivity.this, "Location: "+location, Toast.LENGTH_SHORT).show();


                }
                else{
                    //when permission is not granted
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                }



                if (permissionCheck== PackageManager.PERMISSION_GRANTED){

                    //MyMessage();
                    //String Message= "Help im in danger! My location is: ";
                    for (int i = 0; i < success.size(); i++) {
                        //Log.d(TAG, "PHONE NOS: " + success.get(i).toString().trim());
                        String phoneNumber= success.get(i).toString().trim();

                        if(!phoneNumber.toString().equals("")) {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(phoneNumber, null, Message, null, null);

                            Toast.makeText(MainActivity.this, "Message Sent!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Invalid contact", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else{

                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.SEND_SMS}, 0);
                }


            }


            @SuppressLint("MissingPermission")
            public String getGeoLocation(){
                final String TAG = "MyActivity"; //for log

                LocationManager locationManager = (LocationManager) getSystemService(
                        Context.LOCATION_SERVICE
                );
                //final String[] latitude = new String[2];
                //final String[] longitude = new String[2];
                //check cond
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                    //when loc service is enabled, get last location
                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            //Initialize Location
                            Location location = task.getResult();


                            //Check condition
                            if(location!= null){
                                //latitude[0] = String.valueOf(location.getLatitude());
                                latitude= String.valueOf(location.getLatitude());
                                longitude = String.valueOf(location.getLongitude());
                                //Log.d(TAG, "Latitude: "+ latitude+" Longitude: "+ longitude);
                            }
                            else{
                                //if loc result is null
                                LocationRequest locationRequest= new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(10000).setFastestInterval(1000).setNumUpdates(1);

                                //initialize location callback
                                LocationCallback locationCallback= new LocationCallback(){
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        //Initialize location
                                        Location location1= locationResult.getLastLocation();
                                        //set latitude and longi
                                        latitude= String.valueOf(location1.getLatitude());
                                        longitude= String.valueOf(location1.getLongitude());


                                    }
                                };
                                //req loc updates
                                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

                            }

                        }
                    });
                }
                else{
                    //when location service is not enabled.. open locatiom setting
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }

                Log.d(TAG, "Latitude: "+ latitude+" Longitude: "+ longitude);


                return "Latitude: "+ latitude+" Longitude: "+ longitude;
            }
        });// go to outside of on create method

//-----------High alert----------------------------------

        highAlertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //fetches phone contacts from db
                final String TAG = "MyActivity";
                String location;

                int permissionCheck= ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS);

                DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
                ArrayList success = dataBaseHelper.getContacts();

                //condition for geoLoc
                if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                    //when both permissions are granted
                    location= getGeoLocation();

                    Message= "Help im in danger! My location is: " +location;
                    //Toast.makeText(MainActivity.this, "Location: "+location, Toast.LENGTH_SHORT).show();


                }
                else{
                    //when permission is not granted
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                }

                if (permissionCheck== PackageManager.PERMISSION_GRANTED){

                    //MyMessage();
                    //String Message= "Help im in danger! My location is: ";
                    for (int i = 0; i < success.size(); i++) {
                        //Log.d(TAG, "PHONE NOS: " + success.get(i).toString().trim());
                        String phoneNumber= success.get(i).toString().trim();

                        if(!phoneNumber.toString().equals("")) {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(phoneNumber, null, Message, null, null);

                            Toast.makeText(MainActivity.this, "Message Sent!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Invalid contact", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{

                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.SEND_SMS}, 0);
                }

                //call police feature
                CallButton();

            }


        @SuppressLint("MissingPermission")
        public String getGeoLocation(){
            final String TAG = "MyActivity"; //for log

            LocationManager locationManager = (LocationManager) getSystemService(
                    Context.LOCATION_SERVICE
            );
            //final String[] latitude = new String[2];
            //final String[] longitude = new String[2];
            //check cond
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                //when loc service is enabled, get last location
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        //Initialize Location
                        Location location = task.getResult();


                        //Check condition
                        if(location!= null){
                            //latitude[0] = String.valueOf(location.getLatitude());
                            latitude= String.valueOf(location.getLatitude());
                            longitude = String.valueOf(location.getLongitude());
                            //Log.d(TAG, "Latitude: "+ latitude+" Longitude: "+ longitude);
                        }
                        else{
                            //if loc result is null
                            LocationRequest locationRequest= new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(10000).setFastestInterval(1000).setNumUpdates(1);

                            //initialize location callback
                            LocationCallback locationCallback= new LocationCallback(){
                                @Override
                                public void onLocationResult(LocationResult locationResult) {
                                    //Initialize location
                                    Location location1= locationResult.getLastLocation();
                                    //set latitude and longi
                                    latitude= String.valueOf(location1.getLatitude());
                                    longitude= String.valueOf(location1.getLongitude());


                                }
                            };
                            //req loc updates
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        }
                    }
                });
            }
            else{
                //when location service is not enabled.. open locatiom setting
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
            Log.d(TAG, "Latitude: "+ latitude+" Longitude: "+ longitude);

            return "Latitude: "+ latitude+" Longitude: "+ longitude;
        }
        });

    }

    //for calling police
    private void CallButton(){
        String number="02224456039";
        if(number.trim().length()>0){
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            }
            else{
                String dial= "tel:"+ number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }
    }

    // to run the call button fn
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CALL){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                CallButton();
            }
            else{
                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //for camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        final String TAG = "MyActivity";
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_ACTION_CODE && resultCode==RESULT_OK && data!= null){
            Bundle bundle= data.getExtras();
            finalPhoto= (Bitmap) bundle.get("data");
            //imageView.setImageBitmap(finalPhoto);

        //bitmap to uri conversion
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            finalPhoto.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(MainActivity.this.getContentResolver(), finalPhoto, "Title", null);
            Log.d(TAG, "URI "+ path);
            //return Uri.parse(path);

        }
    }

    //for bitmap to uri conversion
    public Uri getImageUri(Context inContext, Bitmap finalPhoto) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        finalPhoto.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), finalPhoto, "Title", null);
        return Uri.parse(path);
    }
}












