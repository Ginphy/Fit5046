package com.example.ce.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ce.MainActivity;
import com.example.ce.R;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.info);

        RadioGroup rg = (RadioGroup) findViewById(R.id.rg_sex);;
        RadioButton rb_Male = (RadioButton) findViewById(R.id.male);;
        RadioButton rb_Female = (RadioButton) findViewById(R.id.female);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.male:
                        //user choose user option
                        Log.i("Sex","Current sex select " + rb_Male.getText().toString());
                        break;
                    case R.id.courier:
                        //user choose courier option
                        Log.i("Sex", "Current sex select"+ rb_Female.getText().toString());
                        break;
                }
            }
        });

        Button btnLogin = findViewById(R.id.Log);
        Button btnBack = findViewById(R.id.back);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent i = new Intent(InfoActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
                Intent i = new Intent(InfoActivity.this, StartActivity.class);
                startActivity(i);
            }
        });
    }

}


