package com.example.easychat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easychat.model.TaskModel;
import com.example.easychat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

public class TaskManagerActivity extends AppCompatActivity {

    private RecyclerView taskRecyclerView;
    private FloatingActionButton addTaskFab;
    private EditText searchTaskInput;
    private ImageButton searchTaskButton;
    private FirestoreRecyclerAdapter<TaskModel, TaskViewHolder> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);

        // Initialize views
        taskRecyclerView = findViewById(R.id.task_recycler_view);
        addTaskFab = findViewById(R.id.add_task_fab);
        searchTaskInput = findViewById(R.id.search_task_input);
        searchTaskButton = findViewById(R.id.search_task_btn);

        // Set up RecyclerView
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up FAB click listener
        addTaskFab.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditTaskActivity.class);
            startActivity(intent);
        });

        // Set up search functionality
        searchTaskInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = s.toString().trim();
                if (searchText.isEmpty()) {
                    loadTasks(); // Load all tasks if search text is empty
                } else {
                    searchTasks(searchText); // Filter tasks based on search text
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Load tasks initially
        loadTasks();
    }

    private void loadTasks() {
        Query query = FirebaseUtil.getTaskCollectionReference()
                .whereEqualTo("userId", FirebaseUtil.currentUserId())
                .orderBy("createdAt", Query.Direction.DESCENDING);

        setupRecyclerView(query);
    }

    private void searchTasks(String searchText) {
        Query query = FirebaseUtil.getTaskCollectionReference()
                .whereEqualTo("userId", FirebaseUtil.currentUserId())
                .orderBy("title")
                .startAt(searchText)
                .endAt(searchText + "\uf8ff");

        setupRecyclerView(query);
    }

    private void setupRecyclerView(Query query) {
        FirestoreRecyclerOptions<TaskModel> options = new FirestoreRecyclerOptions.Builder<TaskModel>()
                .setQuery(query, TaskModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<TaskModel, TaskViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TaskViewHolder holder, int position, @NonNull TaskModel model) {
                holder.bind(model);
            }

            @NonNull
            @Override
            public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
                return new TaskViewHolder(view);
            }
        };

        taskRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}