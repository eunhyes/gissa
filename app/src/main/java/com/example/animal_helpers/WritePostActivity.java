package com.example.animal_helpers;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class WritePostActivity extends AppCompatActivity {

    DatabaseReference PostDatabaseRef;


    final private String TAG = getClass().getSimpleName();
    EditText edt_title, edt_body, edt_location, edt_store;
    Button btn_upload;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);


        PostDatabaseRef = FirebaseDatabase.getInstance().getReference().child("JobPost");


        try { //예외처리
            edt_title = findViewById(R.id.edt_title);
            edt_store = findViewById(R.id.edt_store);
            edt_location = findViewById(R.id.edt_location);
            edt_body = findViewById(R.id.edt_body);
            btn_upload = findViewById(R.id.btn_upload);
        }catch (NullPointerException e){
            Log.v("아무것도 안쓴상태",""+e);

        }

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WritePost();
//                finish();
                Intent intent = new Intent(WritePostActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void WritePost(){
        String body = edt_body.getText().toString();
        String title = edt_title.getText().toString();
        String location = edt_location.getText().toString();
        String store = edt_store.getText().toString();
        String date = LocalDate.now().atStartOfDay().format(formatter);

        String sKey = PostDatabaseRef.push().getKey();

        if(sKey != null){


            PostDatabaseRef.child(sKey).child("title").setValue(title);
            PostDatabaseRef.child(sKey).child("body").setValue(body);
            PostDatabaseRef.child(sKey).child("location").setValue(location);
            PostDatabaseRef.child(sKey).child("store").setValue(store);
            PostDatabaseRef.child(sKey).child("date").setValue(date);
        }
    }




/*private void writeNewPost(String userId, String username, String title, String body) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = PostDatabaseRef.push().getKey();
        JobPost post = new JobPost(userId, username, title, body);
        Map<String, Object> postValues = JobPost.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        PostDatabaseRef.updateChildren(childUpdates);
    }*/

}