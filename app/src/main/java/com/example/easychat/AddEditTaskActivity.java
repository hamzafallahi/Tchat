package com.example.easychat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.easychat.model.TaskModel;
import com.example.easychat.utils.AndroidUtil;
import com.example.easychat.utils.FirebaseUtil;
import com.google.firebase.Timestamp;

public class AddEditTaskActivity extends AppCompatActivity {

    private EditText taskTitleInput, taskDescriptionInput;
    private Button saveTaskButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_task);

        taskTitleInput = findViewById(R.id.task_title_input);
        taskDescriptionInput = findViewById(R.id.task_description_input);
        saveTaskButton = findViewById(R.id.save_task_btn);

        saveTaskButton.setOnClickListener(v -> saveTask());
    }

    private void saveTask() {
        String title = taskTitleInput.getText().toString().trim();
        String description = taskDescriptionInput.getText().toString().trim();

        if (title.isEmpty()) {
            AndroidUtil.showToast(this, "Title cannot be empty");
            return;
        }

        TaskModel task = new TaskModel(
                FirebaseUtil.getTaskCollectionReference().document().getId(),
                title,
                description,
                false,
                false,
                Timestamp.now()
        );

        // Set the userId for the task
        task.setUserId(FirebaseUtil.currentUserId());

        FirebaseUtil.getTaskCollectionReference()
                .document(task.getTaskId())
                .set(task)
                .addOnSuccessListener(aVoid -> {
                    AndroidUtil.showToast(this, "Task saved");
                    finish();
                })
                .addOnFailureListener(e -> {
                    AndroidUtil.showToast(this, "Failed to save task");
                });
    }
}