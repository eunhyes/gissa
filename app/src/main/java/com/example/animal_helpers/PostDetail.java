package com.example.animal_helpers;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.animal_helpers.databinding.ActivityPostDetailBinding;
import com.example.animal_helpers.models.JobPost;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PostDetail extends AppCompatActivity {

    private ActivityPostDetailBinding binding;
    DatabaseReference rootRef;
    DatabaseReference databaseRef;
    MapView mapView;
    String uid, body, title, address, condition, writingDate, startDate, endDate, startTime, endTime, employees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String myUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        Bundle bundle = getIntent().getExtras();
        HashMap<String, Object> receivedDataMap = (HashMap<String, Object>) bundle.getSerializable("dataMap");

        if (receivedDataMap != null) {
            uid = (String) receivedDataMap.get("uid");
            body = (String) receivedDataMap.get("body");
            title = (String) receivedDataMap.get("title");
            address = (String) receivedDataMap.get("address");
            condition = (String) receivedDataMap.get("condition");
            writingDate = (String) receivedDataMap.get("writingDate");
            startDate = (String) receivedDataMap.get("startDate");
            endDate = (String) receivedDataMap.get("endDate");
            startTime = (String) receivedDataMap.get("startTime");
            endTime = (String) receivedDataMap.get("endTime");
            employees = (String) receivedDataMap.get("employees");
        }
//        Intent intent = this.getIntent();
//        String uid = intent.getStringExtra("uid");

        databaseRef = FirebaseDatabase.getInstance().getReference();
        rootRef = databaseRef.child("Animal-Helpers");

        if (myUid.equals(uid)) {
            binding.btnRegister.setEnabled(false);
            binding.btnRegister.setBackgroundColor(Color.parseColor("#999999"));
        }
        mapView = new MapView(this);
        final Geocoder geocoder = new Geocoder(this);

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostDetail.this, ChatActivity.class);
                intent.putExtra("destUid", uid);
                startActivity(intent);
            }
        });

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                JobPost post = snapshot.getValue(JobPost.class);
                if (post != null) {
                    binding.tvNickname.setText(post.getWritingDate());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        rootRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            String time;
            String date;

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));

                    //            FirebaseDatabase.getInstance().getReference().child("Animal-Helpers").child("JobPost").addListenerForSingleValueEvent(new ValueEventListener() {
//                @SuppressLint("NotifyDataSetChanged")
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for (DataSnapshot item : snapshot.getChildren()) {
//                        JobPostModels.add(item.getValue(JobPost.class));
//                    }
//                    notifyDataSetChanged();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Log.v("error", String.valueOf(error));
//
//                }
//            });

                    date = startDate + "~" + endDate;
                    time = startTime + "~" + endTime;
                    binding.tvNickname.setText(task.getResult().child("UserAccount").child(uid).child("nickname").getValue(String.class));
                    binding.tvTel.setText(task.getResult().child("UserAccount").child(uid).child("tel").getValue(String.class));
                    binding.tvAddress.setText(address);
                    binding.tvTitle.setText(title);
                    binding.tvBody.setText(body);
                    binding.tvEmployees.setText(employees);
                    binding.tvCondition.setText(condition);

                    /*date = task.getResult().child("JobPost").child(uid).child("startDate").getValue(String.class)+"~"+task.getResult().child("JobPost").child(uid).child("endDate").getValue(String.class);
                    time = task.getResult().child("JobPost").child(uid).child("startTime").getValue(String.class)+"~"+task.getResult().child("JobPost").child(uid).child("endTime").getValue(String.class);
                    binding.tvAddress  .setText(task.getResult().child("JobPost").child(uid).child("address").getValue(String.class));
                    binding.tvTitle    .setText(task.getResult().child("JobPost").child(uid).child("title").getValue(String.class));
                    binding.tvBody     .setText(task.getResult().child("JobPost").child(uid).child("body").getValue(String.class));
                    binding.tvEmployees.setText(task.getResult().child("JobPost").child(uid).child("employees").getValue(String.class));
                    binding.tvCondition.setText(task.getResult().child("JobPost").child(uid).child("condition").getValue(String.class));*/


                    String address = binding.tvAddress.getText().toString();
                    double lat = 0.0;
                    double lot = 0.0;
                    List<Address> addressList = null;

                    try {
                        addressList = geocoder.getFromLocationName(address, 10);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (addressList != null && addressList.size() > 0) {
                        Address location = addressList.get(0);
                        lat = location.getLatitude();
                        lot = location.getLongitude();

                        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(lat, lot);

                        MapPOIItem marker = new MapPOIItem();
                        marker.setItemName(address);
                        marker.setTag(0);
                        marker.setMapPoint(mapPoint);
                        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
                        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
                        mapView.addPOIItem(marker);

                        mapView.setMapCenterPoint(mapPoint, true);
                    }

                    ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
                    mapViewContainer.addView(mapView);

                    mapView.setZoomLevel(1, true);
                    mapView.zoomIn(true);
                    mapView.zoomOut(true);

                }
                binding.tvTime.setText(time);
                binding.tvDate.setText(date);
            }
        });


        // 툴바_상세페이지

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar_postdetail);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 활성화
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("상세페이지");

        tb.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed(); // 뒤로가기 버튼 클릭 시 동작
            }

        });



    }

}