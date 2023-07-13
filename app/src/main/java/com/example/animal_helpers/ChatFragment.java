package com.example.animal_helpers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animal_helpers.models.ChatModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;


public class ChatFragment extends Fragment {

    DatabaseReference chatroomRef;
    ChatRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        chatroomRef = FirebaseDatabase.getInstance().getReference().child("Animal-Helpers").child("chatrooms");
        adapter = new ChatRecyclerViewAdapter();

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.fragment_chat_recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));

        return v;
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        // 화면이 갱신될 때 마다 listview를 새로고침 -> list item들이 중복되는 걸 방지
        adapter.notifyDataSetChanged();
    }


    class ChatRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private List<ChatModel> chatModels = new ArrayList<>();
        private String uid;
        public ChatRecyclerViewAdapter() {
            Log.v("items", "실행중");
            uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();


            //채팅방 목록 불러오기
            FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+ uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot item : snapshot.getChildren()){
                            chatModels.add(item.getValue(ChatModel.class));
                    }
                    notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.v("error", String.valueOf(error));

                }
            });
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,parent,false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            CustomViewHolder customViewHolder = (CustomViewHolder) holder;
            String destinationUid = null;

            for(String user : chatModels.get(position).users.keySet()){
                if(!user.equals(uid)){
                    destinationUid = user;
                }
            }
            Log.v("채팅방", destinationUid);
            assert destinationUid != null;
            FirebaseDatabase.getInstance().getReference().child("Animal-Helpers").child("UserAccount").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    /*UserAccount account = snapshot.getValue(UserAccount.class);
                    assert account != null;
                    Log.v("name", Objects.requireNonNull(snapshot.getValue()).toString());
                    */
                    String destUserName = (String) snapshot.child("name").getValue();
                    customViewHolder.textView_title.setText(destUserName);
                    Log.v("destUserName",destUserName);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            //마지막메세지 띄우기
            Map<String,ChatModel.Comment> commentMap = new TreeMap<>(Collections.reverseOrder());
            commentMap.putAll(chatModels.get(position).comments);
            String lastMessageKey = (String) commentMap.keySet().toArray()[0];
            customViewHolder.textView_last_massage.setText(Objects.requireNonNull(chatModels.get(position).comments.get(lastMessageKey)).message);
            Log.v("last massage",Objects.requireNonNull(chatModels.get(position).comments.get(lastMessageKey)).message);

        }

        @Override
        public int getItemCount() {
            return chatModels.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {

            public TextView textView_title;
            public TextView textView_last_massage;
            public CustomViewHolder(View view) {
                super(view);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = getAdapterPosition() ;
                        if (pos != RecyclerView.NO_POSITION) {

                            Intent intent = new Intent(getActivity(), ChatActivity.class);

                            String destinationUid = null;
                            for(String user : chatModels.get(pos).users.keySet()){
                                if(!user.equals(uid)){
                                    destinationUid = user;
                                }
                            }
                            Log.v("destURR",destinationUid);
                            intent.putExtra("destUid", destinationUid);
                            startActivity(intent);
                            notifyItemChanged(pos) ;
                        }
                    }
                });

                textView_title = (TextView) view.findViewById(R.id.item_chat_tv_title);
                textView_last_massage = (TextView) view.findViewById(R.id.item_chat_tv_last_massage);

            }
        }
    }



    /*protected void getchatrooms() {


        chatroomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    //database에서 데이터 가져오기
                    String name = dataSnapshot.child("name").getValue(String.class);

                    adapter.addItem(name);

                }

                //리스트뷰 어뎁터 설정
                chatroomlist.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getActivity(), "error: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }*/
}