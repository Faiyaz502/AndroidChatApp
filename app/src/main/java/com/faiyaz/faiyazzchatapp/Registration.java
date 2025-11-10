package com.faiyaz.faiyazzchatapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class Registration extends AppCompatActivity {

    EditText username,password,rePassword,email;

    Button signupBtn;

    TextView loginNavigate;

    CircleImageView profileImg;


    FirebaseAuth auth;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        username = findViewById(R.id.regUsernameEditText);
        password = findViewById(R.id.regPasswordEditText);
        email = findViewById(R.id.regEmailEditText);
        rePassword = findViewById(R.id.regReenterEditText);
        profileImg = findViewById(R.id.logoImage);

        signupBtn = findViewById(R.id.signupButton);

        loginNavigate = findViewById(R.id.loginNavigation);



        /// Login Navigation

        loginNavigate.setOnClickListener(v->{

            Intent intent = new Intent(Registration.this,Login.class);

            startActivity(intent);

            finish();


        });

        ///  Registration

        profileImg.setOnClickListener(v->{

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);


        });










    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 10){

//            if(data == null)

        }
    }
}