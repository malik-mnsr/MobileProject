package com.hai811i.mobileproject.fragments;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import com.hai811i.mobileproject.R;

public class DoctorProfileFragment extends Fragment {

    public DoctorProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_profile, container, false);

        // Get doctor data from SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("user_prefs", requireActivity().MODE_PRIVATE);

        // Initialize views
        ImageView profileImage = view.findViewById(R.id.profile_image_large);
        TextView doctorName = view.findViewById(R.id.doctor_name);
        TextView doctorId = view.findViewById(R.id.doctor_id);
        TextView doctorAge = view.findViewById(R.id.doctor_age);
        TextView doctorEmail = view.findViewById(R.id.doctor_email);
        TextView doctorSpecialty = view.findViewById(R.id.doctor_specialty);
        TextView doctorPhone = view.findViewById(R.id.doctor_phone);
        TextView doctorMode = view.findViewById(R.id.doctor_mode);
        Button closeButton = view.findViewById(R.id.close_button);

        // Load profile picture
        String profilePic = prefs.getString("doctor_profilePicture", "");
        loadProfilePicture(profileImage, profilePic);

        // Set doctor information
        doctorName.setText("Dr. " + prefs.getString("doctor_firstName", "") + " " + prefs.getString("doctor_lastName", ""));
        doctorId.setText(String.valueOf(prefs.getLong("doctor_id", 0)));
        doctorAge.setText(String.valueOf(prefs.getInt("doctor_age", 0)));
        doctorEmail.setText(prefs.getString("doctor_email", ""));
        doctorSpecialty.setText(prefs.getString("doctor_specialty", ""));
        doctorPhone.setText(prefs.getString("doctor_phone", ""));
        doctorMode.setText(prefs.getString("doctor_currentMode", ""));

        // Close button click listener
        closeButton.setOnClickListener(v -> requireActivity().onBackPressed());

        return view;
    }

    private void loadProfilePicture(ImageView imageView, String profilePicData) {
        if (profilePicData != null && !profilePicData.isEmpty()) {
            try {
                if (isBase64(profilePicData)) {
                    byte[] imageBytes = Base64.decode(profilePicData, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(
                            requireContext().getResources(),
                            bitmap
                    );
                    roundedDrawable.setCircular(true);
                    roundedDrawable.setAntiAlias(true);
                    imageView.setImageDrawable(roundedDrawable);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        imageView.setImageResource(R.drawable.ic_default_profile);
    }

    private boolean isBase64(String str) {
        if (str == null) return false;
        try {
            Base64.decode(str, Base64.DEFAULT);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}