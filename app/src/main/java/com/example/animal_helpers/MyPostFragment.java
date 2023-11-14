package com.example.animal_helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyPostFragment extends Fragment {

    final private String TAG = getClass().getSimpleName();
    RecyclerView recyclerView;
    JobPostRecyclerViewAdapter adapter;
    FirebaseUser user;
    Context context;
    List<JobPost> JobPostItemList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mypost, container, false);

        context = v.getContext();

        adapter = new JobPostRecyclerViewAdapter(getActivity(), JobPostItemList);
        recyclerView = (RecyclerView) v.findViewById(R.id.fragment_my_recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        String currentUserUID = user.getUid();

        DatabaseReference postsReference = FirebaseDatabase.getInstance().getReference().child("Animal-Helpers").child("posts");

        postsReference.orderByChild("writingDate").addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                JobPostItemList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    JobPost post = dataSnapshot.getValue(JobPost.class);
                    if (post != null && post.getUid().equals(currentUserUID)) {
                        JobPostItemList.add(post);
                        Log.v("postdata", String.valueOf(JobPostItemList));
                    }
                }
                Collections.sort(JobPostItemList, new Comparator<JobPost>() {
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
                    Toast.makeText(context, "error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }
}
