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
import com.google.firebase.database.FirebaseDatabase;
import com.project.chatz.R;
import com.project.chatz.source.source.Activities.MainActivity;
import com.project.chatz.source.source.Activities.RegistrationActivity;
import com.project.chatz.source.source.Model.User;

public class SignUpFragment extends Fragment {

    public EditText Username, Email, Password;
    public Button signupButton;
    public FirebaseAuth auth;
    public FirebaseDatabase database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.signup_fragment,container, false);

        Username = view.findViewById(R.id.sigupUsername_id);
        Email = view.findViewById(R.id.signupEmail_id);
        Password = view.findViewById(R.id.signupPassword_id);
        signupButton = view.findViewById(R.id.signupButton_id);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        signupButton.setOnClickListener(v -> {
            // Create Account Code
            String userName = Username.getText().toString();
            String email = Email.getText().toString();
            String password = Password.getText().toString();

            if(!userName.isEmpty() && !email.isEmpty() && !password.isEmpty())
            {
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(), "Account created Succesfully", Toast.LENGTH_SHORT).show();

                                    // Saving data
                                    User user = new User(userName, email, password);
                                    String idd = task.getResult().getUser().getUid();
                                    database.getReference().child("Users").child(idd).setValue(user);
                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                    intent.putExtra("email", email);

                                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("sp", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("email", email);
                                    editor.commit();
                                    ((RegistrationActivity)getContext()).finish();
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            else{
                Toast.makeText(getContext(), "Blanks Empty", Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), userName+" "+email+" "+password+ "ok", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
