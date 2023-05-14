package com.example.animal_helpers;

import static android.content.ContentValues.TAG;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class JoinActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference DatabaseRef;
    private EditText edt_name, edt_email, edt_password_check, edt_password;
    private Button btn_cancel, btn_join;
    private RadioGroup rg_gender;
    private String gender;



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





        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String email = edt_email.getText().toString().trim();
                String password = edt_password.getText().toString().trim();
                String passwordcheck = edt_password_check.getText().toString().trim();
                String name = edt_name.getText().toString().trim();

                if (!email.equals("") && !password.equals("")) {
                    if(password.equals(passwordcheck)){
                        Log.v("test", "email : " + email + " password : " + password);
                        createUser(email, password, name);
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

    private void createUser(String email, String password, String name) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공시
                            Toast.makeText(JoinActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            UserAccount account = new UserAccount();
                            assert firebaseUser != null;
                            account.setIdToken(firebaseUser.getUid());
                            account.setEmailId(firebaseUser.getEmail());
                            account.setName(name);
                            account.setPassword(password);
                            account.setGender(gender);

                            DatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
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