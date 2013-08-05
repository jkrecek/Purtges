package com.frca.purtges.adapters;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.frca.purtges.R;
import com.frca.purtges.fragments.FragmentLog;
import com.frca.purtges.fragments.FragmentMainTeamData;
import com.frca.purtges.fragments.FragmentMainTeamList;

import java.util.Locale;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private Resources mResources;

    private final int[] tabTextIds = {R.string.section_title_log, R.string.section_title_data, R.string.section_title_list};

    //private Fragment[] fragments = new Fragment[tabTextIds.length];
    private Fragment[] fragments = {null, null, null};

    public SectionsPagerAdapter(FragmentManager fm, Resources resources) {
        super(fm);
        mResources = resources;
    }

    @Override
    public Fragment getItem(int position) {

        if (position >= getCount())
            return null;

        if (fragments[position] != null)
            return fragments[position];

        Fragment fragment = null;
        switch (position) {
            case 0:
                Log.e("adapter", "creating new FragmentLog");
                fragment = new FragmentLog();
                break;
            case 1:
                Log.e("adapter", "creating new FragmentMainTeamData");
                fragment = new FragmentMainTeamData();
                break;
            case 2:
                Log.e("adapter", "creating new FragmentMainTeamList");
                fragment = new FragmentMainTeamList();
                break;
        }

        fragments[position] = fragment;
        return fragment;
    }

    @Override
    public int getCount() {
        return tabTextIds.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position >= getCount())
            return null;

        Locale l = Locale.getDefault();

        return mResources.getString(tabTextIds[position]).toUpperCase(l);
    }
}
