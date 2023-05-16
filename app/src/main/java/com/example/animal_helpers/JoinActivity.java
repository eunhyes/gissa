package com.example.animal_helpers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.animal_helpers.models.UserAccount;
import com.example.animal_helpers.models.VolunteerAccount;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class JoinActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseAuth mAuth;
    private DatabaseReference DatabaseRef;
    private EditText edt_name, edt_email, edt_password, edt_password_check;
    private EditText edt_organizationName, edt_detail, edt_tel;
    private Button btn_cancel, btn_join;
    private String city, sigungu;

    private Spinner spi_1, spi_2;
    private ArrayAdapter<CharSequence> adapter, adapter_seoul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        mAuth = FirebaseAuth.getInstance();
        DatabaseRef = FirebaseDatabase.getInstance().getReference("Animal-Helpers");




        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_password = (EditText) findViewById(R.id.edt_password);
        edt_password_check = (EditText) findViewById(R.id.edt_password_check);
        edt_organizationName = (EditText) findViewById(R.id.edt_organizationName);
        edt_detail = (EditText) findViewById(R.id.edt_detail);
        edt_tel = (EditText) findViewById(R.id.edt_tel);
        btn_join = (Button) findViewById(R.id.btn_join);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        spi_1 = (Spinner) findViewById(R.id.spi_1);
        adapter = ArrayAdapter.createFromResource(this, R.array.spinner_region, android.R.layout.simple_spinner_dropdown_item);
        spi_1.setAdapter(adapter);
        spi_1.setOnItemSelectedListener(this);

        spi_2 = (Spinner) findViewById(R.id.spi_2);
        adapter_seoul = ArrayAdapter.createFromResource(this, R.array.spinner_region_seoul, android.R.layout.simple_spinner_dropdown_item);
        spi_2.setAdapter(adapter_seoul);
        spi_2.setOnItemSelectedListener(this);



        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = edt_name.getText().toString().trim();
                String email = edt_email.getText().toString().trim();
                String password = edt_password.getText().toString().trim();
                String passwordcheck = edt_password_check.getText().toString().trim();
                String organizationName = edt_organizationName.getText().toString().trim();
                String detail = edt_detail.getText().toString().trim();
                String tel = edt_tel.getText().toString().trim();



                if (!name.equals("") && !email.equals("") && !password.equals("")
                        && !organizationName.equals("") && !detail.equals("") && !tel.equals("")) {
                    if(password.equals(passwordcheck)){
                        Log.v("test", "email : " + email + " password : " + password);
                        createUser(name, email, password, organizationName, detail, tel);
                    } else {
                        Log.v("test",  ""+password+""+passwordcheck);
                        Toast.makeText(JoinActivity.this, "비밀번호가 동일하지 않습니다.", Toast.LENGTH_LONG).show();
                    }
                    // 이메일과 비밀번호가 공백이 아닌 경우
                } else {
                    // 이메일과 비밀번호가 공백인 경우
                    Toast.makeText(JoinActivity.this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spi_1) {
            // spi_1 스피너의 선택 이벤트 처리
            city = parent.getItemAtPosition(position).toString();
        } else if (parent.getId() == R.id.spi_2) {
            // spi_2 스피너의 선택 이벤트 처리
            sigungu = parent.getItemAtPosition(position).toString();
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // 아무것도 선택되지 않았을 때 처리
    }



    private void createUser(String name, String email, String password, String organizationName, String detail, String tel) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공시
                            Toast.makeText(JoinActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            UserAccount account = new UserAccount();
                            VolunteerAccount volunteer = new VolunteerAccount();
                            assert firebaseUser != null;
                            account.setUid(firebaseUser.getUid());
                            account.setEmail(firebaseUser.getEmail());
                            account.setPassword(password);
                            account.setOrganizationName(organizationName);
                            String address = city + " " + sigungu + " " + detail;
                            account.setAddress(address);
                            account.setTel(tel);

                            DatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
                            DatabaseRef.child("VolunteerAccount").child(firebaseUser.getUid()).setValue(volunteer);
                            finish();
                        } else {
                            // 계정이 중복된 경우
                            Log.w("createUserWithEmail:failure", task.getException());
                            Toast.makeText(JoinActivity.this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}