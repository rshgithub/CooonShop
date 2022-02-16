package com.example.cocoonshop.MainActivities.Authentication;

import androidx.annotation.NonNull;
import es.dmoral.toasty.Toasty;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.cocoonshop.SuperClasses.FirestoreBaseActivity;
import com.example.cocoonshop.MainActivities.HomePageActivity;
import com.example.cocoonshop.SharedPrefrences.SharedPreferencesFile;
import com.example.cocoonshop.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class SignInActivity extends FirestoreBaseActivity {

    private ActivitySignInBinding binding;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferencesFile sharedPreferencesFile = new SharedPreferencesFile();
        sharedPreferencesFile.SessionManager(SignInActivity.this);

        binding.SiginInBtnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = binding.SiginInEtEmail.getText().toString();
                String password = binding.SiginInEtPassword.getText().toString();

                if (email.trim().equals("")) {
                    YoYo.with(Techniques.Tada).duration(500).repeat(1).playOn(binding.SiginInEtEmail);
                    Toast.makeText(getBaseContext(), "Please Enter your Email", Toast.LENGTH_SHORT).show();
                } else if (password.trim().equals("")) {
                    YoYo.with(Techniques.Tada).duration(500).repeat(1).playOn(binding.SiginInEtPassword);
                    Toast.makeText(getBaseContext(), "Please Enter your Password", Toast.LENGTH_SHORT).show();
                } else {
                    showProgress();
                    signInAuthuntecation(email, password);

                }
            }
        });

        binding.SiginInBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                finish();
            }
        });

    }

    private void signInAuthuntecation(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
//                binding.loading.setVisibility(View.GONE);

                if (task.isSuccessful()) {
                    Toasty.success(SignInActivity.this, "Signed In!", Toast.LENGTH_SHORT, true).show();

                    Intent intent = new Intent(SignInActivity.this, HomePageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } else {
                    Toasty.error(SignInActivity.this,  task.getException().toString(), Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    }


}
