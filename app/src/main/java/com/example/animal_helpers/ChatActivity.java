package com.example.animal_helpers;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animal_helpers.models.ChatModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

public class ChatActivity extends AppCompatActivity {
    private String chatRoomUid; //채팅방 하나 id
    private String myUid;       //나의 id
    private String destUid;     //상대방 uid
    private RecyclerView recyclerView;
    private Button button;
    private EditText editText;
    private FirebaseDatabase firebaseDatabase;
    private String destUser;
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy.MM.dd HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();
        sendMsg();


        // 툴바_채팅방

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar_chatactivity);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 활성화
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//      getSupportActionBar().setTitle("채팅");

        tb.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed(); // 뒤로가기 버튼 클릭 시 동작
            }

        });

    }


    private void setToolbarTitle(String nickname) {
        getSupportActionBar().setTitle(nickname);
    }

    private void init() {
        myUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        destUid = getIntent().getStringExtra("destUid");        //채팅 상대

        recyclerView = (RecyclerView) findViewById(R.id.message_recyclerview);
        button = (Button) findViewById(R.id.message_btn);
        editText = (EditText) findViewById(R.id.message_editText);

        firebaseDatabase = FirebaseDatabase.getInstance();

        if (editText.getText().toString() == null) {
            button.setEnabled(false);
        } else {
            button.setEnabled(true);
        }

        checkChatRoom();
    }


    private void sendMsg() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatModel chatModel = new ChatModel();
                chatModel.users.put(myUid, true);
                chatModel.users.put(destUid, true);

                //push() 데이터가 쌓이기 위해 채팅방 key가 생성
                if (chatRoomUid == null) {
                    Toast.makeText(ChatActivity.this, "채팅방 생성", Toast.LENGTH_SHORT).show();
                    button.setEnabled(false);
                    firebaseDatabase.getReference().child("chatrooms").push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoicd) {
                            checkChatRoom();
                        }
                    });
                } else {
                    sendMsgToDataBase();
                }
            }
        });
    }

    //작성한 메시지를 데이터베이스에 보낸다.
    private void sendMsgToDataBase() {
        if (!editText.getText().toString().equals("")) {
            ChatModel.Comment comment = new ChatModel.Comment();
            comment.uid = myUid;
            comment.message = editText.getText().toString();
            comment.timestamp = ServerValue.TIMESTAMP;
            firebaseDatabase.getReference().child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    editText.setText("");
                }
            });
        }
    }

    private void checkChatRoom() {
        //자신 key == true 일때 chatModel 가져온다.
        /* chatModel
        public Map<String,Boolean> users = new HashMap<>(); //채팅방 유저
        public Map<String, ChatModel.Comment> comments = new HashMap<>(); //채팅 메시지
        */
        firebaseDatabase.getReference().child("chatrooms").orderByChild("users/" + myUid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) //나, 상대방 id 가져온다.
                {
                    ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
                    assert chatModel != null;
                    if (chatModel.users.containsKey(destUid)) {           //상대방 id 포함돼 있을때 채팅방 key 가져옴
                        chatRoomUid = dataSnapshot.getKey();
                        button.setEnabled(true);

                        //동기화
                        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
                        recyclerView.setAdapter(new RecyclerViewAdapter());

                        //메시지 보내기
                        sendMsgToDataBase();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        List<ChatModel.Comment> comments;

        public RecyclerViewAdapter() {
            comments = new ArrayList<>();

            getDestUid();
        }

        //상대방 uid 하나(single) 읽기
        private void getDestUid() {
            firebaseDatabase.getReference().child("Animal-Helpers").child("UserAccount").child(destUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    destUser = (String) snapshot.child("name").getValue();
                    Log.v("destUser", String.valueOf(destUser));

                    //채팅 내용 읽어들임
                    getMessageList();

                    // 상대방의 nickname을 툴바 제목으로 설정
                    setToolbarTitle(destUser);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

        //채팅 내용 읽어들임
        private void getMessageList() {
            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    comments.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        comments.add(dataSnapshot.getValue(ChatModel.Comment.class));

                    }
                    notifyDataSetChanged();

                    recyclerView.scrollToPosition(comments.size() - 1);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

        @NonNull
        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_messagebox, parent, false);
            return new ViewHolder(view);
        }

        @SuppressLint("RtlHardcoded")
        @Override
        public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
            Function f = new Function();
            f.getUserProfileImage(destUid, holder.imageViewProfile, getApplicationContext());

            if (comments.get(position).uid.equals(myUid)) //나의 uid 이면
            {
                //나의 말풍선 오른쪽으로
                Log.v("error chat", "나");
                ((ViewHolder) holder).imageViewProfile.setVisibility(View.INVISIBLE);
                ((ViewHolder) holder).textViewMsg.setText(comments.get(position).message);
                ((ViewHolder) holder).textViewMsg.setBackgroundResource(R.drawable.back_et_mymsgbox);
                ((ViewHolder) holder).linearLayoutDest.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).textViewName.setVisibility(View.INVISIBLE);//상대방 레이아웃
                ((ViewHolder) holder).linearLayoutRoot.setGravity(Gravity.RIGHT);
                ((ViewHolder) holder).linearLayoutTime.setGravity(Gravity.RIGHT);
            } else {
                //상대방 말풍선 왼쪽
                Log.v("error chat", "상대방");
                ((ViewHolder) holder).imageViewProfile.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).textViewName.setText(destUser);
                ((ViewHolder) holder).linearLayoutDest.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).textViewMsg.setBackgroundResource(R.drawable.back_et_othermsgbox);
                ((ViewHolder) holder).textViewMsg.setText(comments.get(position).message);
                ((ViewHolder) holder).textViewName.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).linearLayoutRoot.setGravity(Gravity.LEFT);
                ((ViewHolder) holder).linearLayoutTime.setGravity(Gravity.LEFT);
            }
            ((ViewHolder) holder).textViewTimeStamp.setText(getDateTime(position));
        }

        public String getDateTime(int position) {
            long unixTime = (long) comments.get(position).timestamp;
            Date date = new Date(unixTime);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            return simpleDateFormat.format(date);
        }


        @Override
        public int getItemCount() {
            return comments.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textViewMsg;   //메시지 내용
            public TextView textViewName;
            public TextView textViewTimeStamp;
            public ImageView imageViewProfile;
            public LinearLayout linearLayoutDest;
            public LinearLayout linearLayoutRoot;
            public LinearLayout linearLayoutTime;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                imageViewProfile = (ImageView) itemView.findViewById(R.id.item_messagebox_profile);
                textViewMsg = (TextView) itemView.findViewById(R.id.item_messagebox_textview_msg);
                textViewName = (TextView) itemView.findViewById(R.id.item_messagebox_TextView_name);
                textViewTimeStamp = (TextView) itemView.findViewById(R.id.item_messagebox_textview_timestamp);
                linearLayoutDest = (LinearLayout) itemView.findViewById(R.id.item_messagebox_LinearLayout);
                linearLayoutRoot = (LinearLayout) itemView.findViewById(R.id.item_messagebox_root);
                linearLayoutTime = (LinearLayout) itemView.findViewById(R.id.item_messagebox_layout_timestamp);
            }
        }
    }

}


