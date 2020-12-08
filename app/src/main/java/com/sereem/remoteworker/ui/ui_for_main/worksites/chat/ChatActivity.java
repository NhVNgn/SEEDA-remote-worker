package com.sereem.remoteworker.ui.ui_for_main.worksites.chat;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sereem.remoteworker.R;
import com.sereem.remoteworker.model.Message;
import com.sereem.remoteworker.model.User;
import com.sereem.remoteworker.model.workSite.WorkSite;
import com.sereem.remoteworker.ui.ErrorDialog;
import com.sereem.remoteworker.ui.ui_for_main.worksites.SiteDetailActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * ChatActivity class, used for displaying and exchanging messages between users from the same
 * worksite.
 */
public class ChatActivity extends AppCompatActivity {

    DatabaseReference reference;
    EditText editMessage;
    ImageButton sendButton;
    WorkSite workSite;
    ProgressBar progressBar;
    User user;
    HashMap<String, User> userList;

    MessageAdapter messageAdapter;
    List<Message> messageList;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setupActionBar();
        setupBackButton();

        workSite = WorkSite.getChosenWorksite();
        editMessage = findViewById(R.id.editMessage);
        sendButton = findViewById(R.id.buttonSend);
        reference = FirebaseDatabase.getInstance().getReference().child(
                "chats/" + workSite.getSiteID());
        progressBar = findViewById(R.id.progressChat);
        user = User.getInstance();

        recyclerView = findViewById(R.id.messageView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        userList = SiteDetailActivity.getUserList();
        readMessages();
        setupSendButton();
    }

    private void setupSendButton() {
        sendButton.setOnClickListener(v -> {
            if(editMessage.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "The message is empty!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            sendButton.setVisibility(View.INVISIBLE);
            Message message = new Message(
                    user.getUID(),
                    editMessage.getText().toString(),
                    Calendar.getInstance().getTime().toString());

            sendMessage(message);
        });
    }

    private void sendMessage(Message message) {
        reference.push().setValue(message).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_SHORT).show();
            }else {
                ErrorDialog.show(this);
            }
            progressBar.setVisibility(View.INVISIBLE);
            sendButton.setVisibility(View.VISIBLE);
            editMessage.setText("");
        });
    }

    private void readMessages() {
        messageList = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot snapshotItem : snapshot.getChildren()) {
                    Message message = snapshotItem.getValue(Message.class);
                    messageList.add(message);
                }

                messageAdapter = new MessageAdapter(getApplicationContext(), messageList, userList);
                recyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setupActionBar(){
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#275F8E")));
        actionBar.setTitle(R.string.chat);
    }

    private void setupBackButton(){
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}