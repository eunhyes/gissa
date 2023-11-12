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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animal_helpers.adapter.JobPostRecyclerViewAdapter;
import com.example.animal_helpers.models.JobPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {

    final private String TAG = getClass().getSimpleName();
    // 사용할 컴포넌트 선언
    private Button write_button;
    RecyclerView recyclerView;
    JobPostRecyclerViewAdapter adapter;
    FirebaseUser user;
    Context context;
    List<JobPost> jobPostItemList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        context = v.getContext();

        adapter = new JobPostRecyclerViewAdapter(getActivity(), jobPostItemList);
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
                jobPostItemList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    JobPost post = dataSnapshot.getValue(JobPost.class);
                    if (post != null) {
                        jobPostItemList.add(post);
                        Log.v("postdata", String.valueOf(jobPostItemList));
                    }
                }
                // 최신 글이 먼저 오도록 정렬
                Collections.sort(jobPostItemList, new Comparator<JobPost>() {
                    @Override
                    public int compare(JobPost o1, JobPost o2) {
                        return o2.getWritingDate().compareTo(o1.getWritingDate());
                    }
                });
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (error.getCode() != DatabaseError.PERMISSION_DENIED) {
                    // 파일을 찾지 못했을 때, 권한 오류가 아닌 경우에만 토스트 메시지 표시
                    Toast.makeText(context, "error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
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