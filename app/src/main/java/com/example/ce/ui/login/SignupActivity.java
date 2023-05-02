package com.example.ce.ui.login;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ce.MainActivity;
import com.example.ce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.FirebaseFirestore;

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
        EditText confirmEditText = findViewById(R.id.password2);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_txt = emailEditText.getText().toString();
                String password_txt = passwordEditText.getText().toString();
                String confirm_txt = confirmEditText.getText().toString();

                if (TextUtils.isEmpty(email_txt) ||
                        TextUtils.isEmpty(password_txt) ||
                        TextUtils.isEmpty((confirm_txt))) {
                    String msg = "Empty Username or Password";
                    toastMsg(msg);
                } else if (password_txt.length() < 6) {
                    String msg = "Password is too short";
                    toastMsg(msg);
                } else if (!email_txt.contains("@")){
                    String msg = "Not a proper format of an E-mail.";
                    toastMsg(msg);
                } else if (!password_txt.equals(confirm_txt)) {
                    String msg = "Password isn't the same.";
                    toastMsg(msg);
                } else {
                    registerUser(email_txt, password_txt);
                }
            }
        });
    }

    private void registerUser(String email_txt, String password_txt) {
        // To create username and password
        auth.createUserWithEmailAndPassword(email_txt, password_txt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                Exception exception = task.getException();
                if (exception instanceof FirebaseAuthException) {
                    String errorCode = ((FirebaseAuthException) exception).getErrorCode();
                    if (errorCode.equals("ERROR_EMAIL_ALREADY_IN_USE")) {
                        String msg = "Registration failed: email already registered.";
                        toastMsg(msg);
                        Log.d("Firebase", "Registration failed: email already in use.");
                        recreate();
                    }
                }
                if (task.isSuccessful()) {
                    String msg = "Registration Successful";
                    toastMsg(msg);
                    finish();
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    //Should jump into Start enter information page, then jump into MainActivity
                    startActivity(intent);
                }
            }
        });
    }

    public void toastMsg(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}