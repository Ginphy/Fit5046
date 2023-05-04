package com.example.ce.ui.notifications;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ce.R;
import com.example.ce.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView Profile = root.findViewById(R.id.Profile);
        TextView NickName = root.findViewById(R.id.Nickname);
        TextView Name = root.findViewById(R.id.Name);
        TextView Sex = root.findViewById(R.id.Gender);
        TextView Identity = root.findViewById(R.id.Identity);
        TextView Phone = root.findViewById(R.id.phone);
        TextView Email = root.findViewById(R.id.Email);
        notificationsViewModel.getData().observe(getViewLifecycleOwner(), new Observer<NotificationsViewModel.PersonalInfo>() {
            @Override
            public void onChanged(NotificationsViewModel.PersonalInfo personalInfo) {
                Profile.setText(String.format("Profile: %s", personalInfo.Profile));
                NickName.setText(String.format("Nickname: %s", personalInfo.Nickname));
                Name.setText(String.format("Name: %s", personalInfo.Name));
                Identity.setText(String.format("IdentityNum: %s", personalInfo.Identity));
                Sex.setText(String.format("Gender: %s", personalInfo.Gender));
                Phone.setText(String.format("Nickname: %s", personalInfo.Phone));
                Email.setText(String.format("E-mail: %s", personalInfo.Email));

            }
        });
        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}