package com.example.easychat;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easychat.model.TaskModel;
import com.example.easychat.utils.AndroidUtil;
import com.example.easychat.utils.FirebaseUtil;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    private TextView taskTitle;
    private CheckBox taskCheckbox;
    private ImageButton pinTaskButton;
    private ImageButton deleteTaskButton;

    public TaskViewHolder(@NonNull View itemView) {
        super(itemView);
        taskTitle = itemView.findViewById(R.id.task_title);
        taskCheckbox = itemView.findViewById(R.id.task_checkbox);
        pinTaskButton = itemView.findViewById(R.id.pin_task_btn);
        deleteTaskButton = itemView.findViewById(R.id.delete_task_btn);
    }

    public void bind(TaskModel task) {
        taskTitle.setText(task.getTitle());
        taskCheckbox.setChecked(task.isCompleted());

        // Handle checkbox click
        taskCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
            FirebaseUtil.getTaskDocumentReference(task.getTaskId())
                    .update("isCompleted", isChecked);
        });

        // Handle pin button click
        pinTaskButton.setOnClickListener(v -> {
            task.setPinned(!task.isPinned());
            FirebaseUtil.getTaskDocumentReference(task.getTaskId())
                    .update("isPinned", task.isPinned());
        });

        // Handle delete button click
        deleteTaskButton.setOnClickListener(v -> {
            FirebaseUtil.getTaskDocumentReference(task.getTaskId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        AndroidUtil.showToast(itemView.getContext(), "Task deleted");
                    });
        });
    }
}