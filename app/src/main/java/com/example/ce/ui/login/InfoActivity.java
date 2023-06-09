package com.example.ce.ui.login;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ce.MainActivity;
import com.example.ce.R;
import com.example.ce.ui.home.CourierHomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class InfoActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public String role;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        FirebaseAuth auth;
        setContentView(R.layout.info);
        auth = FirebaseAuth.getInstance();
        FirebaseUser User = auth.getCurrentUser();
        Intent intent = getIntent();
        String[] sex = new String[1];
        int role2 = intent.getIntExtra("role",-1);
        if(role2 == 1 )
        {
            role = "User";
        }
        else{
            role = "Courier";
        }
        RadioGroup rg = (RadioGroup) findViewById(R.id.rg_sex);
        RadioButton rb_Male = (RadioButton) findViewById(R.id.male);
        RadioButton rb_Female = (RadioButton) findViewById(R.id.female);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.male:
                        //user choose user option
                        Log.i("Sex","Current sex select " + rb_Male.getText().toString());
                        sex[0] = "Male";
                        break;
                    case R.id.courier:
                        //user choose courier option
                        Log.i("Sex", "Current sex select"+ rb_Female.getText().toString());
                        sex[0] = "Female";
                        break;
                    default:
                        String msg = "You need to choose your gender!";
                        toastMsg(msg);
                }
            }
        });
        EditText nameEditText = findViewById(R.id.NameText);
        EditText phoneEditText = findViewById(R.id.PhoneText);
        EditText AddressEditText = findViewById(R.id.AddressText);
        EditText IdentityEditText = findViewById(R.id.identityText);
        Button btnLogin = findViewById(R.id.Log);
        Button btnBack = findViewById(R.id.back);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new user with a first and last name
                Map<String, Object> user = new HashMap<>();
                user.put("name", nameEditText.getText().toString());
                user.put("phone", phoneEditText.getText().toString());
                user.put("address", AddressEditText.getText().toString());
                user.put("email", User.getEmail());
                user.put("identity", IdentityEditText.getText().toString());
                user.put("uid", User.getUid());
                if (sex[0] == "Male")
                    user.put("sex", "Male");
                else user.put("sex", "Female");
                user.put("role", role);
                user.put("credit", 100);

                // Add a new document with a generated ID
                db.collection("users")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
          //      Log.w(TAG,role);
//                System.out.printf(role);
                if(role2 == 1){
           //         Log.w(TAG,role);
                    finish();
                    Intent i = new Intent(InfoActivity.this, MainActivity.class);
                    startActivity(i);
                }
                if(role2 == 0){
         //           Log.w(TAG,role);
                    finish();
                    Intent i = new Intent(InfoActivity.this, CourierHomeActivity.class);
                    startActivity(i);
                }
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
    public void toastMsg(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}


