package com.example.animal_helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.animal_helpers.models.JobPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    final private String TAG = getClass().getSimpleName();
    // 사용할 컴포넌트 선언
    private Button write_button;
    RecyclerView recyclerView;
    JobPostRecyclerViewAdapter adapter;
    FirebaseUser user;
    Map<String, Object> map;

    String[] result = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false);
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        adapter = new JobPostRecyclerViewAdapter();
        recyclerView = (RecyclerView) v.findViewById(R.id.fragment_home_recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        write_button = (Button) v.findViewById(R.id.write_button);

        getPost();

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

    protected void getPost() {
        FirebaseDatabase.getInstance().getReference().child("Animal-Helpers").child("JobPost").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    //database에서 데이터 가져오기
                    String Uid = dataSnapshot.getKey();
                    String title = dataSnapshot.child("title").getValue(String.class);
                    String location = dataSnapshot.child("location").getValue(String.class);
                    String writingDate = dataSnapshot.child("writingDate").getValue(String.class);

                    adapter.addItem(Uid, title, location, writingDate);
                }
                //리스트뷰 어뎁터 설정
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getActivity(), "error: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    class JobPostRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context context;
        private List<JobPost> JobPostModels = new ArrayList<>();

        public JobPostRecyclerViewAdapter() {
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
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item, parent, false);
//            View itemView = LayoutInflater.inflate(R.layout.item_chat, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            CustomViewHolder customViewHolder = (CustomViewHolder) holder;

            customViewHolder.titleText.setText(JobPostModels.get(position).getTitle());
            customViewHolder.addressText.setText(JobPostModels.get(position).getAddress());
            customViewHolder.writingDateText.setText(JobPostModels.get(position).getWritingDate());
        }

        @Override
        public int getItemCount() {
            return JobPostModels.size();
        }


        private class CustomViewHolder extends RecyclerView.ViewHolder {

            public TextView titleText;
            public TextView addressText;
            public TextView writingDateText;

            public CustomViewHolder(View view) {
                super(view);

                //목록 아이템을 클릭했을 시
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            // 데이터 리스트로부터 아이템 데이터 참조.
                            JobPost vo = JobPostModels.get(pos);
                            String uid = vo.getUid();
                            Intent intent = new Intent(getActivity(), PostDetail.class);
                            intent.putExtra("uid", uid);
                            startActivity(intent);
                            notifyItemChanged(pos);
                            // TODO : use item.
                        }
                    }
                });
                this.titleText = view.findViewById(R.id.textview_title);
                this.addressText = view.findViewById(R.id.textview_address);
                this.writingDateText = view.findViewById(R.id.textview_writingDate);
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        public void addItem(String Uid, String title, String address, String writingDate) {
            JobPost item = new JobPost();

            item.setUid(Uid);
            item.setTitle(title);
            item.setAddress(address);
            item.setWritingDate(writingDate);

            JobPostModels.add(item);
            this.notifyDataSetChanged();

        }
    }

}