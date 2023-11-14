package com.example.animal_helpers.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animal_helpers.PostDetail;
import com.example.animal_helpers.R;
import com.example.animal_helpers.models.JobPost;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JobPostRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    Context context;
    private List<JobPost> itemList;
    private List<JobPost> filteredData;
    private static final String PREF_NAME = "FavoritePosts";
    private static final String KEY_FAVORITE_POSTS = "favoritePosts";
    private SharedPreferences preferences;

    public JobPostRecyclerViewAdapter(Context context, List<JobPost> data) {
        this.context = context;
        this.itemList = data;
        this.filteredData = data;
        this.preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item, parent, false);
        return new CustomViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CustomViewHolder viewHolder = (CustomViewHolder) holder;
        JobPost item = filteredData.get(position);

        ((CustomViewHolder) holder).titleText.setText(item.getTitle());
        ((CustomViewHolder) holder).addressText.setText(item.getAddress());
        ((CustomViewHolder) holder).writingDateText.setText(item.getWritingDate());
        ((CustomViewHolder) holder).favorite.setChecked(item.isFavorite());
        ((CustomViewHolder) holder).favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = viewHolder.favorite.isChecked();
                item.setFavorite(isChecked);
                saveFavoritePosts();
//                item.setFavorite(((CustomViewHolder) holder).favorite.isChecked());
                Log.v("즐겨찾기", String.valueOf(item.isFavorite()));
                // 여기에서 즐겨찾기 상태를 저장 또는 업데이트할 수 있습니다.
                // TODO : 즐겨찾기 기능 포인트
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
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
//                        JobPost vo = itemList.get(pos);
//                        String uid = vo.getUid();
                        Map<String, Object> postValues = filteredData.get(pos).toMap();
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

    private void saveFavoritePosts() {
        List<String> favoritePostIds = new ArrayList<>();
        for (JobPost post : itemList) {
            if (post.isFavorite()) {
                favoritePostIds.add(post.getUid());
            }
        }

        String serializedList = TextUtils.join(",", favoritePostIds);
        preferences.edit().putString(KEY_FAVORITE_POSTS, serializedList).apply();
    }

    // Load favorite posts from SharedPreferences
    public static List<String> loadFavoritePosts(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String serializedList = preferences.getString(KEY_FAVORITE_POSTS, "");
        if (!serializedList.isEmpty()) {
            return new ArrayList<>(Arrays.asList(TextUtils.split(serializedList, ",")));
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toLowerCase();
                Log.d("게시물 itemList 초기 갯수", String.valueOf(itemList.size()));
                if(query.isEmpty() || filteredData.isEmpty()){
                    filteredData = itemList;
                    Log.d("게시물 filteredData 초기 갯수", String.valueOf(filteredData.size()));
                } else {
                    List<JobPost> filteringList = new ArrayList<>();
                    for (JobPost item : itemList){
                        if(item.getTitle().toLowerCase().contains(query)){
                            filteringList.add(item);
                            Log.d("게시물 이름", item.getTitle());
                        }
                    }
                    filteredData = filteringList;

                }

                FilterResults results = new FilterResults();
                results.values = filteredData;
                results.count = filteredData.size();
                return results;
            }

            @SuppressLint("NotifyDataSetChanged")
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
//                filteredData.addAll((List<JobPost>) results.values);
                filteredData = (List<JobPost>) results.values;
                notifyDataSetChanged();
            }
        };
    }

//    @SuppressLint("NotifyDataSetChanged")
//    public void addItem(String Uid, String title, String address, String writingDate, boolean favorite) {
//        JobPost item = new JobPost();
//
//        item.setUid(Uid);
//        item.setTitle(title);
//        item.setAddress(address);
//        item.setWritingDate(writingDate);
//        item.setFavorite(favorite);
//
//        itemList.add(item);
//        this.notifyDataSetChanged();
//    }
}