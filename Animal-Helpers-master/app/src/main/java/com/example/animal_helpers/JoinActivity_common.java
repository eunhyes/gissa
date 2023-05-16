package com.example.animal_helpers;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.animal_helpers.models.UserAccount;
import com.example.animal_helpers.models.VolunteerAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class JoinActivity_common extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference DatabaseRef;
    private EditText edt_name, edt_email, edt_password_check, edt_password;
    private Button btn_cancel, btn_join;
    private RadioGroup rg_gender;
    private String gender, type;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        mAuth = FirebaseAuth.getInstance();
        DatabaseRef = FirebaseDatabase.getInstance().getReference("Animal-Helpers");


        DatePicker datePicker = (DatePicker)findViewById(R.id.vDatePicker);

        DatePicker.OnDateChangedListener listener = new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            }
        };
        datePicker.init(2000, 2, 1, listener);




        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_password = (EditText) findViewById(R.id.edt_password);
        edt_password_check = (EditText) findViewById(R.id.edt_password_check);
        btn_join = (Button) findViewById(R.id.btn_join);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        rg_gender = (RadioGroup) findViewById(R.id.rg_gender);



        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                RadioButton genderButton = (RadioButton)findViewById(i);
                gender = genderButton.getText().toString();
            }
        });



        type = "common";



        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String email = edt_email.getText().toString().trim();
                String birth = String.format("%s%s%s", datePicker.getYear(), (datePicker.getMonth() + 1), datePicker.getDayOfMonth());
                String password = edt_password.getText().toString().trim();
                String passwordcheck = edt_password_check.getText().toString().trim();
                String name = edt_name.getText().toString().trim();

                if (!email.equals("") && !birth.equals("") && !password.equals("")) {
                    if(password.equals(passwordcheck)){
                        Log.v("test", "email : " + email + " password : " + password);
                        createUser(email, password, name, birth);
                    } else {
                        Log.v("test",  ""+password+""+passwordcheck);
                        Toast.makeText(JoinActivity_common.this, "비밀번호가 동일하지 않습니다.", Toast.LENGTH_LONG).show();
                    }
                    // 이메일과 비밀번호가 공백이 아닌 경우
                } else {
                    // 이메일과 비밀번호가 공백인 경우
                    Toast.makeText(JoinActivity_common.this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinActivity_common.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }

    private void createUser(String email, String password, String name, String birth) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공시
                            Toast.makeText(JoinActivity_common.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            UserAccount user = new UserAccount();
//                            VolunteerAccount volunteer = new VolunteerAccount();
                            assert firebaseUser != null;
                            user.setUid(firebaseUser.getUid());
                            user.setEmail(firebaseUser.getEmail());
                            user.setPassword(password);
                            /*volunteer.setIdToken(firebaseUser.getUid());
                            volunteer.setName(name);
                            volunteer.setBirth(birth);
                            volunteer.setGender(gender);
                            volunteer.setType(type);
*/
                            DatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(user);
//                            DatabaseRef.child("VolunteerAccount").child(firebaseUser.getUid()).setValue(volunteer);
                            Intent intent = new Intent(JoinActivity_common.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            // 계정이 중복된 경우
                            Log.w("createUserWithEmail:failure", task.getException());
                            Toast.makeText(JoinActivity_common.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}