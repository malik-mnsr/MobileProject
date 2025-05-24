package com.hai811i.mobileproject;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.hai811i.mobileproject.fragments.SignInFragment;
import com.hai811i.mobileproject.fragments.SignUpFragment;
import com.hai811i.mobileproject.response.LoginResponse;

public class ChoixActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix);

        Button buttonNouvelleInscription = findViewById(R.id.buttonNouvelleInscription);
        Button buttonConnexion = findViewById(R.id.buttonConnexion);

        buttonNouvelleInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide main content
                findViewById(R.id.main_content).setVisibility(View.GONE);

                // Show fragment container
                findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);

                // Fragment transaction
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new SignUpFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        buttonConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.main_content).setVisibility(View.GONE);

                // Show fragment container
                findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);

                // Fragment transaction
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new SignInFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    public void showMainContent() {
        // Show main content
        findViewById(R.id.main_content).setVisibility(View.VISIBLE);
        // Hide fragment container
        findViewById(R.id.fragment_container).setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.fragment_container).getVisibility() == View.VISIBLE) {
            showMainContent();
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }


    public void onLoginSuccess(LoginResponse response) {

        SharedPreferences sharedPrefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putBoolean("is_logged_in", true);


        editor.putLong("doctor_id", response.getDoctor().getId());
        editor.putString("doctor_firstName", response.getDoctor().getFirstName());
        editor.putString("doctor_lastName", response.getDoctor().getLastName());
        editor.putInt("doctor_age", response.getDoctor().getAge());
        editor.putString("doctor_email", response.getDoctor().getEmail());
        editor.putString("doctor_specialty", response.getDoctor().getSpecialty());
        editor.putString("doctor_phone", response.getDoctor().getPhone());
        editor.putString("doctor_profilePicture", response.getDoctor().getProfilePicture());
        editor.putString("doctor_profilePictureContentType", response.getDoctor().getProfilePictureContentType());
        editor.putString("doctor_currentMode", response.getDoctor().getCurrentMode().name());

        editor.apply();


        Intent intent = new Intent(ChoixActivity.this, DoctorActivity.class);


        intent.putExtra("doctor_data", new Gson().toJson(response.getDoctor()));


        if (response.getDoctor().getProfilePicture() != null) {
            Bitmap profileBitmap = decodeBase64ToBitmap(response.getDoctor().getProfilePicture());
        }

        startActivity(intent);
        finish();
    }


    private Bitmap decodeBase64ToBitmap(String base64Image) {
        byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
