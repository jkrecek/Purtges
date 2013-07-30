package com.frca.purtges.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frca.purtges.R;
import com.frca.purtges.adapters.SectionsPagerAdapter;

public class FragmentMainTeam extends Fragment {
    public static final String ARG_SECTION_NUMBER = "section_number";

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    public FragmentMainTeam() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_team, container, false);

        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);

        if (mViewPager != null) {
            mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager(), getResources());

            mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
            mViewPager.setAdapter(mSectionsPagerAdapter);
        }

        return rootView;
    }

    public SectionsPagerAdapter getSectionsPagerAdapter() {
        return mSectionsPagerAdapter;
    }
}
