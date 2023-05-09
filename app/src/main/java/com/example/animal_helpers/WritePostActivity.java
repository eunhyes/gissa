package com.example.animal_helpers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class WritePostActivity extends AppCompatActivity {

    DatabaseReference PostDatabaseRef;


    final private String TAG = getClass().getSimpleName();
    EditText edt_title, edt_body;
    Button btn_upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);


        PostDatabaseRef = FirebaseDatabase.getInstance().getReference().child("JobPost");

        edt_title = findViewById(R.id.edt_title);
        edt_body = findViewById(R.id.edt_body);
        btn_upload = findViewById(R.id.btn_upload);

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WritePost();
                finish();
            }
        });
    }

    private void WritePost(){
        String body = edt_body.getText().toString();
        String title = edt_title.getText().toString();
        String sKey = PostDatabaseRef.push().getKey();

        if(sKey != null){
            PostDatabaseRef.child(sKey).child("JobPost").setValue(body);
            PostDatabaseRef.child(sKey).child("value").setValue(title);
        }
    }

    /*private void writeNewPost(String userId, String username, String title, String body) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(userId, username, title, body);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }*/
}