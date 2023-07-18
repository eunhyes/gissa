package com.example.animal_helpers;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Function {
    public void getUserProfileImage(String uid, ImageView imageView, Context context){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Animal-helper");
        storageReference.child(uid).child("img_profile").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //이미지 로드 성공시

                Glide.with(context)
                        .load(uri)
                        .into(imageView);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //이미지 로드 실패시
                Toast.makeText(context, "실패", Toast.LENGTH_SHORT).show();
                Log.v("에러", String.valueOf(exception));


            }

        });

    }
}
