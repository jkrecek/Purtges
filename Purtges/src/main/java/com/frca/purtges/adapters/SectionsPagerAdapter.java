package com.frca.purtges.adapters;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.frca.purtges.R;
import com.frca.purtges.fragments.FragmentMainTeamData;
import com.frca.purtges.fragments.FragmentMainTeamList;

import java.util.Locale;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private Resources mResources;

    public SectionsPagerAdapter(FragmentManager fm, Resources resources) {
        super(fm);
        mResources = resources;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentMainTeamData();
            case 1:
                return new FragmentMainTeamList();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();

        switch (position) {
            case 0:
                return mResources.getString(R.string.section_title_data).toUpperCase(l);
            case 1:
                return mResources.getString(R.string.section_title_list).toUpperCase(l);
        }
        return null;
    }
}
