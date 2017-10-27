package turnout.example.abhinav.turnout.Utility;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import turnout.example.abhinav.turnout.Profile.LoginActivity;
import turnout.example.abhinav.turnout.R;
import turnout.example.abhinav.turnout.Timeline.MainActivity;

public class SplashScreen extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);




        Thread myThread = new Thread(){
            @Override
            public void run() {

                    try {
                        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        sleep(1000);


                        final DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
                        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String clgID = (String) dataSnapshot.child("CollegeId").getValue();
                                if(clgID==null){
                                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                                startActivity(intent);}
                                else
                                    {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("colgId", clgID);
                                startActivity(intent);
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    } catch (Exception e) {
                        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(intent);
                        e.printStackTrace();
                    }
                }

        };
        myThread.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
