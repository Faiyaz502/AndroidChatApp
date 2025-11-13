package com.faiyaz.faiyazzchatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.faiyaz.faiyazzchatapp.Entity.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Settings extends AppCompatActivity {

    ImageView setProfile;
    EditText setName, setStatus;
    Button saveBtn;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    String email, password, name, imageuri, status;
    Uri setImageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        setProfile = findViewById(R.id.settingprofile);
        setName = findViewById(R.id.settingname);
        setStatus = findViewById(R.id.settingstatus);
        saveBtn = findViewById(R.id.donebutt);

        DatabaseReference reference = database.getReference().child("user").child(auth.getUid());

        // 🔒 Safe Firebase data loading (prevents null crashes)
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) return;

                email = snapshot.child("email").getValue(String.class);
                password = snapshot.child("password").getValue(String.class);
                name = snapshot.child("username").getValue(String.class);
                imageuri = snapshot.child("profileImg").getValue(String.class);
                status = snapshot.child("status").getValue(String.class);

                if (name != null) setName.setText(name);
                if (status != null) setStatus.setText(status);

                if (imageuri != null && !imageuri.isEmpty()) {
                    Picasso.get().load(imageuri).into(setProfile);
                } else {

                    setProfile.setImageResource(R.drawable.man); // 👈 fallback image
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // Pick profile image
        setProfile.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
        });

        // Save button click
        saveBtn.setOnClickListener(v -> {
            String newName = setName.getText().toString().trim();
            String newStatus = setStatus.getText().toString().trim();

            if (newName.isEmpty()) {
                Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (setImageURI != null) {
                uploadToImgBB(setImageURI, newName, newStatus, reference);
            } else {
                Users user = new Users(auth.getUid(), newName, password, email,
                        (imageuri != null ? imageuri : ""), newStatus);
                saveUserData(reference, user);
            }
        });
    }

    // 🔧 Upload new image to ImgBB
    public void uploadToImgBB(Uri imageUri, String newName, String newStatus, DatabaseReference reference) {
        progressDialog.show();
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            byte[] imageBytes = new byte[inputStream.available()];
            inputStream.read(imageBytes);
            String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            String apiKey = "63c820afa6dd1b06638a99a45dffc8d8";
            String uploadUrl = "https://api.imgbb.com/1/upload?key=" + apiKey;

            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest request = new StringRequest(Request.Method.POST, uploadUrl,
                    response -> {
                        try {
                            JSONObject json = new JSONObject(response);
                            String imageUrl = json.getJSONObject("data").getString("url");
                            imageuri = imageUrl;
                            progressDialog.dismiss();

                            Users user = new Users(auth.getUid(), newName, password, email, imageuri, newStatus);
                            saveUserData(reference, user);
                        } catch (Exception e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(this, "Image parse error", Toast.LENGTH_SHORT).show();
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

    private void saveUserData(DatabaseReference reference, Users user) {
        reference.setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(Settings.this, "Data saved successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Settings.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(Settings.this, "Failed to save data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && data != null) {
            setImageURI = data.getData();
            setProfile.setImageURI(setImageURI);
        }
    }
}