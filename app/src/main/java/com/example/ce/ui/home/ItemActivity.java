package com.example.ce.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.example.ce.MainActivity;
import com.example.ce.R;
import com.example.ce.ui.login.LoginActivity;
import com.example.ce.ui.login.SignupActivity;
import com.example.ce.ui.notifications.NotificationsViewModel;


public class ItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.iteminfo);
        TextView editName = findViewById(R.id.editName);
        TextView editWeight = findViewById(R.id.editWeight);
        TextView editStarting = findViewById(R.id.editStarting);
        TextView editDeadline = findViewById(R.id.editDeadline);
        TextView editDescription = findViewById(R.id.editDescrip);



        Button btnConfirm = findViewById(R.id.Confirm);
     //   Button btnLogin = findViewById(R.id.Log);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent i = new Intent(ItemActivity.this, MainActivity.class);
                startActivity(i);
            }
        });


    }
    public NotificationsViewModel extends ViewModel() {

    }

}
