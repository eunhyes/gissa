package com.example.animal_helpers;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animal_helpers.adapter.JobPostRecyclerViewAdapter;
import com.example.animal_helpers.models.JobPost;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    JobPostRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    List<JobPost> favoriteJobPosts = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        adapter = new JobPostRecyclerViewAdapter(getActivity(),favoriteJobPosts);
        recyclerView = (RecyclerView) v.findViewById(R.id.fragment_favorite_recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));

        FirebaseDatabase.getInstance().getReference().child("Animal-Helpers").child("JobPost").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // dataSnapshot에서 데이터를 파싱하고 즐겨찾기 상태를 사용하여 목록을 필터링합니다.
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    JobPost jobPost = postSnapshot.getValue(JobPost.class);
                    if (jobPost != null && jobPost.isFavorite()) {
                        favoriteJobPosts.add(jobPost);
                    }
                }
                adapter.notifyDataSetChanged();
                // 필터링된 즐겨찾기 목록을 사용하여 RecyclerView를 업데이트합니다.
                // RecyclerViewAdapter에 필터링된 목록을 전달하고 notifyDataSetChanged()를 호출하세요.
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 오류 처리
            }


        });
        return v;
    }
}