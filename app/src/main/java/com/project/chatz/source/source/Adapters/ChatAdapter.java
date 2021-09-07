package com.project.chatz.source.source.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.project.chatz.R;
import com.project.chatz.source.source.Model.Message;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {
    public int SENDER_VIEW_TYPE = 1;
    public int RECIEVER_VIEW_TYPE = 2;

    public List<Message> messageList;
    public Context context;

    public ChatAdapter(List<Message> messageList, Context context){
        this.messageList = messageList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if(viewType == SENDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_chatbubble, parent, false);

            return new SenderViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.reciever_chatbubble, parent, false);

            return  new RecieverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);

        if(holder.getClass() == SenderViewHolder.class){
            ((SenderViewHolder)holder).text.setText(message.getMessage());
        }else{
            ((RecieverViewHolder)holder).text.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);

        if(message.id.equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }
        else{
            return RECIEVER_VIEW_TYPE;
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder{
        public TextView text, date;

        public SenderViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.senderText);
//            date = itemView.findViewById(R.id.senderDate);
        }
    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder{
        public TextView text, date;

        public RecieverViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.recieverText_id);
//            date = itemView.findViewById(R.id.recieverDate_id);
        }
    }
}

