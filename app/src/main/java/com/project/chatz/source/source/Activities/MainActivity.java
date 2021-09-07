package com.project.chatz.source.source.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.project.chatz.R;
import com.project.chatz.source.source.Adapters.ListAdapter;
import com.project.chatz.source.source.Model.Message;
import com.project.chatz.source.source.Model.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public FirebaseAuth auth;
    public TextView userName;
    public FirebaseDatabase database;
    public List<User> userList;
    public RecyclerView recyclerView;
    public ListAdapter adapter;
    public String username;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String id = user.getUid();
        userList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);

        adapter = new ListAdapter(userList, MainActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);
        String email = sp.getString("email", "none");

        // getting data of this user from authentication
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                userList.clear();
                int i = 0;
                for(DataSnapshot snap: snapshot.getChildren())
                {
                    User user1 = snap.getValue(User.class);
                    user1.setId(snap.getKey());

                    if(!user1.getId().equals(auth.getUid())){
                        userList.add(user1);
                    }

                    // Getting current user's username by compararing it with shared preference's email
                    if(user1.getEmail().equals(email)){
                        username = user1.getUsername();
                    }

                    adapter.notifyDataSetChanged();
                }

                // getting last message from this user
                for(User user2: userList)
                {
                    FirebaseDatabase.getInstance().getReference().child("Messages").child(FirebaseAuth.getInstance().getUid()+user2.getId())
                            .orderByChild("time")
                            .limitToLast(1)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                    if(snapshot.hasChildren()){
                                        for(DataSnapshot snapshot1: snapshot.getChildren())
                                        {
                                            Message message = snapshot1.getValue(Message.class);
                                            user2.setLastmessage(message.getMessage());
                                        }

                                        adapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        List<User> userList1 = new ArrayList<>();

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.searchView_id);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                userList1.clear();
                for(User user: userList)
                {
                    if(user.getUsername().contains(newText)){
                        userList1.add(user);
                    }
                }
                adapter.notify(userList1);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.settings_id:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                finish();
                break;

            case R.id.logout_id:
                auth.signOut();
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
                finish();
                break;
        }

        return true;
    }
}
