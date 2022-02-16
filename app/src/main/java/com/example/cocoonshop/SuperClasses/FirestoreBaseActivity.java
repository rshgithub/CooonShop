package com.example.cocoonshop.SuperClasses;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FirestoreBaseActivity extends AppCompatActivity {

    public static FirebaseAuth mAuth;
    public static FirebaseUser currentUser;
    public StorageReference storageReference;
    public FirebaseStorage firebaseStorage;
    public static FirebaseFirestore firebaseFirestore;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
    }

    public void getUserFullName(TextView textView) {

        DocumentReference docRef = firebaseFirestore.collection("users").document(currentUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String userFullName = document.getString("userFullName");
                        textView.setText(userFullName);

                    } else {
                        Log.e("TAG", "No such document");
                    }
                } else {
                    Log.e("TAG", "get failed with ", task.getException());
                }
            }
        });

    }

    public void getUserNickName(TextView textView) {

        DocumentReference docRef = firebaseFirestore.collection("users").document(currentUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String userNickName = document.getString("userNickName");
                        textView.setText("@" + userNickName);
                    } else {
                        Log.e("TAG", "No such document");
                    }
                } else {
                    Log.e("TAG", "get failed with ", task.getException());
                }
            }
        });

    }

    public void getUserImage(ImageView imageView) {

//        check if file exists
//        storageRef.child("users/me/file.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                // Got the download URL for 'users/me/profile.png'
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // File not found
//            }
//        });


        storageReference = firebaseStorage.getReference().child("Users_Images/" + currentUser.getUid() + ".jpeg");
        try {
            final File localFile = File.createTempFile(currentUser.getUid(), ".jpeg");

            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap imageBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    Glide.with(getApplicationContext()).load(imageBitmap).circleCrop().into(imageView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void showProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

}
