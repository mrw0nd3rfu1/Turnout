package turnout.example.abhinav.turnout.Comment;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import turnout.example.abhinav.turnout.R;


public class CommentActivity extends ArrayAdapter<CommentPosts> {
    private Activity context;
    List<CommentPosts> cname;
    DatabaseReference mData;
    FirebaseAuth mAuth;

    public CommentActivity(Activity context, List<CommentPosts> artists) {
        super(context , R.layout.comment_layout, artists);
        this.context = context;
        this.cname = artists;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.comment_layout, null, true);
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference().child("Users");


        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        final TextView textViewUser = (TextView) listViewItem.findViewById(R.id.textViewUser);
        final CircleImageView user_pic = (CircleImageView) listViewItem.findViewById(R.id.userPic);



        CommentPosts name = cname.get(position);
        textViewName.setText(name.getuserName());
        mData.child(name.getComment()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String c = (String) dataSnapshot.child("name").getValue();
                textViewUser.setText(c);
                String d = (String) dataSnapshot.child("profile_pic").getValue();
                Picasso.with(context).load(d).into(user_pic);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return listViewItem;
    }


}
