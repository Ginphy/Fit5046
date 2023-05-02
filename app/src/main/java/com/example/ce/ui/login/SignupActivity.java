package com.example.ce.ui.login;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ce.MainActivity;
import com.example.ce.R;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity  extends AppCompatActivity {
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.signup);
        auth = FirebaseAuth.getInstance();
        Button btnLogin = findViewById(R.id.Log);
        EditText emailEditText = findViewById(R.id.Email);
        EditText passwordEditText = findViewById(R.id.password);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(i);
            }
        });




    }
}
