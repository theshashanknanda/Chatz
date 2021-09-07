package com.project.chatz.source.source.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.chatz.R;
import com.project.chatz.source.source.Model.User;

import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    public CircleImageView imageView;
    public EditText editText;
    public Button saveButton;
    public ImageView backButton;
    public FirebaseAuth auth;
    public FirebaseDatabase database;
    public FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().hide();

        imageView = findViewById(R.id.progileImageView);
        editText = findViewById(R.id.usernameEditText);
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.profileBackbutton_id);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        showData();

        backButton.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            finish();
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 33);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editText.getText().toString();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("username", userName);

                database.getReference().child("Users").child(auth.getUid())
                        .updateChildren(hashMap);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        imageView.setImageURI(data.getData());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.getData() != null){
                    Uri file = data.getData();
                    imageView.setImageURI(file);
                    StorageReference reference = storage.getReference().child("profile_pictures")
                            .child(auth.getUid());

                    reference.putFile(file)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            database.getReference().child("Users").child(auth.getUid())
                                                    .child("imageurl").setValue(uri.toString());
                                        }
                                    });
                                }
                            });
                }

//                showData();
            }
        });
    }

    public void showData(){
        database.getReference().child("Users").child(auth.getUid()).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren()){
                            User user = new User();
                            user = snapshot.getValue(User.class);

                            editText.setText(user.getUsername());
                            Glide.with(getApplicationContext()).load(user.getImageurl())
                                    .placeholder(R.drawable.user)
                                    .into(imageView);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
    }
}

