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
import android.util.DisplayMetrics;
import android.widget.CheckBox;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
    private EditText name;

    //buttons
    private TextView saveChangesTextView;
    private CheckBox checkBox;

    //signUp
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase db;
    DatabaseReference dbReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        profileImageView = findViewById(R.id.profileImageView);
        saveChangesTextView = findViewById(R.id.saveChangesTextView);
        name = findViewById(R.id.shopNameEditText);
        checkBox = findViewById(R.id.checkBox);

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

        //help method for picture choosing
        registerActivityForSelectImage();
        profileImageView.setOnClickListener(view -> myImageChooser());

        //saving changes and registering user
        saveChangesTextView.setOnClickListener(view -> {
            String shopName = name.getText().toString();
            if(!email.equals("") && !password.equals("") && !shopName.equals("")) {
//                signUp(email, password, shopName);
//                register(email, password, shopName,"available_today");
                register(email, password, shopName, getCategories(),getOpeningHours());
            }
        });


    }

    private Map<String,ArrayList<String>> getOpeningHours()
    {
        Map<String,ArrayList<String>> openingHours = new HashMap<>();
        // Opening hours for Monday
        ArrayList<String> mondayHours = new ArrayList<>();
        for (int hour = 9; hour < 20; hour++) {
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

    private List<Category> getCategories()
    {
        List<Category> categories = new LinkedList<>();

        Subcategory fSubcategory = new Subcategory("Classic Strih",14.0);
        Subcategory sSubcategory = new Subcategory("Dlhé vlasy",20.0);
        Subcategory tSubcategory = new Subcategory("Junior Strih",10.0);
        List<Subcategory> fsubcategories = new LinkedList<>();
        fsubcategories.add(fSubcategory);
        fsubcategories.add(sSubcategory);
        fsubcategories.add(tSubcategory);
        Category fCategory = new Category("Vlasy",fsubcategories);
        categories.add(fCategory);

        Subcategory ffSubcategory = new Subcategory("Holenie brady",8.0);
        Subcategory ssSubcategory = new Subcategory("Úprava brady",10.0);
        List<Subcategory> ssubcategories = new LinkedList<>();
        ssubcategories.add(ffSubcategory);
        ssubcategories.add(ssSubcategory);
        Category sCategory = new Category("Brada",ssubcategories);
        categories.add(sCategory);


        Subcategory tffSubcategory = new Subcategory("Classic strih + úprava brady",24.0);
        Subcategory tssSubcategory = new Subcategory("Holenie hlavy a holenie brady",20.0);
        List<Subcategory> tsubcategories = new LinkedList<>();
        tsubcategories.add(tffSubcategory);
        tsubcategories.add(tssSubcategory);
        Category tCategory = new Category("Komplet",tsubcategories);
        categories.add(tCategory);

        return categories;
    }

    public void register(String email,String password,String userName,String category) {

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
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
                                        //Temporary solution of categories
                                        userData.put("category",category);
                                        databaseReference.setValue(userData);
                                    });
                                })
                                .addOnFailureListener(e -> Toast.makeText(RegistrationActivity.this, "Failed to upload profile picture", Toast.LENGTH_SHORT).show());
                        Intent intent = new Intent(RegistrationActivity.this, PartnerProfileActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
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
                                int newWidth = getScreenWidth();
                                int newHeight = getScreenHeight();

                                //scale the image height and width
                                selectedImage = Bitmap.createScaledBitmap(selectedImage,newWidth,newHeight / 3,true);

                                //round the corners -> use in reservation activity
//                                selectedImage = getRoundCornerBitmap(selectedImage,120);

                                profileImageView.setImageBitmap(selectedImage);

                                //imageUri = getImageUri(getApplicationContext(),selectedImage);
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

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private int getScreenHeight()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private int getScreenWidth()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public void register(String email, String password, String userName, List<Category> categories,Map<String, ArrayList<String>> openingHours) {
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
                                        userData.put("opening_hours",openingHours);

                                        // Save the list of categories to the database
                                        Map<String, Object> categoriesMap = new HashMap<>();

                                        for (Category category : categories) {
                                            Map<String, Object> categoryMap = new HashMap<>();
                                            categoryMap.put("name", category.getName());

                                            List<Map<String, Object>> subcategoriesList = new ArrayList<>();

                                            for (Subcategory subcategory : category.getSubcategories()) {

                                                Map<String, Object> subcategoryMap = new HashMap<>();
                                                subcategoryMap.put("name", subcategory.getName());
                                                subcategoryMap.put("price", subcategory.getPrice());
                                                subcategoriesList.add(subcategoryMap);

                                            }
                                            categoryMap.put("subcategories", subcategoriesList);

                                            categoriesMap.put(category.getName(), categoryMap);
                                        }
                                        userData.put("categories", categoriesMap);

                                        databaseReference.setValue(userData);
                                    });
                                })
                                .addOnFailureListener(e -> Toast.makeText(RegistrationActivity.this, "Failed to upload profile picture", Toast.LENGTH_SHORT).show());

                        Intent intent = new Intent(RegistrationActivity.this, PartnerProfileActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}