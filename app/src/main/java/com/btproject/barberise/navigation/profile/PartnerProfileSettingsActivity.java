package com.btproject.barberise.navigation.profile;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.btproject.barberise.ContactSupportActivity;
import com.btproject.barberise.R;
import com.btproject.barberise.VerifyPhoneNoActivity;
import com.btproject.barberise.reservation.Category;
import com.btproject.barberise.reservation.DataFetchCallback;
import com.btproject.barberise.reservation.Reservation;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PartnerProfileSettingsActivity extends AppCompatActivity {

    private ShapeableImageView ProfileImageView;
    private TextView inRegEmailTextView,logOutTextView,saveChangesTextView,setUpOpeningHoursTextView,setUpServicesTextView
            ,contactSupportTextView;
    private EditText shopNameEditText,inRegPhoneNoEditText,inRegAddressEditTextView;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private User barberShop;

    public static ArrayList<Category> categories = new ArrayList<>();

    public static Map<String,ArrayList<String>> openingHours = new HashMap<>();
    ActivityResultLauncher<Intent> activityResultLauncher;
    boolean imageControl = false;
    Uri imageUri;
    private Bitmap selectedImage;
    public static boolean hoursConfigured;

    private boolean pictureUpdated, nameUpdated, phoneUpdated, addressUpdated;

    public static boolean hoursUpdated, servicesUpdated;

    private void initComponents()
    {
        ProfileImageView = findViewById(R.id.ProfileImageView);
        inRegEmailTextView = findViewById(R.id.inRegEmailTextView);
        logOutTextView = findViewById(R.id.logOutTextView);
        saveChangesTextView = findViewById(R.id.saveChangesTextView);
        shopNameEditText = findViewById(R.id.shopNameEditText);
        inRegPhoneNoEditText = findViewById(R.id.inRegPhoneNoEditText);
        inRegAddressEditTextView = findViewById(R.id.inRegAddressEditTextView);
        setUpOpeningHoursTextView = findViewById(R.id.setUpOpeningHoursTextView);
        setUpServicesTextView = findViewById(R.id.setUpServicesTextView);
        logOutTextView = findViewById(R.id.logOutTextView);
        saveChangesTextView = findViewById(R.id.saveChangesTextView);
        contactSupportTextView = findViewById(R.id.contactSupportTextView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_profile_settings);
        initComponents();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        DataFetchCallback callback = new DataFetchCallback() {
            @Override
            public void onDataLoaded(User barberShop) {
                updateUI();
            }

            @Override
            public void onReservationsLoaded(ArrayList<Reservation> reservations) {

            }
        };
        getUserData(callback);

        contactSupportTextView.setOnClickListener(view -> {
            startActivity(new Intent(PartnerProfileSettingsActivity.this, ContactSupportActivity.class));
        });

        registerActivityForSelectImage();
        ProfileImageView.setOnClickListener(view -> {
            myImageChooser();
            pictureUpdated = true;
        });

        addListenersForInfoChanges();

        setUpOpeningHoursTextView.setOnClickListener(view ->
        {
            Intent intent = new Intent(PartnerProfileSettingsActivity.this, SetUpOpeningHoursActivity.class);
            intent.putExtra("sourceActivity", "PartnerProfileSettingsActivity");
            startActivity(intent);
        });

        setUpServicesTextView.setOnClickListener(view ->
        {
            Intent intent = new Intent(PartnerProfileSettingsActivity.this, ConfigureServicesActivity.class);
            intent.putExtra("sourceActivity", "PartnerProfileSettingsActivity");
            startActivity(intent);
        });

        logOutTextView.setOnClickListener(view ->{
            auth.signOut();
            startActivity(new Intent(PartnerProfileSettingsActivity.this, VerifyPhoneNoActivity.class));
            finish();
        });

        saveChangesTextView.setOnClickListener(view ->{
            updateProfile();
            onBackPressed();
        });
    }

    private void addListenersForInfoChanges()
    {
        shopNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                // Show or hide the DrawableRight based on whether the text is empty or not
                if (s.length() > 0) {
                    shopNameEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check_circle, 0);
                    nameUpdated = true;
                } else {
                    shopNameEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });

        //Add green check mark if phoneNo defined
        inRegPhoneNoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Show or hide the DrawableRight based on whether the text is empty or not
                if (s.length() > 0) {
                    inRegPhoneNoEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check_circle, 0);
                    phoneUpdated = true;
                } else {
                    inRegPhoneNoEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });

        //Address green checkmark
        inRegAddressEditTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 0) {
                    inRegAddressEditTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check_circle, 0);
                    addressUpdated = true;
                } else {
                    inRegAddressEditTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });

    }


    private void updateProfile() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference userDbRef = db.getReference("users").child(user.getUid());
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        if (pictureUpdated) {
            StorageReference storageRef = firebaseStorage.getReference().child("users").child(user.getUid()).child("profile_picture.jpg");
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        StorageReference storageReference = firebaseStorage.getReference().child("users").child(user.getUid()).child("profile_picture.jpg");
                        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String profilePictureUrl = uri.toString();
                            DatabaseReference databaseReference = db.getReference().child("users").child(user.getUid());
                            userDbRef.child("profile_picture").setValue(profilePictureUrl);
                        });
                    })
                    .addOnFailureListener(e -> Toast.makeText(PartnerProfileSettingsActivity.this, "Failed to upload profile picture", Toast.LENGTH_SHORT).show());
        }
        if(nameUpdated)
        {
            String newName = shopNameEditText.getText().toString();
            userDbRef.child("username").setValue(newName);
        }
        if(phoneUpdated)
        {
            String newPhone = inRegPhoneNoEditText.getText().toString();
            userDbRef.child("phoneNo").setValue(newPhone);
        }
        if(addressUpdated)
        {
            String newAddress = inRegAddressEditTextView.getText().toString();
            userDbRef.child("address").setValue(newAddress);
        }
        if(servicesUpdated){
            userDbRef.child("categories").setValue(categories);
        }
        if(hoursUpdated)
        {
            userDbRef.child("openingHours").setValue(openingHours);
        }

    }


    private void updateUI()
    {
        String ImageUrl = barberShop.getProfile_picture();
        String email = barberShop.getEmail();
        String userName = barberShop.getUsername();
        String address = barberShop.getAddress();

        // Update the UI with the imageURL
        Glide.with(ProfileImageView.getContext())
                .load(ImageUrl)
                .into(ProfileImageView);

        inRegEmailTextView.setText(email);
        shopNameEditText.setText(userName);
        inRegAddressEditTextView.setText(address);
    }

    private void getUserData(DataFetchCallback callback)
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference userDbRef = db.getReference("users").child(user.getUid());
        // Add a listener to the reference to fetch the user data
        userDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.exists()) {
                    barberShop = dataSnapshot.getValue(User.class);
                    // Pass the data to the callback
                    callback.onDataLoaded(barberShop);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Toast.makeText(getApplicationContext(),"db_fetching_error",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void myImageChooser()
    {
        //if we don't have permission -> we ask for it
        if(ContextCompat.checkSelfPermission(PartnerProfileSettingsActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED){

            ActivityCompat.requestPermissions(PartnerProfileSettingsActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

            //if we have permission
        }else{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intent);
        }
    }


    //Image resolution
    private static final int MAX_IMAGE_WIDTH = 1080;
    private static final int MAX_IMAGE_HEIGHT = 710;

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
                                        data.getData()
                                );

                                /** Scale the image height & width to proper values*/
                                selectedImage = Bitmap.createScaledBitmap(selectedImage,MAX_IMAGE_WIDTH,MAX_IMAGE_HEIGHT,true);

                                // Display newly created Image to imageView
                                ProfileImageView.setImageBitmap(selectedImage);

                                /**Load the selectedImage to Uri for further storage*/
                                imageUri = getImageUri(getApplicationContext(),selectedImage);

                                // Image have been chosen
                                imageControl = true;

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
    }

    public Uri getImageUri(Context context, Bitmap bitmap) {
        File tempFile = null;
        try {
            tempFile = File.createTempFile("temp", ".jpg", context.getCacheDir());
            OutputStream os = Files.newOutputStream(tempFile.toPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (tempFile != null) {
            return Uri.fromFile(tempFile);
        } else {
            return null;
        }
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