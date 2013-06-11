package com.frca.gamingscheduler.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.frca.gamingscheduler.R;
import com.frca.gamingscheduler.adapters.TeamListAdapter;

import java.util.ArrayList;
import java.util.List;
import com.frca.gamingscheduler.adapters.TeamListAdapter.TeamListItem.State;

/**
 * Created by KillerFrca on 5.6.13.
 */
public class FragmentMainTeamList extends Fragment {
    public static final String ARG_SECTION_NUMBER = "section_number";

    private ListView list;
    public FragmentMainTeamList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_team_list, container, false);
        list = (ListView) view.findViewById(R.id.list);
        list.setAdapter(getAdapter());

        return view;
    }

    private TeamListAdapter getAdapter() {

        TeamListAdapter adapter;
        if (list.getAdapter() != null && list.getAdapter() instanceof TeamListAdapter) {
            adapter = (TeamListAdapter) list.getAdapter();
        } else {
            List<TeamListAdapter.TeamListItem> items = new ArrayList<TeamListAdapter.TeamListItem>();

            // temp
            items.add(new TeamListAdapter.TeamListItem(getString(R.string.member1), State.DECLINE, getString(R.string.member1_reason)));
            items.add(new TeamListAdapter.TeamListItem(getString(R.string.member2), State.ACCEPT, getString(R.string.member2_reason)));
            items.add(new TeamListAdapter.TeamListItem(getString(R.string.member3), State.ACCEPT, getString(R.string.member3_reason)));
            items.add(new TeamListAdapter.TeamListItem(getString(R.string.member4), State.NOT_DECIDED, getString(R.string.member4_reason)));
            items.add(new TeamListAdapter.TeamListItem(getString(R.string.member5), State.DECLINE, getString(R.string.member5_reason)));

            TeamListAdapter.TeamListItem[] arrItems = items.toArray(new TeamListAdapter.TeamListItem[items.size()]);
            adapter = new TeamListAdapter(getActivity(), R.layout.item_team_list, arrItems);
        }

        return adapter;
    }
}
