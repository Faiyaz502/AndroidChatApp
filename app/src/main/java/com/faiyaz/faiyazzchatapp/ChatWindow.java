package com.faiyaz.faiyazzchatapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faiyaz.faiyazzchatapp.Entity.MessageAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatWindow extends AppCompatActivity {

    String recimg, recuid, recname, senderUID;
    CircleImageView profileImg;
    TextView receiverName;
    CardView sendbtn;
    EditText textmsg;
    FirebaseAuth auth;
    FirebaseDatabase database;

    public static String senderImg;
    public static String receiverImg;

    String senderRoom, receiverRoom;
    RecyclerView chatBoxAdapter;
    List<msgClass> msgList;
    MessageAdapter messageAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_window);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Get intent data
        recname = getIntent().getStringExtra("naam");
        recimg = getIntent().getStringExtra("receiverImg");
        recuid = getIntent().getStringExtra("uid");

        // Find views
        sendbtn = findViewById(R.id.sendbtnn);
        textmsg = findViewById(R.id.textmsg);
        chatBoxAdapter = findViewById(R.id.msgadpter);
        profileImg = findViewById(R.id.profileimgg);
        receiverName = findViewById(R.id.recivername);

        // Setup UI
        Picasso.get().load(recimg).into(profileImg);
        receiverName.setText(recname);

        // Sender/receiver setup
        senderUID = auth.getUid();
        senderRoom = senderUID + recuid;
        receiverRoom = recuid + senderUID;

        // Setup chat list
        msgList = new ArrayList<>();
        messageAdapter = new MessageAdapter(ChatWindow.this, msgList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        chatBoxAdapter.setLayoutManager(linearLayoutManager);
        chatBoxAdapter.setAdapter(messageAdapter);

        // Get sender image and receiver image
        DatabaseReference reference = database.getReference()
                .child("user")
                .child(senderUID);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    senderImg = snapshot.child("profileImg").getValue(String.class);
                    receiverImg = recimg;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // Read messages
        DatabaseReference chatReference = database.getReference()
                .child("chat")
                .child(senderRoom)
                .child("messages");

        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                msgList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    msgClass messages = dataSnapshot.getValue(msgClass.class);
                    msgList.add(messages);
                }
                messageAdapter.notifyDataSetChanged();
                chatBoxAdapter.scrollToPosition(msgList.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // Send message
        sendbtn.setOnClickListener(v -> {
            String message = textmsg.getText().toString().trim();
            if (message.isEmpty()) {
                Toast.makeText(this, "Enter the text first", Toast.LENGTH_SHORT).show();
                return;
            }

            textmsg.setText("");
            Date date = new Date();

            // Create message object
            msgClass messagess = new msgClass(message, date, senderUID);

            // Use consistent path for both sides
            DatabaseReference chatsRef = database.getReference().child("chat");

            // Save to sender room
            chatsRef.child(senderRoom).child("messages")
                    .push().setValue(messagess)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Save to receiver room
                            chatsRef.child(receiverRoom).child("messages")
                                    .push().setValue(messagess);
                        } else {
                            Toast.makeText(ChatWindow.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    }
