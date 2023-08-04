package com.example.animal_helpers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.animal_helpers.models.UserAccount;
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
    private EditText edt_name, edt_email, edt_password, edt_password_check, edt_address, edt_address_detail;
    private EditText edt_nickname, edt_tel;
    private Button btn_cancel, btn_join, btn_address;

    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    private ActivityResultLauncher<Intent> launcher;



    @SuppressLint("MissingInflatedId")
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
        edt_nickname = (EditText) findViewById(R.id.edt_nickname);
        edt_tel = (EditText) findViewById(R.id.edt_tel);
        edt_address = (EditText) findViewById(R.id.edt_address);
        edt_address_detail = (EditText) findViewById(R.id.edt_address_detail);
        btn_join = (Button) findViewById(R.id.btn_join);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_address = (Button) findViewById(R.id.btn_address);



        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // 하위 액티비티의 결과 처리
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            String data = intent.getStringExtra("data");
                            if (data != null) {
                                edt_address.setText(data);
                            }
                        }
                    }
                });
        if (btn_address != null) {
            btn_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(JoinActivity.this, WebViewActivity.class);
                    launcher.launch(i); // startActivityForResult 대신 launcher.launch() 사용
                }
            });
        }



        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = edt_name.getText().toString().trim();
                String email = edt_email.getText().toString().trim();
                String password = edt_password.getText().toString().trim();
                String passwordcheck = edt_password_check.getText().toString().trim();
                String nickname = edt_nickname.getText().toString().trim();
                String tel = edt_tel.getText().toString().trim();
                String address = edt_address.getText().toString().trim();
                String address_detail = edt_address_detail.getText().toString().trim();

                address = address + " " + address_detail;



                if (!name.equals("") && !email.equals("") && !password.equals("")
                        && !nickname.equals("") && !tel.equals("") && !address.equals("")) {
                    if(password.equals(passwordcheck)){
                        Log.v("test", "email : " + email + " password : " + password);
                        createUser(name, email, password, nickname, tel, address);
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



    private void createUser(String name, String email, String password, String nickname, String tel, String address) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공시
                            Toast.makeText(JoinActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            UserAccount account = new UserAccount();
//                            OrganizationAccount organization = new OrganizationAccount();
                            assert firebaseUser != null;
                            account.setUid(firebaseUser.getUid());
                            account.setEmail(firebaseUser.getEmail());
                            account.setName(name);
                            account.setNickname(nickname);
                            account.setTel(tel);
                            account.setAddress(address);

                            DatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
//                            DatabaseRef.child("OrganizationAccount").child(firebaseUser.getUid()).setValue(organization);

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