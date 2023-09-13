package com.example.phone_directory;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewPerson extends AppCompatActivity {
    TextView bodytxt,designation,department,title,mobile,email;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personview);
        bodytxt = findViewById(R.id.bodytxt);
        designation = findViewById(R.id.designation);
        department = findViewById(R.id.department);
        title = findViewById(R.id.title);
        mobile = findViewById(R.id.mobile);
        email = findViewById(R.id.email);

        Intent person_details=getIntent();
        bodytxt.setText(person_details.getStringExtra("name"));
        designation.setText(person_details.getStringExtra("designation"));
        department.setText(person_details.getStringExtra("department"));
        title.setText(person_details.getStringExtra("id"));
        mobile.setText(person_details.getStringExtra("mobile"));
        email.setText(person_details.getStringExtra("email"));
    }
}
