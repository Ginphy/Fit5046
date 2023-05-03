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
import android.widget.RadioButton;

import android.widget.RadioGroup;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.FirebaseFirestore;


public class SignupActivity  extends AppCompatActivity {
    private FirebaseAuth auth;
    RadioGroup rg = (RadioGroup) findViewById(R.id.rg_UserOrCourier);;
    RadioButton rb_User = (RadioButton) findViewById(R.id.user);;
    RadioButton rb_Courier = (RadioButton) findViewById(R.id.courier);
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.signup);
        auth = FirebaseAuth.getInstance();
        //RadioGroup Operation

        rg.setOnCheckedChangeListener(new RadioButtonListener());

        Button btnLogin = findViewById(R.id.Log);

        Button btnBack = findViewById(R.id.Back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent i = new Intent(SignupActivity.this,StartActivity.class );
                startActivity(i);

            }
        });

        EditText emailEditText = findViewById(R.id.NameText);
        EditText passwordEditText = findViewById(R.id.PhoneText);
        EditText confirmEditText = findViewById(R.id.identityText);

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
    public class RadioButtonListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            switch (i){
                case R.id.user:
                    //user choose user option
                    Log.i("Role","Current user select" + rb_User.getText().toString());
                    break;
                case R.id.courier:
                    //user choose courier option
                    Log.i("Role", "Current user select"+ rb_Courier.getText().toString());
                    break;
            }

        }
    }

    public void toastMsg(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}