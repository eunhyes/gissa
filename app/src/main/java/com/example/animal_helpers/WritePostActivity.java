package com.example.animal_helpers;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;


public class WritePostActivity extends AppCompatActivity {
    DatabaseReference PostDatabaseRef;
    Button btn_upload;
    EditText edt_title, edt_body, edt_condition, edt_employees;
    TextView tv_startDate, tv_endDate, tv_startTime, tv_endTime;

    final private String TAG = getClass().getSimpleName();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    FirebaseUser user;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        PostDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Animal-Helpers").child("JobPost");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        try { //예외처리
            edt_title = findViewById(R.id.edt_title);
            edt_body = findViewById(R.id.edt_body);
            edt_condition = findViewById(R.id.edt_condition);
            edt_employees = findViewById(R.id.edt_employees);
            tv_startDate = findViewById(R.id.tv_startDate);
            tv_endDate = findViewById(R.id.tv_endDate);
            tv_startTime = findViewById(R.id.tv_startTime);
            tv_endTime = findViewById(R.id.tv_endTime);
            btn_upload = findViewById(R.id.btn_upload);

        } catch (NullPointerException e) {
            Log.v("아무것도 안쓴상태", "" + e);

        }



        tv_startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate(tv_startDate);
            }
        });

        tv_endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate(tv_endDate);
            }
        });

        tv_startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime(tv_startTime);
            }
        });

        tv_endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime(tv_endTime);
            }
        });



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

    private void WritePost() {

        String uid = user.getUid();
        String body = edt_body.getText().toString();
        String title = edt_title.getText().toString();
        String employees = edt_employees.getText().toString();
        String condition = edt_condition.getText().toString();
        String writingDate = LocalDate.now().atStartOfDay().format(formatter);
        String startDate = tv_startDate.getText().toString();
        String endDate = tv_endDate.getText().toString();
        String startTime = tv_startTime.getText().toString();
        String endTime = tv_endTime.getText().toString();


        PostDatabaseRef.child(uid).child("title").setValue(title);
        PostDatabaseRef.child(uid).child("body").setValue(body);
        PostDatabaseRef.child(uid).child("employees").setValue(employees);
        PostDatabaseRef.child(uid).child("condition").setValue(condition);
        PostDatabaseRef.child(uid).child("writingDate").setValue(writingDate);
        PostDatabaseRef.child(uid).child("startDate").setValue(startDate);
        PostDatabaseRef.child(uid).child("endDate").setValue(endDate);
        PostDatabaseRef.child(uid).child("startTime").setValue(startTime);
        PostDatabaseRef.child(uid).child("endTime").setValue(endTime);


    }



    private void getDate(TextView tv) {
        final Calendar c = Calendar.getInstance(); // Calendar 객체로 날짜 얻어오기
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);

        // 날짜 대화상자 객체 생성
        DatePickerDialog datePickerDialog = new DatePickerDialog(WritePostActivity.this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = year + "/" + (month + 1) + "/" + dayOfMonth;
                tv.setText(date); // 설정한 날짜를 텍스트뷰에 표시
            } // month가 0~11로 되어 있기 때문에 1을 더해서 1~12로 만듬
        }, y, m, d);
        datePickerDialog.show();
    }

    private void getTime(TextView tv){
        final Calendar c = Calendar.getInstance(); //  Calendar 객체로 시간 얻어오기
        int h = c.get(Calendar.HOUR_OF_DAY);
        int m = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(WritePostActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tv.setText(hourOfDay + "시 " + minute+"분");
            }
        }, h, m, false); // true이면 24시각제, false이면 12시각제(오전/오후)인데 텍스트뷰 표시할 때는 24시각제
        timePickerDialog.show();
    }

}
