package com.faiyaz.faiyazzchatapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faiyaz.faiyazzchatapp.Entity.UserAdapter;
import com.faiyaz.faiyazzchatapp.Entity.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    UserAdapter adapter;

    FirebaseDatabase database;

    List<Users> usersList;

    ImageView logout ;

    ImageView cambtn,settingbtn;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        database = FirebaseDatabase.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();

            cambtn = findViewById(R.id.camBtn);
            settingbtn = findViewById(R.id.settings);



            DatabaseReference reference = database.getReference().child("user");

            usersList = new ArrayList<>();

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                        usersList.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Users users = dataSnapshot.getValue(Users.class);

                        usersList.add(users);

                    }
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

                logout = findViewById(R.id.logoutbtn);

                logout.setOnClickListener(v->{

                    Dialog dialog = new Dialog(MainActivity.this,R.style.dialog);
                    dialog.setContentView(R.layout.dialog_layout);
                    Button no , yes ;

                    yes = dialog.findViewById(R.id.logoutyesbtn);
                    no = dialog.findViewById(R.id.logoutnobtn);


                    yes.setOnClickListener(v2->{

                        FirebaseAuth.getInstance().signOut();

                        Intent intent = new Intent(MainActivity.this,Login.class);
                        startActivity(intent);
                        finish();

                    });

                    no.setOnClickListener(v3->{

                        dialog.dismiss();


                    });

                    dialog.show();


                });





            recyclerView = findViewById(R.id.mainUserRecycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new UserAdapter(MainActivity.this,usersList);
            recyclerView.setAdapter(adapter);


        settingbtn.setOnClickListener(v->{

            Intent intent = new Intent(MainActivity.this, Settings.class);
            startActivity(intent);


        });

        cambtn.setOnClickListener(v -> {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

          startActivityForResult(intent,10);

        });




            if(firebaseAuth.getCurrentUser() == null){

                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);

            }








    }


}