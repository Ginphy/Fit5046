package com.example.ce.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ce.R;
import com.example.ce.databinding.FragmentHomeBinding;
import com.example.ce.ui.login.StartActivity;
import com.example.ce.ui.login.LoginActivity;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
       //Initialize a ViewModel
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

     //   final TextView textView = binding.textHome;

        Button btnLogin = root.findViewById(R.id.BtnLogout);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
                Intent i = new Intent(getActivity(), StartActivity.class);
                startActivity(i);

            }
        });

   //     homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        //A data listener is added to bind the page.
        // Once the text of the model changes, the observer will be notified, and the textview can be refreshed.
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}