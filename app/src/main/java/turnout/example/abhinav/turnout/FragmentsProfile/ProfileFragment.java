package turnout.example.abhinav.turnout.FragmentsProfile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import turnout.example.abhinav.turnout.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private View mProfile1View;
    private View dialogView;
    private View dialogView2;

    private TextView mNameUser;
    private TextView mCollegeName;
    private TextView mLocation;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private Button edit_Name;
    private  Button edit_Location;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mProfile1View = inflater.inflate(R.layout.fragment_profile , container ,false);

        mAuth =FirebaseAuth.getInstance();

        dialogView = inflater.inflate(R.layout.name_update, null);
        dialogView2 = inflater.inflate(R.layout.location_update, null);

        edit_Name = (Button) mProfile1View.findViewById(R.id.edit_Name);
        edit_Location = (Button) mProfile1View.findViewById(R.id.edit_Location);

        mNameUser = (TextView) mProfile1View.findViewById(R.id.nameUser);
        mCollegeName = (TextView) mProfile1View.findViewById(R.id.nameCollege);
        mLocation = (TextView) mProfile1View.findViewById(R.id.nameLocation);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mDatabase.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_name = (String) dataSnapshot.child("name").getValue();
                String post_college_name = (String) dataSnapshot.child("college_name").getValue();
                //  hasImage=(Integer) dataSnapshot.child("With_image").getValue();
                String post_uid = (String) dataSnapshot.child("uid").getValue();
                String post_location = (String) dataSnapshot.child("location").getValue();
                mNameUser.setText(post_name);
                mCollegeName.setText(post_college_name);
                mLocation.setText(post_location);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        edit_Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateName();
            }
        });
        edit_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLocation();


            }
        });

        return mProfile1View;
    }


    public boolean updateName(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateArtist);

        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    b.dismiss();
                    mDatabase.child(mAuth.getCurrentUser().getUid()).child("name").setValue(name);
                }
            }
        });
        return true;
    }

    public boolean updateLocation (){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(dialogView2);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateArtist);

        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    DatabaseReference dr= mDatabase.child(mAuth.getCurrentUser().getUid()).child("location");
                    dr.setValue(name);
                    b.dismiss();
                }
            }
        });

        return true;
    }



}
