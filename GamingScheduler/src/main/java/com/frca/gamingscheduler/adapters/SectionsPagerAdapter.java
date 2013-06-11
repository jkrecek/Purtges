package com.frca.gamingscheduler.adapters;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frca.gamingscheduler.R;
import com.frca.gamingscheduler.fragments.FragmentMainTeamData;
import com.frca.gamingscheduler.fragments.FragmentMainTeamList;

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
            case 0: return new FragmentMainTeamData();
            case 1: return new FragmentMainTeamList();
            default:return new Fragment();
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
