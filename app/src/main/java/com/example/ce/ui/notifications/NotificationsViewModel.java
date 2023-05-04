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
        // 获取users集合中ID的文档
        // mOldData.Profile = personaldata[0];
        //        mOldData.Name = personaldata[1];
        //        mOldData.Nickname = "Click to set";
        //        mOldData.Gender = personaldata[2];
        //        mOldData.Identity = personaldata[3];
        //        mOldData.Phone = personaldata[4];
        //        mOldData.Email = personaldata[5];
        //
        //        mData.postValue(mOldData);
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
        mOldData.Profile = "Profile";
        mOldData.Email = "E-mail";
        mOldData.Gender = "TBD";
        mOldData.Identity = "110";
        mOldData.Name = "ZJJSB";
        mOldData.Nickname = "I am Pig";
        mOldData.Phone = "110";
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
