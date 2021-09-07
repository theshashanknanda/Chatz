package com.project.chatz.source.source.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.chatz.R;
import com.project.chatz.source.source.Activities.MainActivity;
import com.project.chatz.source.source.Activities.RegistrationActivity;

import org.jetbrains.annotations.NotNull;

public class SignInFragment extends Fragment {
    public FirebaseAuth auth;
    public FirebaseDatabase database;
    public EditText Password, Email;
    public Button signinButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.signin_fragment,container, false);

        signinButton = view.findViewById(R.id.signinButton_id);
        Email = view.findViewById(R.id.signinEmail_id);
        Password = view.findViewById(R.id.signinPassword_id);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        signinButton.setOnClickListener(v -> {
            String email = Email.getText().toString();
            String password = Password.getText().toString();

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(getContext(), MainActivity.class);

                                SharedPreferences sharedPreferences = getContext().getSharedPreferences("sp", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("email", email);
                                editor.commit();
                                ((RegistrationActivity)getContext()).finish();
                                startActivity(intent);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        return view;
    }
}
