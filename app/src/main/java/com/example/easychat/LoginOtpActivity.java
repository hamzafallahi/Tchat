package com.example.easychat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.easychat.utils.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginOtpActivity extends AppCompatActivity {
    private static final String TAG = "LoginOtpActivity";
    private static final long OTP_TIMEOUT = 60L;

    private String phoneNumber;
    private EditText otpInput;
    private Button nextBtn;
    private ProgressBar progressBar;
    private TextView resendOtp;
    private FirebaseAuth mAuth;
    private String verificationCode;
    private PhoneAuthProvider.ForceResendingToken resendingToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Set up the UI
        otpInput = findViewById(R.id.login_otp);
        nextBtn = findViewById(R.id.login_next_btn);
        progressBar = findViewById(R.id.login_progress_bar);
        resendOtp = findViewById(R.id.resend_otp_textview);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get phone number from extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            phoneNumber = extras.getString("phone");
            if (phoneNumber != null && phoneNumber.matches("\\+?[0-9]{10,15}")) {
                sendOtp(phoneNumber, false);
            } else {
                Toast.makeText(this, "Invalid or missing phone number", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No data passed", Toast.LENGTH_SHORT).show();
        }

        // Set up resend OTP functionality
        resendOtp.setOnClickListener(v -> {
            if (resendingToken != null) {
                sendOtp(phoneNumber, true);
            } else {
                Toast.makeText(this, "Cannot resend OTP at the moment", Toast.LENGTH_SHORT).show();
            }
        });

        nextBtn.setOnClickListener(v->{
            String otp = otpInput.getText().toString();
            PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationCode,otp);
            signIn(credential);
            setInProgress(true);


        });

    }

    private void sendOtp(String phoneNumber, boolean isResend) {
        setInProgress(true);


        PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(OTP_TIMEOUT, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                        Log.d(TAG, "onVerificationCompleted");
                        signIn(credential);
                        setInProgress(false);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Log.e(TAG, "Verification failed", e);
                        AndroidUtil.showToast(getApplicationContext(), "OTP Verification Failed: " + e.getMessage());
                        setInProgress(false);
                    }

                    @Override
                    public void onCodeSent(@NonNull String code, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        super.onCodeSent(code, token);
                        verificationCode = code;
                        resendingToken = token;
                        Log.d(TAG, "OTP sent successfully");
                        AndroidUtil.showToast(getApplicationContext(), "OTP Sent");
                        setInProgress(false);
                    }
                });

        if (isResend) {
            if (resendingToken != null) {
                builder.setForceResendingToken(resendingToken);
            }
        }

        PhoneAuthProvider.verifyPhoneNumber(builder.build());
    }

    private void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            nextBtn.setVisibility(View.VISIBLE);
        }
    }

    private void signIn(PhoneAuthCredential credential) {
        setInProgress(true);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent=new Intent(LoginOtpActivity.this, LoginUsernameActivity.class);
                            intent.putExtra("phone",phoneNumber);
                            startActivity(intent);

                        }else {
                            AndroidUtil.showToast(getApplicationContext(),"OTP Failed");
                        }
                    }
                });
    }
}
