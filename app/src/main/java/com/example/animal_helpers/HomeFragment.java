package com.example.animal_helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animal_helpers.adapter.JobPostRecyclerViewAdapter;
import com.example.animal_helpers.models.JobPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    final private String TAG = getClass().getSimpleName();
    // 사용할 컴포넌트 선언
    private Button write_button;
    RecyclerView recyclerView;
    JobPostRecyclerViewAdapter adapter;
    FirebaseUser user;
    Context context;
    List<JobPost> JobPostItemList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        adapter = new JobPostRecyclerViewAdapter(getActivity(), JobPostItemList);
        recyclerView = (RecyclerView) v.findViewById(R.id.fragment_home_recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        write_button = (Button) v.findViewById(R.id.write_button);

        FirebaseDatabase.getInstance().getReference().child("Animal-Helpers").child("posts").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                JobPostItemList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    JobPost post = dataSnapshot.getValue(JobPost.class);
                    JobPostItemList.add(post);

                    Log.v("postdata", String.valueOf(JobPostItemList));
                    //database에서 데이터 가져오기
//                    String Uid = dataSnapshot.getKey();
//                    String title = dataSnapshot.child("title").getValue(String.class);
//                    String location = dataSnapshot.child("location").getValue(String.class);
//                    String writingDate = dataSnapshot.child("writingDate").getValue(String.class);

//                    adapter.addItem(Uid, title, location, writingDate);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        write_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WritePostActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        // 화면이 갱신될 때 마다 listview를 새로고침 -> list item들이 중복되는 걸 방지
        adapter.notifyDataSetChanged();
    }

}