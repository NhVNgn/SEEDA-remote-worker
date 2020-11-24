package com.sereem.remoteworker.ui.ui_for_main.worksites;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

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
        return null;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

}
