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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.btproject.barberise.R;
import com.btproject.barberise.reservation.Category;
import com.btproject.barberise.reservation.Subcategory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    //profile picture
    private ImageView profileImageView;
    boolean imageControl = false;
    Uri imageUri;
    ActivityResultLauncher<Intent> activityResultLauncher;
    private Bitmap selectedImage;

    //name,email and password
    private String email,password;
    private EditText userNameEditText;
    private TextView emailTextView,PhoneNoEditText;

    //buttons
    private TextView saveChangesTextView;

    //signUp
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase db;
    DatabaseReference dbReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    //Image resolution
    private static final int MAX_IMAGE_WIDTH = 1080;
    private static final int MAX_IMAGE_HEIGHT = 710;

    private TextView setUpOpeningHoursTextView,setUpServicesTextView;

    public static Map<String,ArrayList<String>> openingHours = new HashMap<>();
    public static boolean hoursConfigured = false;

    private TextView openingHoursConfiguredTextView,pictureConfiguredTextView,servicesConfiguredTextView;

    private void setUpUI()
    {
        // Email & phone editText
        emailTextView = findViewById(R.id.inRegEmailTextView);
        PhoneNoEditText = findViewById(R.id.inRegPhoneNoEditText);

        pictureConfiguredTextView = findViewById(R.id.pictureConfiguredTextView);
        pictureConfiguredTextView.setVisibility(View.GONE);

        setUpOpeningHoursTextView = findViewById(R.id.setUpOpeningHoursTextView);
        openingHoursConfiguredTextView = findViewById(R.id.openingHoursConfiguredTextView);
        openingHoursConfiguredTextView.setVisibility(View.GONE);

        setUpServicesTextView = findViewById(R.id.setUpServicesTextView);
        servicesConfiguredTextView = findViewById(R.id.servicesConfiguredTextView);
        servicesConfiguredTextView.setVisibility(View.GONE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCheckMarks();
    }

    private void updateCheckMarks()
    {
        if(hoursConfigured)
            openingHoursConfiguredTextView.setVisibility(View.VISIBLE);
        else
            openingHoursConfiguredTextView.setVisibility(View.GONE);

        if(imageControl)
            pictureConfiguredTextView.setVisibility(View.VISIBLE);
        else
            pictureConfiguredTextView.setVisibility(View.GONE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        setUpUI();

        setUpOpeningHoursTextView.setOnClickListener(view ->
        {
            startActivity(new Intent(RegistrationActivity.this,SetUpOpeningHoursActivity.class));
        });


        /** Helper method to fill users with attributes*/
//        addAttributesToUsers(getOpeningHours(),getCategories());

        profileImageView = findViewById(R.id.ProfileImageView);
        saveChangesTextView = findViewById(R.id.saveChangesTextView);
        userNameEditText = findViewById(R.id.shopNameEditText);


        //auth + db
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        dbReference = db.getReference();
        user = auth.getCurrentUser();

        //storage
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        //getting email + password from previous activity
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");

        emailTextView.setText(email);


        //help method for picture choosing
        registerActivityForSelectImage();
        profileImageView.setOnClickListener(view -> myImageChooser());


        //Add green check mark if name defined
        userNameEditText.addTextChangedListener(new TextWatcher() {
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
                    userNameEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check_circle, 0);
                } else {
                    userNameEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });

        //Add green check mark if phoneNo defined
        PhoneNoEditText.addTextChangedListener(new TextWatcher() {
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
                    userNameEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check_circle, 0);
                } else {
                    userNameEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });

        //saving changes and registering user
        saveChangesTextView.setOnClickListener(view -> {
            String shopName = userNameEditText.getText().toString();
            if(!email.equals("") && !password.equals("") && !shopName.equals("")) {
                if(openingHours != null)
                    register(email, password, shopName, getCategories(),openingHours);
            }
        });

//        saveChangesTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


    }

    private Map<String,ArrayList<String>> getOpeningHours()
    {
        Map<String,ArrayList<String>> openingHours = new HashMap<>();
        // Opening hours for Monday
        ArrayList<String> mondayHours = new ArrayList<>();
        for (int hour = 9; hour < 11; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                String time = String.format("%02d:%02d", hour, minute);
                mondayHours.add(time);
            }
        }
        openingHours.put("Monday", mondayHours);

        // Opening hours for Tuesday
        ArrayList<String> tuesdayHours = new ArrayList<>();
        for (int hour = 10; hour < 21; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                String time = String.format("%02d:%02d", hour, minute);
                tuesdayHours.add(time);
            }
        }
        openingHours.put("Tuesday", tuesdayHours);

        // Opening hours for Wednesday
        ArrayList<String> wednesdayHours = new ArrayList<>();
        for (int hour = 8; hour < 18; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                String time = String.format("%02d:%02d", hour, minute);
                wednesdayHours.add(time);
            }
        }
        openingHours.put("Wednesday", wednesdayHours);

        // Opening hours for Thursday
        ArrayList<String> thursdayHours = new ArrayList<>();
        for (int hour = 12; hour < 22; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                String time = String.format("%02d:%02d", hour, minute);
                thursdayHours.add(time);
            }
        }
        openingHours.put("Thursday", thursdayHours);

        // Opening hours for Friday
        ArrayList<String> fridayHours = new ArrayList<>();
        for (int hour = 9; hour < 17; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                String time = String.format("%02d:%02d", hour, minute);
                fridayHours.add(time);
            }
        }
        openingHours.put("Friday", fridayHours);

        // Opening hours for Saturday
        ArrayList<String> saturdayHours = new ArrayList<>();
        for (int hour = 11; hour < 16; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                String time = String.format("%02d:%02d", hour, minute);
                saturdayHours.add(time);
            }
        }
        openingHours.put("Saturday", saturdayHours);

        // Opening hours for Sunday
        ArrayList<String> sundayHours = new ArrayList<>();
        for (int hour = 12; hour < 18; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                String time = String.format("%02d:%02d", hour, minute);
                sundayHours.add(time);
            }
        }
        openingHours.put("Sunday", sundayHours);
        return openingHours;

    }

    private ArrayList<Category> getCategories()
    {
        ArrayList<Category> categories = new ArrayList<>();

        ArrayList<Subcategory> fsubcategories = new ArrayList<>();
        Subcategory fSubcategory = new Subcategory("Classic Strih",14.0);
        Subcategory sSubcategory = new Subcategory("Dlhé vlasy",20.0);
        Subcategory tSubcategory = new Subcategory("Junior Strih",10.0);
        fSubcategory.setDescription("Strih strojčekom, nožnicový strih, postupka fade, umytie hlavy,styling s" +
                " použitím najkvalitejších prípravkov (Clipper cut, scissor cut, fade + servis)");
        sSubcategory.setDescription("Strih vlasov bez použitia strojčeka (2x umytie vlasov, masáž hlavy, strih - skrátenie, úprava)");
        tSubcategory.setDescription("Detský strih do 10 rokov (podsedak + detská ochranná zástera)nožnicový" +
                " strih, postupka-fade s použitím najkvalitnejších prípravkov");
        fsubcategories.add(fSubcategory);
        fsubcategories.add(sSubcategory);
        fsubcategories.add(tSubcategory);

        Category fCategory = new Category("Vlasy",fsubcategories);
        categories.add(fCategory);

        ArrayList<Subcategory> ssubcategories = new ArrayList<>();
        Subcategory ffSubcategory = new Subcategory("Holenie brady",8.0);
        Subcategory ssSubcategory = new Subcategory("Úprava brady",10.0);
        ffSubcategory.setDescription("Holenie brady s použitím britvy a holiacej štetky " +
                "na uvolnenie a zjemnenie pokožky a balzam po holení na ukludnenie pokožky");

        ssSubcategory.setDescription("Úprava brady s použitím strojčeka, nožníc alebo britvy podľa želania klienta a aplikacia " +
                "prvotriedných prípravkov ako je olej a balzman na bradu (Beard trim with clipper and shave with razor + servis)");
        ssubcategories.add(ffSubcategory);
        ssubcategories.add(ssSubcategory);

        Category sCategory = new Category("Brada",ssubcategories);
        categories.add(sCategory);

        ArrayList<Subcategory> tsubcategories = new ArrayList<>();
        Subcategory tffSubcategory = new Subcategory("Classic strih + úprava brady",24.0);
        Subcategory tssSubcategory = new Subcategory("Holenie hlavy a holenie brady",20.0);

        tffSubcategory.setDescription("Clipper cut, scissor cut, fade, beard trim with clipper and razor + servis");
        tssSubcategory.setDescription("Tradičné pánske holenie metódou ,,HOT TOWEL''");
        tsubcategories.add(tffSubcategory);
        tsubcategories.add(tssSubcategory);

        Category tCategory = new Category("Komplet",tsubcategories);
        categories.add(tCategory);

        return categories;
    }

    public void myImageChooser()
    {
        //if we don't have permission -> we ask for it
        if(ContextCompat.checkSelfPermission(RegistrationActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED){

            ActivityCompat.requestPermissions(RegistrationActivity.this,
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
                                        data.getData()
                                );

                                /** Scale the image height & width to proper values*/
                                selectedImage = Bitmap.createScaledBitmap(selectedImage,MAX_IMAGE_WIDTH,MAX_IMAGE_HEIGHT,true);

                                // Display newly created Image to imageView
                                profileImageView.setImageBitmap(selectedImage);

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

    private static int customPriority = 1;

    public void register(String email, String password, String userName, ArrayList<Category> categories,Map<String, ArrayList<String>> openingHours) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        assert user != null;
                        final String userId = user.getUid();

                        StorageReference storageRef = firebaseStorage.getReference().child("users").child(userId).child("profile_picture.jpg");
                        storageRef.putFile(imageUri)
                                .addOnSuccessListener(taskSnapshot -> {
                                    StorageReference storageReference = firebaseStorage.getReference().child("users").child(userId).child("profile_picture.jpg");
                                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                        String profilePictureUrl = uri.toString();

                                        DatabaseReference databaseReference = db.getReference().child("users").child(userId);
                                        Map<String, Object> userData = new HashMap<>();
                                        userData.put("email", email);
                                        userData.put("username", userName);
                                        userData.put("profile_picture", profilePictureUrl);
                                        userData.put("category","available_today");
                                        userData.put("openingHours",openingHours);
                                        userData.put("categories", categories);
                                        userData.put("customPriority",customPriority);
                                        customPriority++;
                                        databaseReference.setValue(userData);
                                    });
                                })
                                .addOnFailureListener(e -> Toast.makeText(RegistrationActivity.this, "Failed to upload profile picture", Toast.LENGTH_SHORT).show());

                        Intent intent = new Intent(RegistrationActivity.this, PartnerProfileActivity.class);
                        startActivity(intent);
//                        finish();
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}