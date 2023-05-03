package com.example.ce.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ce.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class InfoActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.info);
        //String EditText
        EditText nameEditText = findViewById(R.id.NameText);
        EditText phoneEditText = findViewById(R.id.PhoneText);
        EditText identityEditText = findViewById(R.id.identityText);
        EditText AddressEditText = findViewById(R.id.AddressText);

        Button btnback = findViewById(R.id.back);
        Button btnLogin = findViewById(R.id.Log);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent i = new Intent(InfoActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        btnback.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
                Intent i = new Intent(InfoActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });
    }

}


