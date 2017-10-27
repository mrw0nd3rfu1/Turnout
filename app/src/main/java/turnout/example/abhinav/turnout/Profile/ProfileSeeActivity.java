package turnout.example.abhinav.turnout.Profile;

import android.app.ProgressDialog;
import android.icu.text.DateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import turnout.example.abhinav.turnout.R;


public class ProfileSeeActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    private String mPost_key = null;
    private String postId=null;
    private int hasImage=1;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUser;
    private CircleImageView mProfileImage;
    private TextView mNameUser;
    private TextView mCollegeName;
    private TextView mLocation;
    private FloatingActionButton mFab;
    private ImageView mCollegeImage;
    private DatabaseReference mDatabaseCollege;

    private String user_id = "";

    private String post_image;

    //friends
    private Button mProfileSendReqBtn;
    private Button mProfileDeclineReqBtn;

    private DatabaseReference mUserDatabase;
    private DatabaseReference mFriendsRequestDatabase;
    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mNotificationDatabase;
    private DatabaseReference mRootRef;
    private FirebaseUser mCurrentUser;

    private ProgressDialog mProgress;
    private String mCurrent_status;

    String clgID ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_see);

        clgID = getIntent().getExtras().getString("colgId");
        user_id = getIntent().getStringExtra("user_id");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(null);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Profile");

        mPost_key = getIntent().getExtras().getString("home_id");


        mNameUser = (TextView) findViewById(R.id.nameUser);
        mCollegeName = (TextView) findViewById(R.id.nameCollege);
        mLocation = (TextView) findViewById(R.id.nameLocation);
        mProfileImage = (CircleImageView) findViewById(R.id.userPic);
        mCollegeImage = (ImageView) findViewById(R.id.college_pic);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseCollege = FirebaseDatabase.getInstance().getReference().child("College");

        if (!clgID.equals("")) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child(clgID).child("Post").child(mPost_key);


            // Toast.makeText(HomeSingleActivity.this , mPost_key , Toast.LENGTH_SHORT).show();
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (user_id.equals("")) {
                        user_id = (String) dataSnapshot.child("uid").getValue();
                    }


                    mDatabaseUser.child(user_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String post_name = (String) dataSnapshot.child("name").getValue();
                            String post_college_name = (String) dataSnapshot.child("college_name").getValue();
                            post_image = (String) dataSnapshot.child("profile_pic").getValue();
                            //  hasImage=(Integer) dataSnapshot.child("With_image").getValue();
                            String post_uid = (String) dataSnapshot.child("uid").getValue();
                            String post_location = (String) dataSnapshot.child("location").getValue();
                            postId = (String) dataSnapshot.child("post_id").getValue();
                            mNameUser.setText(post_name);
                            mCollegeName.setText(post_college_name);
                            mLocation.setText(post_location);

                            Picasso.with(ProfileSeeActivity.this).load(post_image).into(mProfileImage);

                            String photo = (String) dataSnapshot.child("CollegeId").getValue();
                            mDatabaseCollege.child(photo).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String photo_college = (String) dataSnapshot.child("Image").getValue();
                                    Picasso.with(ProfileSeeActivity.this).load(photo_college).into(mCollegeImage);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else
        {
            mDatabaseUser.child(user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String post_name = (String) dataSnapshot.child("name").getValue();
                    String post_college_name = (String) dataSnapshot.child("college_name").getValue();
                    post_image = (String) dataSnapshot.child("profile_pic").getValue();
                    //  hasImage=(Integer) dataSnapshot.child("With_image").getValue();
                    String post_uid = (String) dataSnapshot.child("uid").getValue();
                    String post_location = (String) dataSnapshot.child("location").getValue();
                    postId = (String) dataSnapshot.child("post_id").getValue();
                    mNameUser.setText(post_name);
                    mCollegeName.setText(post_college_name);
                    mLocation.setText(post_location);

                    Picasso.with(ProfileSeeActivity.this).load(post_image).into(mProfileImage);

                    String photo = (String) dataSnapshot.child("CollegeId").getValue();
                    mDatabaseCollege.child(photo).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String photo_college = (String) dataSnapshot.child("Image").getValue();
                            Picasso.with(ProfileSeeActivity.this).load(photo_college).into(mCollegeImage);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        //friends

        mRootRef = FirebaseDatabase.getInstance().getReference();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mFriendsRequestDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("Notifications");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        mProfileSendReqBtn  = (Button) findViewById(R.id.prof_send_req_btn);
        mProfileDeclineReqBtn  = (Button) findViewById(R.id.prof_decline_btn);

        mCurrent_status = "not_friends";

        mProfileDeclineReqBtn.setVisibility(View.INVISIBLE);
        mProfileDeclineReqBtn.setEnabled(false);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Loading User Data");
        mProgress.setMessage("Please Wait While We Load");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //friend list
                mFriendsRequestDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(user_id)){
                            String req_type = dataSnapshot.child(user_id).child("request_type").getValue().toString();

                            if (req_type.equals("received")){
                                mCurrent_status = "req_received";
                                mProfileSendReqBtn.setText("Accept Friend Request");

                                mProfileDeclineReqBtn.setVisibility(View.VISIBLE);
                                mProfileDeclineReqBtn.setEnabled(true);

                            }else if (req_type.equals("sent")){
                                mCurrent_status = "req_sent";
                                mProfileSendReqBtn.setText("Cancel Friend Request");
                                mProfileDeclineReqBtn.setVisibility(View.INVISIBLE);
                                mProfileDeclineReqBtn.setEnabled(false);

                            }
                            mProgress.dismiss();
                        }else
                        {
                            mFriendsDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(user_id)){
                                        mCurrent_status = "friends";
                                        mProfileSendReqBtn.setText("Unfriend this person");

                                        mProfileDeclineReqBtn.setVisibility(View.INVISIBLE);
                                        mProfileDeclineReqBtn.setEnabled(false);
                                    }
                                    mProgress.dismiss();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    mProgress.dismiss();
                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mProfileSendReqBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                mProfileSendReqBtn.setEnabled(false);

                //not friends state
                if (mCurrent_status.equals("not_friends")){

                    DatabaseReference newNotificationRef = mRootRef.child("Notifications").child(user_id).push();
                    String newNotificationId = newNotificationRef.getKey();

                    HashMap<String ,String > notificationData = new HashMap<String, String>();
                    notificationData.put("from" , mCurrentUser.getUid());
                    notificationData.put("type", "request");


                    Map requestMap = new HashMap();
                    requestMap.put("Friend_req/" + mCurrentUser.getUid() + "/" + user_id + "/" + "request_type", "sent");
                    requestMap.put("Friend_req/" + user_id + "/" + mCurrentUser.getUid() + "/" + "request_type", "received");
                    requestMap.put("Notifications/" + user_id + "/" + newNotificationId, notificationData);

                    mRootRef.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            mCurrent_status = "req_sent";
                            mProfileSendReqBtn.setText("Cancel Friend Request");

                            if(databaseError != null){
                                Toast.makeText(ProfileSeeActivity.this , "There was some error in sending request" , Toast.LENGTH_LONG).show();
                            }

                        }
                    });


                    mProfileSendReqBtn.setEnabled(true);
                }

                //cancel friend state

                if (mCurrent_status.equals("req_sent")){
                    mFriendsRequestDatabase.child(mCurrentUser.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mFriendsRequestDatabase.child(user_id).child(mCurrentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mProfileSendReqBtn.setEnabled(true);
                                    mCurrent_status = "not_friends";
                                    mProfileSendReqBtn.setText("Send Friend Request");

                                    mProfileDeclineReqBtn.setVisibility(View.INVISIBLE);
                                    mProfileDeclineReqBtn.setEnabled(false);
                                }
                            });
                        }
                    });
                }

                //request recieved state

                if (mCurrent_status.equals("req_received")){

                    final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

                    Map friendsMap = new HashMap();
                    friendsMap.put("Friends/" + mCurrentUser.getUid() + "/" + user_id + "/date", currentDate);
                    friendsMap.put("Friends/" + user_id + "/" + mCurrentUser.getUid() + "/date", currentDate);

                    friendsMap.put("Friend_req/" + mCurrentUser.getUid() + "/" + user_id , null);
                    friendsMap.put("Friend_req/" + user_id + "/" + mCurrentUser.getUid() , null);

                    mRootRef.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if (databaseError == null){
                                mProfileSendReqBtn.setEnabled(true);
                                mCurrent_status = "friends";
                                mProfileSendReqBtn.setText("Unfriend this person");

                                mProfileDeclineReqBtn.setVisibility(View.VISIBLE);
                                mProfileDeclineReqBtn.setEnabled(false);
                            }
                            else {
                                String err = databaseError.getMessage();
                                Toast.makeText(ProfileSeeActivity.this , err , Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }

                //unfriend

                if (mCurrent_status.equals("friends")){
                    Map unfriendMap = new HashMap();
                    unfriendMap.put("Friends/" + mCurrentUser.getUid() + "/" + user_id,null);
                    unfriendMap.put("Friends/" + user_id + "/" + mCurrentUser.getUid() ,null);

                    mRootRef.updateChildren(unfriendMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if (databaseError == null){
                                mProfileSendReqBtn.setEnabled(true);
                                mCurrent_status = "not_friends";
                                mProfileSendReqBtn.setText("Send Friend Request");

                                mProfileDeclineReqBtn.setVisibility(View.VISIBLE);
                                mProfileDeclineReqBtn.setEnabled(false);
                            }
                            else {
                                String err = databaseError.getMessage();
                                Toast.makeText(ProfileSeeActivity.this , err , Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            }
        });

    }

}
