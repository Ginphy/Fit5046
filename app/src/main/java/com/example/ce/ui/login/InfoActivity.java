package com.example.ce.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ce.R;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.start);


        Button btnSignup = findViewById(R.id.Sign);
        Button btnLogin = findViewById(R.id.Log);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent i = new Intent(InfoActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
                Intent i = new Intent(InfoActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });
    }

}


