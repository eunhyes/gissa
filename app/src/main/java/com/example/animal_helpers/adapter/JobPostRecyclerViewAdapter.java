package com.example.animal_helpers.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animal_helpers.PostDetail;
import com.example.animal_helpers.R;
import com.example.animal_helpers.models.JobPost;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JobPostRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private List<JobPost> JobPostModels = new ArrayList<>();
    private List<JobPost> filteredList;


    public JobPostRecyclerViewAdapter(Context context) {
        this.context = context;
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
    public JobPostRecyclerViewAdapter(Context context, List<JobPost> JobPostModels){
        this.context = context;
        this.JobPostModels = JobPostModels;
        this.filteredList = new ArrayList<>(JobPostModels);
    }


    @SuppressLint("NotifyDataSetChanged")
    public void filter(String query) {
        query = query.toLowerCase();
        filteredList.clear();

        for (JobPost post : JobPostModels) {
            if (post.getTitle().toLowerCase().contains(query)) {
                filteredList.add(post);
            }
        }

        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//            CustomViewHolder customViewHolder = (CustomViewHolder) holder;
        JobPost item = JobPostModels.get(position);

        ((CustomViewHolder) holder).titleText.setText(item.getTitle());
        ((CustomViewHolder) holder).addressText.setText(item.getAddress());
        ((CustomViewHolder) holder).writingDateText.setText(item.getWritingDate());
        ((CustomViewHolder) holder).favorite.setChecked(item.isFavorite());
        ((CustomViewHolder) holder).favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setFavorite(((CustomViewHolder) holder).favorite.isChecked());
                Log.v("즐겨찾기", String.valueOf(item.isFavorite()));
                // 여기에서 즐겨찾기 상태를 저장 또는 업데이트할 수 있습니다.
            }
        });
    }

    @Override
    public int getItemCount() {
        return JobPostModels.size();
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView titleText;
        public TextView addressText;
        public TextView writingDateText;
        public CheckBox favorite;

        public CustomViewHolder(View view) {
            super(view);

            //목록 아이템을 클릭했을 시
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        // 데이터 리스트로부터 아이템 데이터 참조.
//                        JobPost vo = JobPostModels.get(pos);
//                        String uid = vo.getUid();
                        Map<String, Object> postValues = JobPostModels.get(pos).toMap();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("dataMap", (Serializable) postValues);
                        Intent intent = new Intent(context, PostDetail.class);
//                        intent.putExtra("uid", postValues);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                        notifyItemChanged(pos);
                        // TODO : use item.
                    }
                }
            });
            this.titleText = view.findViewById(R.id.textview_title);
            this.addressText = view.findViewById(R.id.textview_address);
            this.writingDateText = view.findViewById(R.id.textview_writingDate);
            this.favorite = view.findViewById(R.id.cb_favorite);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredJobPosts(List<JobPost> filteredJobPosts) {
        this.JobPostModels = filteredJobPosts;
        notifyDataSetChanged();
        // 변경 사항을 RecyclerView에 알립니다.
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addItem(String Uid, String title, String address, String writingDate, boolean favorite) {
        JobPost item = new JobPost();

        item.setUid(Uid);
        item.setTitle(title);
        item.setAddress(address);
        item.setWritingDate(writingDate);
        item.setFavorite(favorite);

        JobPostModels.add(item);
        //            this.notifyDataSetChanged();
    }
}