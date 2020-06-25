package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.example.assignment.model.TaskModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowTask extends AppCompatActivity {
     List<TaskModel> listA,listB,listC;
     Adapter adapter;
     RecyclerView recyclerView;
     EditText search;
     FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_task);

        listA = new ArrayList<>();
        listB = new ArrayList<>();
        listC = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
        search = findViewById(R.id.search_et);
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowTask.this,MainActivity.class));
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Tasks");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listA.clear();
                listB.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    TaskModel taskModel = new TaskModel();
                    String title = postSnapshot.child("title").getValue(String.class);
                    String description = postSnapshot.child("description").getValue(String.class);
                    int status =  postSnapshot.child("status").getValue(Integer.class);
                    String path = postSnapshot.child("path").getValue(String.class);
                    taskModel.setDescription(description);
                    taskModel.setTitle(title);
                    taskModel.setStatus(status);
                    taskModel.setKey(postSnapshot.getKey());
                    taskModel.setPath(path);
                    if(status==0)
                    {
                        listA.add(taskModel);
                    }
                    else {
                        listB.add(taskModel);
                    }

                }
                listA.addAll(listB);
                adapter = new Adapter(ShowTask.this,listA);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public void filter(String text){
        List<TaskModel> tempA = new ArrayList();
        List<TaskModel> tempB = new ArrayList();

        for(TaskModel d: listA){
            if( d.status==0 && (d.getTitle().contains(text.toUpperCase()) || d.getTitle().contains(text.toLowerCase())) ){
                tempA.add(d);
            }
            else if(d.status==1 && (d.getTitle().contains(text.toUpperCase()) || d.getTitle().contains(text.toLowerCase()))){
                tempB.add(d);
            }
        }
        tempA.addAll(tempB);
        adapter.updateList(tempA);
    }
}