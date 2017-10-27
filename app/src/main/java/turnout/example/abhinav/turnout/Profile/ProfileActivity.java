package turnout.example.abhinav.turnout.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import turnout.example.abhinav.turnout.College.CollegeListActivity;
import turnout.example.abhinav.turnout.College.CollegePhotoSelector;
import turnout.example.abhinav.turnout.R;
import turnout.example.abhinav.turnout.Timeline.MainActivity;
import turnout.example.abhinav.turnout.Utility.AboutActivity;

import de.hdodenhof.circleimageview.CircleImageView;
import turnout.example.abhinav.turnout.Utility.AboutActivity;


public class ProfileActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    private String mPost_key = null;
     private String postId=null;
    private int hasImage=1;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseCollege;
    private StorageReference mStorage;
    private CircleImageView mProfileImage;
    private TextView mNameUser;
    private TextView mCollegeName;
    private TextView mLocation;
    private ImageView mCollegeImage;
    private DatabaseReference mDatabaseUsers;

    private FirebaseAuth mAuth;

    private String post_image;

    private ViewPager mViewPager;
    private SectionsPagerAdapter2 mSections;
    private TabLayout mTab;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(null);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("  ");


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseCollege = FirebaseDatabase.getInstance().getReference().child("College");


        mAuth = FirebaseAuth.getInstance();

        mProfileImage = (CircleImageView) findViewById(R.id.userPic);
        mCollegeImage = (ImageView) findViewById(R.id.college_pic);

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        // Toast.makeText(HomeSingleActivity.this , mPost_key , Toast.LENGTH_SHORT).show();

        mDatabase.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_name = (String) dataSnapshot.child("name").getValue();
                String post_college_name = (String) dataSnapshot.child("college_name").getValue();
                post_image = (String) dataSnapshot.child("profile_pic").getValue();
                //  hasImage=(Integer) dataSnapshot.child("With_image").getValue();
                postId=(String)dataSnapshot.child("post_id").getValue();

                Picasso.with(ProfileActivity.this).load(post_image).into(mProfileImage);

                String photo = (String) dataSnapshot.child("CollegeId").getValue();

                mDatabaseCollege.child(photo).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String photo_college = (String) dataSnapshot.child("Image").getValue();
                        Picasso.with(ProfileActivity.this).load(photo_college).into(mCollegeImage);

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

        //tabs
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mSections = new SectionsPagerAdapter2(getSupportFragmentManager());

        mViewPager.setAdapter(mSections);

        mTab = (TabLayout) findViewById(R.id.main_tab);
        mTab.setupWithViewPager(mViewPager);




        mProfileImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ProfilePhotoSelector.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

         return true;
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {



        mDatabaseUsers.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               if (dataSnapshot.child("CollegeId").getValue().equals(MainActivity.clgID))
                { if (item.getItemId() == R.id.item_photo){
                    Intent collegeIntent = new Intent(ProfileActivity.this, CollegePhotoSelector.class);
                    collegeIntent.putExtra("colgId", MainActivity.clgID);
                    collegeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(collegeIntent); }
                }
                else {
                    Toast.makeText(ProfileActivity.this , "You are not present in this college" ,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (item.getItemId()== R.id.action_college){
            Intent collegeIntent = new Intent(ProfileActivity.this, CollegeListActivity.class);
            collegeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(collegeIntent);
        }
        if (item.getItemId() == R.id.action_logout) {

            logout();
        }




        if (item.getItemId() == R.id.action_profile) {
            Intent profileIntent = new Intent(ProfileActivity.this, ProfileActivity.class);
            profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(profileIntent);

        }


        if (item.getItemId() == R.id.action_about) {

            Intent profileIntent = new Intent(ProfileActivity.this, AboutActivity.class);
            profileIntent.putExtra("colgId", MainActivity.clgID);
            profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(profileIntent);

        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mAuth.signOut();
    }

}
