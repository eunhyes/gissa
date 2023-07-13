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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class AccountFragment extends Fragment {

    FirebaseStorage storage = null;
    Uri uri = null;
    ImageView iv_profile;
    Button select, view;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account, container, false);


        iv_profile = (ImageView) v.findViewById(R.id.iv_profile);
        select = (Button) v.findViewById(R.id.select);
        view = (Button) v.findViewById(R.id.view);
        storage = FirebaseStorage.getInstance();
        String Uid = FirebaseAuth.getInstance().getUid();

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT );
                intent.setType("image/*");
                launcher.launch(intent);
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload(Uid);

            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show(Uid);

            }
        });

        return v;
    }

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK && result.getData() != null) {
                        uri = result.getData().getData();
                        Log.d("test", uri.toString());

                    }
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
                Glide.with(requireActivity()).load(uri).into(iv_profile);
            }
        });
    }
}