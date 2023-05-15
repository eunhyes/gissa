package com.example.animal_helpers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class JoinActivity_choose extends AppCompatActivity {

    private Button btn_common, btn_agency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_choose);

        btn_common = (Button) findViewById(R.id.btn_common);
        btn_agency = (Button) findViewById(R.id.btn_agency);

        btn_common.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinActivity_choose.this, JoinActivity_common.class);
                startActivity(intent);
            }
        });

        btn_agency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinActivity_choose.this, JoinActivity_agency.class);
                startActivity(intent);
            }
        });
    }
}
