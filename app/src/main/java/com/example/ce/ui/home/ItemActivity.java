package com.example.ce.ui.home;

import static android.app.PendingIntent.getActivity;
import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ce.MainActivity;
import com.example.ce.R;
import com.example.ce.ui.Database.entity.Order;
import com.example.ce.ui.Database.viewmodel.OrderViewModel;
import com.example.ce.ui.login.LoginActivity;
import com.example.ce.ui.login.SignupActivity;
import com.example.ce.ui.login.StartActivity;
import com.example.ce.ui.notifications.NotificationsViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class ItemActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth;
    private OrderViewModel orderViewModel;
    // Postion Info from HomeFragment
    public double StartLatitude;
    public double StartLongitude;
    public String StartName;
    public double EndLatitude;
    public double EndLongitude;
    public String EndName;
    public int distance;
    public double price;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.iteminfo);

        // Get Position Info from HomeFragment
        Intent intent = this.getIntent();
        StartLatitude = intent.getDoubleExtra("StartLatitude",0);
        StartLongitude = intent.getDoubleExtra("StartLongitude",0);
        EndLatitude = intent.getDoubleExtra("EndLatitude",0);
        EndLongitude = intent.getDoubleExtra("EndLongitude",0);
        StartName = intent.getStringExtra("StartName"); //iteminfo
        EndName = intent.getStringExtra("EndName"); //iteminfo
        price = intent.getDoubleExtra("price",0); //iteminfo
        distance = intent.getIntExtra("distance",0);

        // Set Position Info from HomeFragment
        TextView priceTextView = findViewById(R.id.price);
        priceTextView.setText(String.format("%.2f",price));
        TextView startNameTextView = findViewById(R.id.startName);
        TextView endNameTextView = findViewById(R.id.endName);
        startNameTextView.setText(StartName);
        endNameTextView.setText(EndName);

        auth = FirebaseAuth.getInstance();
        FirebaseUser User = auth.getCurrentUser();
        String UserID = User.getUid();
        EditText editName = findViewById(R.id.editName);
      //  EditText editWeight = findViewById(R.id.editWeight);
        Spinner Type = findViewById(R.id.Type_select);
        String type = Type.getSelectedItem().toString();
        EditText editStarting = findViewById(R.id.editStarting);
        EditText editDeadline = findViewById(R.id.editDeadline);
        EditText editDescription = findViewById(R.id.editDescrip);

        Button btnConfirm = findViewById(R.id.Confirm);
        ImageButton btnBack = findViewById(R.id.btnBack);
        orderViewModel =
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(OrderViewModel.class);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent i = new Intent(ItemActivity.this, MainActivity.class );
                startActivity(i);
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] id = new String[1];
                new AlertDialog.Builder(ItemActivity.this)
                        .setTitle("Confirm Item")
                        .setMessage("Item name: "+ editName.getText().toString() +"\nItem type: "
                                + Type.getSelectedItem().toString())
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Next operation
                                if(editName.getText().toString() != null && type != null && editStarting.getText().toString() != null){
                                    Order order = new Order(editName.getText().toString(), editStarting.getText().toString(), StartName, EndName
                                    , type, StartLongitude, StartLatitude, EndLongitude, EndLatitude, price, editDescription.getText().toString(), false, UserID,"Processing",0);
                                    orderViewModel.insert(order);

                                }

                                Intent intent = new Intent(ItemActivity.this, paySuccess.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
    }

}
