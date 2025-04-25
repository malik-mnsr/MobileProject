package com.hai811i.mobileproject;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.hai811i.mobileproject.fragments.SignInFragment;
import com.hai811i.mobileproject.fragments.SignUpFragment;

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
}