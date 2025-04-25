package com.hai811i.mobileproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;
import com.hai811i.mobileproject.ChoixActivity;
import com.hai811i.mobileproject.R;

public class SignInFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_2, container, false);

        // Handle back button click
        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            if (getActivity() instanceof ChoixActivity) {
                ((ChoixActivity) getActivity()).showMainContent();
            }
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Handle create account button click
        MaterialButton createAccountButton = view.findViewById(R.id.signup_redirect_button);
        createAccountButton.setOnClickListener(v -> {

            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new SignUpFragment());
            requireActivity().getSupportFragmentManager().popBackStack();
            transaction.addToBackStack("sign_up_fragment");
            transaction.commit();

        });

        return view;
    }
}