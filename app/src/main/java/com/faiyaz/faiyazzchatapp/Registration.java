package com.faiyaz.faiyazzchatapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.faiyaz.faiyazzchatapp.Entity.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Registration extends AppCompatActivity {

    EditText username,password,rePassword,email;

    Button signupBtn;

    TextView loginNavigate;

    CircleImageView profileImg;


    FirebaseAuth auth;

    FirebaseDatabase database;

    FirebaseStorage storage;

    Uri imageUri;

    String imageuri;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog ;



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

        //dialog for loading

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("On Process......");
        progressDialog.setCancelable(false);

        //----------

        username = findViewById(R.id.regUsernameEditText);
        password = findViewById(R.id.regPasswordEditText);
        email = findViewById(R.id.regEmailEditText);
        rePassword = findViewById(R.id.regReenterEditText);
        profileImg = findViewById(R.id.userImg);
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

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


        /// SignUp

        signupBtn.setOnClickListener(v->{

            String name = username.getText().toString().trim();
            String Password = password.getText().toString().trim();
            String Email = email.getText().toString().trim();
            String RePass = rePassword.getText().toString().trim();


            String status  = "Hey, I'm Using this App";


            //Conditions

            if(TextUtils.isEmpty(name) || TextUtils.isEmpty(Email)||TextUtils.isEmpty(Password)||TextUtils.isEmpty(RePass)){
                progressDialog.dismiss();
                Toast.makeText(Registration.this,"Please Enter All Fields",Toast.LENGTH_SHORT).show();


            }else if(!Email.matches(emailPattern)){
                progressDialog.dismiss();
                email.setError("Invalid Email Address!!");
            }else if(Password.length() < 6){
                progressDialog.dismiss();
                    password.setError("Password should be more than 6 Character!!!!");

            } else if (!Password.equals(RePass)) {
                progressDialog.dismiss();

                password.setError("Invalid Password Match!!!");

            }else {

                    auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                progressDialog.show();

                                String id = task.getResult().getUser().getUid();
                                DatabaseReference reference = database.getReference().child("user").child(id);

                                // If user selected a profile image
                                if (imageuri != null && !imageuri.isEmpty()) {

                                    Users users = new Users();
                                    users.setEmail(Email);
                                    users.setUserId(id);
                                    users.setPassword(Password);
                                    users.setProfileImg(imageuri); // local file path instead of Firebase URL
                                    users.setUsername(name);
                                    users.setStatus(status);

                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> dbTask) {
                                            if (dbTask.isSuccessful()) {
                                                Toast.makeText(Registration.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(Registration.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(Registration.this, "Error in Creating User", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                } else {
                                    // If no image selected, set a default image from drawable or a link
                                    imageuri = "https://ibb.co.com/ksVYDZxh"; // You can later load a local drawable instead of this

                                    Users users = new Users();
                                    users.setEmail(Email);
                                    users.setUserId(id);
                                    users.setPassword(Password);
                                    users.setProfileImg(imageuri);
                                    users.setUsername(name);
                                    users.setStatus(status);

                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> dbTask) {
                                            if (dbTask.isSuccessful()) {
                                                progressDialog.show();
                                                Toast.makeText(Registration.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(Registration.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(Registration.this, "Error in Creating User", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                            } else {
                                Toast.makeText(Registration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



            }


        });










    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 10){

            if(data != null){

            imageUri = data.getData();
            profileImg.setImageURI(imageUri);

                uploadToImgBB(imageUri);


            }

        }
    }

//    private void saveImageToLocalStorage(Uri sourceUri) {
//        try {
//            // Create input stream from the selected image
//            InputStream inputStream = getContentResolver().openInputStream(sourceUri);
//
//            // Create a unique file name
//            String fileName = "profile_" + System.currentTimeMillis() + ".jpg";
//
//            // Save inside internal storage
//            File file = new File(getFilesDir(), fileName);
//            OutputStream outputStream = new FileOutputStream(file);
//
//            // Copy data
//            byte[] buffer = new byte[1024];
//            int length;
//            while ((length = inputStream.read(buffer)) > 0) {
//                outputStream.write(buffer, 0, length);
//            }
//
//            // Close streams
//            outputStream.close();
//            inputStream.close();
//
//            // Save the local URI string
//            imageuri = file.getAbsolutePath();
//            Toast.makeText(this, "Image saved locally!", Toast.LENGTH_SHORT).show();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Error saving image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

    public void uploadToImgBB(Uri imageUri) {
        progressDialog.show();
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            byte[] imageBytes = new byte[inputStream.available()];
            inputStream.read(imageBytes);
            String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            String apiKey = "63c820afa6dd1b06638a99a45dffc8d8"; // your ImgBB API key
            String uploadUrl = "https://api.imgbb.com/1/upload?key=" + apiKey;

            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest request = new StringRequest(Request.Method.POST, uploadUrl,
                    response -> {
                        try {
                            JSONObject json = new JSONObject(response);
                            String imageUrl = json.getJSONObject("data").getString("url");
                            imageuri = imageUrl; // ✅ Store the public image URL
                            progressDialog.dismiss();
                            Toast.makeText(this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        progressDialog.dismiss();
                        Toast.makeText(this, "Upload failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("image", base64Image);
                    return params;
                }
            };

            queue.add(request);
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
            Toast.makeText(this, "Error uploading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}