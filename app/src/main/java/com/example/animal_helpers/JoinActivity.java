package com.example.animal_helpers;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.animal_helpers.models.OrganizationAccount;
import com.example.animal_helpers.models.UserAccount;
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
    private String city, district;

    private Spinner spi_city, spi_district;
    private ArrayAdapter<CharSequence> adapter;

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
        edt_organizationName = (EditText) findViewById(R.id.edt_organizationName);
        edt_detail = (EditText) findViewById(R.id.edt_detail);
        edt_tel = (EditText) findViewById(R.id.edt_tel);
        btn_join = (Button) findViewById(R.id.btn_join);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        spi_city = (Spinner) findViewById(R.id.spi_city);
        adapter = ArrayAdapter.createFromResource(this, R.array.spinner_region, android.R.layout.simple_spinner_dropdown_item);
        spi_city.setAdapter(adapter);
        spi_city.setOnItemSelectedListener(this);

        spi_district = (Spinner) findViewById(R.id.spi_district);
        spi_district.setOnItemSelectedListener(this);



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
        if (parent.getId() == R.id.spi_city) {
            // spi_city 스피너의 선택 이벤트 처리
            String selectedCity = parent.getItemAtPosition(position).toString();
            ArrayAdapter<CharSequence> districtAdapter;

            // 선택된 도시에 따라서 해당 도시의 구/군 목록을 설정
            switch (selectedCity) {
                case "서울특별시": districtAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_region_seoul, android.R.layout.simple_spinner_dropdown_item); city="서울특별시"; break;
                case "인천광역시": districtAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_region_incheon, android.R.layout.simple_spinner_dropdown_item); city="인천광역시"; break;
                case "부산광역시": districtAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_region_busan, android.R.layout.simple_spinner_dropdown_item); city="부산광역시"; break;
                case "대구광역시": districtAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_region_daegu, android.R.layout.simple_spinner_dropdown_item); city="대구광역시"; break;
                case "광주광역시": districtAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_region_gwangju, android.R.layout.simple_spinner_dropdown_item); city="광주광역시"; break;
                case "대전광역시": districtAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_region_daejeon, android.R.layout.simple_spinner_dropdown_item); city="대전광역시"; break;
                case "울산광역시": districtAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_region_ulsan, android.R.layout.simple_spinner_dropdown_item); city="울산광역시"; break;
                case "세종특별자치시": districtAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_region_sejong, android.R.layout.simple_spinner_dropdown_item); city="세종특별자치시"; break;
                case "경기도": districtAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_region_gyeonggi, android.R.layout.simple_spinner_dropdown_item); city="경기도"; break;
                case "강원도": districtAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_region_gangwon, android.R.layout.simple_spinner_dropdown_item); city="강원도"; break;
                case "충청북도": districtAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_region_chung_buk, android.R.layout.simple_spinner_dropdown_item); city="충청북도"; break;
                case "충청남도": districtAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_region_chung_nam, android.R.layout.simple_spinner_dropdown_item); city="충청남도"; break;
                case "경상북도": districtAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_region_gyeong_buk, android.R.layout.simple_spinner_dropdown_item); city="경상북도"; break;
                case "경상남도": districtAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_region_gyeong_nam, android.R.layout.simple_spinner_dropdown_item); city="경상남도"; break;
                case "전라북도": districtAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_region_jeon_buk, android.R.layout.simple_spinner_dropdown_item); city="전라북도"; break;
                case "전라남도": districtAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_region_jeon_nam, android.R.layout.simple_spinner_dropdown_item); city="전라남도"; break;
                case "제주특별자치도": districtAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_region_jeju, android.R.layout.simple_spinner_dropdown_item); city="제주특별자치도"; break;
                default: districtAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_empty, android.R.layout.simple_spinner_dropdown_item); break;
            }
            spi_district.setAdapter(districtAdapter);
        } else if (parent.getId() == R.id.spi_district) {
            district = parent.getItemAtPosition(position).toString();
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
                            OrganizationAccount organization = new OrganizationAccount();
                            assert firebaseUser != null;
                            account.setUid(firebaseUser.getUid());
                            account.setEmail(firebaseUser.getEmail());
                            account.setName(name);
                            organization.setOrganizationName(organizationName);
                            String address = city + " " + district + " " + detail;
                            organization.setAddress(address);
                            organization.setTel(tel);

                            DatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
                            DatabaseRef.child("OrganizationAccount").child(firebaseUser.getUid()).setValue(organization);

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