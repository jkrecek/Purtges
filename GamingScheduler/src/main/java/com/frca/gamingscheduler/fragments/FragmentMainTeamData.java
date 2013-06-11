package com.frca.gamingscheduler.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frca.gamingscheduler.R;

/**
 * Created by KillerFrca on 5.6.13.
 */
public class FragmentMainTeamData extends Fragment {
    public static final String ARG_SECTION_NUMBER = "section_number";

    public FragmentMainTeamData() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_team_data, container, false);
        //TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
        //dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }
}
