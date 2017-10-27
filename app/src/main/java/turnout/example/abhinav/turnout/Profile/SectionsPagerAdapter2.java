package turnout.example.abhinav.turnout.Profile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import turnout.example.abhinav.turnout.FragmentsProfile.PostsFragment;
import turnout.example.abhinav.turnout.FragmentsProfile.ProfileFragment;


class SectionsPagerAdapter2 extends FragmentPagerAdapter {
    public SectionsPagerAdapter2(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0 :
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;

            case 1 :
                PostsFragment postsFragment = new PostsFragment();
                return postsFragment;
            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle (int position){
        switch (position){
            case 0 : return "PROFILE";
            case 1 : return  "POSTS";
            default: return  null;
        }
    }
}
