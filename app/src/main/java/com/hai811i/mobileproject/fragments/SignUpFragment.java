package com.hai811i.mobileproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.hai811i.mobileproject.ChoixActivity;
import com.hai811i.mobileproject.R;

public class SignUpFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false);

        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            // Tell the activity to show main content again
            if (getActivity() instanceof ChoixActivity) {
                ((ChoixActivity) getActivity()).showMainContent();
            }

            // Remove this fragment
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }
}
