package com.hai811i.mobileproject;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.hai811i.mobileproject.fragments.SignInFragment;
import com.hai811i.mobileproject.fragments.SignUpFragment;
import com.hai811i.mobileproject.response.LoginResponse;

public class ChoixActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "test_channel";
    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 101;
    private static final String TAG = "ChoixActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix);

        Button buttonNouvelleInscription = findViewById(R.id.buttonNouvelleInscription);
        Button buttonConnexion = findViewById(R.id.buttonConnexion);

        createNotificationChannel();

        // Request notification permission if needed and show notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_POST_NOTIFICATIONS);
            } else {
                showWelcomeNotification();
            }
        } else {
            showWelcomeNotification();
        }

        buttonNouvelleInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.main_content).setVisibility(View.GONE);
                findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);

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
                findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new SignInFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private void showWelcomeNotification() {
        Log.d(TAG, "Attempting to show welcome notification");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Welcome!")
                .setContentText("This is a test notification on app start.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1001, builder.build());

        Log.d(TAG, "Notification shown");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "onRequestPermissionsResult: requestCode=" + requestCode);

        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Notification permission granted");
                showWelcomeNotification();
            } else {
                Log.d(TAG, "Notification permission denied");
            }
        }
    }

    public void showMainContent() {
        findViewById(R.id.main_content).setVisibility(View.VISIBLE);
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
            // You may want to use this bitmap somewhere
        }

        startActivity(intent);
        finish();
    }

    private Bitmap decodeBase64ToBitmap(String base64Image) {
        byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Test Channel";
            String description = "Channel for test notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            Log.d(TAG, "Notification channel created");
        }
    }
}
