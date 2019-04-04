package com.example.smartdispatch;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

public class RequestDetails extends AppCompatActivity implements View.OnClickListener {

    private EditText edit_TypeOfEmergency;
    private EditText edit_ScaleOfEmergency;
    private EditText edit_phoneno;
    private FirebaseAuth mAuth;
    private String email = "sdb@gmail.com";
    private String Pass = "12345678";
    private ProgressBar mProgressBar;
    private EditText editLatitude;
    private EditText editLongitude;
    private Button btn_send_sms;
    private String phoneNo;
    private String message;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    private String typeofemergency;
    private String scaleofemergency;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private GeoPoint location;
    private String vehicleid;
    private String hospitalid;

    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        edit_TypeOfEmergency = findViewById(R.id.typeofemergency);
        edit_ScaleOfEmergency = findViewById(R.id.scaleofemergency);
        mProgressBar = findViewById(R.id.progressBar);
        edit_phoneno = findViewById(R.id.phoneno);
       // GetCurrentLocation();

        findViewById(R.id.btn_send_request).setOnClickListener(this);
        btn_send_sms = findViewById(R.id.btn_send_sms);

    }

    /*private void GetCurrentLocation() {
        showDialog();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

    }*/

    public void SendReq() {


        Signin();

        //showDialog();
        typeofemergency = edit_TypeOfEmergency.getText().toString();
        scaleofemergency = edit_ScaleOfEmergency.getText().toString();
        phoneNo = edit_phoneno.getText().toString();

        //Get current gps location and store it in variable
        //String lat=editLatitude.getText().toString();
        double latitude = 23.56, longitude = 26.56;
        location = new GeoPoint(latitude, longitude);

        //Find nearby vehicle and store vehicle id in variable
        vehicleid = "1";

        //Find nearby Hospital and store hospital id in variable
        hospitalid = "2";

        message = "<#>" + "\n" + typeofemergency + "\n" + scaleofemergency + "\n" + latitude
                + "\n" + longitude  + "\n" + vehicleid + "\n" + hospitalid + "\n" + email + "\n" +"MamEVHTp4dw";
    }

    @Override
    public void onClick(View v){
        SendReq();
        CollectionReference dbreq = db.collection("Requests");
      //  Toast.makeText(RequestDetails.this, "reference to database",Toast.LENGTH_SHORT).show();
        Request request = new Request(
                email,
                typeofemergency,
                Integer.parseInt(scaleofemergency),
                location,
                vehicleid,
                hospitalid
        );

        //Toast.makeText(RequestDetails.this, "",Toast.LENGTH_SHORT).show();

        dbreq.add(request)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(RequestDetails.this, "Request Stored",Toast.LENGTH_LONG).show();
               // hideDialog();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RequestDetails.this, e.getMessage(),Toast.LENGTH_LONG).show();
                //hideDialog();
            }
        });

    }

    private void Signin() {

        showDialog();
        mAuth.signInWithEmailAndPassword(email, Pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            hideDialog();
                            Toast.makeText(RequestDetails.this, "Signed in as " + email, Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(RequestDetails.this, "Authentication Failed!", Toast.LENGTH_LONG).show();
                            hideDialog();
                        }

                    }
                });
    }

    private void ShowLiveTracking() {
        //Live tracking of driver
    }

    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }


    public void SendSMS(View view) {
        SendReq();
        Toast.makeText(getApplicationContext(), "requesting0",
                Toast.LENGTH_LONG).show();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "requesting1",
                    Toast.LENGTH_LONG).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
                Toast.makeText(getApplicationContext(), "requesting2",
                        Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
                Toast.makeText(getApplicationContext(), "requesting3",
                        Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            SendingSMS();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SendingSMS();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }

    public void SendingSMS()
    {
        Toast.makeText(getApplicationContext(), "SMS sending.",
                Toast.LENGTH_LONG).show();
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, message, null, null);
        Toast.makeText(getApplicationContext(), "SMS sent.",
                Toast.LENGTH_LONG).show();
    }


}
