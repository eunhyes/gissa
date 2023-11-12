package com.example.animal_helpers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.animal_helpers.databinding.ActivityJoinBinding;
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
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    private ActivityResultLauncher<Intent> launcher;
    private ActivityJoinBinding binding;

    EditText et_email;
    EditText et_tel;
    TextView tv_error_email;
    TextView tv_error_tel;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityJoinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        DatabaseRef = FirebaseDatabase.getInstance().getReference("Animal-Helpers");



        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // 하위 액티비티의 결과 처리
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            String data = intent.getStringExtra("data");
                            if (data != null) {
                                binding.edtAddress.setText(data);
                            }
                        }
                    }
                });



        /*binding.btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(JoinActivity.this, WebViewActivity.class);
                launcher.launch(i); // startActivityForResult 대신 launcher.launch() 사용

            }
        });*/

        binding.inputLayoutAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(JoinActivity.this, WebViewActivity.class);
                launcher.launch(i); // startActivityForResult 대신 launcher.launch() 사용
            }
        });


        binding.btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = binding.edtName.getText().toString().trim();
                String email = binding.edtEmail.getText().toString().trim();
                String password = binding.edtPassword.getText().toString().trim();
                String passwordcheck = binding.edtPasswordCheck.getText().toString().trim();
                String nickname = binding.edtNickname.getText().toString().trim();
                String tel = binding.edtTel.getText().toString().trim();
                String address = binding.edtAddress.getText().toString().trim();
                String address_detail = binding.edtAddressDetail.getText().toString().trim();

                address = address + " " + address_detail;


                if (!name.equals("") && !email.equals("") && !password.equals("") && !nickname.equals("") && !tel.equals("") && !address.equals("")) {
                    if (password.equals(passwordcheck)) {
                        Log.v("test", "email : " + email + " password : " + password);
                        createUser(name, email, password, nickname, tel, address);
                    } else {
                        Log.v("test", "" + password + "" + passwordcheck);
                        Toast.makeText(JoinActivity.this, "비밀번호가 동일하지 않습니다.", Toast.LENGTH_LONG).show();
                    }
                    // 이메일과 비밀번호가 공백이 아닌 경우
                } else {
                    // 이메일과 비밀번호가 공백인 경우
                    Toast.makeText(JoinActivity.this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //이메일 warning text
        et_email = findViewById(R.id.edt_email);
        tv_error_email = findViewById(R.id.tv_error_email);


        et_email.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                    tv_error_email.setText("이메일 형식으로 입력해주세요.");
                    et_email.setBackgroundResource(R.drawable.red_edittext);
                }

                else {
                    tv_error_email.setText("");
                    et_email.setBackgroundResource(R.drawable.white_edittext);
                }

            }

        });


        // 휴대폰 번호 warning text

        et_tel = findViewById(R.id.edt_tel);
        tv_error_tel = findViewById(R.id.tv_error_tel);

        et_tel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String tel = editable.toString().trim();

                if (tel.length() == 11 && isValidPhoneNumber(tel)) {
                    // 11자리 숫자가 입력되고 유효한 전화번호인 경우
                    tv_error_tel.setText("");
                    et_tel.setBackgroundResource(R.drawable.white_edittext);
                }

                else {

                    tv_error_tel.setText("'-' 빼고 11개의 숫자를 입력해주세요.");
                    et_tel.setBackgroundResource(R.drawable.red_edittext);

                }
            }

            // 유효한 전화번호인지를 체크하는 메소드
            private boolean isValidPhoneNumber(String phoneNumber) {

                return phoneNumber.matches("[0-9]+");

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




