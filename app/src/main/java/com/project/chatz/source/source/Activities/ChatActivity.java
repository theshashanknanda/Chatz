package com.project.chatz.source.source.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.chatz.R;
import com.project.chatz.source.source.Adapters.ChatAdapter;
import com.project.chatz.source.source.Model.Message;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    public Toolbar toolbar;
    public ImageView backButton;
    public CircleImageView recieverImage;
    public TextView recieverUsername;
    public EditText editText;
    public ImageView sendButton;
    public FirebaseAuth auth;
    public FirebaseDatabase database;
    public List<Message> messageList;
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar = findViewById(R.id.chatToolbar_id);
        backButton = toolbar.findViewById(R.id.chatBackbutton_id);
        recieverImage = toolbar.findViewById(R.id.chatProfilephoto_id);
        recieverUsername = toolbar.findViewById(R.id.chatUsername_id);
        editText = findViewById(R.id.chatEditText_id);
        sendButton = findViewById(R.id.chatSendButton_id);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        messageList = new ArrayList<>();
        recyclerView = findViewById(R.id.chatRecyclerView_id);

        getSupportActionBar().hide();
        String recieverName = getIntent().getStringExtra("name");
        String recieverId = getIntent().getStringExtra("id");
        String recieverImageurl = getIntent().getStringExtra("image");
        String senderId = auth.getUid();
        ChatAdapter adapter  = new ChatAdapter(messageList, this);

        database.getReference().child("Messages").child(senderId+recieverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                messageList.clear();

                for(DataSnapshot snapshot1: snapshot.getChildren())
                {
                    Message message = snapshot1.getValue(Message.class);
                    messageList.add(message);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        backButton.setOnClickListener(v -> {
            startActivity(new Intent(ChatActivity.this, MainActivity.class));
            finish();
        });

        Glide.with(ChatActivity.this).load(recieverImageurl).placeholder(R.drawable.user).into(recieverImage);
        recieverUsername.setText(recieverName);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editText.getText().toString();

                Message message1 = new Message();
                message1.setMessage(message);
                message1.setId(senderId);
                message1.setTime(new Date().getTime());

                // sending data to the database
                database.getReference().child("Messages").child(senderId+recieverId).push().setValue(message1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        database.getReference().child("Messages").child(recieverId+senderId).push().setValue(message1);
                    }
                });
                editText.setText("");
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(ChatActivity.this, MainActivity.class));
        finish();
    }
}

