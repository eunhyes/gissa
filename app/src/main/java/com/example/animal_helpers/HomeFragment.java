package com.example.animal_helpers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.animal_helpers.adapter.JobPostAdapter;
import com.example.animal_helpers.models.JobPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


public class HomeFragment extends Fragment {

    final private String TAG = getClass().getSimpleName();
    ArrayList<String> titleList = new ArrayList<>();


    DatabaseReference PostDatabaseRef;

    // 사용할 컴포넌트 선언
    private ListView postlist;
    private Button write_button;
    JobPostAdapter adapter;
    FirebaseUser user;
    Map<String, Object> map;

    String[] result = null;
    String dt = "";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false);
        View v = inflater.inflate(R.layout.fragment_home, container, false);




        adapter = new JobPostAdapter();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        PostDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Animal-Helpers").child("JobPost");
        postlist = (ListView) v.findViewById(R.id.postlist);
        write_button = (Button) v.findViewById(R.id.write_button);

        getPost();




        postlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("작동", "게시글 클릭");
                Intent intent = new Intent(getActivity(), PostDetail.class);
                JobPost vo = (JobPost)adapterView.getAdapter().getItem(i);
                String uid = vo.getUid();
                intent.putExtra("uid", uid);

                startActivity(intent);
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
    @Override
    public void onResume() {
        super.onResume();
        // 화면이 갱신될 때 마다 listview를 새로고침 -> list item들이 중복되는 걸 방지
        adapter.notifyDataSetChanged();


    }


    protected void getPost() {


        PostDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    //database에서 데이터 가져오기

                    String Uid = dataSnapshot.getKey();
                    String title = dataSnapshot.child("title").getValue(String.class);
                    String location = dataSnapshot.child("location").getValue(String.class);
                    String writingDate = dataSnapshot.child("writingDate").getValue(String.class);

                    adapter.addItem(Uid, title, location, writingDate);

                }

                //리스트뷰 어뎁터 설정
                postlist.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getActivity(), "error: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


}