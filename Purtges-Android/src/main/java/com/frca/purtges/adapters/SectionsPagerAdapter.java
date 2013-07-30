package com.frca.purtges.adapters;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.frca.purtges.R;
import com.frca.purtges.fragments.FragmentLog;
import com.frca.purtges.fragments.FragmentMainTeamData;
import com.frca.purtges.fragments.FragmentMainTeamList;

import java.util.Locale;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private Resources mResources;

    private Fragment[] fragments = {null, null, null};

    public SectionsPagerAdapter(FragmentManager fm, Resources resources) {
        super(fm);
        mResources = resources;
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments[position] != null)
            return fragments[position];

        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new FragmentLog();
                break;
            case 1:
                fragment = new FragmentMainTeamData();
                break;
            case 2:
                fragment = new FragmentMainTeamList();
                break;
            default:
                fragment = new Fragment();
        }

        fragments[position] = fragment;
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();

        switch (position) {
            case 0:
                return "Log";
            case 1:
                return mResources.getString(R.string.section_title_data).toUpperCase(l);
            case 2:
                return mResources.getString(R.string.section_title_list).toUpperCase(l);
        }
        return null;
    }
}
