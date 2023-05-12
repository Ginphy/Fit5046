package com.example.ce.ui.home.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ce.databinding.ViewFragmentBinding;
import com.example.ce.ui.home.viewmodel.SharedViewModel;
import androidx.annotation.Nullable;

public class ViewFragment extends Fragment {


    private ViewFragmentBinding binding;

    public ViewFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment using the binding
        binding = ViewFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        SharedViewModel model = new
                ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        model.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                binding.textMessage.setText(s);

            }
        });
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
