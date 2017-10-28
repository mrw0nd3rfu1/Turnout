package turnout.example.abhinav.turnout.Timeline;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import turnout.example.abhinav.turnout.Fragments.HotEventsFragment;
import turnout.example.abhinav.turnout.Fragments.EventFragment;
import turnout.example.abhinav.turnout.Fragments.HomeFragment;

/**
 * Created by Abhinav on 21-Jun-17.
 */

class SectionsPagerAdapter extends FragmentPagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0 :
                return new HomeFragment();
            case 1 :
                return new EventFragment();
            case 2: return new HotEventsFragment();
           default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle (int position){
       /* switch (position){
            case 0 : return "HOME";
            case 1 : return  "EVENTS";
           default:
               return  null;
        }*/
       return null;
    }
}
