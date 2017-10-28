package turnout.example.abhinav.turnout.Timeline;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import turnout.example.abhinav.turnout.College.CollegeListActivity;
import turnout.example.abhinav.turnout.College.CollegePhotoSelector;
import turnout.example.abhinav.turnout.Event.AddEventActivity;
import turnout.example.abhinav.turnout.Event.EventListActivity;
import android.Manifest;

import turnout.example.abhinav.turnout.Profile.LoginActivity;
import turnout.example.abhinav.turnout.Profile.ProfileActivity;
import turnout.example.abhinav.turnout.Profile.SetupActivity;
import turnout.example.abhinav.turnout.R;
import turnout.example.abhinav.turnout.Utility.AboutActivity;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final int GALLERY_REQUEST = 1;
    public static String clgID;
    Toolbar mtoolbar;
    FloatingActionButton mfab;
    FloatingActionButton mfabPost;
    FloatingActionButton mfabEvent;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mCollege;
    private DatabaseReference mDatabaseLike;
    private FirebaseAuth mAuth;
    private Query orderData;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean isUserClickedBackButton = false;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    private CircleImageView mProfileImage;
    private ImageView mCollegePic;
    private TextView mNameUser;
    private ImageButton imageView;
    private TextView userClgPic;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSections;
    private TabLayout mTab;
    private View hview ;
    private TextView nav_user ;
    private CircleImageView nav_pic;
    private ImageButton mChat;
    private ImageView nav_col_pic;

    //location
    private LocationManager locationManager;
    private static final int REQUEST_LOCATION = 1;
    String lattitude,longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clgID = getIntent().getExtras().getString("colgId");


        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    loginIntent.putExtra("colgId", clgID);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout , R.string.open , R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationview = (NavigationView) findViewById(R.id.nav_view);
        hview = navigationview.getHeaderView(0);
        nav_user = (TextView) hview.findViewById(R.id.menu_username);
        nav_pic = (CircleImageView) hview.findViewById(R.id.menu_profile_pic);
        nav_col_pic = (ImageView) hview.findViewById(R.id.menu_college_pic);
        navigationview.setNavigationItemSelectedListener(this);

        mfab = (FloatingActionButton) findViewById(R.id.fab);
        mfabPost = (FloatingActionButton) findViewById(R.id.postButton);
        mfabEvent = (FloatingActionButton) findViewById(R.id.eventButton);
        final LinearLayout mPostLayout = (LinearLayout) findViewById(R.id.postLayout);
        final LinearLayout mEventLayout = (LinearLayout) findViewById(R.id.eventLayout);
        final Animation mShowButton = AnimationUtils.loadAnimation(MainActivity.this , R.anim.show_button);
        final Animation mHideButton = AnimationUtils.loadAnimation(MainActivity.this , R.anim.hide_button);
        final Animation mShowLayout = AnimationUtils.loadAnimation(MainActivity.this , R.anim.show_layout);
        final Animation mHideLayout = AnimationUtils.loadAnimation(MainActivity.this , R.anim.hide_layout);
        //final View mShadowView = (View) findViewById(R.id.shadowView);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mtoolbar.setNavigationIcon(null);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("  ");
        mProfileImage = (CircleImageView) findViewById(R.id.profile_pic);
        mChat = (ImageButton) findViewById(R.id.messagingAcitivity);
        mCollege = FirebaseDatabase.getInstance().getReference().child("College").child(clgID);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(clgID).child("Post");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Like");
        orderData = mDatabase.orderByChild("post_id").limitToFirst(30);
        mDatabaseUsers.keepSynced(true);
        mDatabaseLike.keepSynced(true);
        mDatabase.keepSynced(true);
        orderData.keepSynced(true);

        //tabs
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mSections = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSections);

        mTab = (TabLayout) findViewById(R.id.main_tab);
        mTab.setupWithViewPager(mViewPager);
        setupTabIcons();

        //checking user exists in college
        mDatabaseUsers.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("CollegeId").getValue().equals(clgID))
                    mfab.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        mChat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent neabyLoc = new Intent(MainActivity.this , NearbyEvents.class);
//                neabyLoc.putExtra("colgId",clgID);
//                neabyLoc.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(neabyLoc);
//            }
//        });

        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPostLayout.getVisibility() == View.VISIBLE && mEventLayout.getVisibility() == View.VISIBLE){
                    mPostLayout.setVisibility(View.GONE);
                    mEventLayout.setVisibility(View.GONE);
                    mPostLayout.startAnimation(mHideLayout);
                    mEventLayout.startAnimation(mHideLayout);
                    mfab.startAnimation(mHideButton);
                   // mShadowView.setVisibility(View.GONE);
                }
                else
                {
                    mPostLayout.setVisibility(View.VISIBLE);
                    mEventLayout.setVisibility(View.VISIBLE);
                    mPostLayout.startAnimation(mShowLayout);
                    mEventLayout.startAnimation(mShowLayout);
                    mfab.startAnimation(mShowButton);
                   // mShadowView.setVisibility(View.VISIBLE);
                }

            }
        });
        mfabPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPostLayout.setVisibility(View.GONE);
                mEventLayout.setVisibility(View.GONE);
                mPostLayout.startAnimation(mHideLayout);
                mEventLayout.startAnimation(mHideLayout);
                mfab.startAnimation(mHideButton);
             //   mShadowView.setVisibility(View.GONE);
                Intent postIntent = new Intent(MainActivity.this, EventListActivity.class);
                postIntent.putExtra("colgId",clgID);
                postIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(postIntent);
            }
        });
        mfabEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPostLayout.setVisibility(View.GONE);
                mEventLayout.setVisibility(View.GONE);
                mPostLayout.startAnimation(mHideLayout);
                mEventLayout.startAnimation(mHideLayout);
                mfab.startAnimation(mHideButton);
              //  mShadowView.setVisibility(View.GONE);
                Intent postIntent = new Intent(MainActivity.this, AddEventActivity.class);
                postIntent.putExtra("colgId",clgID);
                postIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(postIntent);
            }
        });

        mAuth.addAuthStateListener(mAuthListener);


        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }

        checkUserExist();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                mDatabaseUsers.child(mAuth.getCurrentUser().getUid()).child("latitude").setValue(lattitude);
                mDatabaseUsers.child(mAuth.getCurrentUser().getUid()).child("longitude").setValue(longitude);

                }else{
                Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void setupTabIcons() {
        mTab.getTabAt(0).setIcon(R.drawable.ic_home_white_24dp);
        mTab.getTabAt(1).setIcon(R.drawable.ic_event_white_24dp);
        mTab.getTabAt(2).setIcon(R.drawable.ic_flame);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabaseUsers.child(mAuth.getCurrentUser().getUid()).child("online").setValue("true");

    }



    @Override
    public void onBackPressed() {
        if (!isUserClickedBackButton) {
            Toast.makeText(this, "Press Back button again to Exit", Toast.LENGTH_SHORT).show();
            isUserClickedBackButton = true;
        }
        else
        {
            super.onBackPressed();
        }
        new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                isUserClickedBackButton=false;
            }
        }.start();
    }

    public void displayInterstitial() {
// If Ads are loaded, show Interstitial else show nothing.
        // if (interstitial.isLoaded()) {
        //   interstitial.show();
        // }
    }

    private void checkUserExist() {

        if (mAuth.getCurrentUser() != null) {

            final String user_ID = mAuth.getCurrentUser().getUid();

            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild(user_ID)) {

                        Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
                        setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupIntent);

                    } else {
                        mDatabaseUsers.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String profile_name = (String) dataSnapshot.child("name").getValue();
                                String profile_image = (String) dataSnapshot.child("profile_pic").getValue();
                                Picasso.with(MainActivity.this).load(profile_image).into(mProfileImage);
                                 Picasso.with(MainActivity.this).load(profile_image).into(nav_pic);
                                 nav_user.setText(profile_name);
                                mCollege.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String college_image = (String) dataSnapshot.child("Image").getValue();
                                        /*Picasso.with(MainChatActivity.this).load(college_image).into(mCollegePic);
                                        String college_user_name = (String) dataSnapshot.child("ImagePost").getValue();
                                        userClgPic.setText("Last Uploaded By "+college_user_name); */
                                        Picasso.with(MainActivity.this).load(college_image).into(nav_col_pic);


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                //  String clg = (String) dataSnapshot.child("CollegeId").getValue();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


    //    getMenuInflater().inflate(R.menu.main_menu, menu);

   //     getMenuInflater().inflate(R.menu.college_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {


            mDatabaseUsers.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final String clgID = getIntent().getExtras().getString("colgId");
                    if (dataSnapshot.child("CollegeId").getValue().equals(clgID)) {
                        if (item.getItemId() == R.id.item_photo) {
                            Intent collegeIntent = new Intent(MainActivity.this, CollegePhotoSelector.class);
                            collegeIntent.putExtra("colgId", clgID);
                            collegeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(collegeIntent);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "You are not present in this college", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            if (item.getItemId() == R.id.action_college) {
                Intent collegeIntent = new Intent(MainActivity.this, CollegeListActivity.class);
                collegeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(collegeIntent);
            }
            if (item.getItemId() == R.id.action_logout) {

                logout();
            }


            if (item.getItemId() == R.id.action_profile) {
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(profileIntent);

            }


            if (item.getItemId() == R.id.action_about) {
                final String clgID = getIntent().getExtras().getString("colgId");

                Intent profileIntent = new Intent(MainActivity.this, AboutActivity.class);
                profileIntent.putExtra("colgId", clgID);
                profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(profileIntent);

            }
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }

    }

    private void logout() {
        mAuth.signOut();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {

        mDatabaseUsers.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String clgID = getIntent().getExtras().getString("colgId");
                if (dataSnapshot.child("CollegeId").getValue().equals(clgID))
                { if (item.getItemId() == R.id.item_photo){
                    Intent collegeIntent = new Intent(MainActivity.this, CollegePhotoSelector.class);
                    collegeIntent.putExtra("colgId", clgID);
                    collegeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(collegeIntent); }
                }
                else {
                    Toast.makeText(MainActivity.this , "You are not present in this college" ,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (item.getItemId() == R.id.action_college){
            Intent collegeIntent = new Intent(MainActivity.this, CollegeListActivity.class);
            collegeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(collegeIntent);
        }
        if (item.getItemId() == R.id.action_logout) {

            logout();
        }

        if (item.getItemId() == R.id.action_profile) {
            Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
            profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(profileIntent);

        }




        if (item.getItemId() == R.id.action_about) {
            final String clgID = getIntent().getExtras().getString("colgId");

            Intent profileIntent = new Intent(MainActivity.this, AboutActivity.class);
            profileIntent.putExtra("colgId", clgID);
            profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(profileIntent);

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



}


