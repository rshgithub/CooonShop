package com.example.cocoonshop.MainActivities.Authentication;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.cocoonshop.MainActivities.HomePageActivity;
import com.example.cocoonshop.SuperClasses.Constraints;
import com.example.cocoonshop.SuperClasses.FirestoreBaseActivity;
import com.example.cocoonshop.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import es.dmoral.toasty.Toasty;

public class SignUpActivity extends FirestoreBaseActivity {

    private ActivitySignUpBinding binding;
    Uri UriImage;
    ProgressDialog progressDialog;
    String email, password, rePassword, fullNname, nickName, phone, birthdate, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


//        -------------------------------- Pick Image From Gallery  -------------------------------------

        binding.SiginUpUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gal = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gal, Constraints.RESULT_LOAD_IMAGE);
            }
        });

//        -------------------------------- Open calender to choose birth date when click on edittext -------------------------------------
        final Calendar myCalendar = Calendar.getInstance();


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yy"; //In which you need put here

                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                binding.SiginUpEtBirthDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        binding.SiginUpEtBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SignUpActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        //----------------------------------------------------------------------------------------------------------------------------

        binding.SiginUpBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get User Full Number
                binding.countryCode.registerCarrierNumberEditText(binding.SiginUpEtPhone);

                email = binding.SiginUpEtEmail.getText().toString();
                password = binding.SiginUpEtPassword.getText().toString();
                rePassword = binding.SiginUpEtRePassword.getText().toString();
                fullNname = binding.SiginUpEtFullname.getText().toString();
                nickName = binding.SiginUpEtNickname.getText().toString();
                birthdate = binding.SiginUpEtBirthDate.getText().toString();

                phone = binding.countryCode.getFormattedFullNumber();

                if (binding.SiginUpEtFullname.getText().toString().trim().equals("")) {
                    YoYo.with(Techniques.Tada).duration(500).repeat(1).playOn(binding.SiginUpEtFullname);
                    Toast.makeText(getBaseContext(), "Please Enter your Full Name", Toast.LENGTH_SHORT).show();
                } else if (binding.SiginUpEtNickname.getText().toString().trim().equals("")) {
                    YoYo.with(Techniques.Tada).duration(500).repeat(1).playOn(binding.SiginUpEtNickname);
                    Toast.makeText(getBaseContext(), "Please Enter your User Name ", Toast.LENGTH_SHORT).show();
                } else if (binding.SiginUpEtEmail.getText().toString().trim().equals("")) {
                    YoYo.with(Techniques.Tada).duration(500).repeat(1).playOn(binding.SiginUpEtEmail);
                    Toast.makeText(getBaseContext(), "Please Enter your Email", Toast.LENGTH_SHORT).show();
                } else if (binding.SiginUpEtPassword.getText().toString().trim().equals("")) {
                    YoYo.with(Techniques.Tada).duration(500).repeat(1).playOn(binding.SiginUpEtPassword);
                    Toast.makeText(getBaseContext(), "Please Enter your Password", Toast.LENGTH_SHORT).show();
//                } else if (binding.SiginUpEtPassword.getText().toString().trim().length() < 6) {
//                    binding.SiginUpEtPassword.setError("* Password doesn't equal 6 digits");
                } else if (binding.SiginUpEtRePassword.getText().toString().trim().equals("")) {
                    YoYo.with(Techniques.Tada).duration(500).repeat(1).playOn(binding.SiginUpEtRePassword);
                    Toast.makeText(getBaseContext(), "Please ReEnter your Password again", Toast.LENGTH_SHORT).show();
                } else if (!binding.SiginUpEtRePassword.getText().toString().trim().equals(binding.SiginUpEtPassword.getText().toString().trim())) {
                    YoYo.with(Techniques.Tada).duration(500).repeat(1).playOn(binding.SiginUpEtRePassword);
                    YoYo.with(Techniques.Tada).duration(500).repeat(1).playOn(binding.SiginUpEtPassword);
                    Toast.makeText(getBaseContext(), " Passwords Doesn't Match !!", Toast.LENGTH_SHORT).show();
//                    binding.SiginUpEtPassword.setError("* Passwords Doesn't Match !!");
                } else if (binding.SiginUpEtBirthDate.getText().toString().trim().equals("")) {
                    YoYo.with(Techniques.Tada).duration(500).repeat(1).playOn(binding.SiginUpEtBirthDate);
                    Toast.makeText(getBaseContext(), "Please Enter your BirthDate", Toast.LENGTH_SHORT).show();
                } else if (binding.SiginUpEtPhone.getText().toString().trim().equals("")) {
                    YoYo.with(Techniques.Tada).duration(500).repeat(1).playOn(binding.SiginUpEtPhone);
                    Toast.makeText(getBaseContext(), "Please Enter your phone", Toast.LENGTH_SHORT).show();
                } else if (binding.SiginUpRDGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getBaseContext(), "Please Check your Gender", Toast.LENGTH_SHORT).show();
                } else if (UriImage == null) {
//                    binding.SiginUpUserImage.setImageResource(R.drawable.ic_user);
//                    UriImage = Uri.parse("android.resource://com.example.cocoonshop/drawable/ic_user");
//                    Glide.with(SignUpActivity.this).load(uriDefaultImg).circleCrop().into(binding.SiginUpUserImage);
                    Toast.makeText(getBaseContext(), "Please Choose an Image", Toast.LENGTH_SHORT).show();
                } else {

                    if (binding.SiginUpRmale.isChecked()) {
                        gender = "Male";
                    } else if (binding.SiginUpRfemale.isChecked()) {
                        gender = "Female";
                    }
                    showProgress();
                    registerUser(email, password, fullNname, nickName, phone, birthdate, gender, UriImage);
                }
            }
        });
    }

    //----------------------------------------------------------------------------------

    private void registerUser(String email, String password, String fullNname, String nickName, String phone,
                              String birthdate, String gender, Uri UriImage) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    DocumentReference documentReference = firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid());

                    Map<String, Object> user = new HashMap<>();
                    user.put("userFullName", fullNname);
                    user.put("userNickName", nickName);
                    user.put("userPhoneNumber", phone);
                    user.put("userBirthdate", birthdate);
                    user.put("userGender", gender);

                    if (UriImage != null) {
                        uploadImage();
                    }

                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toasty.success(SignUpActivity.this, "YOU MADE IT !", Toast.LENGTH_SHORT, true).show();
                        }
                    });
                    progressDialog.dismiss();
                    startActivity(new Intent(SignUpActivity.this, HomePageActivity.class));
                    finish();

                } else {
                     progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, task.getException() + "", Toast.LENGTH_SHORT).show();
                 }
            }
        });
    }

    private void uploadImage() {
        storageReference = firebaseStorage.getReference("Users_Images/" + mAuth.getCurrentUser().getUid() + ".jpeg");
        StorageTask<UploadTask.TaskSnapshot> uploadTask = storageReference.putFile(UriImage);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //this is the new way to do it
                storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        String profileImageUrl = task.getResult().toString();
                        Log.i("UploadActivity", profileImageUrl);
                    }
                });
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i( "ImageError","Image Uploaded Failed!!" + e );
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        progressDialog.setProgress((int) progress);
                        Log.d("UploadActivity", "Upload is " + progress + "% done");
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // GET IMAGE FROM GALLERY
        //requestCode بعرف الشاشة الي انتا راجع منها
        //RESULT_OK ازا رجع بشكل سليم وتمت العملية برجع اوك
        //null != data هاي للامان لو فش تطبيق كاميرا
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constraints.RESULT_LOAD_IMAGE) {
            if (resultCode == RESULT_OK && null != data) {
                //remove old pic
                binding.SiginUpUserImage.setBackgroundResource(0);
                UriImage = data.getData();
                Glide.with(this).load(UriImage).circleCrop().into(binding.SiginUpUserImage);
                Toast.makeText(getBaseContext(), "DONE ", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getBaseContext(), "CANCELED", Toast.LENGTH_SHORT).show();

            }
        }
    }

}

