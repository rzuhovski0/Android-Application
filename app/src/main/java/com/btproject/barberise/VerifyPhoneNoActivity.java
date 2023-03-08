package com.btproject.barberise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.btproject.barberise.navigation.MenuActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneNoActivity extends AppCompatActivity {

    private String verificationCodeBySystem;
    private Button verify_btn,send_btn;
    private EditText phoneNoEnteredByTheUser,code_entered_by_user;
    private ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_no);

        verify_btn = findViewById(R.id.verify_btn);
        phoneNoEnteredByTheUser = findViewById(R.id.phone_entered_by_user);
        code_entered_by_user = findViewById(R.id.code_entered_by_user);
        progressBar = findViewById(R.id.progress_bar);
        send_btn = findViewById(R.id.send_btn);

        // Hide progress bar
        progressBar.setVisibility(View.GONE);
        verify_btn.setVisibility(View.GONE);
        code_entered_by_user.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        send_btn.setOnClickListener(view -> {

            String phoneNo = phoneNoEnteredByTheUser.getText().toString();

            if(!phoneNo.isEmpty()) {
                progressBar.setVisibility(View.VISIBLE);
                animateVerifyButtonAndTextView();
                sendVerificationCodeToUser(phoneNo);
            } else {
                phoneNoEnteredByTheUser.setError("Phone number is required");
//                phoneNoEnteredByTheUser.requestFocus();
            }
        });

    }

    private void animateVerifyButtonAndTextView()
    {
        // Calculate the animation distance
        int distance = 300;

        // Set the initial translationY value of the views to be below the screen
        code_entered_by_user.setTranslationY(distance);
        verify_btn.setTranslationY(distance);

        // Create the animation objects
        ObjectAnimator textViewAnimator = ObjectAnimator.ofFloat(code_entered_by_user, "translationY", 0);
        ObjectAnimator buttonAnimator = ObjectAnimator.ofFloat(verify_btn, "translationY", 0);

        // Set the visibility of the views to visible after the animations end
        textViewAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                code_entered_by_user.setVisibility(View.VISIBLE);
            }
        });
        buttonAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                verify_btn.setVisibility(View.VISIBLE);
            }
        });

        // Set the animation duration
        long duration = 1000;
        textViewAnimator.setDuration(duration);
        buttonAnimator.setDuration(duration);

        // Start the animations
        textViewAnimator.start();
        buttonAnimator.start();
    }

    private void sendVerificationCodeToUser(String phoneNo) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+421"+phoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                //if the phone number is not of the current device
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);

                    //store the code
                    verificationCodeBySystem = s;
                }

                //if the phone number is of the current device
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    String code = phoneAuthCredential.getSmsCode();

                    if(code == null)
                        return;

                    code_entered_by_user.setText(code);
                    verifyCode(code);

                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(VerifyPhoneNoActivity.this,
                            e.getMessage(),Toast.LENGTH_LONG).show();
                }
            };

    private void verifyCode(String codeByUser)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem,codeByUser);
        signIn(credential);
    }

    private void signIn(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Intent intent = new Intent(VerifyPhoneNoActivity.this, MenuActivity.class);

                            //To prevent user from going back to previous activity by clicking back button
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);
                        }else{
                            Toast.makeText(VerifyPhoneNoActivity.this,
                                    task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

}