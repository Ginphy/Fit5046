package com.example.ce.ui.notifications;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ce.R;
import com.example.ce.databinding.FragmentNotificationsBinding;
import com.example.ce.ui.login.LoginActivity;
import com.example.ce.ui.login.StartActivity;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ImageButton buttonback = root.findViewById(R.id.BackButton);
        buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个AlertDialog.Builder对象
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                // 设置对话框的标题
                builder.setTitle("Choose to:");

                // 设置对话框的单选项
                final String[] items = {"Modify Information", "Log out", "Change Account"};
                int checkedItem = 0; // 默认选中第一个选项
                builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 在这里处理选中的选项
                        switch (which){
                            case 0:
                                //Modify option
                                //TBD
                                break;
                            case 1:
                                //Log out
                                Intent intent = new Intent(getActivity(), StartActivity.class);
                                startActivity(intent);
                                break;
                            case 2:
                                //Change Account
                                Intent intent2 = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent2);
                                break;

                        }
                    }
                });
                // 显示对话框
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


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
                Phone.setText(String.format("Phone: %s", personalInfo.Phone));
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