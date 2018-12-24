
package com.example.shesafe;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class addContacts extends AppCompatActivity {

    Button btn_add, btn_view;
    EditText et_name, et_phone;
    ListView lv_customerList;

    ArrayAdapter contactArrayAdapter;
    DataBaseHelper dataBaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);

        btn_add = findViewById(R.id.btn_addContact);
        btn_view = findViewById(R.id.btn_viewAll);
        et_name = findViewById(R.id.et_name);
        et_phone = findViewById(R.id.et_phone);

        dataBaseHelper = new DataBaseHelper(addContacts.this);

        contactArrayAdapter = new ArrayAdapter<Contact_model>(addContacts.this, android.R.layout.simple_list_item_1, dataBaseHelper.getEveryone());
        lv_customerList = findViewById(R.id.lv_contactList);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String TAG = "MyActivity"; //for log

                Contact_model contact_model;
                try {
//                    contact_model = new Contact_model(-1, et_name.getText().toString(), Integer.parseInt(valueOf(et_phone.getText())));

                    contact_model = new Contact_model(-1, et_name.getText().toString(), et_phone.getText().toString());
                    Toast.makeText(addContacts.this, contact_model.toString(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(addContacts.this, "Fill details correctly", Toast.LENGTH_SHORT).show();
//                    contact_model = new Contact_model(-1, "error", 0);
                    contact_model = new Contact_model(-1, "error", "0");
                }

                DataBaseHelper dataBaseHelper = new DataBaseHelper(addContacts.this);


                boolean success = dataBaseHelper.addOne(contact_model);
                Toast.makeText(addContacts.this, "Success= " + success, Toast.LENGTH_SHORT).show();

                ArrayAdapter contactArrayAdapter = new ArrayAdapter<Contact_model>(addContacts.this, android.R.layout.simple_list_item_1, dataBaseHelper.getEveryone());
                lv_customerList = findViewById(R.id.lv_contactList);

                Log.d(TAG, "onClick: " + et_phone.getText());

            }
        });

        /* btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(addContacts.this, "View all button", Toast.LENGTH_SHORT).show();
            }
        });*/

        btn_view.setOnClickListener((v) -> {

            DataBaseHelper dataBaseHelper = new DataBaseHelper(addContacts.this);
            //List<Contact_model> everyone = dataBaseHelper.getEveryone();

            ArrayAdapter contactArrayAdapter = new ArrayAdapter<Contact_model>(addContacts.this, android.R.layout.simple_list_item_1, dataBaseHelper.getEveryone());
            lv_customerList.setAdapter(contactArrayAdapter);

            //Toast.makeText(addContacts.this, everyone.toString(), Toast.LENGTH_SHORT).show();




        });

        lv_customerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact_model clickedContact= (Contact_model) parent.getItemAtPosition(position);
                dataBaseHelper.deleteOne(clickedContact);
                ArrayAdapter contactArrayAdapter = new ArrayAdapter<Contact_model>(addContacts.this, android.R.layout.simple_list_item_1, dataBaseHelper.getEveryone());
                lv_customerList.setAdapter(contactArrayAdapter);

                Toast.makeText(addContacts.this, "Deleted "+ clickedContact.toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}