package com.sereem.remoteworker.ui.ui_for_main.worksites.worksiteDetails;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    private int numOfTabs;
    public Fragment[] fragments = new Fragment[4];
    public PagerAdapter(FragmentManager fm, int numOfTabs){
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numOfTabs = numOfTabs;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return new SiteViewFragment();
        else if (position == 1)
            return new MapsFragment();
        else if (position == 2)
            return new ContactsViewFragment();
        else if (position == 3)
            return new LiveMeetingFragment();
        return null;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        fragments[position]  = createdFragment;
        return createdFragment;
    }


}
