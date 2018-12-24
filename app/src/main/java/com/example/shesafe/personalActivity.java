package com.example.shesafe;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class personalActivity extends AppCompatActivity {

    EditText et_personalName, et_polNo;
    Button per_addPolNo;

    DataBaseHelper dataBaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        dataBaseHelper = new DataBaseHelper(personalActivity.this);

        //String[] polContList= new String[]{"contact"};


        et_personalName = findViewById(R.id.et_personalName);
        et_polNo = findViewById(R.id.et_polNo);
        per_addPolNo=findViewById(R.id.per_addPolNo);

        per_addPolNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String TAG = "MyActivity";
                String polPhoneNo= et_personalName.getText().toString();

                boolean success = dataBaseHelper.policeaddOne(polPhoneNo);
                Toast.makeText(personalActivity.this, "Contact Added! ", Toast.LENGTH_SHORT).show();
            }
        });

    }
}