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

        storageReference.child(uid).child("img_profile").getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    // 이미지 다운로드 성공
                    String imageUrl = uri.toString();
                    // 이미지를 imageView에 설정
                    Glide.with(context)
                            .load(imageUrl)
                            .placeholder(R.drawable.default_profile) // 기본 이미지 설정
                            .into(imageView);
                })
                .addOnFailureListener(e -> {
                    // 이미지 다운로드 실패
                    Log.e("Firebase Storage", "Error downloading image: " + e.getMessage());
                    // 이미지가 없는 경우 기본 이미지 설정 또는 다른 처리를 수행
                    Glide.with(context)
                            .load(R.drawable.default_profile)
                            .into(imageView);
                });

    }
}
