package com.sereem.remoteworker.ui.ui_for_main.worksites.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
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
import com.sereem.remoteworker.model.workSite.WorkSite;

import org.w3c.dom.Text;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context context;
    private List<Message> messages;

    private User user;
    private HashMap<String, User> userList;

    public MessageAdapter(Context context, List<Message> messages, HashMap<String, User> userList) {
        this.context = context;
        this.messages = messages;
        this.userList = userList;

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
        holder.dateText.setText(message.getTimeStamp().substring(11, 16));
        if(getItemViewType(position) == MSG_TYPE_LEFT) {
            User user = userList.get(message.getUserId());
            String name =  user.getFirstName() + " " + user.getLastName();
            holder.nameText.setText(name);
            holder.profileIcon.setImageURI(Uri.fromFile(
                    new File(context.getCacheDir() + "/" + user.getUID() + ".jpeg")
            ));
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView messageText, nameText, dateText;
        ImageView profileIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.textMessage);
            nameText = itemView.findViewById(R.id.textName);
            dateText = itemView.findViewById(R.id.textDate);
            profileIcon = itemView.findViewById(R.id.profileIconChatLeft);
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
