package com.sereem.remoteworker.ui.ui_for_main.worksites.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sereem.remoteworker.R;
import com.sereem.remoteworker.model.Message;
import com.sereem.remoteworker.model.User;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context context;
    private List<Message> messages;

    private User user;

    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;

        user = User.getInstance();
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.chat_item_right, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(
                    R.layout.chat_item_left, parent, false);

        }
        return new MessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Message message = messages.get(position);

        holder.messageText.setText(message.getText());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView messageText;
//        ImageView profileIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.textMessage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).getUserId().equals(user.getUID())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
