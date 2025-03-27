package com.example.eventmanagementsystem;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class SupplierHomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_supplier_home_fragment, container, false);


        TextView firstMsg = view.findViewById(R.id.first_msg);

        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1f, 1.1f,
                1f, 1.1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(1000);
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        scaleAnimation.setRepeatMode(Animation.REVERSE);

        firstMsg.startAnimation(scaleAnimation);


//        Button navigateToRequests = view.findViewById(R.id.restockBtn);
//        navigateToRequests.setOnClickListener(v -> {
//            // Create an instance of the SupplierRestockFragment
//            Fragment selectedFragment = new SupplierRestockFragment();
//
//            // Begin a transaction to replace the current fragment with the new one
//            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.frame_layout, selectedFragment); // Use 'frame_layout' as the container in your activity
//            transaction.addToBackStack(null); // Add this transaction to the back stack, so you can navigate back
//            transaction.commit(); // Commit the transaction
//        });


        return view;
    }
}