package com.example.animal_helpers;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.animal_helpers.models.JobPost;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


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
        PostDatabaseRef = FirebaseDatabase.getInstance().getReference().child("JobPost");
        postlist = (ListView) v.findViewById(R.id.postlist);
        write_button = (Button) v.findViewById(R.id.write_button);

        getPost();




        postlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

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
    /*@Override
    public void onStart(){
        super.onStart();

        titleList.clear();
        PostDatabaseRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    //database에서 데이터 가져오기
                    titleList.add(dataSnapshot.getValue(String.class));

                }

                //리스트뷰 어뎁터 설정

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getActivity(), "error: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
*/


    protected void getPost() {


        PostDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    //database에서 데이터 가져오기

                    String Uid = dataSnapshot.getKey();
                    String title = dataSnapshot.child("title").getValue(String.class);
                    String location = dataSnapshot.child("location").getValue(String.class);
                    String store = dataSnapshot.child("store").getValue(String.class);
                    String date = dataSnapshot.child("date").getValue(String.class);

                    adapter.addItem(Uid, title, location, store, date);

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

    /*
    // 게시물 리스트를 읽어오는 함수
    class GetBoard extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute");
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "onPostExecute, " + result);

            // 배열들 초기화
            titleList.clear();
            seqList.clear();

            try {

                // 결과물이 JSONArray 형태로 넘어오기 때문에 파싱
                JSONArray jsonArray = new JSONArray(result);

                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    //optString = getString 과 비슷함
                    String title = jsonObject.optString("title");
                    String seq = jsonObject.optString("seq");

                    // title, seq 값을 변수로 받아서 배열에 추가
                    titleList.add(title);
                    seqList.add(seq);

                }

                // ListView 에서 사용할 arrayAdapter를 생성하고, ListView 와 연결
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, titleList);
                postlist.setAdapter(arrayAdapter);

                // arrayAdapter의 데이터가 변경되었을때 새로고침
                arrayAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        @Override
        protected String doInBackground(String... params) {
//
//            String userid = params[0];
//            String passwd = params[1];
            String response ="";

            try {

            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }
    }*/

}