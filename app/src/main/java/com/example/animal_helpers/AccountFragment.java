package com.example.animal_helpers;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class AccountFragment extends Fragment {

    FirebaseStorage storage = null;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    Uri uri = null;
    ImageView iv_profile;
    Button btn_viewmyposts, btn_logout, btn_quit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account, container, false);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        iv_profile = (ImageView) v.findViewById(R.id.iv_profile);
        btn_viewmyposts = (Button) v.findViewById(R.id.btn_viewmyposts);
        btn_logout = (Button) v.findViewById(R.id.btn_logout);
        btn_quit = (Button) v.findViewById(R.id.btn_quit);
        storage = FirebaseStorage.getInstance();
        String Uid = FirebaseAuth.getInstance().getUid();

        show(Uid);

        iv_profile.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT );
            intent.setType("image/*");
            launcher.launch(intent);
        });

        btn_viewmyposts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPostFragment myPostFragment = new MyPostFragment();

                // FragmentTransaction을 사용하여 MyPostFragment를 표시
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, myPostFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    mAuth.signOut(); // 로그아웃
                    Log.d("logout", "로그아웃");

                    // 로그아웃 후 로그인 화면으로 이동
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                } else {
                    Log.d("logout", "사용자 세션이 null입니다.");
                }
            }
        });

        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = mAuth.getCurrentUser();

                if (currentUser != null) {
                    // Firebase Authentication에서 사용자 삭제
                    currentUser.delete()
                            .addOnCompleteListener(getActivity(), task -> {
                                if (task.isSuccessful()) {
                                    // 로그아웃
                                    mAuth.signOut();
                                    // 회원탈퇴 완료 후 원하는 화면으로 이동 (예: 로그인 화면)
                                    startActivity(new Intent(getActivity(), LoginActivity.class));
                                    requireActivity().finish();
                                } else {
                                    Toast.makeText(getActivity(), "회원탈퇴 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        // AccountFragment.java

        if (user != null) {
            // 사용자가 로그인한 경우
            String userId = user.getUid(); // 사용자의 UID 가져오기

            // Firebase Database 또는 Firestore를 사용하여 사용자의 닉네임을 가져오기
            // 예를 들어 Firebase Database를 사용한다면 다음과 같이 데이터를 가져올 수 있습니다.
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Animal-Helpers").child("UserAccount").child(userId);

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // 사용자의 닉네임을 가져오고 CardView의 TextView에 설정
                        String nickname = dataSnapshot.child("nickname").getValue(String.class);

                        CardView cardView = v.findViewById(R.id.cardView);
                        TextView nicknameTextView = cardView.findViewById(R.id.nicknameTextView);
                        nicknameTextView.setText(nickname);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 오류 처리
                }
            });
        } else {
            // 사용자가 로그인하지 않은 경우
            // 로그인 화면으로 이동하거나 다른 처리를 수행하세요.
        }

        return v;
    }

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    String Uid = FirebaseAuth.getInstance().getUid();
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference("Animal-helper");

                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        uri = result.getData().getData();
                        Log.d("test", uri.toString());
                        Glide.with(requireActivity()).load(uri).circleCrop().into(iv_profile);

                        if (uri != null) {
                            storageReference.child(Objects.requireNonNull(Uid)).child("img_profile").putFile(uri)
                                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            // 프래그먼트가 여전히 활성화된 상태인지 확인
                                            if (isAdded()) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(requireActivity(), "업로드에 성공했습니다", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(requireActivity(), "업로드에 실패했습니다", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                }


            });

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    private void upload(String Uid) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Animal-helper");
        storageReference.child(Uid).child("img_profile").putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(getActivity(), "업로드에 성공했습니다", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "업로드에 실패했습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void show(String Uid) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Animal-helper");
        storageReference.child(Uid).child("img_profile").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(requireActivity()).load(uri).circleCrop().into(iv_profile);
            }

        });
    }

    public void onDetach() {
        super.onDetach();
    }
}