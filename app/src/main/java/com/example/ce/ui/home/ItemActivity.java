package com.example.ce.ui.home;

import static android.app.PendingIntent.getActivity;
import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ce.MainActivity;
import android.Manifest;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class ItemActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth;

    // Set Permission Array
    private static final String[] PERMISSIONS = new String[]{
            // Clander Permission
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
    };
    private static final int REQUEST_CODE=1;

    private OrderViewModel orderViewModel;
    private CalendarReminderUtils calendarUtils;

    // Postion Info from HomeFragment
    public double StartLatitude;
    public double StartLongitude;
    public String StartName;
    public double EndLatitude;
    public double EndLongitude;
    public String EndName;
    public int distance;
    public double price;
    public String timestamp;
    public Long timestampLong;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.iteminfo);
        // Get Calendar Permission
        PermissionUtil.checkPermission(ItemActivity.this,PERMISSIONS,REQUEST_CODE);

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
        DatePicker datePicker = findViewById(R.id.calendar);
        CheckBox ckSave = (CheckBox)  findViewById(R.id.saveToCalendar);

        auth = FirebaseAuth.getInstance();
        FirebaseUser User = auth.getCurrentUser();
        String UserID = User.getUid();
        EditText editName = findViewById(R.id.editName);
      //  EditText editWeight = findViewById(R.id.editWeight);
        Spinner Type = findViewById(R.id.Type_select);
        String type = Type.getSelectedItem().toString();
        EditText editDescription = findViewById(R.id.editDescrip);

        Button btnConfirm = findViewById(R.id.Confirm);
        ImageButton btnBack = findViewById(R.id.btnBack);
        orderViewModel =
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(OrderViewModel.class);

        datePicker.init(2023, 05, 14, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                Date date = calendar.getTime();
                // Do something with the date
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                timestamp = sdf.format(date);
                String StringDate = timestamp + " 12:00:00";
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date date2 = format.parse(StringDate);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                timestampLong =date.getTime();
            }
        });
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
                                if(editName.getText().toString() != null && type != null && timestamp != null){
                                    Order order = new Order(editName.getText().toString(), StartName, EndName
                                    , type, timestamp, StartLongitude, StartLatitude, EndLongitude, EndLatitude, price, editDescription.getText().toString(), false, UserID,"Processing",0);
                                    orderViewModel.insert(order);
                                    // Add Calendar Event
                                    if (ckSave.isChecked()) {
                                        calendarUtils=new CalendarReminderUtils ();
                                        String ClendarEvent = "Your " + editName.getText().toString() +" should be sent to " + EndName + " today.";
                                        calendarUtils.addCalendarEvent(ItemActivity.this,"Citizen Express Event",ClendarEvent,timestampLong);
                                        String msg = "This order has been add to your System Calendar.";
                                        toastMsg(msg);
                                    }
                                }
                                else{
                                    String msg = "The item information should not be null!";
                                    toastMsg(msg);
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
    public void toastMsg(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}



