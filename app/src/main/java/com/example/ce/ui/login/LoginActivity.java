package com.example.ce.ui.login;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ce.MainActivity;
import com.example.ce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.checkerframework.common.subtyping.qual.Bottom;


public class LoginActivity extends AppCompatActivity {

    // Connect to firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle saveInstanceState) {

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Set view
        super.onCreate(saveInstanceState);
        setContentView(R.layout.userlogin);

        // Get input information
        EditText emailEditText = findViewById(R.id.Email);
        EditText passwordEditText = findViewById(R.id.password);
        Button btnLogin = findViewById(R.id.Log);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailInfo = emailEditText.getText().toString();
                String passwordInfo = passwordEditText.getText().toString();
                mAuth.signInWithEmailAndPassword(emailInfo, passwordInfo)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "Login success!");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            updateUI(user);
                                            finish();
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                                            Toast.makeText(LoginActivity.this, "Login failed, invalid account or wrong password.",
                                                    Toast.LENGTH_SHORT).show();
                                            updateUI(null);
                                            recreate();
                                        }
                                    }
                                });
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

//        String email = mBinding.fieldEmail.getText().toString();
//        if (TextUtils.isEmpty(email)) {
//            mBinding.fieldEmail.setError("Required.");
//            valid = false;
//        } else {
//            mBinding.fieldEmail.setError(null);
//        }
//
//        String password = mBinding.fieldPassword.getText().toString();
//        if (TextUtils.isEmpty(password)) {
//            mBinding.fieldPassword.setError("Required.");
//            valid = false;
//        } else {
//            mBinding.fieldPassword.setError(null);
//        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        // hideProgressBar();
        if (user != null) {
            Intent i = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(i);
        } else {
            ;
        }
    }

}
