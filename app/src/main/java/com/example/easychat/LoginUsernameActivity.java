
package com.example.easychat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.easychat.model.UserModel;
import com.example.easychat.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginUsernameActivity extends AppCompatActivity {
    EditText username;
    Button LetMeIn;
    ProgressBar progressBar;
    String phoneNumber;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_username);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        username = findViewById(R.id.login_username);
        LetMeIn = findViewById(R.id.login_let_me_in_btn);
        progressBar = findViewById(R.id.login_progress_bar);

        phoneNumber = getIntent().getStringExtra("phone");
        getUsername();
        LetMeIn.setOnClickListener(v -> {
            setUsername();
        });


    }

    void getUsername(){
        setInProgress(true);
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            setInProgress(false);
            if(task.isSuccessful()) {
                userModel = task.getResult().toObject(UserModel.class);
                if(userModel != null){
                    username.setText(userModel.getUsername());
                }

            }

        });




    }

    void setUsername() {

        String usernameInput = username.getText().toString();
        if (usernameInput.isEmpty() || usernameInput.length() < 3) {
            username.setError("Username length should be at least 3 characters");
            setInProgress(false); // Ensure progress state is reset
            return;
        }
        setInProgress(true);
        if (userModel != null) {
            userModel.setUsername(usernameInput);
        } else {
            userModel = new UserModel(phoneNumber, usernameInput, Timestamp.now(),FirebaseUtil.currentUserId());
        }

        FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(
                task -> {
                    setInProgress(false);


                        Intent intent = new Intent(LoginUsernameActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                }
        );
    }

    private void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            LetMeIn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            LetMeIn.setVisibility(View.VISIBLE);
        }
    }
}