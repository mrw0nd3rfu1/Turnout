package turnout.example.abhinav.turnout.College;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;




import java.util.List;

import turnout.example.abhinav.turnout.R;


public class CollegeActivity extends ArrayAdapter<CollegeName> {
    private Activity context;
    List<CollegeName> cname;

    public CollegeActivity(Activity context, List<CollegeName> artists) {
        super(context, R.layout.college_list, artists);
        this.context = context;
        this.cname = artists;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.college_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);

        CollegeName name = cname.get(position);
        textViewName.setText(name.getCollegeName());


        return listViewItem;
    }


}
