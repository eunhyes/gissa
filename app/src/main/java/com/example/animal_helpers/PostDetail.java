package com.example.animal_helpers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animal_helpers.models.JobPost;
import com.example.animal_helpers.models.OrganizationAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;

import kotlinx.coroutines.Job;

public class PostDetail extends AppCompatActivity {

    DatabaseReference PostDatabaseRef, rootRef, OrganizationRef;
    TextView tv_body, tv_title, tv_address, tv_employees, tv_store, tv_condition, tv_tel, tv_time, tv_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        Intent intent = this.getIntent();
        String uid = intent.getStringExtra("uid");
        rootRef = FirebaseDatabase.getInstance().getReference().child("Animal-Helpers");
        PostDatabaseRef = rootRef.child("JobPost");
        OrganizationRef = rootRef.child("OrganizationAccount");

        tv_body = (TextView) findViewById(R.id.tv_body);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_employees = (TextView) findViewById(R.id.tv_employees);
        tv_store = (TextView) findViewById(R.id.tv_store);
        tv_condition = (TextView) findViewById(R.id.tv_condition);
        tv_tel = (TextView) findViewById(R.id.tv_tel);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_time = (TextView) findViewById(R.id.tv_time);




        rootRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            String time;
            String date;
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));

                    date = task.getResult().child("JobPost").child(uid).child("startDate").getValue(String.class)+"~"+task.getResult().child("JobPost").child(uid).child("endDate").getValue(String.class);
                    time = task.getResult().child("JobPost").child(uid).child("startTime").getValue(String.class)+"~"+task.getResult().child("JobPost").child(uid).child("endTime").getValue(String.class);
                    tv_store    .setText(task.getResult().child("OrganizationAccount").child(uid).child("organizationName").getValue(String.class));
                    tv_address  .setText(task.getResult().child("OrganizationAccount").child(uid).child("address").getValue(String.class));
                    tv_tel      .setText(task.getResult().child("OrganizationAccount").child(uid).child("tel").getValue(String.class));
                    tv_title    .setText(task.getResult().child("JobPost").child(uid).child("title").getValue(String.class));
                    tv_body     .setText(task.getResult().child("JobPost").child(uid).child("body").getValue(String.class));
                    tv_employees.setText(task.getResult().child("JobPost").child(uid).child("employees").getValue(String.class));
                    tv_condition.setText(task.getResult().child("JobPost").child(uid).child("condition").getValue(String.class));
                }
                tv_time.setText(time);
                tv_date.setText(date);
            }

        });

    }
}