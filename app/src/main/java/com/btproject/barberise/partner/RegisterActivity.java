package com.btproject.barberise.partner;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.btproject.barberise.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {


    private EditText emailEditText, passwordEditText, usernameEditText;
    private Button signUpButton;
    private ImageView usrImageView;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference dbReference;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    boolean imageControl = false;
    Uri imageUri;
    ActivityResultLauncher<Intent> activityResultLauncher;
    private Bitmap selectedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        usernameEditText = findViewById(R.id.usernameUpdateEditText);
        signUpButton = findViewById(R.id.buttonUpdate);
        usrImageView = findViewById(R.id.usrUpdateImageView);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        dbReference = db.getReference();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        //register activity
        registerActivityForSelectImage();

        usrImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myImageChooser();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String userName = usernameEditText.getText().toString();

                if(!email.equals("") && !password.equals("") && !userName.equals("")){
                    signUp(email,password,userName);
                }
            }
        });

    }

    public void signUp(String email,String password,String userName)
    {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {

            if (task.isSuccessful())
            {
                dbReference.child("Users").child(auth.getUid()).child("userName").setValue(userName);

                if(imageControl)
                {
                    UUID randomID = UUID.randomUUID();
                    final String imageName = "images/"+randomID+".jpg";
                    storageReference.child(imageName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            StorageReference myStorageRef = firebaseStorage.getReference(imageName);
                            myStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String filePath = uri.toString();
                                    dbReference.child("Users").child(auth.getUid()).child("image").setValue(filePath).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(RegisterActivity.this, "Write to database is successful.", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterActivity.this, "Write to database is not successful.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
                else
                {
                    dbReference.child("Users").child(auth.getUid()).child("image").setValue("null");
                }

                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(RegisterActivity.this, "There is a problem.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void myImageChooser()
    {
        //if we don't have permission -> we ask for it
        if(ContextCompat.checkSelfPermission(RegisterActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED){

            ActivityCompat.requestPermissions(RegisterActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

            //if we have permission
        }else{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intent);
        }
    }

    public void registerActivityForSelectImage()
    {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        int resultCode = result.getResultCode();
                        Intent data = result.getData();

                        if(resultCode == RESULT_OK && data != null){
                            try {
                                selectedImage = MediaStore.Images.Media.getBitmap(
                                        getContentResolver(),
                                        data.getData());
                                usrImageView.setImageBitmap(selectedImage);

                                imageUri = data.getData();
                                imageControl = true;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intent);
        }
    }
}
