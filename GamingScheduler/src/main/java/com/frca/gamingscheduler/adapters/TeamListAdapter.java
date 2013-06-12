package com.frca.gamingscheduler.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.frca.gamingscheduler.R;

public class TeamListAdapter extends ArrayAdapter<String> {

    final TeamListItem[] items;
    final private int resourceLayout;

    public TeamListAdapter(Context context, int resourceLayout, TeamListItem[] items) {
        super(context, resourceLayout);
        this.items = items;
        this.resourceLayout = resourceLayout;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    public View getView(final int position, final View convertView, final ViewGroup parent) {

        TeamListItem item = items[position];

        View itemView;
        if (convertView != null)
            itemView = convertView;
        else {
            final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);

            itemView = inflater.inflate(resourceLayout, null);
        }

        TextView nickname = (TextView) itemView.findViewById(R.id.text_name);
        ImageView icon = (ImageView) itemView.findViewById(R.id.icon);
        TextView reason = (TextView) itemView.findViewById(R.id.text_content);


        nickname.setText(item.nickname);
        icon.setImageResource(item.state.getImgResId());
        reason.setText(item.reason);

        return itemView;
    }

    public static class TeamListItem {

        public enum State {
            NOT_DECIDED(0),
            ACCEPT(1),
            DECLINE(2);

            private int state;
            private static int imgIds[] = {R.drawable.icon_not_decided, R.drawable.icon_accepted, R.drawable.icon_decline};

            State(int state) {
                this.state = state;
            }

            public int toInt() {
                return state;
            }

            private int getImgResId() {
                return imgIds[state];
            }

            public static State fromInt(int stateId) {
                switch (stateId) {
                    case 0:
                        return NOT_DECIDED;
                    case 1:
                        return ACCEPT;
                    case 2:
                        return DECLINE;
                    default:
                        throw new UnsupportedOperationException("Team List State can be accessed only from values from 0 to 2.");
                }
            }
        }

        public String nickname;
        public State state;
        public String reason;

        public TeamListItem(String nickname, State state, String reason) {
            this.nickname = nickname;
            this.state = state;
            this.reason = reason;
        }

        public TeamListItem(String nickname, int state, String reason) {
            this(nickname, State.fromInt(state), reason);
        }
    }
}
