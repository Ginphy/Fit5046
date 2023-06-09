package com.example.ce.ui.notifications;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class NotificationsViewModel extends ViewModel {
    private final MutableLiveData<PersonalInfo> mData;
    private PersonalInfo mOldData;
    public void Getprofile(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        String id = auth.getCurrentUser().getUid();
        Log.d(TAG, "id: "+id);

        db.collection("users")
                .whereEqualTo("uid", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                mOldData.Profile = document.getString("uid");
                                mOldData.Nickname = "Click to Set!";
                                mOldData.Name = document.getString("name");
                                mOldData.Gender = document.getString("sex");
                                mOldData.Identity = document.getString("identity");
                                mOldData.Phone = document.getString("phone");
                                mOldData.Email = document.getString("email");
                                mOldData.Credit = document.getLong("credit").intValue();

                                Log.d(TAG, "Successful getting documents!" + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        mData.postValue(mOldData);
                    }
                });
    }
    public NotificationsViewModel() {
        mOldData = new PersonalInfo();
        mOldData.Profile = "Should be your UserID";
        mOldData.Email = "E-mail";
        mOldData.Gender = "LGBT";
        mOldData.Identity = "Your Identity Number";
        mOldData.Name = "Username";
        mOldData.Nickname = "ThaiCoolLa";
        mOldData.Phone = "Your Phone Number";
        mOldData.Credit = 100;


        mData = new MutableLiveData<>();
        mData.setValue(mOldData);
    }
    public LiveData<PersonalInfo> getData() {
        Getprofile();
        return mData;
    }


    public class PersonalInfo {
        public String Profile;
        public String Nickname;
        public String Name;
        public String Gender;
        public String Identity;
        public String Phone;
        public String Email;
        public int Credit;
    }
}
