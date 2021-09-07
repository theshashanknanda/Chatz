package com.project.chatz.source.source.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.chatz.R;
import com.project.chatz.source.source.Activities.ChatActivity;
import com.project.chatz.source.source.Activities.MainActivity;
import com.project.chatz.source.source.Activities.RegistrationActivity;
import com.project.chatz.source.source.Model.Message;
import com.project.chatz.source.source.Model.User;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.comparing;

import static android.os.Build.VERSION_CODES.R;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    public List<User> userList;
    public Context context;

    public void notify(List<User> list){
        userList = list;
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ListAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;

//         Sorting arraylist by last message time
//        for(int it = 0; it < userList.size()-1; it++) {
//            for (int j = 0; j < userList.size() - it - 1; j++) {
//                if (userList.get(j).getLastmessagetime() > userList.get(j + 1).getLastmessagetime()) {
//                    Collections.swap(userList, j, j + 1);
//                }
//            }
//        }
//        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(com.project.chatz.R.layout.userlayout, parent, false);


        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        User user = userList.get(position);

        Glide.with(context).load(user.getImageurl()).placeholder(com.project.chatz.R.drawable.user).into(holder.profilePhoto);
        holder.name.setText(user.getUsername());
        holder.lastMessage.setText(user.getLastmessage());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("id", user.getId());
                intent.putExtra("name", user.getUsername());
                intent.putExtra("image", user.getImageurl());
                ((MainActivity)context).finish();
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profilePhoto;
        public TextView name;
        public TextView lastMessage;

        public ViewHolder(@NonNull @NotNull View itemView, Context context) {
            super(itemView);

            profilePhoto = itemView.findViewById(com.project.chatz.R.id.profilephoto_id);
            name = itemView.findViewById(com.project.chatz.R.id.name_id);
            lastMessage = itemView.findViewById(com.project.chatz.R.id.lastmessage_id);
        }
    }
}
