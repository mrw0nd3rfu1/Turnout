package turnout.example.abhinav.turnout.Timeline;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import turnout.example.abhinav.turnout.R;

public class HomeSingleActivity extends AppCompatActivity {

    private String mPost_key = null;
    private String postId=null;
    private int hasImage=1;
    private DatabaseReference mDatabase;
     private StorageReference mStorage;
    private ImageView mHomeSingleImage;
    private TextView mHomeSingleEvent;
    private TextView mHomeSinglePost;
    private TextView mHomeSingleUsername;

    private FirebaseAuth mAuth;

    private FloatingActionButton mHomeSingleRemoveBtn;

    private String post_image;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_single);
        final String clgID = getIntent().getExtras().getString("colgId");

        mDatabase = FirebaseDatabase.getInstance().getReference().child(clgID).child("Post");


        mAuth = FirebaseAuth.getInstance();

        mPost_key = getIntent().getExtras().getString("home_id");

        mHomeSingleEvent = (TextView) findViewById(R.id.singleHomeEvent);
        mHomeSinglePost = (TextView) findViewById(R.id.singleHomePost);
        mHomeSingleUsername = (TextView) findViewById(R.id.singleHomeUsername);
        mHomeSingleImage = (ImageView) findViewById(R.id.singleHomeImage);

        mHomeSingleRemoveBtn = (FloatingActionButton) findViewById(R.id.removeButton);

       // Toast.makeText(HomeSingleActivity.this , mPost_key , Toast.LENGTH_SHORT).show();

        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_event = (String) dataSnapshot.child("event").getValue();
                String post_post = (String) dataSnapshot.child("post").getValue();
                post_image = (String) dataSnapshot.child("image").getValue();
               //  hasImage=(Integer) dataSnapshot.child("With_image").getValue();
                String post_uid = (String) dataSnapshot.child("uid").getValue();
                String post_username = (String) dataSnapshot.child("username").getValue();
                postId=(String)dataSnapshot.child("post_id").getValue();
                mHomeSingleEvent.setText(post_event);
                mHomeSinglePost.setText(post_post);
                mHomeSingleUsername.setText(post_username);

                Picasso.with(HomeSingleActivity.this).load(post_image).into(mHomeSingleImage);

                if (mAuth.getCurrentUser().getUid().equals(post_uid)) {
                    mHomeSingleRemoveBtn.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mHomeSingleRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           Long val = (Long) dataSnapshot.child(mPost_key).child("With_image").getValue();
                           if (val == 1) {
                               mStorage = FirebaseStorage.getInstance().getReference().child("Posts/" + postId);
                               mStorage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void aVoid) {
                                       Toast.makeText(HomeSingleActivity.this, "Removed Succesfully", Toast.LENGTH_SHORT).show();

                                   }

                               }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception exception) {
                                       Toast.makeText(HomeSingleActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                   }
                               });
                           } else
                               Toast.makeText(HomeSingleActivity.this, "Removed Successfully", Toast.LENGTH_SHORT).show();
                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });
               mDatabase.child(mPost_key).removeValue();

                Intent mainIntent = new Intent(HomeSingleActivity.this, MainActivity.class);
                mainIntent.putExtra("colgId", clgID);
                startActivity(mainIntent);

            }
        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }


}
