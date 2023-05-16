package com.example.animal_helpers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animal_helpers.models.JobPost;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import kotlinx.coroutines.Job;

public class PostDetail extends AppCompatActivity {

    DatabaseReference PostDatabaseRef;
    TextView tv_body, tv_title, tv_address, tv_employees, tv_store, tv_condition, tv_tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        Intent intent = this.getIntent();
        String uid = intent.getStringExtra("uid");
        PostDatabaseRef = FirebaseDatabase.getInstance().getReference().child("JobPost");

        tv_body = (TextView) findViewById(R.id.tv_body);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_employees = (TextView) findViewById(R.id.tv_employees);
        tv_store = (TextView) findViewById(R.id.tv_store);
        tv_condition = (TextView) findViewById(R.id.tv_condition);
        tv_tel = (TextView) findViewById(R.id.tv_tel);


//        makePost();


        PostDatabaseRef.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));

                    tv_body.setText(task.getResult().child("body").getValue(String.class));
                    tv_title.setText(task.getResult().child("title").getValue(String.class));
                    tv_address.setText(task.getResult().child("address").getValue(String.class));
                    tv_body.setText(task.getResult().child("body").getValue(String.class));
                    tv_employees.setText(task.getResult().child("employees").getValue(String.class));
                    tv_tel.setText(task.getResult().child("tel").getValue(String.class));
                    tv_condition.setText(task.getResult().child("condition").getValue(String.class));
                }
            }
        });


    }


    private void makePost() {

        PostDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

//                    JobPost post = dataSnapshot.getValue(JobPost.class);

                    tv_body.setText(dataSnapshot.child("body").getValue(String.class));
                    tv_title.setText(dataSnapshot.child("title").getValue(String.class));
                    tv_address.setText(dataSnapshot.child("address").getValue(String.class));
                    tv_body.setText(dataSnapshot.child("body").getValue(String.class));
                    tv_employees.setText(dataSnapshot.child("employees").getValue(String.class));
                    tv_tel.setText(dataSnapshot.child("tel").getValue(String.class));
                    tv_condition.setText(dataSnapshot.child("condition").getValue(String.class));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(PostDetail.this, "error: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}