package com.faiyaz.faiyazzchatapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    Button loginbtn;
    EditText email , password ;

    TextView register ;

    FirebaseAuth auth;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        auth = FirebaseAuth.getInstance();


        loginbtn = findViewById(R.id.loginBtn);
        email = findViewById(R.id.loginEmailInput);
        password = findViewById(R.id.loginPasswordInput);



        loginbtn.setOnClickListener(v->{

            String Email = email.getText().toString();
            String Pass = password.getText().toString();


            if(TextUtils.isEmpty(Email)){

                Toast.makeText(Login.this,"ENTER THE EMAIL",Toast.LENGTH_LONG).show();

            }else if(TextUtils.isEmpty(Pass)){

                Toast.makeText(Login.this,"ENTER THE PASSWORD",Toast.LENGTH_LONG).show();
            } else if (!Email.matches(emailPattern)) {

                Toast.makeText(Login.this,"INVALID EMAIL ADDRESS",Toast.LENGTH_LONG).show();

            } else if (password.length() < 6) {
                password.setError("Input 6 Character");
                Toast.makeText(Login.this,"PASSWORD MORE THAN 6 CHARACTER",Toast.LENGTH_LONG).show();
            }else{


                auth.signInWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            try {
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            }catch (Exception e){

                                Toast.makeText(Login.this,e.getMessage(),Toast.LENGTH_LONG).show();

                            }



                        }else {

                            Toast.makeText(Login.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();


                        }

                    }
                });


            }





        });






        register = findViewById(R.id.createAccount);




        register.setOnClickListener(v->{

            Intent intent = new Intent(Login.this,Registration.class);
            startActivity(intent);
            finish();


        });



    }
}