package com.example.animal_helpers;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.animal_helpers.databinding.ActivityWritePostBinding;
import com.example.animal_helpers.models.JobPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class WritePostActivity extends AppCompatActivity {

    DatabaseReference rootRef;
    private ActivityWritePostBinding binding;

    final private String TAG = getClass().getSimpleName();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    FirebaseUser user;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWritePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        rootRef = FirebaseDatabase.getInstance().getReference().child("Animal-Helpers");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        binding.tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate(binding.tvStartDate);
            }
        });
        binding.tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate(binding.tvEndDate);
            }
        });
        binding.tvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime(binding.tvStartTime);
            }
        });
        binding.tvEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime(binding.tvEndTime);
            }
        });


        binding.btnUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String uid = user.getUid();
                String body = binding.edtBody.getText().toString();
                String title = binding.edtTitle.getText().toString();
                String address = binding.edtAddress.getText().toString();
                String condition = binding.edtCondition.getText().toString();
                String startDate = binding.tvStartDate.getText().toString();
                String endDate = binding.tvEndDate.getText().toString();
                String startTime = binding.tvStartTime.getText().toString();
                String endTime = binding.tvEndTime.getText().toString();
                String employees = binding.edtEmployees.getText().toString();
                String writingDate = LocalDate.now().atStartOfDay().format(formatter);
                writeNewPost(uid, body, title, address, condition, writingDate, startDate, endDate, startTime, endTime, employees);
                Intent intent = new Intent(WritePostActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        // 툴바_글작성페이지
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar_writepost);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 활성화
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("글 작성하기");

        tb.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed(); // 뒤로가기 버튼 클릭 시 동작
            }

        });

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

    private void getTime(TextView tv) {
        final Calendar c = Calendar.getInstance(); //  Calendar 객체로 시간 얻어오기
        int h = c.get(Calendar.HOUR_OF_DAY);
        int m = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(WritePostActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tv.setText(hourOfDay + "시 " + minute + "분");
            }
        }, h, m, false); // true이면 24시각제, false이면 12시각제(오전/오후)인데 텍스트뷰 표시할 때는 24시각제
        timePickerDialog.show();
    }

    private void writeNewPost(String uid, String body, String title, String address, String condition, String writingDate, String startDate, String endDate, String startTime, String endTime, String employees) {
        JobPost post = new JobPost(uid, body, title, address, condition, writingDate, startDate, endDate, startTime, endTime, employees);
        String key = rootRef.child("posts").push().getKey();
        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + uid + "/" + key, postValues);
        Log.v("에러", childUpdates.toString());

        rootRef.updateChildren(childUpdates);

    }


}
