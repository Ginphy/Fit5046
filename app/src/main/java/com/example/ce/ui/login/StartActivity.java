package com.example.ce.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ce.MainActivity;
import com.example.ce.R;
import com.example.ce.ui.dashboard_courier.DashboardFragment;
import com.example.ce.ui.home.CourierHomeActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.start);


        Button btnSignup = findViewById(R.id.Sign);
        Button btnLogin = findViewById(R.id.Log);
        Button btnTest = findViewById(R.id.Test);
        Button btnTest2 = findViewById(R.id.Test2);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent i = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
                Intent i = new Intent(StartActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });

        btnTest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
                Intent i = new Intent(StartActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        btnTest2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
                Intent i = new Intent(StartActivity.this, CourierHomeActivity.class);
                startActivity(i);
            }
        });
    }

}
