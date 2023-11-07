package com.example.animal_helpers;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Log.d("logout", "로그아웃");
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
                    }
                    storageReference.child(Objects.requireNonNull(Uid)).child("img_profile").putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "업로드에 성공했습니다", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "업로드에 실패했습니다", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });

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
}